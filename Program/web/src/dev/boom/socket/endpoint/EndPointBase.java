package dev.boom.socket.endpoint;

import java.io.IOException;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import dev.boom.core.GameLog;
import dev.boom.socket.SocketSessionBase;
import dev.boom.socket.SocketSessionPool;

public abstract class EndPointBase {
	
	protected abstract String getValidationKey();
	protected abstract String getEndPointName();
	protected abstract SocketSessionBase getInstance(Session session);
	@OnOpen
	public void onOpen(Session session) {
		GameLog.getInstance().info("[EndPointBase] Session id:" + session.getId() + " connect!");
		GameLog.getInstance().info("[EndPointBase] open connection: " + session.getRequestURI().toString());
		SocketSessionBase socketSession = getInstance(session);
		if (socketSession != null) {
			SocketSessionPool.applySocketSession(socketSession);
			GameLog.getInstance().info("[EndPointBase] Session id:" + session.getId() + " is apply!");
			return;
		}
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GameLog.getInstance().error("[EndPointBase] (onOpen) Session id:" + session.getId() + " is invalid!");
	}
	
	@OnMessage
	public void onMessage(Session session, String message) {
		SocketSessionBase socketSession = getStoredSocketSession(session);
		if (socketSession != null) {
			if (socketSession.isExpired()) {
				socketSession.closeSession();
			} else {
				socketSession.process(message);
			}
		} else {
			GameLog.getInstance().error("[EndPointBase] (onMessage) session not found!");
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@OnClose
	public void onClose(Session session) {
		SocketSessionBase socketSession = getStoredSocketSession(session);
		if (socketSession != null) {
			SocketSessionPool.removeSocketSession(socketSession, false);
			GameLog.getInstance().info("[EndPointBase] Session id:" + session.getId() + " close!");
		} else {
			GameLog.getInstance().error("[EndPointBase] (onClose) Session is invalid!");
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@OnError
	public void onError(Throwable t) {
		//t.printStackTrace();
		GameLog.getInstance().error("[EndPointBase] (onError) error!");
	}
	
	protected SocketSessionBase getStoredSocketSession(Session session) {
		List<SocketSessionBase> sessionList = SocketSessionPool.getSocketSessionList(getEndPointName());
		if (sessionList == null) {
			GameLog.getInstance().error("[FridayEndpoint] (onMessage) session pool is empty!");
			return null;
		}
		for (SocketSessionBase socketSession : sessionList) {
			if (socketSession.getSessionId().equals(session.getId())) {
				return socketSession;
			}
		}
		return null;
	}
	
}
