package dev.boom.services;

import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.NihongoUserInfo;

public class NihongoUserService {

	
	public static NihongoUserInfo getNihongoUserInfo(long user_id) {
		NihongoUserInfo info = new NihongoUserInfo();
		info.setUser_id(user_id);
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		return (NihongoUserInfo) list.get(0);
	}
}
