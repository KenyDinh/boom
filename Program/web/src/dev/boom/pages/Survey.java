package dev.boom.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.common.CommonDefine;
import dev.boom.core.GameLog;
import dev.boom.core.SurveySession;
import dev.boom.services.SurveyInfo;
import dev.boom.services.SurveyOptionInfo;
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
		if (surveySession != null ) {
			
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
		List<SurveyOptionInfo> surveyOptionList = new ArrayList<>();
		surveyOptionList = SurveyService.getSurveyOptionList(activeSurvey.getId());
		if (surveyOptionList == null) {
			return;
		}
		renderOptions(surveyOptionList);
		//List<SurveyResultInfo> surveyResult = SurveyService.getSurveyResultBySurveyId(activeSurvey)
	}
	
	private void renderOptions(List<SurveyOptionInfo> surveyOptionList) {
		String str = "";
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"row\">");
		sb.append("<form method=\"post\">");
		sb.append("<div class=\"form-group\">");
		for (SurveyOptionInfo info : surveyOptionList) {
//			sb.append("<div class=\"col\">");
//			sb.append("<div class=\"row\" style=\"display: flex;justify-content: center;align-items: center;height: 100%;\">");
			sb.append("<div class=\"col-md-3\">");
			sb.append("<label class=\"btn btn-primary\">");
			sb.append("<img src=\""+info.getImage()+"\" alt=\"...\" class=\"img-thumbnail img-check\">");
			sb.append("<input type=\"checkbox\" name=\""+info.getName()+"\" id="+info.getId()+" value="+info.getId()+" class=\"d-none\" autocomplete=\"off\">");
			sb.append("<span>"+info.getName()+"</span>");
			sb.append("</label>");
//			sb.append("</div>");
			sb.append("</div>");
		}
		sb.append("</div>");
		sb.append("</form>");
		sb.append("</div>");
		sb.append("<div class=\"text-center\">");
		sb.append("<button type=\"submit\" class=\"btn btn-primary\">Submit</button>");
		sb.append("</div>");
		str += sb.toString();
		addModel("options", str);
	}
	
	private void renderResult() {
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new CssImport("/css/vote/vote.css"));
		headElements.add(new JsImport("/js/vote/vote.js"));

		if (isDataTableFormat) {
			initHeadElementsForTableData();
		}
		headElements.add(new JsImport("/js/socket.js"));
		
		return headElements;
	}
}
