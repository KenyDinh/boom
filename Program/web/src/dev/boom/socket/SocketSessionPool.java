package dev.boom.socket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dev.boom.common.CommonMethod;
import dev.boom.core.GameLog;
import dev.boom.services.User;

public class SocketSessionPool {
	
	private static Log log = LogFactory.getLog(SocketSessionPool.class);

	private static Map<String, Map<String, SocketSessionBase>> mapSocketSession = new ConcurrentHashMap<>();
	private static Set<String> listValidToken = Collections.synchronizedSet(new HashSet<>());
	private static Map<String, User> mapTokenUser = new ConcurrentHashMap<>();
	private static Map<String, String> mapTokenUUID = new ConcurrentHashMap<>();

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
		if (mapTokenUser.containsKey(token)) {
			socketSession.setUserId(mapTokenUser.get(token).getId());
			socketSession.setUsername(mapTokenUser.get(token).getUsername());
		} else if (mapTokenUUID.containsKey(token)) {
			socketSession.setUsername(mapTokenUUID.get(token));
			GameLog.getInstance().error("[SocketSessionPool] (applySocketSession) this socket session is using UUID");
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
	
	public static SocketSessionBase getStoredSocketSessionByUserId(String uuid, String endPointName) {
		if (mapSocketSession.containsKey(endPointName)) {
			for (String key : mapSocketSession.get(endPointName).keySet()) {
				SocketSessionBase ssb = mapSocketSession.get(endPointName).get(key);
				if (ssb.getEndPointName().equals(endPointName) && ssb.getUserId() == 0 && ssb.getUsername() != null && ssb.getUsername().equals(uuid)) {
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
	
	public static void removeToken(String token) {
		if (listValidToken.contains(token)) {
			listValidToken.remove(token);
		}
		if (mapTokenUser.containsKey(token)) {
			mapTokenUser.remove(token);
		}
		if (mapTokenUUID.containsKey(token)) {
			mapTokenUUID.remove(token);
		}
	}
	
	public static boolean isValidToken(String token) {
		return listValidToken.contains(token);
	}
	
	public static boolean isExistToken(String enpoint, User userInfo) {
		String token = getPlayerKey(enpoint, userInfo);
		if (listValidToken.contains(token)) {
			return true;
		}
		return false;
	}
	
	public static String generateValidToken(String endpoint, User userInfo) {
		String key = getPlayerKey(endpoint, userInfo);
		log.info("Generate token for " + endpoint + ", token:" + key);
		listValidToken.add(key);
		mapTokenUser.put(key, userInfo);
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
		log.info("Generate token for " + endpoint + ", token:" + key);
		listValidToken.add(key);
		mapTokenUUID.put(key, uuid);
		return key;
	}
	
	private static String getPlayerKey(String endpoint, User userInfo) {
		String base = endpoint + userInfo.getId();
		String key = CommonMethod.getEncryptMD5(base);
		return key;
	}
}
