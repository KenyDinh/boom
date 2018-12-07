package dev.boom.pages.vote;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.click.element.JsImport;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.core.GameLog;
import dev.boom.core.SurveySession;
import dev.boom.pages.PageBase;
import dev.boom.services.CommonDaoService;
import dev.boom.services.SurveyInfo;
import dev.boom.services.SurveyOptionInfo;
import dev.boom.services.SurveyService;
import dev.boom.services.SurveyValidCodeData;

public class ManageVote extends PageBase {

	private static final long serialVersionUID = 1L;
	private static final int MODE_SURVEY_NEW = 1;
	private static final int MODE_SURVEY_EDIT = 2;
	private static final int MODE_OPTION_NEW = 3;
	private static final int MODE_OPTION_EDIT = 4;
	private static final int MODE_SHOW_RESULT = 5;
	private static final String SURVEY_SESSION = "servey_session";
	
	private SurveySession surveySession = null;
	private SurveyValidCodeData data = null;
	private SurveyInfo survey = null;
	private int mode = 0;
	private boolean error = false;

	public ManageVote() {
	}

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		surveySession = (SurveySession) getContext().getSessionAttribute(SURVEY_SESSION);
		if (surveySession == null) {
			GameLog.getInstance().error("[ManageVote] survey session is null!");
			setRedirect(Vote.class);
			return false;
		}
		data = SurveyService.getSurveyValidData(surveySession.getCode());
		if (data == null) {
			GameLog.getInstance().error("[ManageVote] survey valid data is null!");
			setRedirect(Vote.class);
			return false;
		}
		if (!data.isEditable() && !data.isReadonly()) {
			GameLog.getInstance().error("[ManageVote] not allow to access!");
			setRedirect(Vote.class);
			return false;
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		initHeadElementsLocalForTableData();
		headElements.add(new JsImport("/js/lib/chart-2.7.2.js"));
		headElements.add(new JsImport("/js/lib/random-color.min.js"));
		headElements.add(new JsImport("/js/vote/manage_vote.js"));
		
		return headElements;
	}

