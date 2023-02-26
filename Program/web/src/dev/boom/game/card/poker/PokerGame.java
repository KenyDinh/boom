package dev.boom.game.card.poker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.socket.endpoint.PokerGameEndPoint;
import net.arnx.jsonic.JSON;

public class PokerGame {
	
	private long hostId;
	private String hostName;
	private String gameId;
	private PokerCardPool cardPool;
	private PokerGameStatus status;
	private PokerTurn currentTurn;
	private PokerTurnState turnState;
	private List<PokerSeat> listSeats;
	private Map<PokerTurn, Map<Long, Integer>> playerCoinBetPerTurn;
	private int minCoinBet = 1000;
	private long delay = 0;
	private long currentTime;
	
	public PokerGame(long hostId, String hostName) {
		super();
		this.hostId = hostId;
		this.hostName = hostName;
		this.gameId = RandomStringUtils.randomAlphanumeric(8);
		listSeats = new ArrayList<>();
		for (int i = 0; i < PokerDef.MAX_POKER_GAME_PLAYER; i++) {
			listSeats.add(new PokerSeat(i + 1));
		}
		newGame();
	}
	
	public long getHostId() {
		return hostId;
	}

	public String getGameId() {
		return gameId;
	}
	
	public String getHostName() {
		return hostName;
	}

	public void setStatus(PokerGameStatus status) {
		this.status = status;
	}

	public void newGame() {
		status = PokerGameStatus.PREPARE;
		currentTurn = PokerTurn.PRE_FLOP;
		turnState = PokerTurnState.START;
		cardPool = new PokerCardPool();
		playerCoinBetPerTurn = new HashMap<>();
		setRole();
	}
	
	public boolean isPrepare() {
		return (status == PokerGameStatus.PREPARE);
	}
	
	public boolean isPlayering() {
		return (status == PokerGameStatus.PLAYING);
	}
	
	public boolean isFinished() {
		return (status == PokerGameStatus.FINISHED);
	}
	
	private void pushClientData() {
		for (PokerSeat ps : listSeats) {
			if (!ps.hasValidPlayer()) {
				continue;
			}
			long pid = ps.getPlayer().getPayerId();
			PokerGameEndPoint.sendSocketGameUpdate(getGameId(), pid, JSON.encode(getClientData(pid)));
		}
	}
	
	private Map<String, Object> getClientData(long pid) {
		Map<String, Object> data = new HashMap<>();
		
		//players
		List<Map<String, Object>> players = new ArrayList<>();
		for (PokerSeat ps : listSeats) {
			if (ps.isEmpty()) {
				continue;
			}
			Map<String, Object> player = new HashMap<>();
			boolean self = (ps.getPlayer().getPayerId() == pid);
			player.put("self", self ? 1 : 0);
			player.put("avatar", "http://localhost/friday-legacy/img/page/common-user.png");
			player.put("name", ps.getPlayer().getPlayerName());
			player.put("coin", ps.getPlayer().getCoin());
			player.put("hold_card", ps.getPlayer().getListCard(self));
			if (self) {
				player.put("action", ps.getPlayer().getActionList());
			}
			players.add(player);
		}
		data.put("players", players);
		long potSize = 0;
		for (PokerTurn turn : playerCoinBetPerTurn.keySet()) {
			Map<Long, Integer> playerCoinBet = playerCoinBetPerTurn.get(turn);
			for (Long playerID : playerCoinBet.keySet()) {
				potSize += playerCoinBet.get(playerID);
			}
		}
		data.put("pot_size", potSize);
		return data;
	}

	public void update() {
		currentTime = System.currentTimeMillis();
		if (isPlayering()) {
			switch (currentTurn) {
			case PRE_FLOP:
				preflopAction();
				break;
			case THE_FLOP:
			case THE_TURN:
			case THE_RIVER:
				flopTurnRiverAction();
				break;
			case SHOW_HAND:
				break;
			default:
				break;
			}
		}
		
		pushClientData();
	}
	
