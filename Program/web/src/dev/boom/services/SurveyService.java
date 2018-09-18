package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblSurveyInfo;
import dev.boom.tbl.info.TblSurveyOptionInfo;
import dev.boom.tbl.info.TblSurveyResultInfo;

public class SurveyService {

	public static SurveyInfo getActiveSurveyInfo() {
		TblSurveyInfo info = new TblSurveyInfo();
		info.setStatus((byte)1);
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			GameLog.getInstance().error("[getActiveSurveyInfo] there are mone than one survey actived!");
		}
		
		return new SurveyInfo((TblSurveyInfo) list.get(0));
	}
	
	public static List<SurveyOptionInfo> getSurveyOptionList(long survey_id) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.setSurvey_id(survey_id);
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		List<SurveyOptionInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyOptionInfo((TblSurveyOptionInfo) dao));
		}
		
		return ret;
	}
	
	public static List<SurveyResultInfo> getSurveyResultBySurveyId(long survey_id) {
		TblSurveyResultInfo info = new TblSurveyResultInfo();
		info.setSurvey_id(survey_id);
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		List<SurveyResultInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyResultInfo((TblSurveyResultInfo) dao));
		}
		
		return ret;
	}
	
	public static SurveyResultInfo getSurveyResultByUser(String user) {
		TblSurveyResultInfo info = new TblSurveyResultInfo();
		info.setUser(user);
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return new SurveyResultInfo((TblSurveyResultInfo) list.get(0));
		
	}
	
	public static boolean isExistSurveyResult(String user) {
		TblSurveyResultInfo info = new TblSurveyResultInfo();
		info.setUser(user);
		
		long count = CommonDaoService.count(info);
		
		return (count > 0);
	}
	
	public static boolean isValidUserCode(String userCode) {
		return true;
	}
}
