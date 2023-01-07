package dev.boom.socket.endpoint;

import java.util.List;
import java.util.Map;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import dev.boom.socket.DeviceSocketSession;
import dev.boom.socket.SocketSessionBase;
import dev.boom.socket.SocketSessionPool;

@ServerEndpoint(value = DeviceEndPoint.SOCKET_PATH)
public class DeviceEndPoint extends EndPointBase {
	
	public static final String ENDPOINT_NAME = DeviceEndPoint.class.getSimpleName();
	public static final String SOCKET_PATH = "/socket/tools/device";
	public static final String VALIDATION_KEY = "device_token";
	
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
					return new DeviceSocketSession(session, ENDPOINT_NAME, token);
				}
			}
		}
		return null;
	}

	public static void sendMessageUpdate(long userId) {
		List<SocketSessionBase> listSocket = SocketSessionPool.getSocketSessionList(ENDPOINT_NAME);
		if (listSocket == null || listSocket.isEmpty()) {
			return;
		}
		for (SocketSessionBase socket : listSocket) {
			if (socket.getUserId() == userId) {
				continue;
			}
			socket.sendMessage("{\"msg_type\":\"update\"}");
		}
	}
}
