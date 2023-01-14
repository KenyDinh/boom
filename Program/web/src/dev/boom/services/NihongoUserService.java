package dev.boom.services;

import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblNihongoUserInfo;

public class NihongoUserService {

	private NihongoUserService() {
	}

	public static TblNihongoUserInfo getNihongoUserInfo(long user_id) {
		TblNihongoUserInfo info = new TblNihongoUserInfo();
		info.Set("user_id", user_id);
		
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		return (TblNihongoUserInfo) list.get(0);
	}
	
}

