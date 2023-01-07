package dev.boom.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonMethod;
import dev.boom.common.enums.SurveyQuestionType;
import dev.boom.common.enums.SurveyStatus;
import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.services.json.SurveyAnswer;
import dev.boom.services.json.SurveyAnswerWrapper;
import dev.boom.services.json.SurveyOptionStatistics;
import dev.boom.services.json.SurveyResultObject;
import dev.boom.tbl.info.TblSurveyInfo;
import dev.boom.tbl.info.TblSurveyOptionInfo;
import dev.boom.tbl.info.TblSurveyQuestionInfo;
import dev.boom.tbl.info.TblSurveyResultInfo;
import dev.boom.tbl.info.TblSurveyUserAccessInfo;

public class SurveyService {
	
	public static final String YES_NO_OPTION_YES_TEMPL = "Có";
	public static final String YES_NO_OPTION_NO_TEMPL = "Không";
	
	private SurveyService() {
	}
	
	public static TblSurveyInfo getSurveyInfoByPathName(String name) {
		TblSurveyInfo info = new TblSurveyInfo();
		info.Set("pathname", name);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			GameLog.getInstance().error("[getActiveSurveyInfo] there are mone than one survey actived!");
		}
		
		return (TblSurveyInfo) list.get(0);
	}
	
	public static List<TblSurveyInfo> getSurveyInfoList() {
		TblSurveyInfo info = new TblSurveyInfo();
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<TblSurveyInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblSurveyInfo) dao);
		}
		
		return ret;
	}
	
	public static List<TblSurveyInfo> getSurveyInfoListForDisplay(boolean isCompleted) {
		TblSurveyInfo info = new TblSurveyInfo();
		if (!isCompleted) {
			info.setSelectOption("WHERE status <> " + SurveyStatus.FINISHED.ordinal());
		}
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<TblSurveyInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblSurveyInfo) dao);
		}
		
		return ret;
	}
	
	public static TblSurveyInfo getSurveyInfoById(long id) {
		TblSurveyInfo info = new TblSurveyInfo();
		info.Set("id", id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		return (TblSurveyInfo) list.get(0);
	}
	
	public static List<TblSurveyInfo> getActiveSurveyList(int flag) {
		TblSurveyInfo info = new TblSurveyInfo();
		info.setStatus((byte)SurveyStatus.IN_SESSION.ordinal());
		if (flag <= 0) {
		} else {
			info.setSelectOption("AND (flag = 0 OR (flag & " + flag + ") <> 0)");
		}
		info.setSelectOption("AND expired > NOW()");
		info.setSelectOption("ORDER BY id DESC");
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<TblSurveyInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblSurveyInfo) dao);
		}
		
		return ret;
	}
	
	public static TblSurveyInfo getActiveSurveyInfo() {
		TblSurveyInfo info = new TblSurveyInfo();
		info.setStatus((byte)SurveyStatus.IN_SESSION.ordinal());
		info.setSelectOption("AND expired > NOW()");
		info.setSelectOption("ORDER BY id DESC");
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			GameLog.getInstance().error("[getActiveSurveyInfo] there are mone than one survey actived!");
		}
		
		return (TblSurveyInfo) list.get(0);
	}
	
	public static TblSurveyResultInfo getSurveyResult(long surveyId, String userId) {
		TblSurveyResultInfo info = new TblSurveyResultInfo();
		info.Set("survey_id", surveyId);
		info.Set("user_id", userId);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		return (TblSurveyResultInfo) list.get(0);
	}
	
	public static List<TblSurveyResultInfo> getSurveyResultList(long surveyId) {
		TblSurveyResultInfo info = new TblSurveyResultInfo();
		info.Set("survey_id", surveyId);
//		info.setSelectOption("AND progress <> " + PROGRESS_STOP);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<TblSurveyResultInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblSurveyResultInfo) dao);
		}
		
		return ret;
	}
	
	public static List<TblSurveyQuestionInfo> getSurveyQuestionList(long surveyId, String option) {
		TblSurveyQuestionInfo info = new TblSurveyQuestionInfo();
		info.Set("survey_id", surveyId);
		if (StringUtils.isNotEmpty(option)) {
			info.setSelectOption(option);
		}
		info.setSelectOption("ORDER BY idx ASC");
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<TblSurveyQuestionInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblSurveyQuestionInfo) dao);
		}
		return ret;
	}
	
	public static List<TblSurveyQuestionInfo> getSurveyQuestionList(long surveyId) {
		return getSurveyQuestionList(surveyId, null);
	}
	
	public static Map<Byte, TblSurveyQuestionInfo> getSurveyQuestionMapByIndexs(long surveyId, List<Byte> indexs) {
		TblSurveyQuestionInfo info = new TblSurveyQuestionInfo();
		info.Set("survey_id", surveyId);
		StringBuilder option = new StringBuilder();
		for (byte index : indexs) {
			if (option.length() > 0) {
				option.append(",");
			}
			option.append(index);
		}
		info.setSelectOption("AND idx IN (" + option.toString() + ")");
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		Map<Byte, TblSurveyQuestionInfo> ret = new HashMap<>();
		for (DaoValue dao : list) {
			Byte index = (Byte) dao.Get("idx");
			ret.put(index, (TblSurveyQuestionInfo) dao);
		}
		
		return ret;
	}
	
	public static TblSurveyQuestionInfo getSurveyQuestion(long surveyId, byte index) {
		TblSurveyQuestionInfo info = new TblSurveyQuestionInfo();
		info.Set("survey_id", surveyId);
		info.Set("idx", index);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<TblSurveyQuestionInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblSurveyQuestionInfo) dao);
		}
		return (TblSurveyQuestionInfo) list.get(0);
	}
	
	public static TblSurveyQuestionInfo getSurveyQuestionById(long id) {
		TblSurveyQuestionInfo info = new TblSurveyQuestionInfo();
		info.Set("id", id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<TblSurveyQuestionInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblSurveyQuestionInfo) dao);
		}
		return (TblSurveyQuestionInfo) list.get(0);
	}
	
	public static long getCountSurveyQuestion(long surveyId) {
		TblSurveyQuestionInfo info = new TblSurveyQuestionInfo();
		info.Set("survey_id", surveyId);
		return CommonDaoService.count(info);
	}
	
	public static long getCountValidSurveyOption(List<Long> ids) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.setSelectOption("WHERE id > 0");
		StringBuilder sb = new StringBuilder();
		for (long id : ids) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}
		if (sb.length() > 0) {
			info.setSelectOption("AND id IN (" + sb.toString() + ")");
		}
		
		return CommonDaoService.count(info);
	}
	
	public static TblSurveyOptionInfo getSurveyOptionById(long id) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.Set("id", id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		return (TblSurveyOptionInfo) list.get(0);
	}
	
	public static List<TblSurveyOptionInfo> getSurveyOptionList(long questionId) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.Set("question_id", questionId);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<TblSurveyOptionInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblSurveyOptionInfo) dao);
		}
		
		return ret;
	}
	
	public static List<TblSurveyOptionInfo> getSurveyOptionList(List<Long> ids) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.setSelectOption("WHERE id > 0");
		StringBuilder sb = new StringBuilder();
		for (long id : ids) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}
		if (sb.length() > 0) {
			info.setSelectOption("AND id IN (" + sb.toString() + ")");
		}
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<TblSurveyOptionInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblSurveyOptionInfo) dao);
		}
		
		return ret;
	}
	
	public static Map<Long, TblSurveyOptionInfo> getSurveyOptionMapById(List<Long> ids) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.setSelectOption("WHERE id > 0");
		StringBuilder sb = new StringBuilder();
		for (long id : ids) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}
		if (sb.length() > 0) {
			info.setSelectOption("AND question_id IN (" + sb.toString() + ")");
		}
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		Map<Long, TblSurveyOptionInfo> ret = new HashMap<>();
		for (DaoValue dao : list) {
			long id = (Long) dao.Get("id");
			ret.put(id, (TblSurveyOptionInfo) dao);
		}
		
		return ret;
	}
	
	public static List<Map<String, Object>> calcSurveyOverralResult(List<TblSurveyQuestionInfo> questionList, TblSurveyInfo surveyInfo) {
		if (questionList == null || questionList.isEmpty() || surveyInfo == null) {
			return Collections.emptyList();
		}
		List<TblSurveyResultInfo> resultList = getSurveyResultList(surveyInfo.getId());
		if (resultList == null || resultList.isEmpty()) {
			return Collections.emptyList();
		}
		List<Long> questionIds = new ArrayList<>();
		Map<Byte, TblSurveyQuestionInfo> mapQuestion = new HashMap<>();
		for (TblSurveyQuestionInfo questionInfo : questionList) {
			questionIds.add(questionInfo.getId());
			mapQuestion.put(questionInfo.getIdx(), questionInfo);
		}
		Map<Long, TblSurveyOptionInfo> surveyOptionMap = getSurveyOptionMapById(questionIds);
		List<Map<String, Object>> ret = new ArrayList<>();
		for (TblSurveyResultInfo surveyResult : resultList) {
			String resultData = surveyResult.getResult();
			if (StringUtils.isEmpty(resultData)) {
				continue;
			}
			SurveyAnswerWrapper surveyAnswerWapper = SurveyAnswerWrapper.parse(resultData);
			List<SurveyAnswer> answerList = surveyAnswerWapper.getSurvey_answer();
			if (answerList == null || answerList.isEmpty()) {
				continue;
			}
			Map<String, Object> mapData = new HashMap<>();
			mapData.put("id", surveyResult.getId());
			mapData.put("user_id", surveyResult.getUser_id());
			mapData.put("username", surveyResult.getUsername());
			mapData.put("dep", surveyResult.getDepartment());
			StringBuilder sb = new StringBuilder();
			for (SurveyAnswer answer : answerList) {
				sb.setLength(0);
				byte questionIndex = answer.getQuestion_index();
				String strAnsw = answer.getAnswer();
				String key = String.format("question_%d", questionIndex);
				mapData.put(key, "");
				if (StringUtils.isNotEmpty(strAnsw)) {
					if (mapQuestion.containsKey(questionIndex)) {
						TblSurveyQuestionInfo questionInfo = mapQuestion.get(questionIndex);
						if (questionInfo.getType() == SurveyQuestionType.GIVING_ANSWER.ordinal()) {
							mapData.put(key, strAnsw);
						} else if (questionInfo.getType() == SurveyQuestionType.OPTION_SELECT.ordinal()) {
							String[] strOptionIds = strAnsw.split(",");
							for (String strOptionId : strOptionIds) {
								if (CommonMethod.isValidNumeric(strOptionId, 1, Long.MAX_VALUE)) {
									long optionId = Long.parseLong(strOptionId);
									if (surveyOptionMap.containsKey(optionId)) {
										if (sb.length() > 0) {
											sb.append(",");
										}
										sb.append(surveyOptionMap.get(optionId).getTitle());
									}
								}
							}
							if (sb.length() > 0) {
								mapData.put(key, sb.toString());
							}
						} else if (questionInfo.getType() == SurveyQuestionType.YES_NO.ordinal()) {
							if (strAnsw.equals(YES_NO_OPTION_NO_TEMPL)) {
								mapData.put(key, "No");
							} else if (strAnsw.equals(YES_NO_OPTION_YES_TEMPL)) {
								mapData.put(key, "Yes");
							} else {
								mapData.put(key, strAnsw);
							}
						} else if (questionInfo.getType() == SurveyQuestionType.NUMERAL_LIST.ordinal()) {
							mapData.put(key, strAnsw);
						} else if (questionInfo.getType() == SurveyQuestionType.DATE_PICKER.ordinal()) {
							mapData.put(key, strAnsw);
						} else if (questionInfo.getType() == SurveyQuestionType.OPTION_LIST.ordinal()) {
							mapData.put(key, strAnsw);
						} else {
							mapData.put(key, strAnsw);
						}
					}
				}
			}
			ret.add(mapData);
		}
		
		return ret;
	}
	
	public static Map<Byte, List<SurveyResultObject>> calcSurveyQuestResult(List<TblSurveyQuestionInfo> questionList, TblSurveyInfo surveyInfo) {
		return calcSurveyQuestResult(questionList, surveyInfo, null);
	}
	
	public static Map<Byte, List<SurveyResultObject>> calcSurveyQuestResult(List<TblSurveyQuestionInfo> questionList, TblSurveyInfo surveyInfo, Map<Long, SurveyOptionStatistics> statistics) {
		if (questionList == null || questionList.isEmpty() || surveyInfo == null) {
			return null;
		}
		List<TblSurveyResultInfo> resultList = getSurveyResultList(surveyInfo.getId());
		if (resultList == null || resultList.isEmpty()) {
			return null;
		}
		List<Long> questionIds = new ArrayList<>();
		Map<Byte, TblSurveyQuestionInfo> mapQuestion = new HashMap<>();
		//List<Long> listYesNoQuestionIds = new ArrayList<>();
		for (TblSurveyQuestionInfo questionInfo : questionList) {
			if (questionInfo.getType() == SurveyQuestionType.YES_NO.ordinal()) {
				//listYesNoQuestionIds.add(questionInfo.getId());
			} else {
				questionIds.add(questionInfo.getId());
			}
			mapQuestion.put(questionInfo.getIdx(), questionInfo);
		}
		Map<Long, TblSurveyOptionInfo> surveyOptionMap = getSurveyOptionMapById(questionIds);
		//
		int totalResult = resultList.size();
		if (statistics != null && surveyOptionMap != null) {
			for (long id : surveyOptionMap.keySet()) {
				statistics.put(id, new SurveyOptionStatistics(id, surveyOptionMap.get(id).getQuestion_id(), surveyOptionMap.get(id).getTitle(), 0, totalResult));
			}
		}
		Map<Byte, List<SurveyResultObject>> ret = new HashMap<>();
		for (TblSurveyResultInfo surveyResult : resultList) {
			String resultData = surveyResult.getResult();
			if (StringUtils.isEmpty(resultData)) {
				continue;
			}
			SurveyAnswerWrapper surveyAnswerWapper = SurveyAnswerWrapper.parse(resultData);
			List<SurveyAnswer> answerList = surveyAnswerWapper.getSurvey_answer();
			if (answerList == null || answerList.isEmpty()) {
				continue;
			}
			StringBuilder sb = new StringBuilder();
			for (SurveyAnswer answer : answerList) {
				sb.setLength(0);
				SurveyResultObject resultObject = new SurveyResultObject(surveyResult.getUser_id(), surveyResult.getUsername(), surveyResult.getDepartment(), "");
				byte questionIndex = answer.getQuestion_index();
				String strAnsw = answer.getAnswer();
				if (StringUtils.isNotEmpty(strAnsw)) {
					if (mapQuestion.containsKey(questionIndex)) {
						TblSurveyQuestionInfo questionInfo = mapQuestion.get(questionIndex);
						if (questionInfo.getType() == SurveyQuestionType.GIVING_ANSWER.ordinal()) {
							resultObject.setResult(strAnsw);
						} else if (questionInfo.getType() == SurveyQuestionType.OPTION_SELECT.ordinal()) {
							String[] strOptionIds = strAnsw.split(",");
							for (String strOptionId : strOptionIds) {
								if (CommonMethod.isValidNumeric(strOptionId, 1, Long.MAX_VALUE)) {
									long optionId = Long.parseLong(strOptionId);
									if (surveyOptionMap.containsKey(optionId)) {
										if (sb.length() > 0) {
											sb.append(", ");
										}
										sb.append(surveyOptionMap.get(optionId).getTitle());
										//
										if (statistics != null) {
											if (!statistics.containsKey(optionId)) {
												statistics.put(optionId, new SurveyOptionStatistics(optionId, surveyOptionMap.get(optionId).getQuestion_id(), surveyOptionMap.get(optionId).getTitle(), 1, totalResult));
											} else {
												statistics.get(optionId).addCount();
											}
										}
									}
								}
							}
							if (sb.length() > 0) {
								resultObject.setResult(sb.toString());
							}
						} else if (questionInfo.getType() == SurveyQuestionType.YES_NO.ordinal()) {
							if (strAnsw.equals(YES_NO_OPTION_NO_TEMPL)) {
								resultObject.setResult("No");
							} else if (strAnsw.equals(YES_NO_OPTION_YES_TEMPL)) {
								resultObject.setResult("Yes");
							} else {
								resultObject.setResult(strAnsw);
							}
						} else if (questionInfo.getType() == SurveyQuestionType.NUMERAL_LIST.ordinal()) {
							resultObject.setResult(strAnsw);
						} else if (questionInfo.getType() == SurveyQuestionType.DATE_PICKER.ordinal()) {
							resultObject.setResult(strAnsw);
						} else if (questionInfo.getType() == SurveyQuestionType.OPTION_LIST.ordinal()) {
							resultObject.setResult(strAnsw);
						} else {
							resultObject.setResult(strAnsw);
						}
					}
				}
				if (!ret.containsKey(questionIndex)) {
					ret.put(questionIndex, new ArrayList<>());
				}
				ret.get(questionIndex).add(resultObject);
			}
		}
		return ret;
	}
	
	public static TblSurveyUserAccessInfo getSurveyUserAccess(long surveyID, String userCode) {
		TblSurveyUserAccessInfo info = new TblSurveyUserAccessInfo();
		info.Set("survey_id", surveyID);
		info.Set("user_code", userCode);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return (TblSurveyUserAccessInfo) list.get(0);
	}
	
	public static List<TblSurveyUserAccessInfo> getSurveyUserAccessList(long surveyID) {
		return getSurveyUserAccessList(surveyID, null);
	}
	
	public static List<TblSurveyUserAccessInfo> getSurveyUserAccessList(long surveyID, String option) {
		TblSurveyUserAccessInfo info = new TblSurveyUserAccessInfo();
		info.Set("survey_id", surveyID);
		if (option != null && !option.isEmpty()) {
			info.setSelectOption(option);
		}
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<TblSurveyUserAccessInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblSurveyUserAccessInfo) dao);
		}
		return ret;
	}
	
}
