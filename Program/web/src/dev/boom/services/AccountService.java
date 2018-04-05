package dev.boom.services;

import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.core.dao.DaoValue;
import dev.boom.info.AccountInfo;

public class AccountService {

	public static AccountInfo getAccount(String username, String password) {
		AccountInfo info = new AccountInfo();
		info.setUsername(username);
		info.setPassword(CommonMethod.getEncryptMD5(password));
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return (AccountInfo) daos.get(0);
	}
	
	public static AccountInfo getAccount(int id) {
		AccountInfo info = new AccountInfo();
		info.setId(id);
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return (AccountInfo) daos.get(0);
	}
	
	public static boolean createAccount(String username, String password) {
		AccountInfo info = new AccountInfo();
		info.setUsername(username);
		info.setPassword(CommonMethod.getEncryptMD5(password));
		return CommonDaoService.insert(info);
	}
}
