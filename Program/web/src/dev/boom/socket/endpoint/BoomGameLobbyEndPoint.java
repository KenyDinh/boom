package dev.boom.socket.endpoint;

import java.util.List;
import java.util.Map;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import dev.boom.socket.BoomGameLobbySocketSession;
import dev.boom.socket.SocketSessionBase;
import dev.boom.socket.SocketSessionPool;

@ServerEndpoint(value = BoomGameLobbyEndPoint.SOCKET_PATH)
public class BoomGameLobbyEndPoint extends EndPointBase {
	
	public static final String ENDPOINT_NAME = BoomGameLobbyEndPoint.class.getSimpleName();
	public static final String SOCKET_PATH = "/socket/game/blobby";
	public static final String VALIDATION_KEY = "l_boom_token";
	
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
		if (params != null && params.containsKey(getValidationKey())) {
			List<String> values = params.get(getValidationKey());
			if (!values.isEmpty()) {
				String token = values.get(0);
				if (SocketSessionPool.isValidToken(token)) {
					return new BoomGameLobbySocketSession(session, ENDPOINT_NAME, token);
				}
			}
		}
		return null;
	}
	
	public static void sendSocketLobbyUpdate() {
		Map<String, SocketSessionBase> socketsList = SocketSessionPool.getMapSocketSession(ENDPOINT_NAME);
		if (socketsList == null || socketsList.isEmpty()) {
			return;
		}
		for (String key : socketsList.keySet()) {
			BoomGameLobbySocketSession socket = (BoomGameLobbySocketSession) socketsList.get(key);
			socket.sendMessage("{\"type\":\"update\"}");
		}
	}

}
