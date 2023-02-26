package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblBoomPlayerStageInfo;

public class BoomPlayerStageService {

	private BoomPlayerStageService() {
	}

	public static List<BoomPlayerStage> getBoomPlayerStageListAll(String option) {
		TblBoomPlayerStageInfo tblInfo = new TblBoomPlayerStageInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<BoomPlayerStage> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new BoomPlayerStage((TblBoomPlayerStageInfo) dao));
		}

		return ret;
	}

	public static List<BoomPlayerStage> getBoomPlayerStageListAll() {
		return getBoomPlayerStageListAll(null);
	}
	
	public static BoomPlayerStage getBoomPlayerStage(long playerId, long stageId) {
		if (stageId <= 0) {
			return null;
		}
		TblBoomPlayerStageInfo tblInfo = new TblBoomPlayerStageInfo();
		tblInfo.Set("stage_id", stageId);
		tblInfo.Set("player_id", playerId);
		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		return new BoomPlayerStage((TblBoomPlayerStageInfo) list.get(0));
	}
	
	public static BoomPlayerStage getBoomPlayerStageById(long id) {
		TblBoomPlayerStageInfo tblInfo = new TblBoomPlayerStageInfo();
		tblInfo.Set("id", id);
		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		return new BoomPlayerStage((TblBoomPlayerStageInfo) list.get(0));
	}
	
	public static boolean hasAccess(long playerId, long stageId) {
		if (stageId <= 0) {
			return true;
		}
		TblBoomPlayerStageInfo tblInfo = new TblBoomPlayerStageInfo();
		tblInfo.Set("stage_id", stageId);
		tblInfo.Set("player_id", playerId);
		return (CommonDaoFactory.Count(tblInfo) > 0);
	}
}

