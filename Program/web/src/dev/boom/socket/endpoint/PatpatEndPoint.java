package dev.boom.socket.endpoint;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.RandomStringUtils;

import dev.boom.socket.PatpatSocketSession;
import dev.boom.socket.SocketSessionBase;

@ServerEndpoint(value = PatpatEndPoint.SOCKET_PATH)
public class PatpatEndPoint extends EndPointBase {

	public static final String ENDPOINT_NAME = PatpatEndPoint.class.getSimpleName();
	public static final String SOCKET_PATH = "/socket/patpat";
	private static final String VALIDATION_KEY = "patpat_token";
	
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
//		Map<String, List<String>> params = session.getRequestParameterMap();
//		if (params != null && params.containsKey(getValidationKey())) {
//			List<String> values = params.get(getValidationKey());
//			if (!values.isEmpty()) {
//				String token = values.get(0);
//				if (SocketSessionPool.isValidToken(token)) {
					return new PatpatSocketSession(session, getEndPointName(), RandomStringUtils.random(10, true, true));
//				}
//			}
//		}
//		return null;
	}

}
