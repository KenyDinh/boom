package dev.boom.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.common.CommonDefine;
import dev.boom.core.GameLog;
import dev.boom.core.SurveySession;
import dev.boom.services.CommonDaoService;
import dev.boom.services.SurveyInfo;
import dev.boom.services.SurveyOptionInfo;
import dev.boom.services.SurveyResultInfo;
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
		if (getContext().getRequestParameter("out") != null) {
			getContext().removeSessionAttribute(SURVEY_SESSION);
			setRedirect(this.getClass());
			return;
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		if (surveySession == null || surveySession.isExpired(now.getTime())) {
			String strCode = getContext().getRequestParameter("user_code");
			if (strCode != null && strCode.length() == CODE_LENGTH && strCode.matches("[0-9]+")) {
				if (!SurveyService.isValidUserCode(strCode)) {
					GameLog.getInstance().error("[Survey] user code is invalid!, code:" + strCode);
					return;
				}
				long timeout = new Date().getTime() + SURVEY_SESSION_TIMEOUT;
				surveySession = new SurveySession(strCode, timeout);
				getContext().setSessionAttribute(SURVEY_SESSION, surveySession);
				GameLog.getInstance().info("regist session!");
			}
			return;
		}
		if (surveySession != null ) {
			String strOptionList = getContext().getRequestParameter("options");
			if (strOptionList == null || strOptionList.isEmpty()) {
				GameLog.getInstance().error("No option selected!");
				return;
			}
			activeSurvey = SurveyService.getActiveSurveyInfo();
			if (activeSurvey == null) {
				GameLog.getInstance().error("No active survey!");
				return;
			}
			SurveyResultInfo surveyResultInfo = SurveyService.getSurveyResultByUser(surveySession.getCode());
			boolean isInsert = false;
			if (surveyResultInfo != null) {
				if (surveyResultInfo.getRetryRemain() <= 0) {
					GameLog.getInstance().error("No more retry");
					return;
				}
				surveyResultInfo.setRetryRemain((byte)(surveyResultInfo.getRetryRemain() - 1));
			} else {
				surveyResultInfo = new SurveyResultInfo();
				surveyResultInfo.setUser(surveySession.getCode());
				surveyResultInfo.setSurveyId(activeSurvey.getId());
				surveyResultInfo.setRetryRemain(activeSurvey.getMaxRetry());
				isInsert = true;
			}
			surveyResultInfo.setResult(strOptionList);
			if (isInsert) {
				if (CommonDaoService.insert(surveyResultInfo.getInfo()) == null) {
					GameLog.getInstance().info("Cant insert option result!");
					return;
				}
				GameLog.getInstance().info("Inserted option result successfully!");
			} else {
				if (!CommonDaoService.update(surveyResultInfo.getInfo())) {
					GameLog.getInstance().info("Cant update option result!");
					return;
				}
				GameLog.getInstance().info("Updated option result successfully!");
			}
		}
		setRedirect(this.getClass());
		return;
	}

	@Override
	public void onRender() {
		super.onRender();
		if (getRedirect() != null) {
			return;
		}
		if (surveySession == null) {
			addModel("valid_form", 1);
			return;
		}
		if (surveySession == null) {
			return;
		}
		activeSurvey = SurveyService.getActiveSurveyInfo();
		if (activeSurvey == null) {
			return;
		}
		addModel("survey", activeSurvey);
		Long surveyId = activeSurvey.getId();
		List<SurveyOptionInfo> surveyOptionList = new ArrayList<>();
		surveyOptionList = SurveyService.getSurveyOptionList(activeSurvey.getId());
		if (surveyOptionList == null) {
			return;
		}
		String strMode = getContext().getRequestParameter("mode");
		SurveyResultInfo resultInfo = SurveyService.getSurveyResultByUser(surveySession.getCode());
		if (resultInfo == null) {
			renderOptions(surveyOptionList);
		} else {
			if (strMode == null || strMode.isEmpty()) {
				renderResult(resultInfo, surveyId);
			} else if (strMode.equals("retry")) {
				renderOptions(surveyOptionList);
			} 
		}
	}
	
	private void renderOptions(List<SurveyOptionInfo> surveyOptionList) {
		StringBuilder sb = new StringBuilder();
		sb.append("<form method=\"post\" name=\"optionForm\">");
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"row\">");
		for (SurveyOptionInfo info : surveyOptionList) {
			sb.append("<div class=\"col-sm-4 col-lg-4\">");
			sb.append("<label class=\"btn btn-primary\">");
			sb.append("<img src=\""+info.getImage()+"\" alt=\"...\" class=\"img-thumbnail img-check\">");
			sb.append("<input type=\"checkbox\" name=\"option\" id=\"option_"+info.getId()+"\" value="+info.getId()+" class=\"d-none\" autocomplete=\"off\">");
			sb.append("</label>");
			sb.append("<span>"+info.getName()+"</span>");
			sb.append("</div>");
		}
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</form>");
		sb.append("<div class=\"text-center\">");
		sb.append("<button type=\"submit\" onClick=\"sendVote(); this.blur; return false;\" class=\"btn btn-success\">Submit</button>");
		sb.append("</div>");
		addModel("options", sb.toString());
	}
	
	private void renderResult(SurveyResultInfo resultInfo, Long surveyId) {
		if (resultInfo == null) {
			return;
		}
		String result = resultInfo.getResult();
		List<SurveyOptionInfo> optionInfos = SurveyService.getSurveyOptionListWithResult(result);
		StringBuilder sb = new StringBuilder();
		sb.append("<div>");
		for (SurveyOptionInfo info : optionInfos) {
//			sb.append("<div class=\"\">");
		}
		sb.append("</div>");
		if (resultInfo.getRetryRemain() > 0 ) {
			sb.append("<div class=\"text-center\">");
			sb.append("<button type=\"submit\" onClick=\"sendRetry(); this.blur; return false;\" class=\"btn btn-success\">Retry</button>");
			sb.append("</div>");
		}
		addModel("options", sb.toString());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new CssImport("/css/vote/vote.css"));
		headElements.add(new JsImport("/js/vote/vote.js"));

		return headElements;
	}
}
