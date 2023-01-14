package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblMilkteaUserInfo;

public class MilkteaUserService {

	private MilkteaUserService() {
	}

	public static MilkteaUser getMilkTeaUserInfoById(long user_id) {
		TblMilkteaUserInfo info = new TblMilkteaUserInfo();
		info.Set("user_id", user_id);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new MilkteaUser((TblMilkteaUserInfo) list.get(0));
	}
	
	public static List<MilkteaUser> getMilkTeaUserInfoById(List<Long> ids) {
		TblMilkteaUserInfo info = new TblMilkteaUserInfo();
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
			info.SetSelectOption("WHERE user_id IN (" + option + ")");
		}
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() == 0) {
			return null;
		}
		List<MilkteaUser> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new MilkteaUser((TblMilkteaUserInfo) dao));
		}
		return ret;
	}
	
	public static List<MilkteaUser> getMilkteaUserInfo(String option) {
		TblMilkteaUserInfo info = new TblMilkteaUserInfo();
		if (option != null && option.length() > 0) {
			info.SetSelectOption(option);
		}
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() == 0) {
			return null;
		}
		List<MilkteaUser> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new MilkteaUser((TblMilkteaUserInfo) dao));
		}
		return ret;
	}
	
}

