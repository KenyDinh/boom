package dev.boom.socket;

import javax.websocket.Session;

public class BoomGameLobbySocketSession extends SocketSessionBase {

	public BoomGameLobbySocketSession(Session session, String endPointName, String token) {
		super(session, endPointName, token);
	}

	@Override
	public void process(String message) {
		
	}

}
