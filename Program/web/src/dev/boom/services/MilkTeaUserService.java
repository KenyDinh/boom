package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblMilkTeaUserInfo;

public class MilkTeaUserService {

	public static MilkTeaUserInfo getMilkTeaUserInfoById(long user_id) {
		TblMilkTeaUserInfo info = new TblMilkTeaUserInfo();
		info.setUser_id(user_id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new MilkTeaUserInfo((TblMilkTeaUserInfo) list.get(0));
	}
	
	public static List<MilkTeaUserInfo> getMilkTeaUserInfoById(List<Long> ids) {
		TblMilkTeaUserInfo info = new TblMilkTeaUserInfo();
		String option = "";
		if (ids != null) {
			for (Long id : ids) {
				if (!option.isEmpty()) {
					option += ",";
				}
				option += id.longValue();
			}
		}
		if (!option.isEmpty()) {
			info.setSelectOption("WHERE user_id IN (" + option + ")");
		}
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() == 0) {
			return null;
		}
		List<MilkTeaUserInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new MilkTeaUserInfo((TblMilkTeaUserInfo) dao));
		}
		return ret;
	}
}
