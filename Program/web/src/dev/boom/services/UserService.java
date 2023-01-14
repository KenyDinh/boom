package dev.boom.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.common.CommonMethod;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblUserInfo;

public class UserService {

	private UserService() {
	}

	public static User getUser(String username, String password) {
		TblUserInfo info = new TblUserInfo();
		info.Set("username", username);
		List<DaoValue> daos = CommonDaoFactory.Select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		User ret = new User((TblUserInfo) daos.get(0));
		if (ret.getPassword().equals(CommonMethod.getEncryptMD5(password))) {
			return ret;
		}
		return null;
	}
	
	public static User getUserById(long id) {
		TblUserInfo info = new TblUserInfo();
		info.Set("id", id);
		List<DaoValue> daos = CommonDaoFactory.Select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return new User((TblUserInfo) daos.get(0));
	}
	
	public static User getUserByName(String username) {
		TblUserInfo info = new TblUserInfo();
		info.Set("username", username);
		List<DaoValue> daos = CommonDaoFactory.Select(info);
		if (daos == null || daos.isEmpty()) {
			return null;
		}
		
		return new User((TblUserInfo) daos.get(0));
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
		User info = new User();
		info.setUsername(username);
		info.setPassword(CommonMethod.getEncryptMD5(password));
		info.setRole(role);
		info.setDept(dept);
		info.setFlag(flag);
		info.setEmpid(empid);
		info.setName(fullName);
		return (CommonDaoFactory.Insert(info.getUserInfo())> 0);
	}
	
	public static List<User> getUserList() {
		TblUserInfo info = new TblUserInfo();
		List<DaoValue> list = CommonDaoFactory.Select(info);
		
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		List<User> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new User((TblUserInfo) dao));
		}
		
		return ret;
	}
	
	public static Map<Long, User> loadMapUsers(List<Long> ids) {
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
		info.SetSelectOption("WHERE id IN (" + sb.toString() + ")");
		List<DaoValue> list = CommonDaoFactory.Select(info);
		
		if (list == null || list.isEmpty()) {
			return null;
		}
		Map<Long, User> ret = new HashMap<>();
		for (DaoValue dao : list) {
			User userInfo = new User((TblUserInfo) dao);
			ret.put(userInfo.getId(), userInfo);
		}
		
		return ret;
	}
	
	public static List<User> searchForUser(String name) {
		TblUserInfo info = new TblUserInfo();
		info.SetSelectOption("WHERE id > 0 AND username LIKE '%" + name + "%'");
		List<DaoValue> list = CommonDaoFactory.Select(info);
		
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		List<User> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new User((TblUserInfo) dao));
		}
		
		return ret;
	}
	
}

