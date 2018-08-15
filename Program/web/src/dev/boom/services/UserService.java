package dev.boom.services;

import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblUserInfo;

public class UserService {

	public static TblUserInfo getUser(String username, String password) {
		TblUserInfo info = new TblUserInfo();
		info.setUsername(username);
		info.setPassword(CommonMethod.getEncryptMD5(password));
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return (TblUserInfo) daos.get(0);
	}
	
	public static TblUserInfo getUserById(long id) {
		TblUserInfo info = new TblUserInfo();
		info.setId(id);
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return (TblUserInfo) daos.get(0);
	}
	
	public static TblUserInfo getUserByName(String username) {
		TblUserInfo info = new TblUserInfo();
		info.setUsername(username);
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty()) {
			return null;
		}
		
		return (TblUserInfo) daos.get(0);
	}
	
	public static boolean createUser(String username, String password) {
		TblUserInfo info = new TblUserInfo();
		info.setUsername(username);
		info.setPassword(CommonMethod.getEncryptMD5(password));
		Long id = (Long)CommonDaoService.insert(info);
		return (id != null && id > 0);
	}
}
