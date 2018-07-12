package dev.boom.services;

import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.UserInfo;

public class UserService {

	public static UserInfo getUser(String username, String password) {
		UserInfo info = new UserInfo();
		info.setUsername(username);
		info.setPassword(CommonMethod.getEncryptMD5(password));
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return (UserInfo) daos.get(0);
	}
	
	public static UserInfo getUserById(long id) {
		UserInfo info = new UserInfo();
		info.setId(id);
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return (UserInfo) daos.get(0);
	}
	
	public static UserInfo getUserByName(String username) {
		UserInfo info = new UserInfo();
		info.setUsername(username);
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty()) {
			return null;
		}
		
		return (UserInfo) daos.get(0);
	}
	
	public static boolean createUser(String username, String password) {
		UserInfo info = new UserInfo();
		info.setUsername(username);
		info.setPassword(CommonMethod.getEncryptMD5(password));
		Long id = (Long)CommonDaoService.insert(info);
		return (id != null && id > 0);
	}
}
