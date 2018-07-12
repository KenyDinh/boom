package dev.boom.socket.endpoint;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import dev.boom.core.GameLog;
import dev.boom.entity.info.UserInfo;
import dev.boom.socket.SocketSessionBase;
import dev.boom.socket.SocketSessionPool;

@ServerEndpoint(value = "/socket/friday")
public class FridayEndpoint {

	private static final String ENDPOINT_NAME = FridayEndpoint.class.getSimpleName();
	private static final String VALIDATION_KEY = "friday_token";

	@OnOpen
	public void onOpen(Session session) {
		GameLog.getInstance().info("[FridayEndpoint] Session id:" + session.getId() + " connect!");
		Map<String, List<String>> params = session.getRequestParameterMap();
		if (params != null && params.containsKey(VALIDATION_KEY)) {
			List<String> values = params.get(VALIDATION_KEY);
			if (!values.isEmpty()) {
				String token = values.get(0);
				if (SocketSessionPool.isValidToken(token)) {
					FridaySocketSession fsSession = new FridaySocketSession(session, ENDPOINT_NAME, token);
					SocketSessionPool.applySocketSession(fsSession);
					GameLog.getInstance().info("[FridayEndpoint] Session id:" + session.getId() + " is apply!");
					return;
				}
			}
		}
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GameLog.getInstance().error("[FridayEndpoint] (onOpen) Session id:" + session.getId() + " is invalid!");
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		List<SocketSessionBase> sessionList = SocketSessionPool.getSocketSessionList(ENDPOINT_NAME);
		if (sessionList == null) {
			GameLog.getInstance().error("[FridayEndpoint] (onMessage) session pool is empty!");
			return;
		}
		for (SocketSessionBase socketSession : sessionList) {
			if (socketSession.getSessionId().equals(session.getId())) {
				socketSession.process(message);
				return;
			}
		}
		GameLog.getInstance().error("[FridayEndpoint] (onMessage) session not found!");
	}

	@OnClose
	public void onClose(Session session) {
		List<SocketSessionBase> sessionList = SocketSessionPool.getSocketSessionList(ENDPOINT_NAME);
		if (sessionList != null) {
			for (SocketSessionBase socketSession : sessionList) {
				if (socketSession.getSessionId().equals(session.getId())) {
					SocketSessionPool.removeSocketSession(socketSession, false);
					GameLog.getInstance().info("[FridayEndpoint] Session id:" + session.getId() + " close!");
					return;
				}
			}
		}
		GameLog.getInstance().error("[FridayEndpoint] (onClose) Session is invalid!");
	}

	@OnError
	public void onError(Throwable t) {
		GameLog.getInstance().error("[FridayEndpoint] (onError) error!");
		t.printStackTrace();
	}
	
	public static void registerToken(UserInfo accountInfo) {
		SocketSessionPool.generateValidToken(ENDPOINT_NAME, accountInfo);
	}
}
