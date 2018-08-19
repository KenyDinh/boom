package dev.boom.socket.endpoint;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaSocketMessage;
import dev.boom.common.milktea.MilkTeaSocketType;
import dev.boom.core.GameLog;
import dev.boom.socket.MilkTeaSocketSession;
import dev.boom.socket.SocketSessionBase;
import dev.boom.socket.SocketSessionPool;

@ServerEndpoint(value = "/socket/milktea")
public class MilkTeaEndPoint extends EndPointBase{

	public static final String ENDPOINT_NAME = MilkTeaEndPoint.class.getSimpleName();
	public static final String SOCKET_PATH = "/socket/milktea";
	public static final String VALIDATION_KEY = "milktea_token";
	
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
		try {
			Map<String, List<String>> params = session.getRequestParameterMap();
			if (params != null && params.containsKey(getValidationKey())) {
				List<String> values = params.get(getValidationKey());
				if (!values.isEmpty()) {
					String token = values.get(0);
					if (SocketSessionPool.isValidToken(token)) {
						MilkTeaSocketSession socketSession = new MilkTeaSocketSession(session, ENDPOINT_NAME, token);
						if (params.containsKey("type")) {
							String strType = params.get("type").get(0);
							if (CommonMethod.isValidNumeric(strType, 1, Integer.MAX_VALUE)) {
								socketSession.setType(Integer.parseInt(strType));
								if (params.containsKey("data")) {
									socketSession.setData(params.get("data").get(0));
								}
								return socketSession;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	@OnMessage
	public void onMessage(Session session, String message) {
		GameLog.getInstance().error("[onMessage] UnsupportedOperationException!");
		SocketSessionBase socketSession = getStoredSocketSession(session);
		if (socketSession != null) {
			socketSession.closeSession();
		} else {
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param type
	 * @param message
	 */
	public static void sendSocketUpdate(long menuId, MilkTeaSocketType type, MilkTeaSocketMessage msg) {
		if (type == MilkTeaSocketType.INVALID) {
			return;
		}
		List<SocketSessionBase> listSocket = SocketSessionPool.getSocketSessionList(ENDPOINT_NAME);
		if (listSocket == null || listSocket.isEmpty()) {
			return;
		}
		String match = "menu_id=" + menuId;
		for (SocketSessionBase socket : listSocket) {
			MilkTeaSocketSession milkteaSocket = (MilkTeaSocketSession) socket;
			if (type == MilkTeaSocketType.ALL || milkteaSocket.getType() == type.getType()) {
				String data = "msg_id=" + msg.getId();
				if (milkteaSocket.getData() != null && milkteaSocket.getData().equals(match)) {
					data += "&" + milkteaSocket.getData();
				}
				socket.sendMessage(String.format("{\"message_type\":\"update\",\"data\":\"%s\"}",data));
			}
		}
	}
	
}
