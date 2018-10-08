package dev.boom.pages.vote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.core.GameLog;
import dev.boom.core.SurveySession;
import dev.boom.pages.PageBase;
import dev.boom.services.CommonDaoService;
import dev.boom.services.SurveyInfo;
import dev.boom.services.SurveyOptionInfo;
import dev.boom.services.SurveyResultInfo;
import dev.boom.services.SurveyService;
import dev.boom.services.SurveyValidCodeData;

public class Vote extends PageBase {

	private static final long serialVersionUID = 1L;
	private static final String SURVEY_SESSION = "servey_session";
	private static final int CODE_LENGTH = 10;
	private static final long SURVEY_SESSION_TIMEOUT = CommonDefine.MILLION_SECOND_MINUTE * 5;
	
	private SurveyInfo activeSurvey = null;
	private SurveySession surveySession = null;
	private SurveyValidCodeData userData = null;
	private Date now = new Date();
	
	public Vote() {
	}
	
	@Override
	public boolean onSecurityCheck() {
		surveySession = (SurveySession) getContext().getSessionAttribute(SURVEY_SESSION);
		if (surveySession != null) {
			userData = SurveyService.getSurveyValidData(surveySession.getCode());
			if (userData == null) {
				GameLog.getInstance().error("[onSecurityCheck] user's data is null!");
				return false;
			}
			if (!userData.isEditable() && !userData.isReadonly()) {
				if (surveySession.isExpired(now.getTime())) {
					GameLog.getInstance().info("survey session expired!");
					getContext().removeSessionAttribute(SURVEY_SESSION);
					surveySession = null;
				}
			}
		}
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
		activeSurvey = SurveyService.getActiveSurveyInfo();
		if (getContext().getRequestParameter("out") != null) {
			getContext().removeSessionAttribute(SURVEY_SESSION);
			setRedirect(this.getClass());
			return;
		} 
		if (getContext().getRequestParameter("clear") != null) {
			if (activeSurvey == null) {
				return;
			}
			if (surveySession == null) {
				setRedirect(this.getClass());
				return;
			}
			if (userData == null || !userData.isEditable()) {
				setRedirect(this.getClass());
				return;
			}
			String strCode = getContext().getRequestParameter("clear");
			if (strCode == null || strCode.length() < CODE_LENGTH || !strCode.matches("^[0-9]+$") || !SurveyService.isValidUserCode(strCode)) {
				setRedirect(this.getClass());
				return;
			}
			SurveyResultInfo resultInfo = SurveyService.getSurveyResult(strCode, activeSurvey.getId());
			if (resultInfo == null) {
				setRedirect(this.getClass());
				return;
			}
			resultInfo.getInfo().setDelete();
			if (!CommonDaoService.delete(resultInfo.getInfo())) {
				GameLog.getInstance().error("[onInit] clear result failed!");
			}
			setRedirect(this.getClass());
			return;
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		if (getRedirect() != null) {
			return;
		}
		if (surveySession == null || surveySession.isExpired(now.getTime())) {
			if (userData == null || (!userData.isEditable() && !userData.isReadonly())) {
				String strCode = getContext().getRequestParameter("user_code");
				if (strCode != null && strCode.length() >= CODE_LENGTH && strCode.matches("^[0-9]+$")) {
					if (!SurveyService.isValidUserCode(strCode)) {
						GameLog.getInstance().error("[Survey] user code is invalid!, code:" + strCode);
						return;
					}
					long timeout = new Date().getTime() + SURVEY_SESSION_TIMEOUT;
					surveySession = new SurveySession(strCode, timeout);
					getContext().setSessionAttribute(SURVEY_SESSION, surveySession);
					GameLog.getInstance().info("regist session!");
				}
				setRedirect(this.getClass());
				return;
			}
		}
		if (surveySession != null ) {
			String strOptionList = getContext().getRequestParameter("options");
			if (strOptionList == null || strOptionList.isEmpty()) {
				GameLog.getInstance().error("No option selected!");
				return;
			}
			if (!strOptionList.matches("[0-9]+(,[0-9]+)*")) {
				GameLog.getInstance().error("Option is invalid format!");
				return;
			}
			String[] arr = strOptionList.split(",");
			List<Long> ids = new ArrayList<>();
			for (String strId : arr) {
				if (CommonMethod.isValidNumeric(strId, 1, Long.MAX_VALUE)) {
					ids.add(Long.parseLong(strId));
				}
			}
			if (SurveyService.getCountValidSurveyOption(ids) != arr.length) {
				GameLog.getInstance().error("Option list in invalid!");
				return;
			}
			if (arr.length > activeSurvey.getMaxChoice()) {
				GameLog.getInstance().error("Option list in invalid!");
				return;
			}
			activeSurvey = SurveyService.getActiveSurveyInfo();
			if (activeSurvey == null) {
				GameLog.getInstance().error("No active survey!");
				return;
			}
			SurveyResultInfo surveyResultInfo = SurveyService.getSurveyResult(surveySession.getCode(), activeSurvey.getId());
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
				surveyResultInfo.setUserInfo(userData.getName());
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
		if (activeSurvey == null) {
			return;
		}
		if (userData == null) {
			return;
		}
		addModel("survey", activeSurvey);
		addModel("userData", getUserInfo(userData));
		Long surveyId = activeSurvey.getId();
		SurveyResultInfo resultInfo = SurveyService.getSurveyResult(surveySession.getCode(), activeSurvey.getId());
		if (resultInfo == null) {
			renderOptions(resultInfo);
		} else {
			if (resultInfo.getRetryRemain() > 0) {
				if (getContext().getRequestParameter("retry") != null) {
					renderResult(resultInfo, surveyId, true);
					renderOptions(resultInfo);
					addModel("retry", "1");
					return;
				}
			}
			renderResult(resultInfo, surveyId, false);
		}
	}
	
	private void renderOptions(SurveyResultInfo resultInfo) {
		List<SurveyOptionInfo> surveyOptionList = SurveyService.getSurveyOptionList(activeSurvey.getId());
		if (surveyOptionList == null || surveyOptionList.isEmpty()) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		List<Long> ids = (resultInfo == null ? null : resultInfo.getResultList());
		sb.append("<div class=\"\">");
			sb.append("<div class=\"row\">");
			for (SurveyOptionInfo info : surveyOptionList) {
				sb.append("<div class=\"col-6 col-lg-3\">");
				boolean checked = (Boolean)(ids != null && ids.contains(info.getId()));
				sb.append(getOptionPreview(info, true, checked));
				sb.append("</div>");
			}
			sb.append("</div>");
		sb.append("</div>");
		if (resultInfo == null) {
			sb.append("<div class=\"text-center\" style=\"margin-top:1rem;\">");
			sb.append("<div class=\"row\">");
			sb.append("<div class=\"col-sm-12 col-lg-4\" style=\"margin:0 auto;\">");
			sb.append("<button type=\"submit\" onclick=\"sendVote(); this.blur; return false;\" class=\"btn btn-success\" style=\"width:100%;\">Submit</button>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("</div>");
		} else {
			sb.append("<div class=\"text-center\" style=\"margin-top:1rem;\">");
			sb.append("<div class=\"row\">");
				sb.append("<div class=\"col-6 col-lg-4\" style=\"margin:0 auto;\">");
				sb.append("<button type=\"submit\" onclick=\"sendVote(); this.blur; return false;\" class=\"btn btn-success\" style=\"width:100%;\">Submit</button>");
				sb.append("</div>");
				sb.append("<div class=\"col-6 col-lg-4\" style=\"margin:0 auto;\">");
				sb.append("<a href=\"" + getContextPath() + getContext().getPagePath(getClass()) + "\">");
				sb.append("<button type=\"submit\" class=\"btn btn-danger\" style=\"width:100%;\">Cancel</button>");
				sb.append("</a>");
				sb.append("</div>");
			sb.append("</div>");
			sb.append("</div>");
		}
		addModel("options", sb.toString());
	}
	
	private void renderResult(SurveyResultInfo resultInfo, Long surveyId, boolean retryMode) {
		if (resultInfo == null) {
			return;
		}
		List<Long> ids = resultInfo.getResultList();
		if (ids == null || ids.isEmpty()) {
			return;
		}
		List<SurveyOptionInfo> optionInfos = SurveyService.getSurveyOptionList(ids);
		if (optionInfos == null || optionInfos.isEmpty()) {
			GameLog.getInstance().error("[Survey] Result's option list is null!");
			return;
		}
		if (optionInfos.size() > activeSurvey.getMaxChoice()) {
			GameLog.getInstance().error("[Survey] Result's option list is invalid!");
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"result-frame\">");
		sb.append("<div class=\"row\">");
		int size = (retryMode ? activeSurvey.getMaxChoice() :optionInfos.size());
		int n = size - size % 3; // 12/4
		int l = (n == size ? 0 : n + 3 - size);
		int i = 0;
		for (; i < n; i++) {
			sb.append("<div class=\"col-4 col-lg-4\" style=\"padding:0.5rem;\">");
			if (i < optionInfos.size()) {
				sb.append(getOptionPreview(optionInfos.get(i), false, false));
			} else {
				sb.append(getOptionPreview(null, false, false));
			}
			sb.append("</div>");
		}
		if (l > 0) {
			sb.append(String.format("<div class=\"col-%d col-lg-%d\">", 2 * l, 2 * l));
			sb.append("</div>");
		}
		for (; i < size; i++) {
			sb.append("<div class=\"col-4 col-lg-4\" style=\"padding:0.5rem;\">");
			if (i < optionInfos.size()) {
				sb.append(getOptionPreview(optionInfos.get(i), false, false));
			} else {
				sb.append(getOptionPreview(null, false, false));
			}
			sb.append("</div>");
		}
		if (l > 0) {
			sb.append(String.format("<div class=\"col-%d col-lg-%d\">", 2 * l, 2 * l));
			sb.append("</div>");
		}
		sb.append("</div>");
		sb.append("</div>");
		if (!retryMode && resultInfo.getRetryRemain() > 0 ) {
			sb.append("<div class=\"row\">");
			sb.append("<div class=\"col-lg-12 text-center\">");
			sb.append("<label class=\"text-info\" style=\"\">");
			sb.append("You have " + resultInfo.getRetryRemain() + " times to change your choices, would you wanna do?");
			sb.append("</label>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("<div class=\"text-center\">");
			sb.append("<a href=\"" + getContextPath() + getContext().getPagePath(getClass()) + "?retry=1\">");
			sb.append("<button type=\"submit\" class=\"btn btn-info\">Change</button>");
			sb.append("</a>");
			sb.append("</div>");
		}
		addModel("results", sb.toString());
	}
	
	private String getOptionPreview(SurveyOptionInfo info, boolean selectable, boolean checked) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"survey-option-wrapper" + ((info == null && !selectable) ? " result-empty" : "") + "\">");
		if (info != null) {
			sb.append("<div style=\"position:relative;\" data=\"option-" + info.getId() + "\" class=\"survey-option option-" + (selectable ? "select-" + info.getId() : "result-" + info.getId()) + "\">");
				sb.append("<img src=\"" + CommonMethod.getStaticFile(info.getImage()) + "\" alt=\"" + info.getName() + "\" class=\"survey-opt-image" + (checked ? " check" : "") + "\">");
				sb.append("<label class=\"font-weight-bold\" data-toggle=\"tooltip\" data-placement=\"bottom\" style=\"text-overflow:ellipsis;overflow:hidden;white-space:nowrap;width:100%;\" title=\"" + info.getName() + "\">");
					sb.append(info.getName());
				sb.append("</label>");
				if (selectable) {
					sb.append("<div class=\"option-checked\" style=\"display:" + (checked ? "block" : "none") + ";\">");
					sb.append("<img src=\"" + getHostURL() + getContextPath() + "/img/vote/check_icon_c.png" + "\" style=\"width:90%;\" />");
					sb.append("</div>");
				}
			sb.append("</div>");
		}
		sb.append("</div>");
		return sb.toString();
	}
	
	private String getUserInfo(SurveyValidCodeData userData) {
		if (userData == null) {
			return "";
		}
		String userName = userData.getName();
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"row\">");
			sb.append("<div class=\"col-lg-12 text-center\">");
				sb.append("<label class=\"font-weight-bold text-info\" style=\"margin-top:0.8rem;font-size:" + (userName.length() > 24 ? "1" : "1.125") + "rem;\" >");
					sb.append("Welcome, " + userName);
					sb.append("<a href=\"" + getContextPath() + getContext().getPagePath(getClass()) + "?out=1\">");
					sb.append("<img src=\"" + getContextPath() + "/img/vote/logout-3.png" + "\" style=\"transform:scale(0.5,0.5);\"/>");
					sb.append("</a>");
				sb.append("</label>");
			sb.append("</div>");
			String pageLink = null;
			if (userData.isEditable()) {
				pageLink = String.format("<a href=\"%s\">Administration Page</a>", getPagePath(ManageVote.class));
			} else if (userData.isReadonly()) {
				pageLink = String.format("<a href=\"%s\">Result Page</a>", getPagePath(ManageVote.class) + "?mode=5" + "&survey_id=" + activeSurvey.getId());
			}
			if (pageLink != null) {
				sb.append("<div class=\"col-lg-12 text-center\">");
				sb.append("<label class=\"\" style=\"margin-bottom:1rem;\" >");
					sb.append(pageLink);
				sb.append("</label>");
				sb.append("</div>");
			}
		sb.append("</div>");
		return sb.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new CssImport("/css/vote/vote.css?@vote.css@"));
		headElements.add(new JsImport("/js/vote/vote.js?@vote.js@"));
		headElements.add(new JsImport("/js/lib/random-color.min.js?@random-color.min.js@"));

		return headElements;
	}
}
