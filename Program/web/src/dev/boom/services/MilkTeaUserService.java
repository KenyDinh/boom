package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.MilkTeaUserInfo;

public class MilkTeaUserService {

	public static MilkTeaUserInfo getFridayUserInfoByUserId(long user_id) {
		MilkTeaUserInfo info = new MilkTeaUserInfo();
		info.setUser_id(user_id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		return (MilkTeaUserInfo) list.get(0);
	}
	
	public static List<MilkTeaUserInfo> getFridayUserInfoByIdList(List<Long> ids) {
		MilkTeaUserInfo info = new MilkTeaUserInfo();
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
			ret.add((MilkTeaUserInfo) dao);
		}
		return ret;
	}
}
