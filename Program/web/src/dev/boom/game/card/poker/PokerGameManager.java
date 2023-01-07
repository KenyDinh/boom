package dev.boom.game.card.poker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dev.boom.core.GameLog;

public class PokerGameManager {

	private static final int POKER_MAX_GAME_CREATED = 3;
	private static final Map<String, PokerGame> CACHE_POKER_GAME = new HashMap<>();
	private static final Map<String, PokerGameThread> CACHE_POKER_THREAD = new HashMap<>();

	public static PokerGame getExistGame(long hostId) {
		if (CACHE_POKER_GAME.isEmpty()) {
			return null;
		}
		for (String gameID : CACHE_POKER_GAME.keySet()) {
			if (CACHE_POKER_GAME.get(gameID).getHostId() == hostId) {
				return CACHE_POKER_GAME.get(gameID);
			}
		}
		return null;
	}
	
	public static synchronized PokerGame createPokerGame(long hostId, String hostName) {
		PokerGame game = null;
		do {
			game = new PokerGame(hostId, hostName);
		} while (CACHE_POKER_GAME.containsKey(game.getGameId()));
		if (!addPokerGame(game)) {
			return null;
		}
		return game;
	}
	
	public static boolean onCreatePokerGame(PokerGame pokerGame) {
		if (pokerGame == null) {
			return false;
		}
		if (CACHE_POKER_THREAD.containsKey(pokerGame.getGameId())) {
			return false;
		}
		PokerGameThread thread = new PokerGameThread(pokerGame);
		CACHE_POKER_THREAD.put(pokerGame.getGameId(), thread);
		thread.start();
		return true;
	}
	
	public static boolean startPokerGame(PokerGame pokerGame) {
		if (pokerGame == null || !pokerGame.isPrepare()) {
			return false;
		}
		pokerGame.setStatus(PokerGameStatus.PLAYING);
		return true;
	}
	
	public static synchronized boolean stopPokerGame(PokerGame pokerGame) {
		if (pokerGame == null) {
			return false;
		}
		if (!CACHE_POKER_THREAD.containsKey(pokerGame.getGameId())) {
			return false;
		}
		pokerGame.setStatus(PokerGameStatus.FINISHED);
		CACHE_POKER_THREAD.remove(pokerGame.getGameId());
		CACHE_POKER_GAME.remove(pokerGame.getGameId());
		GameLog.getInstance().info("Stop Poker Game : " + pokerGame.getGameId());
		return true;
	}

	public static PokerGame getPokerGame(String gameId) {
		if (StringUtils.isBlank(gameId)) {
			return null;
		}
		if (!CACHE_POKER_GAME.containsKey(gameId)) {
			return null;
		}
		return CACHE_POKER_GAME.get(gameId);
	}

	public static boolean addPokerGame(PokerGame pokerGame) {
		if (CACHE_POKER_GAME.size() >= POKER_MAX_GAME_CREATED) {
			return false;
		}
		if (CACHE_POKER_GAME.containsKey(pokerGame.getGameId())) {
			return false;
		}
		CACHE_POKER_GAME.put(pokerGame.getGameId(), pokerGame);
		return true;
	}
	
	public static List<PokerGame> getListAvailableGame(long id) {
		List<PokerGame> list = new ArrayList<>();
//		for (String gameID : CACHE_POKER_GAME.keySet()) {
//			PokerGame boomGame = CACHE_POKER_GAME.get(gameID);
//			if (boomGame.isFinished()) {
//				continue;
//			}
//			if (boomGame.isExistPlayerInGame(id)) {
//				return Arrays.asList(boomGame);
//			}
//			if (boomGame.isFull() || !boomGame.isInitState()) {
//				continue;
//			}
//			list.add(boomGame);
//		}
		return list;
	}
	
	public static boolean isPlayerAlreadyInGame(long id) {
		for (String gameID : CACHE_POKER_GAME.keySet()) {
//			PokerGame boomGame = CACHE_POKER_GAME.get(gameID);
//			if (!boomGame.isFinished() && boomGame.isExistPlayerInGame(id)) {
//				return true;
//			}
		}
		return false;
	}
	
	public static void clearAllGame() {
		CACHE_POKER_GAME.clear();
		CACHE_POKER_THREAD.clear();
	}
}
