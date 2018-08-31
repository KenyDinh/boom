package dev.boom.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.boom.common.CommonMethod;
import dev.boom.core.GameLog;
import dev.boom. services.UserInfo;

public class SocketSessionPool {

	private static Map<String, Map<String, SocketSessionBase>> mapSocketSession = new HashMap<>();
	private static Set<String> listValidToken = new HashSet<>();
	private static Map<String, Long> mapTokenUserId = new HashMap<>();

	public static void applySocketSession(SocketSessionBase socketSession) {
		String endPointName = socketSession.getEndPointName();
		String token = socketSession.getToken();
		if (!mapSocketSession.containsKey(endPointName)) {
			Map<String, SocketSessionBase> mapSocket = new HashMap<>();
			mapSocketSession.put(endPointName, mapSocket);
		}
		if (mapSocketSession.get(endPointName).containsKey(token)) {
			socketSession.closeSession();
			GameLog.getInstance().error("[SocketSessionPool] (applySocketSession) socket session is already in session pool!");
			return;
		}
		if (mapTokenUserId.containsKey(token)) {
			socketSession.setUserId(mapTokenUserId.get(token));
		} else {
			GameLog.getInstance().error("[SocketSessionPool] (applySocketSession) this socket session doesn't have user_id!");
		}
		mapSocketSession.get(endPointName).put(token, socketSession);
	}
	
	public static Map<String, SocketSessionBase> getMapSocketSession(String endPoint) {
		return mapSocketSession.get(endPoint);
	}
	
	public static List<SocketSessionBase> getSocketSessionList(String endPoint) {
		List<SocketSessionBase> ret = null;
		Map<String, SocketSessionBase> map = getMapSocketSession(endPoint);
		if (map != null) {
			ret = new ArrayList<>();
			for (String key : map.keySet()) {
				ret.add(map.get(key));
			}
		}
		return ret;
	}
	
	public static void removeSocketSession(long userId) {
		List<SocketSessionBase> socketSessionList = getListSocketSessionByUserId(userId);
		if (socketSessionList != null && socketSessionList.size() > 0) {
			for (SocketSessionBase socketSession : socketSessionList) {
				removeSocketSession(socketSession);
			}
		}
	}
	
	public static void removeSocketSession(long userId, String endPointName) {
		SocketSessionBase socketSession = null;
		if (mapSocketSession.containsKey(endPointName)) {
			for (String key : mapSocketSession.get(endPointName).keySet()) {
				SocketSessionBase ssb = mapSocketSession.get(endPointName).get(key);
				if (ssb.getEndPointName().equals(endPointName) && ssb.getUserId() == userId) {
					socketSession = ssb;
					break;
				}
			}
		}
		if (socketSession != null) {
			removeSocketSession(socketSession);
		} else {
			GameLog.getInstance().error("[SocketSessionPool] (removeSocketSession) socket not found by user_id:" + userId);
		}
	}
	
	public static SocketSessionBase getStoredSocketSessionByUserId(long userId, String endPointName) {
		if (mapSocketSession.containsKey(endPointName)) {
			for (String key : mapSocketSession.get(endPointName).keySet()) {
				SocketSessionBase ssb = mapSocketSession.get(endPointName).get(key);
				if (ssb.getEndPointName().equals(endPointName) && ssb.getUserId() == userId) {
					return ssb;
				}
			}
		}
		return null;
	}
	
	public static List<SocketSessionBase> getListSocketSessionByUserId(long userId) {
		List<SocketSessionBase> listSocketSession = null;
		for (String endPointName : mapSocketSession.keySet()) {
			for (String key : mapSocketSession.get(endPointName).keySet()) {
				SocketSessionBase ssb = mapSocketSession.get(endPointName).get(key);
				if (ssb.getEndPointName().equals(endPointName) && ssb.getUserId() == userId) {
					if (listSocketSession == null) {
						listSocketSession = new ArrayList<>();
					}
					listSocketSession.add(ssb);
				}
			}
			
		}
		return listSocketSession;
	}
	
	public static void removeSocketSession(SocketSessionBase socketSession) {
		removeSocketSession(socketSession, true);
	}
	
	public static void removeSocketSession(SocketSessionBase socketSession, boolean removeToken) {
		if (removeToken) {
			removeToken(socketSession.getToken());
		}
		String endPointName = socketSession.getEndPointName();
		if (mapSocketSession.containsKey(endPointName)) {
			if (mapSocketSession.get(endPointName).containsKey(socketSession.getToken())) {
				mapSocketSession.get(endPointName).remove(socketSession.getToken());
				socketSession.closeSession();
			}
		}
	}
	
	public static void removeToken(String key) {
		if (listValidToken.contains(key)) {
			listValidToken.remove(key);
		}
		if (mapTokenUserId.containsKey(key)) {
			mapTokenUserId.remove(key);
		}
	}
	
	public static boolean isValidToken(String key) {
		return listValidToken.contains(key);
	}
	
	public static String generateValidToken(String endpoint, UserInfo userInfo) {
		String key = getPlayerKey(endpoint, userInfo);
		listValidToken.add(key);
		mapTokenUserId.put(key, userInfo.getId());
		return key;
	}
	
	/**
	 * @param endpoint
	 * @param uuid
	 * @return token for non-user
	 */
	public static String generateValidToken(String endpoint, String uuid) {
		String base = endpoint + "-" + uuid;
		String key = CommonMethod.getEncryptMD5(base);
		listValidToken.add(key);
		return key;
	}
	
	private static String getPlayerKey(String endpoint, UserInfo userInfo) {
		String base = endpoint + userInfo.getId();
		String key = CommonMethod.getEncryptMD5(base);
		return key;
	}
}
