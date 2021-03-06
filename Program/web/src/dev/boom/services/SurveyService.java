package dev.boom.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.data.TblSurveyValidCodeData;
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
	
	public static List<SurveyInfo> getSurveyList() {
		TblSurveyInfo info = new TblSurveyInfo();
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<SurveyInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyInfo((TblSurveyInfo) dao));
		}
		
		return ret;
	}
	
	public static SurveyInfo getSurveyById(long id) {
		TblSurveyInfo info = new TblSurveyInfo();
		info.setId(id);
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
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
	
	public static SurveyOptionInfo getSurveyOptionById(long id) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.setId(id);
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		return new SurveyOptionInfo((TblSurveyOptionInfo) list.get(0));
	}
	
	public static boolean isExistOptionImgFileName(String imgName) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.setImage(imgName);
		return (CommonDaoService.count(info) > 0);
	}
	
	public static List<SurveyOptionInfo> getSurveyOptionList(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		String option = "";
		for (Long id : ids) {
			if (!option.isEmpty()) {
				option += ",";
			}
			option += id;
		}
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.setSelectOption("WHERE id IN(" + option + ")");
		
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
	
	public static SurveyResultInfo getSurveyResult(String user, long survey_id) {
		TblSurveyResultInfo info = new TblSurveyResultInfo();
		info.setUser(user);
		info.setSurvey_id(survey_id);
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			GameLog.getInstance().error("There are more than one result record!");
		}
		
		return new SurveyResultInfo((TblSurveyResultInfo) list.get(0));
		
	}
	
	public static long getCountSurveyResult(long survey_id) {
		TblSurveyResultInfo info = new TblSurveyResultInfo();
		info.setSurvey_id(survey_id);
		
		return CommonDaoService.count(info);
	}
	
//	public static List<SurveyOptionInfo> calculateSurveyResult(long survey_id) {
//		List<SurveyOptionInfo> listOptions = getSurveyOptionList(survey_id);
//		if (listOptions == null || listOptions.isEmpty()) {
//			return null;
//		}
//		String sql = "SELECT GROUP_CONCAT(result SEPARATOR ';') AS results from survey_result_info where survey_id = " + survey_id;
//		List<String> commands = new ArrayList<>();
//		commands.add(new String("SET SESSION group_concat_max_len = 2048"));
//		List<Object> results = CommonDaoService.executeNativeSQLQuery(sql);
//		if (results != null && !results.isEmpty()) {
//			String strResults = (String) results.get(0);
//			if (strResults != null) {
//				String[] ids = strResults.split(",");
//				for (String optId : ids) {
//					if (!optId.matches("^[0-9]+$")) {
//						continue;
//					}
//					int serialNum = listOptions.size();
//					for (SurveyOptionInfo surveyOption : listOptions) {	
//						if (surveyOption.getId() == Long.parseLong(optId)) {
//							surveyOption.setSelectedCount(surveyOption.getSelectedCount() + 1);
//							surveyOption.setTotalPoint(surveyOption.getTotalPoint() + serialNum);
//							serialNum--;
//							break;
//						}
//					}
//					
//				}
//			}
//		}
//		
//		return listOptions;
//	}
	
	public static List<SurveyOptionInfo> calculateSurveyResult(long survey_id) {
		List<SurveyOptionInfo> listOptions = getSurveyOptionList(survey_id);
		if (listOptions == null || listOptions.isEmpty()) {
			return null;
		}
		
		Map<Long, SurveyOptionInfo> mapSurveyOption = new HashMap<>();
		for(SurveyOptionInfo surveyOption : listOptions) {
			mapSurveyOption.put(surveyOption.getId(), surveyOption);
		}
		
		List<SurveyResultInfo> listResultSurvey = getSurveyResultBySurveyId(survey_id);
		
		if (listResultSurvey == null || listResultSurvey.isEmpty()) {
			return null;
		}
		
		for(SurveyResultInfo surveyResult : listResultSurvey) {
			String[] ids = surveyResult.getResult().split(",");
			int serialNum = ids.length;
			for (String optId : ids) {
				if (!optId.matches("^[0-9]+$")) {
					continue;
				}
				SurveyOptionInfo surveyOption = mapSurveyOption.get(Long.parseLong(optId));
				surveyOption.setSelectedCount(surveyOption.getSelectedCount() + 1);
				surveyOption.setTotalPoint(surveyOption.getTotalPoint() + serialNum);
				serialNum--;
			}
		}
		
		return listOptions;
	}
	public static long getCountValidSurveyOption(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return 0;
		}
		String options = "";
		for (long id : ids) {
			if (!options.isEmpty()) {
				options += ",";
			}
			options += id;
		}
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.setSelectOption("WHERE id IN(" + options + ")");
		
		return CommonDaoService.count(info);
	}
	
	public static boolean isExistSurveyResult(String user) {
		TblSurveyResultInfo info = new TblSurveyResultInfo();
		info.setUser(user);
		
		long count = CommonDaoService.count(info);
		
		return (count > 0);
	}
	
	public static boolean isValidUserCode(String userCode) {
		TblSurveyValidCodeData infoData = new TblSurveyValidCodeData();
		infoData.Set("code", userCode);
		List<DaoValue> list = CommonDaoService.select(infoData);
		if (list == null || list.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public static List<SurveyValidCodeData> getValidCodeList() {
		TblSurveyValidCodeData infoData = new TblSurveyValidCodeData();
		List<DaoValue> list = CommonDaoService.select(infoData);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<SurveyValidCodeData> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyValidCodeData((TblSurveyValidCodeData) dao));
		}
		
		return ret;
	}
	
	public static SurveyValidCodeData getSurveyValidData(String code) {
		TblSurveyValidCodeData infoData = new TblSurveyValidCodeData();
		infoData.Set("code", code);
		List<DaoValue> list = CommonDaoService.select(infoData);
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return new SurveyValidCodeData((TblSurveyValidCodeData) list.get(0));
	}
}
