package dev.boom.pages;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.core.GameLog;
import dev.boom.core.SurveySession;
import dev.boom.services.SurveyInfo;
import dev.boom.services.SurveyService;

public class Survey extends PageBase {

	private static final long serialVersionUID = 1L;
	private static final String SURVEY_SESSION = "servey_session";
	private static final int CODE_LENGTH = 6;
	private static final long SURVEY_SESSION_TIMEOUT = CommonDefine.MILLION_SECOND_MINUTE;
	
	
	private SurveyInfo activeSurvey = null;
	private SurveySession surveySession = null;
	private Date now = new Date();
	
	public Survey() {
	}
	
	@Override
	public boolean onSecurityCheck() {
		surveySession = (SurveySession) getContext().getSessionAttribute(SURVEY_SESSION);
		if (surveySession != null && surveySession.isExpired(now.getTime())) {
			GameLog.getInstance().info("survey session expired!");
			getContext().removeSessionAttribute(SURVEY_SESSION);
			surveySession = null;
		}
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
		if (surveySession != null) {
			activeSurvey = SurveyService.getActiveSurveyInfo();
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		if (surveySession == null || surveySession.isExpired(now.getTime())) {
			String strCode = getContext().getRequestParameter("user_code");
			if (strCode != null && strCode.length() == CODE_LENGTH && strCode.matches("[0-9]+")) {
				long timeout = new Date().getTime() + SURVEY_SESSION_TIMEOUT;
				surveySession = new SurveySession(strCode, timeout);
				getContext().setSessionAttribute(SURVEY_SESSION, surveySession);
				GameLog.getInstance().info("regist session!");
			}
			return;
		}
	}

	@Override
	public void onRender() {
		super.onRender();
		if (surveySession == null) {
			addModel("valid_form", 1);
			return;
		}
		if (activeSurvey == null) {
			return;
		}
		addModel("survey", activeSurvey);
		//List<SurveyResultInfo> surveyResult = SurveyService.getSurveyResultBySurveyId(activeSurvey)
	}
	
}
