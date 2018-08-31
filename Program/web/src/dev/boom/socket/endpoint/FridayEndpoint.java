package dev.boom.socket.endpoint;

import java.util.List;
import java.util.Map;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import dev.boom.services.UserInfo;
import dev.boom.socket.FridaySocketSession;
import dev.boom.socket.SocketSessionBase;
import dev.boom.socket.SocketSessionPool;

@ServerEndpoint(value = "/socket/friday")
public class FridayEndpoint extends EndPointBase{

	public static final String ENDPOINT_NAME = FridayEndpoint.class.getSimpleName();
	private static final String VALIDATION_KEY = "friday_token";

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
	
	public static String registerToken(UserInfo accountInfo) {
		return SocketSessionPool.generateValidToken(ENDPOINT_NAME, accountInfo);
	}


}