	private PokerSeat nextSeatPlayerAfter(int index) {
		for (PokerSeat ps : listSeats) {
			if (ps.isEmpty() || ps.getPlayer().isViewer() || ps.isRemove()) {
				continue;
			}
			if (ps.getIndex() > index) {
				return ps;
			}
		}
		for (PokerSeat ps : listSeats) {
			if (ps.isEmpty() || ps.getPlayer().isViewer() || ps.isRemove()) {
				continue;
			}
			PokerActionType lastActionType = ps.getPlayer().getLastAction();
			if (lastActionType != null) {
				if (lastActionType == PokerActionType.FOLD || lastActionType == PokerActionType.ALLIN) {
					continue;
				}
			}
			if (ps.getIndex() <= index) {
				return ps;
			}
		}
		return null;
	}
	
	private void setRole() {
		int index = 0;
		int countPlayer = 0;
		for (int i = 0; i < listSeats.size(); i++) {
			PokerSeat ps = listSeats.get(i);
			if (ps.is(PokerPlayerRole.DEALER)) {
				index = ps.getIndex();
			}
			ps.resetRole();
			if (ps.isRemove()) {
				ps.setPlayer(null);
				continue;
			}
			if (!ps.isEmpty()) {
				ps.getPlayer().resetActionList();
				ps.getPlayer().setLastAction(null);
				if (!isSufficientCoin(ps.getPlayer())) {
					ps.getPlayer().setViewer(true);
				} else {
					ps.getPlayer().setViewer(false);
					countPlayer++;
				}
			}
		}
		PokerPlayerRole[] assignedRoles = new PokerPlayerRole[] {PokerPlayerRole.DEALER, PokerPlayerRole.SMALL_BLIND, PokerPlayerRole.BIG_BLIND, PokerPlayerRole.UNDER_THE_GUN};
		for (PokerPlayerRole role : assignedRoles) {
			System.out.println("Index : " + index);
			PokerSeat ps = nextSeatPlayerAfter(index);
			if (ps == null) {
				return;
			}
			ps.addRole(role);
			if (role != PokerPlayerRole.DEALER || countPlayer > 2) {
				index = ps.getIndex();
			}
			System.out.println("Index after : " + index);
		}
		
		//
		for (PokerSeat ps : listSeats) {
			System.out.print(ps.getIndex());
			System.out.print(" : ");
			if (ps.isEmpty()) {
				System.out.println("EMPTY");
			} else {
				for (PokerPlayerRole role : PokerPlayerRole.values()) {
					if (ps.is(role)) {
						System.out.print(role.name());
						System.out.print(" ");
					}
				}
				System.out.println();
			}
		}
	}
	
	private void addCoinBet(long playerId, int amount) {
		if (playerCoinBetPerTurn == null) {
			return;
		}
		if (!playerCoinBetPerTurn.containsKey(currentTurn)) {
			playerCoinBetPerTurn.put(currentTurn, new HashMap<>());
		}
		Map<Long, Integer> playerCointBet = playerCoinBetPerTurn.get(currentTurn);
		int addCoint = amount;
		if (playerCointBet.containsKey(playerId)) {
			addCoint += playerCointBet.get(playerId);
		}
		playerCointBet.put(playerId, addCoint);
	}
	
	private int getCurrentCoinBet(long playerId) {
		if (playerCoinBetPerTurn == null) {
			return 0;
		}
		if (!playerCoinBetPerTurn.containsKey(currentTurn)) {
			return 0;
		}
		Map<Long, Integer> playerCointBet = playerCoinBetPerTurn.get(currentTurn);
		if (!playerCointBet.containsKey(playerId)) {
			return 0;
		}
		return playerCointBet.get(playerId);
	}
	
