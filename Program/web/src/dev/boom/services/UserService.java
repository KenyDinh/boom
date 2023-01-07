package dev.boom.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.common.CommonMethod;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblUserInfo;

public class UserService {

	public static UserInfo getUser(String username, String password) {
		TblUserInfo info = new TblUserInfo();
		info.setUsername(username);
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		UserInfo ret = new UserInfo((TblUserInfo) daos.get(0));
		if (ret.getPassword().equals(CommonMethod.getEncryptMD5(password))) {
			return ret;
		}
		return null;
	}
	
	public static UserInfo getUserById(long id) {
		TblUserInfo info = new TblUserInfo();
		info.setId(id);
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return new UserInfo((TblUserInfo) daos.get(0));
	}
	
	public static UserInfo getUserByName(String username) {
		TblUserInfo info = new TblUserInfo();
		info.setUsername(username);
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty()) {
			return null;
		}
		
		return new UserInfo((TblUserInfo) daos.get(0));
	}
	
	public static boolean createUser(String username, String password) {
		return createUser(username, password, 0);
	}
	
	public static boolean createUser(String username, String password, int flag) {
		return createUser(username, password, 0, 0, flag);
	}
	
	public static boolean createUser(String username, String password, int role, int dept, int flag) {
		return createUser(username, password, role, dept, flag, "", "");
	}
	
	public static boolean createUser(String username, String password, int role, int dept, int flag, String empid, String fullName) {
		TblUserInfo info = new TblUserInfo();
		info.setUsername(username);
		info.setPassword(CommonMethod.getEncryptMD5(password));
		info.setRole(role);
		info.setDept(dept);
		info.setFlag(flag);
		info.setEmpid(empid);
		info.setName(fullName);
		Long id = (Long)CommonDaoService.insert(info);
		return (id != null && id > 0);
	}
	
	public static List<UserInfo> getUserList() {
		TblUserInfo info = new TblUserInfo();
		List<DaoValue> list = CommonDaoService.select(info);
		
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		List<UserInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new UserInfo((TblUserInfo) dao));
		}
		
		return ret;
	}
	
	public static Map<Long, UserInfo> loadMapUsers(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		TblUserInfo info = new TblUserInfo();
		StringBuilder sb = new StringBuilder();
		for (long id : ids) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}
		info.setSelectOption("WHERE id IN (" + sb.toString() + ")");
		List<DaoValue> list = CommonDaoService.select(info);
		
		if (list == null || list.isEmpty()) {
			return null;
		}
		Map<Long, UserInfo> ret = new HashMap<>();
		for (DaoValue dao : list) {
			UserInfo userInfo = new UserInfo((TblUserInfo) dao);
			ret.put(userInfo.getId(), userInfo);
		}
		
		return ret;
	}
	
	public static List<UserInfo> searchForUser(String name) {
		TblUserInfo info = new TblUserInfo();
		info.setSelectOption("WHERE id > 0 AND username LIKE '%" + name + "%'");
		List<DaoValue> list = CommonDaoService.select(info);
		
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		List<UserInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new UserInfo((TblUserInfo) dao));
		}
		
		return ret;
	}
}
