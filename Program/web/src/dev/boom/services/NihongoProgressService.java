package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblNihongoProgressInfo;
import dev.boom.tbl.info.TblNihongoUserInfo;

public class NihongoProgressService {

	public static final int MAX_PROGRESS = 99;
	
	private NihongoProgressService() {
	}

	public static TblNihongoProgressInfo getProgress(int test_id, long user_id) {
		TblNihongoProgressInfo info = new TblNihongoProgressInfo();
		info.Set("user_id", user_id);
		info.Set("test_id", test_id);

		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() != 1) {
			return null;
		}

		return (TblNihongoProgressInfo) list.get(0);
	}

	public static List<TblNihongoProgressInfo> getUserProgressList(long user_id) {
		TblNihongoProgressInfo info = new TblNihongoProgressInfo();
		info.Set("user_id", user_id);

		List<DaoValue> list = CommonDaoFactory.Select(info);
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
			if ((Integer)info.Get("progress") == MAX_PROGRESS || ((Integer)info.Get("progress") > progress && progress < MAX_PROGRESS)) {
				return false;
			}
			info.Set("updated", CommonMethod.getFormatStringNow());
		} else {
			info = new TblNihongoProgressInfo();
			info.Set("user_id", user_id);
			info.Set("test_id", test_id);
		}
		info.Set("progress", progress);
		updates.add(info);
		if (progress == MAX_PROGRESS) {
			nihonUser.Set("star", (Integer)nihonUser.Get("star") + 1);
			updates.add(nihonUser);
		}

		return (CommonDaoFactory.Update(updates) > 0);
	}

	public static boolean insertProgress(long user_id, int test_id, int progress) {
		TblNihongoUserInfo nihonUser = NihongoUserService.getNihongoUserInfo(user_id);
		if (nihonUser == null) {
			return false;
		}
		List<DaoValue> updates = new ArrayList<>();
		TblNihongoProgressInfo info = new TblNihongoProgressInfo();
		info.Set("user_id", user_id);
		info.Set("test_id", test_id);
		info.Set("progress", progress);
		updates.add(info);
		if (progress == MAX_PROGRESS) {
			nihonUser.Set("star", (Integer)nihonUser.Get("star") + 1);
			updates.add(nihonUser);
		}
		return (CommonDaoFactory.Update(updates) > 0);
	}
	
}

