package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblBoomSeasonInfo;

public class BoomSeasonService {

	private BoomSeasonService() {
	}

	public static List<BoomSeason> getBoomSeasonListAll(String option) {
		TblBoomSeasonInfo tblInfo = new TblBoomSeasonInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<BoomSeason> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new BoomSeason((TblBoomSeasonInfo) dao));
		}

		return ret;
	}

	public static List<BoomSeason> getBoomSeasonListAll() {
		return getBoomSeasonListAll(null);
	}
	
	public static BoomSeason getCurrentBoomSeason() {
		TblBoomSeasonInfo tblInfo = new TblBoomSeasonInfo();
		tblInfo.SetSelectOption("WHERE start_time <= NOW() AND end_time > NOW() ORDER BY start_time DESC LIMIT 1");
		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return new BoomSeason((TblBoomSeasonInfo) list.get(0));
	}
	
	public static BoomSeason getBoomSeasonById(long id) {
		TblBoomSeasonInfo tblInfo = new TblBoomSeasonInfo();
		tblInfo.Set("id", id);
		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return new BoomSeason((TblBoomSeasonInfo) list.get(0));
	}
}

