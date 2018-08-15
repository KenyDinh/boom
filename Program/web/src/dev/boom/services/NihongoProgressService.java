package dev.boom.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblNihongoProgressInfo;
import dev.boom.tbl.info.TblNihongoUserInfo;

public class NihongoProgressService {

	public static final int MAX_PROGRESS = 99;

	public static TblNihongoProgressInfo getProgress(int test_id, long user_id) {
		TblNihongoProgressInfo info = new TblNihongoProgressInfo();
		info.setUser_id(user_id);
		info.setTest_id(test_id);

		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}

		return (TblNihongoProgressInfo) list.get(0);
	}

	public static List<TblNihongoProgressInfo> getUserProgressList(long user_id) {
		TblNihongoProgressInfo info = new TblNihongoProgressInfo();
		info.setUser_id(user_id);

		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() == 0) {
			return null;
		}

		List<TblNihongoProgressInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblNihongoProgressInfo) dao);
		}

		return ret;
	}

	public static boolean updateProgress(int test_id, long user_id, int progress) {
		TblNihongoProgressInfo info = getProgress(test_id, user_id);
		TblNihongoUserInfo nihonUser = NihongoUserService.getNihongoUserInfo(user_id);
		if (nihonUser == null) {
			return false;
		}
		List<DaoValue> updates = new ArrayList<>();
		if (info != null) {
			if (info.getProgress() == MAX_PROGRESS || (info.getProgress() > progress && progress < MAX_PROGRESS)) {
				return false;
			}
			info.setUpdated(new Date());
		} else {
			info = new TblNihongoProgressInfo();
			info.setUser_id(user_id);
			info.setTest_id(test_id);
		}
		info.setProgress(progress);
		updates.add(info);
		if (progress == MAX_PROGRESS) {
			nihonUser.setStar(nihonUser.getStar() + 1);
			updates.add(nihonUser);
		}

		return CommonDaoService.update(updates);
	}

	public static boolean insertProgress(long user_id, int test_id, int progress) {
		TblNihongoUserInfo nihonUser = NihongoUserService.getNihongoUserInfo(user_id);
		if (nihonUser == null) {
			return false;
		}
		List<DaoValue> updates = new ArrayList<>();
		TblNihongoProgressInfo info = new TblNihongoProgressInfo();
		info.setUser_id(user_id);
		info.setTest_id(test_id);
		info.setProgress(progress);
		updates.add(info);
		if (progress == MAX_PROGRESS) {
			nihonUser.setStar(nihonUser.getStar() + 1);
			updates.add(nihonUser);
		}
		return CommonDaoService.update(updates);
	}
}