	private void preflopAction() {
		switch (turnState) {
		case START: {
			//
			int dealerIndex = -1;
			int utgIndex = -1;
			for (PokerSeat ps : listSeats) {
				if (ps.is(PokerPlayerRole.DEALER)) {
					dealerIndex = ps.getIndex();
				}
				if (ps.is(PokerPlayerRole.UNDER_THE_GUN)) {
					utgIndex = ps.getIndex();
				}
				if (ps.is(PokerPlayerRole.SMALL_BLIND) || ps.is(PokerPlayerRole.BIG_BLIND)) {
					int cointBet = minCoinBet;
					if (ps.is(PokerPlayerRole.BIG_BLIND)) {
						cointBet = minCoinBet * 2;
					}
					if (!ps.hasValidPlayer() || !ps.getPlayer().subCoin(cointBet)) {
						// reset
					} else {
						addCoinBet(ps.getPlayer().getPayerId(), cointBet);
					}
				}
			}
			//
			if (dealerIndex < 0 || utgIndex < 0) {
				return;
			}
			for (int i = dealerIndex; i < listSeats.size(); i++) {
				PokerSeat ps = listSeats.get(i);
				if (!ps.hasValidPlayer()) {
					continue;
				}
				ps.getPlayer().setListCards(cardPool.nextNCard(currentTurn.getPlayerCard()));
			}
			for (int i = 0; i < dealerIndex; i++) {
				PokerSeat ps = listSeats.get(i);
				if (!ps.hasValidPlayer()) {
					continue;
				}
				ps.getPlayer().setListCards(cardPool.nextNCard(currentTurn.getPlayerCard()));
			}
			
			for (PokerSeat ps : listSeats) {
				if (ps.getIndex() == utgIndex) {
					if (!ps.hasValidPlayer()) {
						PokerSeat next = nextSeatPlayerAfter(utgIndex);
						if (next == null) {
							//end
						} else {
							setPlayerActionTurn(next);
						}
					} else {
						setPlayerActionTurn(ps);
					}
					break;
				}
			}
			turnState = PokerTurnState.WAIT;
			// delay 10 seconds for checking hand cards
			delay = currentTime + CommonDefine.MILLION_SECOND_SECOND * 10;
		}
			break;
		case WAIT: {
			if (currentTime < delay) {
				return;
			}
			checkAndUpdatePlayerTurn();
		}
			break;
		case FINISHED: {
			
		}
			break;
		default:
			break;
		}
	}
	
	private void flopTurnRiverAction() {
		switch (turnState) {
		case START:
			int dealerIdx = 0;
			for (PokerSeat ps : listSeats) {
				if (ps.is(PokerPlayerRole.DEALER)) {
					dealerIdx = ps.getIndex();
					break;
				}
			}
			turnState = PokerTurnState.WAIT;
			// delay 10 seconds for checking hand cards
			delay = currentTime + CommonDefine.MILLION_SECOND_SECOND * 10;
			break;

		case WAIT: {
			if (currentTime < delay) {
				return;
			}
			checkAndUpdatePlayerTurn();
		}
			break;
		case FINISHED: {
			
		}
			break;
		default:
			break;
		}
	}
	
	private void checkAndUpdatePlayerTurn() {
		for (PokerSeat ps : listSeats) {
			if (ps.isEmpty()) {
				continue;
			}
			PokerPlayer player = ps.getPlayer();
			if (!player.isTurn()) {
				continue;
			}
			PokerAction action = player.checkAction();
			if (action == null) {
				return;
			}
			player.setLastAction(PokerActionType.valueOf(action.getId()));
			if (action.getId() == PokerActionType.FOLD.ordinal()) {
			} else {
				addCoinBet(player.getPayerId(), action.getPlayerParam());
			}
			player.resetActionList();
			PokerSeat nextSeatPlayer = nextSeatPlayerAfter(ps.getIndex());
			if (nextSeatPlayer == null || nextSeatPlayer.getIndex() == ps.getIndex()) {
				// end game (1 player)
			} else {
				if (checkFinishTurn()) {
					turnState = PokerTurnState.FINISHED;
				} else {
					setPlayerActionTurn(nextSeatPlayer);
				}
			}
			break;
		}
	}
	
