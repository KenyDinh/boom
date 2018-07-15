package dev.boom.socket;

import javax.websocket.Session;

public class MilkTeaSocketSession extends SocketSessionBase {

	private int type;

	private String data;

	public MilkTeaSocketSession(Session session, String endPointName, String token) {
		super(session, endPointName, token);
	}

	@Override
	public void process(String message) {

	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