	@Override
	public void onInit() {
		super.onInit();
		String strSurveyId = getContext().getRequestParameter("survey_id");
		if (CommonMethod.isValidNumeric(strSurveyId, 1, Long.MAX_VALUE)) {
			survey = SurveyService.getSurveyById(Long.parseLong(strSurveyId));
		}
		if (survey != null) {
			addModel("survey", survey);
		}
		String strMode = getContext().getRequestParameter("mode");
		if (CommonMethod.isValidNumeric(strMode, 1, Integer.MAX_VALUE)) {
			mode = Integer.parseInt(strMode);
		}
		addModel("mode", mode);
		if (data == null) {
			setRedirect(Vote.class);
			return;
		}
		if (data.isEditable()) {
		} else if (data.isReadonly()) {
			if (mode != MODE_SHOW_RESULT) {
				GameLog.getInstance().error("[ManageVote] not allowed to access other page beside result page.");
				setRedirect(Vote.class);
				return;
			}
			if (survey == null || !survey.isActive()) {
				GameLog.getInstance().error("[ManageVote] no survey active!");
				setRedirect(Vote.class);
				return;
			}
		} else {
			GameLog.getInstance().error("[ManageVote] access deny!");
			setRedirect(Vote.class);
			return;
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		if (error) {
			return;
		}
		boolean update = false;
		switch (mode) {
		case MODE_SURVEY_NEW:
		case MODE_SURVEY_EDIT:
			SurveyInfo surveyInfo = null;
			if (mode == MODE_SURVEY_EDIT) {
				surveyInfo = survey;
			} else {
				surveyInfo = new SurveyInfo();
			}
			if (surveyInfo == null) {
				GameLog.getInstance().error("[ManageVote] Survey is null!");
				error = true;
				return;
			}
			if (mode == MODE_SURVEY_EDIT && getContext().getRequestParameter("delete") != null) {
				List<String> command = Arrays.asList(new String[] {"DELETE FROM survey_result_info WHERE survey_id = " + surveyInfo.getId()});
				List<Object> ret = CommonDaoService.executeNativeSQLQuery("SELECT * FROM survey_info WHERE id = " + surveyInfo.getId(), command);
				if (ret == null || ret.isEmpty()) {
					GameLog.getInstance().error("[ManageVote] Clear Survey failed!");
					error = true;
					return;
				}
			} else {
				String strName = getContext().getRequestParameter("name");
				if (strName != null && strName.length() > 0 && !surveyInfo.getName().equals(strName)) {
					if (strName.indexOf("<script") >= 0) {
						strName = StringEscapeUtils.escapeHtml(strName);
					}
					surveyInfo.setName(strName);
					update = true;
				}
				String strStatus = getContext().getRequestParameter("status");
				if (CommonMethod.isValidNumeric(strStatus, 0, 1)) {
					byte status = Byte.parseByte(strStatus);
					if (surveyInfo.getStatus() != status) {
						surveyInfo.setStatus(status);
						update = true;
					}
				}
				String strMaxChoice = getContext().getRequestParameter("max_choice");
				if (CommonMethod.isValidNumeric(strMaxChoice, 1, Byte.MAX_VALUE)) {
					byte max_choice = Byte.parseByte(strMaxChoice);
					if (surveyInfo.getMaxChoice() != max_choice) {
						surveyInfo.setMaxChoice(max_choice);
						update = true;
					}
				}
				String strMinChoice = getContext().getRequestParameter("min_choice");
				if (CommonMethod.isValidNumeric(strMinChoice, 1, Byte.MAX_VALUE)) {
					byte min_choice = Byte.parseByte(strMinChoice);
					if (surveyInfo.getMinChoice() != min_choice) {
						surveyInfo.setMinChoice(min_choice);
						update = true;
					}
				}
				String strMaxRetry = getContext().getRequestParameter("max_retry");
				if (CommonMethod.isValidNumeric(strMaxRetry, 1, Byte.MAX_VALUE)) {
					byte max_retry = Byte.parseByte(strMaxRetry);
					if (surveyInfo.getMaxRetry() != max_retry) {
						surveyInfo.setMaxRetry(max_retry);
						update = true;
					}
				}
				String strExpiration = getContext().getRequestParameter("expired");
				if (strExpiration != null && strExpiration.matches(CommonDefine.DATE_REGEX_PATTERN)) {
					Date expired = CommonMethod.getDate(strExpiration, CommonDefine.DATE_FORMAT_PATTERN);
					if (expired != null && expired.getTime() != surveyInfo.getExpired().getTime()) {
						surveyInfo.setExpired(expired);
						update = true;
					}
				}
				if (mode == MODE_SURVEY_NEW) {
					if (CommonDaoService.insert(surveyInfo.getInfo()) == null) {
						GameLog.getInstance().error("[ManageVote] insert survey fail!");
						error = true;
						return;
					}
				} else if (update){
					if (!CommonDaoService.update(surveyInfo.getInfo())) {
						GameLog.getInstance().error("[ManageVote] update survey fail!");
						error = true;
						return;
					}
				}
			}
			break;
		case MODE_OPTION_NEW:
		case MODE_OPTION_EDIT:
			SurveyOptionInfo surveyOption = null;
			if (mode == MODE_OPTION_EDIT) {
				String strOptionId = getContext().getRequestParameter("option_id");
				if (!CommonMethod.isValidNumeric(strOptionId, 1, Long.MAX_VALUE)) {
					GameLog.getInstance().error("[ManageVote] invalid option id!");
					error = true;
					return;
				}
				surveyOption = SurveyService.getSurveyOptionById(Long.parseLong(strOptionId));
			} else {
				surveyOption = new SurveyOptionInfo();
			}
			if (surveyOption == null) {
				GameLog.getInstance().error("[ManageVote] Option is null!");
				error = true;
				return;
			}
			if (survey == null) {
				GameLog.getInstance().error("[ManageVote] no survey selected!");
				error = true;
				return;
			}
			if (surveyOption.getSurveyId() != survey.getId()) {
				surveyOption.setSurveyId(survey.getId());
				update = true;
			}
			if (mode == MODE_OPTION_EDIT && getContext().getRequestParameter("delete") != null) {
				if (!CommonDaoService.delete(surveyOption.getInfo())) {
					GameLog.getInstance().error("[ManageVote] delete option fail!");
					error = true;
					return;
				}
			} else {
				String strOptName = getContext().getRequestParameter("name");
				if (strOptName != null && strOptName.length() > 0 && !surveyOption.getName().equals(strOptName)) {
					if (strOptName.indexOf("<script") >= 0) {
						strOptName = StringEscapeUtils.escapeHtml(strOptName);
					}
					surveyOption.setName(strOptName);
					update = true;
				}
				String strContent = getContext().getRequestParameter("content");
				if (strContent != null && !surveyOption.getContent().equals(strContent)) {
					if (strContent.indexOf("<script") >= 0) {
						strContent = StringEscapeUtils.escapeHtml(strContent);
					}
					surveyOption.setContent(strContent);
					update = true;
				}
				String oldFileName = surveyOption.getImage();
				FileItem image = getContext().getFileItem("image");
				if (image != null) {
					String imgName = image.getName();
					if (StringUtils.isNotBlank(imgName)) {
						GameLog.getInstance().info("[ManageVote] getImage: " + image.getName());
						String imgExt = imgName.substring(imgName.lastIndexOf(".") + 1);
						String newImgName = "";
						do {
							newImgName = RandomStringUtils.randomAlphanumeric(8) + "." + imgExt;
						} while (SurveyService.isExistOptionImgFileName(newImgName));
						surveyOption.setImage(newImgName);
						File newFile = new File(getFileUploadDir(), newImgName);
						try {
							image.write(newFile);
							GameLog.getInstance().info("[ManageVote] created new image file: " + newImgName);
							update = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (mode == MODE_OPTION_NEW) {
					if (CommonDaoService.insert(surveyOption.getInfo()) == null) {
						GameLog.getInstance().error("[ManageVote] insert option fail!");
						error = true;
						return;
					}
				} else if (update){
					if (!CommonDaoService.update(surveyOption.getInfo())) {
						GameLog.getInstance().error("[ManageVote] update option fail!");
						error = true;
						return;
					}
					if (!surveyOption.getImage().equals(oldFileName)) {
						File oldFile = new File(getFileUploadDir(), oldFileName);
						if (oldFile.exists()) {
							if (!oldFile.delete()) {
								GameLog.getInstance().error("[ManageVote] cannot delete old image file: " + oldFileName);
							}
						}
					}
				}
			}
			break;
		default:
			break;
		}
		if (survey != null) {
			Map<String, String> params = new HashMap<>();
			params.put("survey_id", String.valueOf(survey.getId()));
			setRedirect(this.getClass(), params);
		} else {
			setRedirect(this.getClass());
		}
	}

	@Override
	public void onRender() {
		super.onRender();
		if (error) {
			return;
		}
		if (getRedirect() != null) {
			return;
		}
		switch (mode) {
		case MODE_SURVEY_NEW:
		case MODE_SURVEY_EDIT:
			SurveyInfo surveyInfo = null;
			String strSurveyId = getContext().getRequestParameter("survey_id");
			if (mode == MODE_SURVEY_EDIT) {
				if (!CommonMethod.isValidNumeric(strSurveyId, 1, Long.MAX_VALUE)) {
					GameLog.getInstance().error("[ManageVote] invalid servey id!");
					error = true;
					return;
				}
				surveyInfo = SurveyService.getSurveyById(Long.parseLong(strSurveyId));
				addModel("title", "Edit Vote");
			} else {
				surveyInfo = new SurveyInfo();
				addModel("title", "New Vote");
			}
			if (surveyInfo == null) {
				GameLog.getInstance().error("[ManageVote] Survey is null!");
				error = true;
				return;
			}
			addModel("survey_form", surveyInfo);
			break;
		case MODE_OPTION_NEW:
		case MODE_OPTION_EDIT:
			List<SurveyInfo> listSurvey = SurveyService.getSurveyList();
			if (listSurvey != null && listSurvey.isEmpty()) {
				GameLog.getInstance().error("[ManageVote] no survey info!");
				error = true;
				return;
			}
			SurveyOptionInfo surveyOption = null;
			if (mode == MODE_OPTION_EDIT) {
				String strOptionId = getContext().getRequestParameter("option_id");
				if (!CommonMethod.isValidNumeric(strOptionId, 1, Long.MAX_VALUE)) {
					GameLog.getInstance().error("[ManageVote] invalid option id!");
					error = true;
					return;
				}
				surveyOption = SurveyService.getSurveyOptionById(Long.parseLong(strOptionId));
				addModel("title", "Edit Option");
			} else {
				surveyOption = new SurveyOptionInfo();
				if (survey != null) {
					surveyOption.setSurveyId(survey.getId());
				} else {
					surveyOption.setSurveyId(listSurvey.get(0).getId());
				}
				addModel("title", "New Option");
			}
			if (surveyOption == null) {
				GameLog.getInstance().error("[ManageVote] Option is null!");
				error = true;
				return;
			}
			StringBuilder optionSelects = new StringBuilder();
			optionSelects.append("<select class=\"form-control\" id=\"survey_id\" name=\"survey_id\">");
			for (SurveyInfo svInfo : listSurvey) {
				optionSelects.append("<option value=\"").append(svInfo.getId()).append("\" ").append((svInfo.getId() == surveyOption.getSurveyId() ? "selected" : "")).append(">");
				optionSelects.append(svInfo.getName()).append("</option>");
			}
			optionSelects.append("</select>");
			addModel("survey_list", optionSelects.toString());
			addModel("option_form", surveyOption);
			break;
		case MODE_SHOW_RESULT:
			showResult();
			break;
		default:
			showDetail();
			break;
		}
	}
	
	private void showResult() {
		if (survey == null) {
			GameLog.getInstance().error("[ManageVote] no survey active at the moment!");
			return;
		}
		List<SurveyOptionInfo> listResultOption = SurveyService.calculateSurveyResult(survey.getId());
		if (listResultOption == null || listResultOption.isEmpty()) {
			return;
		}
		Collections.sort(listResultOption, new Comparator<SurveyOptionInfo>() {

			@Override
			public int compare(SurveyOptionInfo o1, SurveyOptionInfo o2) {
				return o2.getSelectedCount() - o1.getSelectedCount();
			}
		});
		long resultCount = SurveyService.getCountSurveyResult(survey.getId());
//		if (resultCount <= 0) {
//			GameLog.getInstance().error("[ManageVote] no result record found!");
//			return;
//		}
		addModel("resultCount", resultCount);
		int total = 0;
		for (SurveyOptionInfo option : listResultOption) {
			total += option.getSelectedCount();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"\" class=\"\">");
		sb.append("<table class=\"table table-striped\">");
		sb.append("<thead>");
			sb.append("<tr class=\"text-info\">");
				sb.append("<th>");
					sb.append("Rank");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Performances");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Rating");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Vote");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Point");
				sb.append("</th>");
			sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		int rank = 1;
		for (SurveyOptionInfo option : listResultOption) {
			sb.append("<tr id=\"option-rank-" + rank + "\">");
				sb.append("<td class=\"rank\">");
					sb.append(rank++);
				sb.append("</td>");
				sb.append("<td class=\"name\">");
					sb.append(option.getName());
				sb.append("</td>");
				sb.append("<td class=\"rate\">");
					sb.append(option.getFormatVoteRating(total));
				sb.append("</td>");
				sb.append("<td class=\"count\">");
					sb.append(option.getSelectedCount());
				sb.append("</td>");
				sb.append("<td class=\"point\">");
					sb.append(option.getTotalPoint());
					sb.append("</td>");
			sb.append("</tr>");
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		
		addModel("result", sb.toString());
	}
	
	private void showDetail() {
		StringBuilder sb = new StringBuilder();
		List<SurveyOptionInfo> listOption = null;
		sb.append(showSurveyListTable());
		if (survey != null) {
			listOption = SurveyService.getSurveyOptionList(survey.getId());
		}
		sb.append("<div style=\"text-align:center;font-size:1.5rem;margin-top:2rem;\">");
		sb.append("<label class=\"font-weight-bold text-info\">Option List</label>");
		sb.append("</div>");
		sb.append("<a href=\"").append(getPagePath(this.getClass()) + "?mode=" + MODE_OPTION_NEW + (survey != null ? "&survey_id=" + survey.getId() : "") + "\">");
		sb.append("<button class=\"btn btn-info\">New Option</button>");
		sb.append("</a>");
		if (survey != null) {
			sb.append("<a style=\"margin-left:1rem;\" href=\"").append(getPagePath(this.getClass()) + "?mode=" + MODE_SHOW_RESULT + "&survey_id=" + survey.getId() + "\">");
			sb.append("<button class=\"btn btn-warning text-white\">Show Result</button>");
			sb.append("</a>");
		}
		sb.append("<div id=\"\" class=\"\">");
		sb.append("<table id=\"option_table\" class=\"table\">");
		sb.append("<thead>");
			sb.append("<tr class=\"text-info\">");
				sb.append("<th>");
					sb.append("Name");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("For");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Content");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Image");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Edit");
				sb.append("</th>");
			sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		if (listOption == null || listOption.isEmpty()) {
			sb.append("<tr>");
				sb.append("<td id=\"no-option-record\" colspan=\"5\">");
				sb.append("No record found!");
				sb.append("</td>");
			sb.append("</tr>");
		} else {
			for (SurveyOptionInfo option : listOption) {
				sb.append("<tr>");
					sb.append("<td class=\"name\">");
						sb.append(option.getName());
					sb.append("</td>");
					sb.append("<td class=\"for\">");
						sb.append(survey.getName());
					sb.append("</td>");
					sb.append("<td class=\"content\">");
						sb.append(option.getContent());
					sb.append("</td>");
					sb.append("<td class=\"image\">");
						sb.append("<img src=\"" + CommonMethod.getStaticFile(option.getImage()) + "\" alt=\"" + option.getName() + "\" class=\"survey-opt-image\">");
					sb.append("</td>");
					sb.append("<td class=\"content\">");
						sb.append(String.format("<a href=\"%s\">Edit</a>", getPagePath(this.getClass()) + "?mode=" + MODE_OPTION_EDIT + "&survey_id=" + option.getSurveyId() + "&option_id=" + option.getId()));
					sb.append("</td>");
				sb.append("</tr>");
			}
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		addModel("detail", sb.toString());
	}
	
	private String showSurveyListTable() {
		List<SurveyInfo> listSurvey = SurveyService.getSurveyList();
		StringBuilder sb = new StringBuilder();
		sb.append("<div style=\"text-align:center;font-size:1.5rem;margin-top:2rem;\">");
		sb.append("<label class=\"font-weight-bold text-info\">Subject List</label>");
		sb.append("</div>");
		sb.append("<a href=\"").append(getPagePath(this.getClass()) + "?mode=" + MODE_SURVEY_NEW +"\">");
		sb.append("<button class=\"btn btn-info\">New Vote</button>");
		sb.append("</a>");
		sb.append("<a href=\"").append(getPagePath(Vote.class) + "\" style=\"margin-left:1rem;\">");
		sb.append("<button class=\"btn btn-secondary\">&nbsp;Back&nbsp;</button>");
		sb.append("</a>");
		sb.append("<div id=\"\" class=\"\">");
		sb.append("<table id=\"survey_table\" class=\"table\">");
		sb.append("<thead>");
			sb.append("<tr class=\"text-info\">");
				sb.append("<th>");
					sb.append("Name");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Status");
				sb.append("</th>");
//					sb.append("<th>");
//						sb.append("Description");
//					sb.append("</th>");
				sb.append("<th>");
					sb.append("Max Choices");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Max Retry");
				sb.append("</th>");
//					sb.append("<th>");
//						sb.append("Created");
//					sb.append("</th>");
				sb.append("<th>");
					sb.append("Expire Date");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Edit");
				sb.append("</th>");
			sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		if (listSurvey != null && !listSurvey.isEmpty()) {
			for (SurveyInfo survey : listSurvey) {
				String highlight = ((this.survey != null && this.survey.getId() == survey.getId()) ? "style=\"background-color:#903b76;\"" : "");
				sb.append(String.format("<tr %s>", highlight));
					sb.append("<td>");
						sb.append(String.format("<a href=\"%s\">%s</a>", getPagePath(this.getClass()) + "?survey_id=" + survey.getId(), survey.getName()));
					sb.append("</td>");
					sb.append("<td>");
						sb.append((survey.isActive() ? "ON" : "OFF"));
					sb.append("</td>");
	//					sb.append("<td>");
	//						sb.append(survey.getDescription());
	//					sb.append("</td>");
					sb.append("<td>");
						sb.append(survey.getMaxChoice());
					sb.append("</td>");
					sb.append("<td>");
						sb.append(survey.getMaxRetry());
					sb.append("</td>");
	//					sb.append("<td>");
	//						sb.append(survey.getCreated());
	//					sb.append("</td>");
					sb.append("<td>");
						sb.append(survey.getExpired());
					sb.append("</td>");
					sb.append("<td>");
						sb.append(String.format("<a href=\"%s\">Edit</a>", getPagePath(this.getClass()) + "?mode=" + MODE_SURVEY_EDIT + "&survey_id=" + survey.getId()));
					sb.append("</td>");
				sb.append("</tr>");
			}
		} else {
			sb.append("<tr>");
				sb.append("<td id=\"no-survey-record\" colspan=\"6\">");
					sb.append("No record found!");
				sb.append("</td>");
			sb.append("</tr>");
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}
}
