package dev.boom.socket.endpoint;

import java.util.List;
import java.util.Map;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import dev.boom.services.User;
import dev.boom.socket.FridaySocketSession;
import dev.boom.socket.SocketSessionBase;
import dev.boom.socket.SocketSessionPool;

@ServerEndpoint(value = FridayEndpoint.SOCKET_PATH)
public class FridayEndpoint extends EndPointBase{

	public static final String ENDPOINT_NAME = FridayEndpoint.class.getSimpleName();
	public static final String SOCKET_PATH = "/socket/friday";
	private static final String VALIDATION_KEY = "friday_token";
	
	public static final String FRIDAY_BOT_UUID = "3daa60a268c0";

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
					return new FridaySocketSession(session, getEndPointName(), token);
				}
			}
		}
		return null;
	}
	
	public static String registerToken(User accountInfo) {	
		return SocketSessionPool.generateValidToken(ENDPOINT_NAME, accountInfo);
	}

	public static void initFridayBotToken() {
		SocketSessionPool.generateValidToken(ENDPOINT_NAME, FRIDAY_BOT_UUID);
	}

}
