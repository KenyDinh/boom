package dev.boom.pages.vote;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.common.CommonMethod;
import dev.boom.common.VoteFuncs;
import dev.boom.common.enums.SurveyQuestionType;
import dev.boom.common.enums.SurveyStatus;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.Survey;
import dev.boom.services.SurveyQuestion;
import dev.boom.services.SurveyResult;
import dev.boom.services.SurveyService;
import dev.boom.services.SurveyUserAccess;
import dev.boom.services.User;
import dev.boom.services.UserService;
import dev.boom.services.json.SurveyAnswerWrapper;
import dev.boom.tbl.info.TblSurveyResultInfo;

public class VoteUpdate extends JsonPageBase {

	private static final long serialVersionUID = 1L;
	
	public VoteUpdate() {
	}
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
		if (!isLocal()) {
			return;
		}
		List<Survey> surveyList = SurveyService.getSurveyInfoListForDisplay(false);
		if (surveyList == null || surveyList.isEmpty()) {
			return;
		}
		Map<String, User> mapUserByCode = null;
		Date now = new Date();
		List<DaoValue> updateList = new ArrayList<>();
		for (Survey surveyInfo : surveyList) {
			if (surveyInfo.getStatus() == SurveyStatus.FINISHED.ordinal()) {
				continue;
			}
			if (surveyInfo.getExpiredDate().getTime() > now.getTime()) {
				continue;
			}
			surveyInfo.setStatus((byte)SurveyStatus.FINISHED.ordinal());
			updateList.add(surveyInfo.getSurveyInfo());
			
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
			//
			List<SurveyUserAccess> accessList = SurveyService.getSurveyUserAccessList(surveyInfo.getId());
			if (accessList == null || accessList.isEmpty()) {
				continue;
			}
			if (mapUserByCode == null) {
				List<User> userList = UserService.getUserList();
				if (userList == null || userList.isEmpty()) {
					return; // stop process
				}
				mapUserByCode = new HashMap<>();
				for (User userInfo : userList) {
					String strCode = userInfo.getEmpid();
					if (strCode == null || strCode.isEmpty()) {
						continue;
					}
					//
					mapUserByCode.put(strCode, userInfo);
				}
			}
			Map<String, SurveyUserAccess> listAccessCode = new HashMap<>();
			for (SurveyUserAccess accessInfo : accessList) {
				listAccessCode.put(accessInfo.getUserCode(), accessInfo);
			}
			//
			List<SurveyResult> resultList = SurveyService.getSurveyResultList(surveyInfo.getId());
			if (resultList != null && !resultList.isEmpty()) {
				for (SurveyResult resultInfo : resultList) {
					SurveyAnswerWrapper resultObject = SurveyAnswerWrapper.parse(resultInfo.getResult());
					if (resultObject == null) {
						continue;
					}
					String userCode = resultInfo.getUserId();
					if (!listAccessCode.containsKey(userCode)) {
						continue;
					}
					int maxIdx = 0;
					int myNum = listAccessCode.get(userCode).getFlag();
					boolean update = false;
					for (Byte questionIdx : mapIdxQuestionInfo.keySet()) {
						SurveyQuestion questionInfo = mapIdxQuestionInfo.get(questionIdx);
						String answer = resultObject.getAnswer(questionIdx);
						if (CommonMethod.isValidNumeric(answer, 1, questionInfo.getMaxChoice())) {
							continue;
						}
						maxIdx = CommonMethod.max(maxIdx, questionIdx);
						int rewardID = VoteFuncs.getLuckyRewardId(surveyInfo.getId(), questionIdx, questionInfo.getMaxChoice(), myNum);
						if (rewardID <= 0) {
							continue;
						}
						resultObject.addAnswer(questionIdx, String.valueOf(rewardID));
						update = true;
					}
					if (update) {
						resultInfo.setProgress((byte) (maxIdx + 1));;
						resultInfo.setResult(resultObject.toString());;
						updateList.add(resultInfo.getSurveyResultInfo());
					}
					listAccessCode.remove(userCode);
				}
			}
			if (!listAccessCode.isEmpty()) {
				for (String key : listAccessCode.keySet()) {
					if (!mapUserByCode.containsKey(key)) {
						continue;
					}
					int myNum = listAccessCode.get(key).getFlag();
					User userInfo = mapUserByCode.get(key);
					TblSurveyResultInfo resultInfo = new TblSurveyResultInfo();
					resultInfo.Set("survey_id", surveyInfo.getId());
					resultInfo.Set("user_id", userInfo.getEmpid());
					resultInfo.Set("username", userInfo.getName());
					resultInfo.Set("department", userInfo.getDepartment());
					SurveyAnswerWrapper resultObject = new SurveyAnswerWrapper();
					int maxIdx = 0;
					for (Byte questionIdx : mapIdxQuestionInfo.keySet()) {
						SurveyQuestion questionInfo = mapIdxQuestionInfo.get(questionIdx);
						maxIdx = CommonMethod.max(maxIdx, questionIdx);
						int rewardID = VoteFuncs.getLuckyRewardId(surveyInfo.getId(), questionIdx, questionInfo.getMaxChoice(), myNum);
						if (rewardID <= 0) {
							continue;
						}
						resultObject.addAnswer(questionIdx, String.valueOf(rewardID));
					}
					resultInfo.Set("progress", (byte) (maxIdx + 1));
					resultInfo.Set("result", resultObject.toString());
					updateList.add(resultInfo);
				}
			}
		}
		if (updateList.isEmpty()) {
			return;
		}
		if (CommonDaoFactory.Update(updateList) < 0) {
			GameLog.getInstance().error("[VoteUpdate] Update servey failed!");
		}
	}

	@Override
	public void onRender() {
		super.onRender();
	}
	
}
