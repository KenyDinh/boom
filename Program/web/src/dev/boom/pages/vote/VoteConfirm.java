package dev.boom.pages.vote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonMethod;
import dev.boom.common.VoteFuncs;
import dev.boom.common.enums.SurveyQuestionType;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.Survey;
import dev.boom.services.SurveyQuestion;
import dev.boom.services.SurveyResult;
import dev.boom.services.SurveyService;
import dev.boom.services.SurveyUserAccess;
import dev.boom.services.json.SurveyAnswerWrapper;

public class VoteConfirm extends JsonPageBase {

	private static final long serialVersionUID = 1L;
	
	private Survey activeSurvey = null;
	private SurveyUserAccess surveyAccess = null;
	
	public VoteConfirm() {
	}
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!getContext().isAjaxRequest()) {
			return false;
		}
		if (!getContext().isPost()) {
			return false;
		}
		if (!initUserInfo()) {
			return false;
		}
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
		String strSurveyName = getContext().getRequestParameter("p");
		if (StringUtils.isNotBlank(strSurveyName)) {
			activeSurvey = SurveyService.getSurveyInfoByPathName(strSurveyName);
			if (activeSurvey == null) {
				return;
			}
			if (VoteFuncs.isSurveyExpired(activeSurvey)) {
				activeSurvey = null;
				return;
			}
			surveyAccess = SurveyService.getSurveyUserAccess(activeSurvey.getId(), userInfo.getEmpid());
			if (activeSurvey.getFlag() == 0) { // allow all users
				return;
			}
			if (surveyAccess == null) { // require permission
				activeSurvey = null;
				return;
			}
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		if (activeSurvey == null) {
			return;
		}
		byte questionIndex = 1;
		SurveyAnswerWrapper resultObject = null;
		SurveyResult resultInfo = SurveyService.getSurveyResult(activeSurvey.getId(), userInfo.getEmpid());
		if (resultInfo != null) {
			questionIndex = resultInfo.getProgress();
			resultObject = SurveyAnswerWrapper.parse(resultInfo.getResult()); // TODO check null
		} else {
			resultInfo = new SurveyResult();
			resultInfo.setSurveyId(activeSurvey.getId());
			resultInfo.setUserId(userInfo.getEmpid());
			resultInfo.setUsername(userInfo.getName());
			resultInfo.setDepartment(userInfo.getDepartment());
		}
		if (resultObject == null) {
			resultObject = new SurveyAnswerWrapper();
		}
		SurveyQuestion questionInfo = SurveyService.getSurveyQuestion(activeSurvey.getId(), questionIndex);
		if (questionInfo == null) {
			GameLog.getInstance().error("[VoteConfirm] Survey question not found! index : " + questionIndex);
			return;
		}
		SurveyQuestionType type = SurveyQuestionType.valueOf(questionInfo.getType());
		if (type != SurveyQuestionType.MYSTERY_GIFT_BOX) {
			GameLog.getInstance().error("[VoteConfirm] Survey question type is invalid! type : " + type.name());
			return;
		}
		if (questionInfo.getMinChoice() <= 0 || questionInfo.getMaxChoice() < questionInfo.getMinChoice()) {
			GameLog.getInstance().error("[VoteConfirm] Survey question param is invalid!");
			return;
		}
		String strAns = getContext().getRequestParameter("ans");
		if (!CommonMethod.isValidNumeric(strAns, 1, questionInfo.getMinChoice())) {
			GameLog.getInstance().error("[VoteConfirm] Invalid answer : " + strAns);
			return;
		}
		int myNum = (surveyAccess == null ? 0 : surveyAccess.getFlag());
		int num = Integer.parseInt(strAns);
		byte newProgress = (byte) (questionIndex + 1);
		int rewardID = VoteFuncs.getLuckyRewardId(activeSurvey.getId(), questionIndex, questionInfo.getMaxChoice(), myNum);
		if (rewardID <= 0) {
			GameLog.getInstance().error("[VoteConfirm] No reward ID available!");
			return;
		}
		resultObject.addAnswer(questionIndex, String.valueOf(rewardID));
		resultInfo.setProgress(newProgress);;
		resultInfo.setResult(resultObject.toString());;
		if (resultInfo.getSurveyResultInfo().isInsert()) {
			if (CommonDaoFactory.Insert(resultInfo.getSurveyResultInfo()) <= 0) {
				GameLog.getInstance().info("Cant insert option result!");
				return;
			}
			GameLog.getInstance().info("Inserted option result successfully!");
		} else {
			if (CommonDaoFactory.Update(resultInfo.getSurveyResultInfo()) < 0) {
				GameLog.getInstance().info("Cant update option result!");
				return;
			}
			GameLog.getInstance().info("Updated option result successfully!");
		}
		int maxID = questionInfo.getMaxChoice();
		List<Integer> randomList = new ArrayList<>();
		for (int rID = 1; rID <= maxID; rID++) {
			if (rID == rewardID) {
				continue;
			}
			randomList.add(rID);
		}
		Collections.shuffle(randomList);
		List<Integer> allRewardID = new ArrayList<>();
		int rIdx = 0;
		for (int i = 0; i < questionInfo.getMinChoice(); i++) {
			if (i + 1 == num) {
				allRewardID.add(rewardID);
			} else {
				allRewardID.add(randomList.get(rIdx++));
			}
		}
		putJsonData("reward_id", allRewardID);
	}

	@Override
	public void onRender() {
		super.onRender();
	}
	
}
