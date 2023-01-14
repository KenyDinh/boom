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
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
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
	
	public static Survey getSurveyInfoByPathName(String name) {
		TblSurveyInfo info = new TblSurveyInfo();
		info.Set("pathname", name);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			GameLog.getInstance().error("[getActiveSurveyInfo] there are mone than one survey actived!");
		}
		
		return new Survey((TblSurveyInfo) list.get(0));
	}
	
	public static List<Survey> getSurveyInfoList() {
		TblSurveyInfo info = new TblSurveyInfo();
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<Survey> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Survey((TblSurveyInfo) dao));
		}
		
		return ret;
	}
	
	public static List<Survey> getSurveyInfoListForDisplay(boolean isCompleted) {
		TblSurveyInfo info = new TblSurveyInfo();
		if (!isCompleted) {
			info.SetSelectOption("WHERE status <> " + SurveyStatus.FINISHED.ordinal());
		}
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<Survey> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Survey((TblSurveyInfo) dao));
		}
		
		return ret;
	}
	
	public static Survey getSurveyInfoById(long id) {
		TblSurveyInfo info = new TblSurveyInfo();
		info.Set("id", id);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		return new Survey((TblSurveyInfo) list.get(0));
	}
	
	public static List<Survey> getActiveSurveyList(int flag) {
		TblSurveyInfo info = new TblSurveyInfo();
		info.Set("status", (byte)SurveyStatus.IN_SESSION.ordinal());
		if (flag <= 0) {
		} else {
			info.SetSelectOption("AND (flag = 0 OR (flag & " + flag + ") <> 0)");
		}
		info.SetSelectOption("AND expired > NOW()");
		info.SetSelectOption("ORDER BY id DESC");
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<Survey> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Survey((TblSurveyInfo) dao));
		}
		
		return ret;
	}
	
	public static Survey getActiveSurveyInfo() {
		TblSurveyInfo info = new TblSurveyInfo();
		info.Set("status", (byte)SurveyStatus.IN_SESSION.ordinal());
		info.SetSelectOption("AND expired > NOW()");
		info.SetSelectOption("ORDER BY id DESC");
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			GameLog.getInstance().error("[getActiveSurveyInfo] there are mone than one survey actived!");
		}
		
		return new Survey((TblSurveyInfo) list.get(0));
	}
	
	public static SurveyResult getSurveyResult(long surveyId, String userId) {
		TblSurveyResultInfo info = new TblSurveyResultInfo();
		info.Set("survey_id", surveyId);
		info.Set("user_id", userId);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		return new SurveyResult((TblSurveyResultInfo) list.get(0));
	}
	
	public static List<SurveyResult> getSurveyResultList(long surveyId) {
		TblSurveyResultInfo info = new TblSurveyResultInfo();
		info.Set("survey_id", surveyId);
//		info.SetSelectOption("AND progress <> " + PROGRESS_STOP);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<SurveyResult> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyResult((TblSurveyResultInfo) dao));
		}
		
		return ret;
	}
	
	public static List<SurveyQuestion> getSurveyQuestionList(long surveyId, String option) {
		TblSurveyQuestionInfo info = new TblSurveyQuestionInfo();
		info.Set("survey_id", surveyId);
		if (StringUtils.isNotEmpty(option)) {
			info.SetSelectOption(option);
		}
		info.SetSelectOption("ORDER BY idx ASC");
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<SurveyQuestion> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyQuestion((TblSurveyQuestionInfo) dao));
		}
		return ret;
	}
	
	public static List<SurveyQuestion> getSurveyQuestionList(long surveyId) {
		return getSurveyQuestionList(surveyId, null);
	}
	
	public static Map<Byte, SurveyQuestion> getSurveyQuestionMapByIndexs(long surveyId, List<Byte> indexs) {
		TblSurveyQuestionInfo info = new TblSurveyQuestionInfo();
		info.Set("survey_id", surveyId);
		StringBuilder option = new StringBuilder();
		for (byte index : indexs) {
			if (option.length() > 0) {
				option.append(",");
			}
			option.append(index);
		}
		info.SetSelectOption("AND idx IN (" + option.toString() + ")");
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		Map<Byte, SurveyQuestion> ret = new HashMap<>();
		for (DaoValue dao : list) {
			Byte index = (Byte) dao.Get("idx");
			ret.put(index, new SurveyQuestion((TblSurveyQuestionInfo) dao));
		}
		
		return ret;
	}
	
	public static SurveyQuestion getSurveyQuestion(long surveyId, byte index) {
		TblSurveyQuestionInfo info = new TblSurveyQuestionInfo();
		info.Set("survey_id", surveyId);
		info.Set("idx", index);
		
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}

		return new SurveyQuestion((TblSurveyQuestionInfo) list.get(0));
	}
	
	public static SurveyQuestion getSurveyQuestionById(long id) {
		TblSurveyQuestionInfo info = new TblSurveyQuestionInfo();
		info.Set("id", id);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<TblSurveyQuestionInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblSurveyQuestionInfo) dao);
		}
		return new SurveyQuestion((TblSurveyQuestionInfo) list.get(0));
	}
	
	public static long getCountSurveyQuestion(long surveyId) {
		TblSurveyQuestionInfo info = new TblSurveyQuestionInfo();
		info.Set("survey_id", surveyId);
		return CommonDaoFactory.Count(info);
	}
	
	public static long getCountValidSurveyOption(List<Long> ids) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.SetSelectOption("WHERE id > 0");
		StringBuilder sb = new StringBuilder();
		for (long id : ids) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}
		if (sb.length() > 0) {
			info.SetSelectOption("AND id IN (" + sb.toString() + ")");
		}
		
		return CommonDaoFactory.Count(info);
	}
	
	public static SurveyOption getSurveyOptionById(long id) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.Set("id", id);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		return new SurveyOption((TblSurveyOptionInfo) list.get(0));
	}
	
	public static List<SurveyOption> getSurveyOptionList(long questionId) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.Set("question_id", questionId);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<SurveyOption> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyOption((TblSurveyOptionInfo) dao));
		}
		
		return ret;
	}
	
	public static List<SurveyOption> getSurveyOptionList(List<Long> ids) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.SetSelectOption("WHERE id > 0");
		StringBuilder sb = new StringBuilder();
		for (long id : ids) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}
		if (sb.length() > 0) {
			info.SetSelectOption("AND id IN (" + sb.toString() + ")");
		}
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<SurveyOption> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyOption((TblSurveyOptionInfo) dao));
		}
		
		return ret;
	}
	
	public static Map<Long, SurveyOption> getSurveyOptionMapById(List<Long> ids) {
		TblSurveyOptionInfo info = new TblSurveyOptionInfo();
		info.SetSelectOption("WHERE id > 0");
		StringBuilder sb = new StringBuilder();
		for (long id : ids) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}
		if (sb.length() > 0) {
			info.SetSelectOption("AND question_id IN (" + sb.toString() + ")");
		}
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		Map<Long, SurveyOption> ret = new HashMap<>();
		for (DaoValue dao : list) {
			long id = (Long) dao.Get("id");
			ret.put(id, new SurveyOption((TblSurveyOptionInfo) dao));
		}
		
		return ret;
	}
	
	public static List<Map<String, Object>> calcSurveyOverralResult(List<SurveyQuestion> questionList, Survey surveyInfo) {
		if (questionList == null || questionList.isEmpty() || surveyInfo == null) {
			return Collections.emptyList();
		}
		List<SurveyResult> resultList = getSurveyResultList(surveyInfo.getId());
		if (resultList == null || resultList.isEmpty()) {
			return Collections.emptyList();
		}
		List<Long> questionIds = new ArrayList<>();
		Map<Byte, SurveyQuestion> mapQuestion = new HashMap<>();
		for (SurveyQuestion questionInfo : questionList) {
			questionIds.add(questionInfo.getId());
			mapQuestion.put(questionInfo.getIdx(), questionInfo);
		}
		Map<Long, SurveyOption> surveyOptionMap = getSurveyOptionMapById(questionIds);
		List<Map<String, Object>> ret = new ArrayList<>();
		for (SurveyResult surveyResult : resultList) {
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
			mapData.put("user_id", surveyResult.getUserId());
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
						SurveyQuestion questionInfo = mapQuestion.get(questionIndex);
						int questiontype = questionInfo.getType();
						if (questiontype == SurveyQuestionType.GIVING_ANSWER.ordinal()) {
							mapData.put(key, strAnsw);
						} else if (questiontype == SurveyQuestionType.OPTION_SELECT.ordinal()) {
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
						} else if (questiontype == SurveyQuestionType.YES_NO.ordinal()) {
							if (strAnsw.equals(YES_NO_OPTION_NO_TEMPL)) {
								mapData.put(key, "No");
							} else if (strAnsw.equals(YES_NO_OPTION_YES_TEMPL)) {
								mapData.put(key, "Yes");
							} else {
								mapData.put(key, strAnsw);
							}
						} else if (questiontype == SurveyQuestionType.NUMERAL_LIST.ordinal()) {
							mapData.put(key, strAnsw);
						} else if (questiontype == SurveyQuestionType.DATE_PICKER.ordinal()) {
							mapData.put(key, strAnsw);
						} else if (questiontype == SurveyQuestionType.OPTION_LIST.ordinal()) {
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
	
	public static Map<Byte, List<SurveyResultObject>> calcSurveyQuestResult(List<SurveyQuestion> questionList, Survey surveyInfo) {
		return calcSurveyQuestResult(questionList, surveyInfo, null);
	}
	
	public static Map<Byte, List<SurveyResultObject>> calcSurveyQuestResult(List<SurveyQuestion> questionList, Survey surveyInfo, Map<Long, SurveyOptionStatistics> statistics) {
		if (questionList == null || questionList.isEmpty() || surveyInfo == null) {
			return null;
		}
		List<SurveyResult> resultList = getSurveyResultList(surveyInfo.getId());
		if (resultList == null || resultList.isEmpty()) {
			return null;
		}
		List<Long> questionIds = new ArrayList<>();
		Map<Byte, SurveyQuestion> mapQuestion = new HashMap<>();
		//List<Long> listYesNoQuestionIds = new ArrayList<>();
		for (SurveyQuestion questionInfo : questionList) {
			if (questionInfo.getType() == SurveyQuestionType.YES_NO.ordinal()) {
				//listYesNoQuestionIds.add(questionInfo.getId());
			} else {
				questionIds.add(questionInfo.getId());
			}
			mapQuestion.put(questionInfo.getIdx(), questionInfo);
		}
		Map<Long, SurveyOption> surveyOptionMap = getSurveyOptionMapById(questionIds);
		//
		int totalResult = resultList.size();
		if (statistics != null && surveyOptionMap != null) {
			for (long id : surveyOptionMap.keySet()) {
				statistics.put(id, new SurveyOptionStatistics(id, surveyOptionMap.get(id).getQuestionId(), surveyOptionMap.get(id).getTitle(), 0, totalResult));
			}
		}
		Map<Byte, List<SurveyResultObject>> ret = new HashMap<>();
		for (SurveyResult surveyResult : resultList) {
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
				SurveyResultObject resultObject = new SurveyResultObject(surveyResult.getUserId(), surveyResult.getUsername(), surveyResult.getDepartment(), "");
				byte questionIndex = answer.getQuestion_index();
				String strAnsw = answer.getAnswer();
				if (StringUtils.isNotEmpty(strAnsw)) {
					if (mapQuestion.containsKey(questionIndex)) {
						SurveyQuestion questionInfo = mapQuestion.get(questionIndex);
						int questionType = questionInfo.getType();
						if (questionType == SurveyQuestionType.GIVING_ANSWER.ordinal()) {
							resultObject.setResult(strAnsw);
						} else if (questionType == SurveyQuestionType.OPTION_SELECT.ordinal()) {
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
												statistics.put(optionId, new SurveyOptionStatistics(optionId, surveyOptionMap.get(optionId).getQuestionId(), surveyOptionMap.get(optionId).getTitle(), 1, totalResult));
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
						} else if (questionType == SurveyQuestionType.YES_NO.ordinal()) {
							if (strAnsw.equals(YES_NO_OPTION_NO_TEMPL)) {
								resultObject.setResult("No");
							} else if (strAnsw.equals(YES_NO_OPTION_YES_TEMPL)) {
								resultObject.setResult("Yes");
							} else {
								resultObject.setResult(strAnsw);
							}
						} else if (questionType == SurveyQuestionType.NUMERAL_LIST.ordinal()) {
							resultObject.setResult(strAnsw);
						} else if (questionType == SurveyQuestionType.DATE_PICKER.ordinal()) {
							resultObject.setResult(strAnsw);
						} else if (questionType == SurveyQuestionType.OPTION_LIST.ordinal()) {
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
	
	public static SurveyUserAccess getSurveyUserAccess(long surveyID, String userCode) {
		TblSurveyUserAccessInfo info = new TblSurveyUserAccessInfo();
		info.Set("survey_id", surveyID);
		info.Set("user_code", userCode);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return new SurveyUserAccess((TblSurveyUserAccessInfo) list.get(0));
	}
	
	public static List<SurveyUserAccess> getSurveyUserAccessList(long surveyID) {
		return getSurveyUserAccessList(surveyID, null);
	}
	
	public static List<SurveyUserAccess> getSurveyUserAccessList(long surveyID, String option) {
		TblSurveyUserAccessInfo info = new TblSurveyUserAccessInfo();
		info.Set("survey_id", surveyID);
		if (option != null && !option.isEmpty()) {
			info.SetSelectOption(option);
		}
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<SurveyUserAccess> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyUserAccess((TblSurveyUserAccessInfo) dao));
		}
		return ret;
	}
	
}

