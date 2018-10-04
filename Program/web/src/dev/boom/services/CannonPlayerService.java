package dev.boom.services;

import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblCannonPlayerInfo;


public class CannonPlayerService {
	
	public static CannonPlayerInfo getCannonPlayerInfo(long user_id) {
		TblCannonPlayerInfo info = new TblCannonPlayerInfo();
		info.setUser_id(user_id);
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new CannonPlayerInfo((TblCannonPlayerInfo) list.get(0));
	}
}
