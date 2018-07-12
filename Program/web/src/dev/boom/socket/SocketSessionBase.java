package dev.boom.socket;

import java.io.IOException;

import javax.websocket.Session;

import org.apache.click.service.ConsoleLogService;

import dev.boom.core.GameLog;
import net.arnx.jsonic.JSON;

public abstract class SocketSessionBase {

	private Session session;

	private long userId;

	private String name;

	private String token;

	private String endPointName;

	public SocketSessionBase(Session session, String endPointName, String token) {
		this.session = session;
		this.endPointName = endPointName;
		this.token = token;
	}

	public String getSessionId() {
		return session.getId();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEndPointName() {
		return endPointName;
	}

	public void setEndPointName(String endPointName) {
		this.endPointName = endPointName;
	}

	public void sendMessage(String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract void process(String message);

	public Object parseMessage(String mesage, Class<? extends Object> clazz) {
		try {
			Object obj = JSON.decode(mesage, clazz);
			return obj;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public Object parseMessage(String mesage) {
		try {
			Object obj = JSON.decode(mesage);
			return obj;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public boolean isOpen() {
		try {
			return session.isOpen();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public void closeSession() {
		try {
			session.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void log(int level, String message) {
		if (getUserId() != 0) {
			message = String.format("[BOOM:%d] ", getUserId()) + message;
		}
		GameLog.getInstance().log(level, message);
	}
	
	public void logInfo(String message) {
		log(ConsoleLogService.INFO_LEVEL, message);
	}
	
	public void logDebug(String message) {
		log(ConsoleLogService.DEBUG_LEVEL, message);
	}
	
	public void logError(String message) {
		log(ConsoleLogService.ERROR_LEVEL, message);
	}
}
