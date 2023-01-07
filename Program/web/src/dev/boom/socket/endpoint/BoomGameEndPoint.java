package dev.boom.socket.endpoint;

import java.util.List;
import java.util.Map;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import dev.boom.game.boom.BoomGame;
import dev.boom.game.boom.BoomGameManager;
import dev.boom.socket.BoomGameSocketSession;
import dev.boom.socket.SocketSessionBase;
import dev.boom.socket.SocketSessionPool;

@ServerEndpoint(value = BoomGameEndPoint.SOCKET_PATH)
public class BoomGameEndPoint extends EndPointBase {
	
	public static final String ENDPOINT_NAME = BoomGameEndPoint.class.getSimpleName();
	public static final String SOCKET_PATH = "/socket/game/boom";
	public static final String VALIDATION_KEY = "g_boom_token";
	public static final String GAME_KEY = "g_boom_id";
	
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
			return null;
		}
		List<String> values = params.get(getValidationKey());
		if (values.isEmpty()) {
			return null;
		}
		String token = values.get(0);
		if (!SocketSessionPool.isValidToken(token)) {
			return null;
		}
		values = params.get(GAME_KEY);
		if (values.isEmpty()) {
			return null;
		}
		String gameId = values.get(0);
		BoomGame boomGame = BoomGameManager.getBoomGame(gameId);
		if (boomGame == null) {
			return null;
		}
		BoomGameSocketSession bss = new BoomGameSocketSession(session, ENDPOINT_NAME, token);
		bss.setGameId(gameId);
		return bss;
	}
	
	public static boolean checkGameSocketUser(String gameId, long userId) {
		Map<String, SocketSessionBase> socketsList = SocketSessionPool.getMapSocketSession(ENDPOINT_NAME);
		if (socketsList == null || socketsList.isEmpty()) {
			return false;
		}
		for (String key : socketsList.keySet()) {
			BoomGameSocketSession socket = (BoomGameSocketSession) socketsList.get(key);
			if (socket.getGameId().equals(gameId) && socket.getUserId() == userId) {
				return true;
			}
		}
		return false;
	}
	
	public static void sendSocketGameUpdate(String gameId, String data) {
		Map<String, SocketSessionBase> socketsList = SocketSessionPool.getMapSocketSession(ENDPOINT_NAME);
		if (socketsList == null || socketsList.isEmpty()) {
			return;
		}
		for (String key : socketsList.keySet()) {
			BoomGameSocketSession socket = (BoomGameSocketSession) socketsList.get(key);
			if (socket.getGameId().equals(gameId)) {
				socket.sendMessage(String.format("{\"pid\":%d,\"data\":%s}", socket.getUserId(), data));
			}
		}
	}

}
