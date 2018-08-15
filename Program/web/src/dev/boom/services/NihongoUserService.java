package dev.boom.services;

import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblNihongoUserInfo;

public class NihongoUserService {

	
	public static TblNihongoUserInfo getNihongoUserInfo(long user_id) {
		TblNihongoUserInfo info = new TblNihongoUserInfo();
		info.setUser_id(user_id);
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		return (TblNihongoUserInfo) list.get(0);
	}
}
