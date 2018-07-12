package dev.boom.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.boom.common.CommonMethod;
import dev.boom.core.GameLog;
import dev.boom.entity.info.UserInfo;

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
	
	private static String getPlayerKey(String endpoint, UserInfo userInfo) {
		String base = endpoint + userInfo.getId();
		String key = CommonMethod.getEncryptMD5(base);
		return key;
	}
}
