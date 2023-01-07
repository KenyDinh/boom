package dev.boom.socket;

import java.io.IOException;
import java.util.Date;

import javax.websocket.Session;

import org.apache.click.service.ConsoleLogService;

import dev.boom.common.CommonDefine;
import dev.boom.core.GameLog;
import net.arnx.jsonic.JSON;

public abstract class SocketSessionBase {

	private Session session;

	private long userId;

	private String username;

	private String token;

	private String endPointName;
	
	private Date start_date;
	
	private Date expire_date;
	
	private Object refSocketId;
	
	public SocketSessionBase(Session session, String endPointName, String token) {
		this.session = session;
		this.endPointName = endPointName;
		this.token = token;
		this.start_date = new Date();
		this.expire_date = new Date(this.start_date.getTime() + CommonDefine.SOCKET_SESSION_INTERVAL);
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getExpire_date() {
		return expire_date;
	}

	public void setExpire_date(Date expire_date) {
		this.expire_date = expire_date;
	}
	
	public boolean isExpired() {
		// not available now
		return false;
	}

	public Object getRefSocketId() {
		return refSocketId;
	}

	public void setRefSocketId(Object refSocketId) {
		this.refSocketId = refSocketId;
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
	public void logWarning(String message) {
		log(ConsoleLogService.WARN_LEVEL, message);
	}
}
