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
import dev.boom.dao.core.DaoValue;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.CommonDaoService;
import dev.boom.services.SurveyService;
import dev.boom.services.UserInfo;
import dev.boom.services.UserService;
import dev.boom.services.json.SurveyAnswerWrapper;
import dev.boom.tbl.info.TblSurveyInfo;
import dev.boom.tbl.info.TblSurveyQuestionInfo;
import dev.boom.tbl.info.TblSurveyResultInfo;
import dev.boom.tbl.info.TblSurveyUserAccessInfo;

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
		List<TblSurveyInfo> surveyList = SurveyService.getSurveyInfoListForDisplay(false);
		if (surveyList == null || surveyList.isEmpty()) {
			return;
		}
		Map<String, UserInfo> mapUserByCode = null;
		Date now = new Date();
		List<DaoValue> updateList = new ArrayList<>();
		for (TblSurveyInfo surveyInfo : surveyList) {
			if (surveyInfo.getStatus() == SurveyStatus.FINISHED.ordinal()) {
				continue;
			}
			if (surveyInfo.getExpired().getTime() > now.getTime()) {
				continue;
			}
			surveyInfo.setStatus((byte)SurveyStatus.FINISHED.ordinal());
			updateList.add(surveyInfo);
			
			List<TblSurveyQuestionInfo> questionList = SurveyService.getSurveyQuestionList(surveyInfo.getId(), "AND type = " + SurveyQuestionType.MYSTERY_GIFT_BOX.ordinal());
			if (questionList == null || questionList.isEmpty()) {
				continue;
			}
			Map<Byte, TblSurveyQuestionInfo> mapIdxQuestionInfo = new HashMap<>();
			for (TblSurveyQuestionInfo questionInfo : questionList) {
				if (questionInfo.getType() != SurveyQuestionType.MYSTERY_GIFT_BOX.ordinal()) {
					continue;
				}
				mapIdxQuestionInfo.put(questionInfo.getIdx(), questionInfo);
			}
			if (mapIdxQuestionInfo.isEmpty()) {
				continue;
			}
			//
			List<TblSurveyUserAccessInfo> accessList = SurveyService.getSurveyUserAccessList(surveyInfo.getId());
			if (accessList == null || accessList.isEmpty()) {
				continue;
			}
			if (mapUserByCode == null) {
				List<UserInfo> userList = UserService.getUserList();
				if (userList == null || userList.isEmpty()) {
					return; // stop process
				}
				mapUserByCode = new HashMap<>();
				for (UserInfo userInfo : userList) {
					String strCode = userInfo.getEmpid();
					if (strCode == null || strCode.isEmpty()) {
						continue;
					}
					//
					mapUserByCode.put(strCode, userInfo);
				}
			}
			Map<String, TblSurveyUserAccessInfo> listAccessCode = new HashMap<>();
			for (TblSurveyUserAccessInfo accessInfo : accessList) {
				listAccessCode.put(accessInfo.getUser_code(), accessInfo);
			}
			//
			List<TblSurveyResultInfo> resultList = SurveyService.getSurveyResultList(surveyInfo.getId());
			if (resultList != null && !resultList.isEmpty()) {
				for (TblSurveyResultInfo resultInfo : resultList) {
					SurveyAnswerWrapper resultObject = SurveyAnswerWrapper.parse(resultInfo.getResult());
					if (resultObject == null) {
						continue;
					}
					String userCode = resultInfo.getUser_id();
					if (!listAccessCode.containsKey(userCode)) {
						continue;
					}
					int maxIdx = 0;
					int myNum = listAccessCode.get(userCode).getFlag();
					boolean update = false;
					for (Byte questionIdx : mapIdxQuestionInfo.keySet()) {
						TblSurveyQuestionInfo questionInfo = mapIdxQuestionInfo.get(questionIdx);
						String answer = resultObject.getAnswer(questionIdx);
						if (CommonMethod.isValidNumeric(answer, 1, questionInfo.getMax_choice())) {
							continue;
						}
						maxIdx = CommonMethod.max(maxIdx, questionIdx);
						int rewardID = VoteFuncs.getLuckyRewardId(surveyInfo.getId(), questionIdx, questionInfo.getMax_choice(), myNum);
						if (rewardID <= 0) {
							continue;
						}
						resultObject.addAnswer(questionIdx, String.valueOf(rewardID));
						update = true;
					}
					if (update) {
						resultInfo.Set("progress", (byte) (maxIdx + 1));
						resultInfo.Set("result", resultObject.toString());
						updateList.add(resultInfo);
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
					UserInfo userInfo = mapUserByCode.get(key);
					TblSurveyResultInfo resultInfo = new TblSurveyResultInfo();
					resultInfo.Set("survey_id", surveyInfo.getId());
					resultInfo.Set("user_id", userInfo.getEmpid());
					resultInfo.Set("username", userInfo.getName());
					resultInfo.Set("department", userInfo.getDepartment());
					SurveyAnswerWrapper resultObject = new SurveyAnswerWrapper();
					int maxIdx = 0;
					for (Byte questionIdx : mapIdxQuestionInfo.keySet()) {
						TblSurveyQuestionInfo questionInfo = mapIdxQuestionInfo.get(questionIdx);
						maxIdx = CommonMethod.max(maxIdx, questionIdx);
						int rewardID = VoteFuncs.getLuckyRewardId(surveyInfo.getId(), questionIdx, questionInfo.getMax_choice(), myNum);
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
		if (!CommonDaoService.update(updateList)) {
			GameLog.getInstance().error("[VoteUpdate] Update servey failed!");
		}
	}

	@Override
	public void onRender() {
		super.onRender();
	}
	
}
