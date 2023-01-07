package dev.boom.socket;

import javax.websocket.Session;

public class DeviceSocketSession extends SocketSessionBase {

	public DeviceSocketSession(Session session, String endPointName, String token) {
		super(session, endPointName, token);
	}

	@Override
	public void process(String message) {

	}

}
