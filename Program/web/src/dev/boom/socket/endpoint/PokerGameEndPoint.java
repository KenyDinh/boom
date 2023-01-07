package dev.boom.socket.endpoint;

import java.util.List;
import java.util.Map;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import dev.boom.core.GameLog;
import dev.boom.game.card.poker.PokerGame;
import dev.boom.game.card.poker.PokerGameManager;
import dev.boom.socket.PokerGameSocketSession;
import dev.boom.socket.SocketSessionBase;
import dev.boom.socket.SocketSessionPool;

@ServerEndpoint(value = PokerGameEndPoint.SOCKET_PATH)
public class PokerGameEndPoint extends EndPointBase {

	public static final String ENDPOINT_NAME = PokerGameEndPoint.class.getSimpleName();
	public static final String SOCKET_PATH = "/socket/game/poker";
	public static final String VALIDATION_KEY = "g_poker_token";
	public static final String GAME_KEY = "g_poker_id";

	@Override
	protected String getValidationKey() {
		return VALIDATION_KEY;
	}

	@Override
	protected String getEndPointName() {
		return ENDPOINT_NAME;
	}

	@Override
	protected SocketSessionBase getInstance(Session session) {
		Map<String, List<String>> params = session.getRequestParameterMap();
		if (params == null || !params.containsKey(getValidationKey()) || !params.containsKey(GAME_KEY)) {
			GameLog.getInstance().error("[PokerGameEndPoint] no parameters!");
			return null;
		}
		List<String> values = params.get(getValidationKey());
		if (values.isEmpty()) {
			GameLog.getInstance().error("[PokerGameEndPoint] no validation key!");
			return null;
		}
		String token = values.get(0);
		if (!SocketSessionPool.isValidToken(token)) {
			GameLog.getInstance().error("[PokerGameEndPoint] invalid key!");
			return null;
		}
		values = params.get(GAME_KEY);
		if (values.isEmpty()) {
			GameLog.getInstance().error("[PokerGameEndPoint] no game key!");
			return null;
		}
		String gameId = values.get(0);
		PokerGame pokerGame = PokerGameManager.getPokerGame(gameId);
		if (pokerGame == null) {
			GameLog.getInstance().error("[PokerGameEndPoint] no game!");
			return null;
		}
		PokerGameSocketSession bss = new PokerGameSocketSession(session, ENDPOINT_NAME, token);
		bss.setGameId(gameId);
		return bss;
	}

	public static boolean checkGameSocketUser(String gameId, long userId) {
		Map<String, SocketSessionBase> socketsList = SocketSessionPool.getMapSocketSession(ENDPOINT_NAME);
		if (socketsList == null || socketsList.isEmpty()) {
			return false;
		}
		for (String key : socketsList.keySet()) {
			PokerGameSocketSession socket = (PokerGameSocketSession) socketsList.get(key);
			if (socket.getGameId().equals(gameId) && socket.getUserId() == userId) {
				return true;
			}
		}
		return false;
	}

	public static void sendSocketGameUpdate(String gameId, long userId, String data) {
		Map<String, SocketSessionBase> socketsList = SocketSessionPool.getMapSocketSession(ENDPOINT_NAME);
		if (socketsList == null || socketsList.isEmpty()) {
			return;
		}
		for (String key : socketsList.keySet()) {
			PokerGameSocketSession socket = (PokerGameSocketSession) socketsList.get(key);
			if (socket.getGameId().equals(gameId) && socket.getUserId() == userId) {
				socket.sendMessage(data);
			}
		}
	}

}