	private boolean checkFinishTurn() {
		int maxCoinBet = 0;
		for (PokerSeat ps : listSeats) {
			if (ps.isEmpty()) {
				continue;
			}
			if (ps.getPlayer().isViewer()) {
				continue;
			}
			maxCoinBet = CommonMethod.max(maxCoinBet, getCurrentCoinBet(ps.getPlayer().getPayerId()));
		}
		for (PokerSeat ps : listSeats) {
			if (ps.isEmpty()) {
				continue;
			}
			if (ps.getPlayer().isViewer() || ps.getPlayer().getLastAction() == PokerActionType.FOLD) {
				continue;
			}
			int playerCoinBet = getCurrentCoinBet(ps.getPlayer().getPayerId());
			if (playerCoinBet < maxCoinBet && ps.getPlayer().getLastAction() != PokerActionType.ALLIN) {
				return false;
			}
		}
		
		return true;
	}
	
	private void setPlayerActionTurn(PokerSeat psPlayer) {
		// reset turn
		int countPlayer = 0;
		int maxCoinBet = 0;
		boolean hasPlayerBet = false;
		for (PokerSeat ps : listSeats) {
			if (!ps.hasValidPlayer()) {
				continue;
			}
			countPlayer++;
			ps.getPlayer().setTurn(false);
			ps.getPlayer().resetActionList();
			if (ps.getPlayer().getLastAction() == PokerActionType.BET
					|| ps.getPlayer().getLastAction() == PokerActionType.ALLIN) {
				hasPlayerBet = true;
			}
			maxCoinBet = CommonMethod.max(maxCoinBet, getCurrentCoinBet(ps.getPlayer().getPayerId()));
		}
		//
		PokerPlayer player = psPlayer.getPlayer();
		player.setTurn(true);
		
		player.setAction(PokerActionType.ALLIN, player.getCoin(), player.getCoin(), "");
		player.setAction(PokerActionType.FOLD, 0, 0, "");
		if (maxCoinBet <= player.getCoin()) {
			if (!hasPlayerBet) {
				player.setAction(PokerActionType.BET, maxCoinBet, maxCoinBet, "");
			} else {
				player.setAction(PokerActionType.RAISE, maxCoinBet, maxCoinBet, "");
				player.setAction(PokerActionType.CALL, maxCoinBet, maxCoinBet, "");
			}
		}
	}
	
	private boolean isSufficientCoin(PokerPlayer player) {
		return (player.getCoin() >= 10 * minCoinBet);
	}

	private boolean hasSeat() {
		if (listSeats == null || listSeats.isEmpty()) {
			return false;
		}
		return true;
	}
	
	synchronized public boolean addPlayer(PokerPlayer player) {
		if (isFinished()) {
			return false;
		}
		if (player == null || !hasSeat()) {
			return false;
		}
		for (PokerSeat ps : listSeats) {
			if (ps.isEmpty()) {
				ps.setPlayer(player);
				if (isPlayering() || !isSufficientCoin(player)) {
					player.setViewer(true);
				}
				return true;
			}
		}
		return false;
	}
	
	synchronized public boolean removePlayer(long playerID) {
		if (!hasSeat()) {
			return false;
		}
		for (PokerSeat ps : listSeats) {
			if (!ps.isEmpty() && ps.getPlayer().getPayerId() == playerID) {
				ps.setRemove(true);
				return true;
			}
		}
		return false;
	}
	
	public PokerPlayer getPokerPlayer(long playerID) {
		if (!hasSeat()) {
			return null;
		}
		for (PokerSeat ps : listSeats) {
			if (!ps.isEmpty() && ps.getPlayer().getPayerId() == playerID) {
				return ps.getPlayer();
			}
		}
		return null;
	}
	
	public int getCountPlayer() {
		if (!hasSeat()) {
			return 0;
		}
		int count = 0;
		for (PokerSeat ps : listSeats) {
			if (!ps.isEmpty() && !ps.getPlayer().isViewer()) {
				count++;
			}
		}
		return count;
	}
	
	public void processPlayerAction(long playerId, PokerAction action) {
		for (PokerSeat ps : listSeats) {
			if (!ps.hasValidPlayer()) {
				continue;
			}
			if (ps.getPlayer().getPayerId() == playerId) {
				ps.getPlayer().processAction(action);
				break;
			}
		}
	}
	
}
