package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblBoomStageInfo;

public class BoomStageService {

	private BoomStageService() {
	}

	public static List<BoomStage> getBoomStageListAll(String option) {
		TblBoomStageInfo tblInfo = new TblBoomStageInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<BoomStage> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new BoomStage((TblBoomStageInfo) dao));
		}

		return ret;
	}

	public static List<BoomStage> getBoomStageListAll() {
		return getBoomStageListAll(null);
	}
	
	public static BoomStage getBoomStage(long id) {
		TblBoomStageInfo tblInfo = new TblBoomStageInfo();
		tblInfo.Set("id", id);

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		return new BoomStage((TblBoomStageInfo) list.get(0));
	}
	
}

