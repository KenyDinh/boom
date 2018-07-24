package dev.boom.socket.endpoint;

import java.util.List;
import java.util.Map;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import dev.boom.socket.ManageMilkTeaSocketSession;
import dev.boom.socket.SocketSessionBase;
import dev.boom.socket.SocketSessionPool;

@ServerEndpoint(value = ManageMilkTeaEndPoint.SOCKET_PATH)
public class ManageMilkTeaEndPoint extends EndPointBase {
	
	public static final String ENDPOINT_NAME = ManageMilkTeaEndPoint.class.getSimpleName();
	public static final String SOCKET_PATH = "/socket/manage/milktea";
	public static final String VALIDATION_KEY = "manage_milktea_token";
	
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
					return new ManageMilkTeaSocketSession(session, ENDPOINT_NAME, token);
				}
			}
		}
		return null;
	}

}
