package dev.boom.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.boom.common.enums.SurveyQuestionType;
import dev.boom.common.enums.SurveyStatus;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.services.Survey;
import dev.boom.services.SurveyQuestion;
import dev.boom.services.SurveyResult;
import dev.boom.services.SurveyService;
import dev.boom.services.SurveyUserAccess;
import dev.boom.services.User;
import dev.boom.services.json.SurveyAnswerWrapper;
import dev.boom.tbl.info.TblSurveyUserAccessInfo;

public class VoteFuncs {
	
	private static final Map<String, Set<Integer>> LUCKY_REWARD_ID_LIST = new HashMap<>();
	private static final Object lock = new Object();
	
	private VoteFuncs() {
	}
	
	public static void initRewardID() {
		LUCKY_REWARD_ID_LIST.clear();
		List<Survey> listActiveSurvey = SurveyService.getActiveSurveyList(0);
		for (Survey surveyInfo : listActiveSurvey) {
			List<SurveyQuestion> questionList = SurveyService.getSurveyQuestionList(surveyInfo.getId(), "AND type = " + SurveyQuestionType.MYSTERY_GIFT_BOX.ordinal());
			if (questionList == null || questionList.isEmpty()) {
				continue;
			}
			Map<Byte, SurveyQuestion> mapIdxQuestionInfo = new HashMap<>();
			for (SurveyQuestion questionInfo : questionList) {
				if (questionInfo.getType() != SurveyQuestionType.MYSTERY_GIFT_BOX.ordinal()) {
					continue;
				}
				mapIdxQuestionInfo.put(questionInfo.getIdx(), questionInfo);
			}
			if (mapIdxQuestionInfo.isEmpty()) {
				continue;
			}
			List<SurveyResult> resultList = SurveyService.getSurveyResultList(surveyInfo.getId());
			if (resultList == null || resultList.isEmpty()) {
				continue;
			}
			for (SurveyResult resultInfo : resultList) {
				SurveyAnswerWrapper resultObject = SurveyAnswerWrapper.parse(resultInfo.getResult());
				if (resultObject == null) {
					continue;
				}
				for (Byte questionIdx : mapIdxQuestionInfo.keySet()) {
					SurveyQuestion questionInfo = mapIdxQuestionInfo.get(questionIdx);
					String answer = resultObject.getAnswer(questionIdx);
					if (!CommonMethod.isValidNumeric(answer, 1, questionInfo.getMaxChoice())) {
						continue;
					}
					Integer num = Integer.valueOf(answer);
					String key = makeKey(surveyInfo.getId(), questionIdx);
					if (!LUCKY_REWARD_ID_LIST.containsKey(key)) {
						LUCKY_REWARD_ID_LIST.put(key, new HashSet<>());
					}
					LUCKY_REWARD_ID_LIST.get(key).add(num);
				}
			}
		}
	}
	
	public static String makeKey(long surveyID, byte questionIdx) {
		return String.format("key_%d_%d", surveyID, questionIdx);
	}
	
	public static int getLuckyRewardId(long surveyID, byte questionIdx, int maxID, int myNumber) {
		String key = makeKey(surveyID, questionIdx);
		List<Integer> randomList = new ArrayList<>();
		synchronized (lock) {
			Set<Integer> exist = LUCKY_REWARD_ID_LIST.get(key);
			int r = 0;
			for (int i = 1; i <= maxID; i++) {
				if (exist != null && exist.contains(i)) {
					continue;
				}
				if (i == myNumber) {
					r = 1;
					continue;
				}
				randomList.add(i);
			}
			int range = randomList.size();
			if (range == 0) {
				return 0;
			}
			int rewardID = 0;
			if (range == 1) {
				rewardID = randomList.get(0);
			} else if (range == 2 && r == 0 && myNumber != 0) {
				rewardID = getIdWithConflictCheck(surveyID, questionIdx, randomList.get(0), randomList.get(1));
				if (rewardID == 0) {
					rewardID = randomList.get(CommonMethod.random(range));
				}
			} else {
				rewardID = randomList.get(CommonMethod.random(range));
			}
			if (exist == null) {
				exist = new HashSet<>();
				LUCKY_REWARD_ID_LIST.put(key, exist);
			}
			exist.add(rewardID);
			return rewardID;
		}
	}
	
	private static int getIdWithConflictCheck(long surveyID, byte questionIdx, int firstNum, int secondNum) {
		List<SurveyUserAccess> listAccess = SurveyService.getSurveyUserAccessList(surveyID, String.format("AND flag IN (%d,%d)", firstNum, secondNum));
		if (listAccess == null || listAccess.isEmpty() || listAccess.size() != 2) { // expect 2 record only
			return 0;
		}
		for (SurveyUserAccess accessInfo : listAccess) {
			SurveyResult resultInfo = SurveyService.getSurveyResult(surveyID, accessInfo.getUserCode());
			if (resultInfo == null) { // not answered yet
				return accessInfo.getFlag();
			}
			SurveyAnswerWrapper resultObject = SurveyAnswerWrapper.parse(resultInfo.getResult());
			if (resultObject == null || resultObject.getAnswer(questionIdx) == null) {
				return accessInfo.getFlag(); // not answered yet
			}
		}
		return 0;
	}
	
	public static void removeExistingRewardID(long surveyID, byte questionIdx) {
		synchronized (lock) {
			String key = makeKey(surveyID, questionIdx);
			LUCKY_REWARD_ID_LIST.remove(key);
		}
	}
	
	public static void reInitAllRewardId() {
		synchronized (lock) {
			initRewardID();
		}
	}
	
	public static boolean hasSurveyAccess(User userInfo, Survey surveyInfo) {
		if (userInfo == null || surveyInfo == null) {
			return false;
		}
		if (surveyInfo.getFlag() == 0) {
			return true;
		}
		TblSurveyUserAccessInfo sas = new TblSurveyUserAccessInfo();
		sas.Set("survey_id", surveyInfo.getId());
		sas.Set("user_code", userInfo.getEmpid());
		return (CommonDaoFactory.Count(sas) > 0);
	}
	
	public static boolean isSurveyExpired(Survey surveyInfo) {
		if (surveyInfo == null) {
			return false;
		}
		if (surveyInfo.getStatus() == SurveyStatus.FINISHED.ordinal()) {
			return true;
		}
		Date expire = surveyInfo.getExpiredDate();
		Date now = new Date();
		return (expire.getTime() < now.getTime());
	}
}
