package dev.boom.game.card.poker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import dev.boom.game.card.Card;

public class PokerPlayer {

	private long payerId;
	private String playerName;
	private int coin;
	private List<Card> listCards;
	private boolean viewer;
	private boolean turn;
	private long timeout;
	private Queue<PokerAction> playerAction;
	private List<PokerAction> actionList;
	private PokerActionType lastAction;

	public PokerPlayer(long payerId, String playerName, int coin) {
		super();
		this.payerId = payerId;
		this.playerName = playerName;
		this.coin = coin;
		this.listCards = new ArrayList<>();
		this.viewer = false;
		this.playerAction = new LinkedList<>();
		this.actionList = new ArrayList<>();
		initActionList();
	}
	
	private void initActionList() {
		resetActionList();
		for (PokerActionType type : PokerActionType.values()) {
			if (type.ordinal() <= 0) {
				continue;
			}
			this.actionList.add(new PokerAction(type.ordinal()));
		}
	}
	
	public void resetActionList() {
		for (PokerAction action : actionList) {
			action.reset();
		}
	}

	public long getPayerId() {
		return payerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getCoin() {
		return coin;
	}

	public List<Card> getListCards() {
		return listCards;
	}
	
	public boolean isViewer() {
		return viewer;
	}

	public void setViewer(boolean viewer) {
		this.viewer = viewer;
	}
	
	public void setCoin(int coin) {
		this.coin = coin;
	}
	
	public void addCoin(long amount) {
		if (amount <= 0) {
			return;
		}
		this.coin += amount;
	}
	
	public boolean subCoin(long amount) {
		if (amount <= 0) {
			return true;
		}
		if (amount > this.coin) {
			return false;
		}
		this.coin -= amount;
		return true;
	}
	
	public long allIn() {
		long subCount = this.coin;
		this.coin = 0;
		return subCount;
	}

	public void setListCards(List<Card> listCards) {
		this.listCards = listCards;
	}

	public boolean isTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	public PokerActionType getLastAction() {
		return lastAction;
	}

	public void setLastAction(PokerActionType lastAction) {
		this.lastAction = lastAction;
	}
	
	public List<PokerAction> getActionList() {
		return actionList;
	}
	
	public void setAction(PokerActionType type, int minValue, int maxValue, String message) {
		for (PokerAction action : actionList) {
			if (action.getId() == type.ordinal()) {
				action.setMinValue(minValue);
				action.setMaxValue(maxValue);
				action.setMessage(message);
				break;
			}
		}
	}

	private boolean isValidAction(PokerAction check) {
		for (PokerAction action : actionList) {
			if (!action.isValid()) {
				continue;
			}
			if (action.getId() != check.getId()) {
				continue;
			}
			if (check.getPlayerParam() < action.getMinValue()) {
				return false;
			}
			if (check.getPlayerParam() > action.getMaxValue()) {
				return false;
			}
		}
		return true;
	}
	
	public void processAction(PokerAction action) {
		if (!playerAction.isEmpty()) {
			return;
		}
		if (!isValidAction(action)) {
			return;
		}
		playerAction.add(action);
	}
	
	public PokerAction checkAction() {
		if (playerAction.isEmpty()) {
			return null;
		}
		return playerAction.poll();
	}
	
	
	public List<Map<String, Object>> getListCard(boolean self) {
		List<Map<String, Object>> cards = new ArrayList<>();
		for (Card card : listCards) {
			Map<String, Object> cardObj = new HashMap<>();
			if (self) {
				cardObj.put("num", card.getCardNum().getId());
				cardObj.put("type", card.getCardType().getId());
			} else {
				cardObj.put("num", 0);
				cardObj.put("type", 0);
			}
			cards.add(cardObj);
		}
		return cards;
	}

}
