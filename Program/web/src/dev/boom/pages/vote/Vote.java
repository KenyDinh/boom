package dev.boom.pages.vote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonHtmlFunc;
import dev.boom.common.CommonMethod;
import dev.boom.common.VoteFuncs;
import dev.boom.common.enums.MainNavBarEnum;
import dev.boom.common.enums.SurveyOptionType;
import dev.boom.common.enums.SurveyQuestionType;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.services.Survey;
import dev.boom.services.SurveyOption;
import dev.boom.services.SurveyQuestion;
import dev.boom.services.SurveyResult;
import dev.boom.services.SurveyService;
import dev.boom.services.User;
import dev.boom.services.json.SurveyAnswer;
import dev.boom.services.json.SurveyAnswerWrapper;

public class Vote extends VotePageBase {

	private static final long serialVersionUID = 1L;
	//private static final int CODE_LENGTH = 6;
	
	private Survey activeSurvey = null;
	
	public Vote() {
		hideMenubar = true;
	}
	
	@Override
	public void onInit() {
		super.onInit();
		String strSurveyName = getContext().getRequestParameter("p");
		if (StringUtils.isNotBlank(strSurveyName)) {
			activeSurvey = SurveyService.getSurveyInfoByPathName(strSurveyName);
			if (VoteFuncs.isSurveyExpired(activeSurvey)) {
				activeSurvey = null;
				return;
			}
			if (!VoteFuncs.hasSurveyAccess(userInfo, activeSurvey)) {
				activeSurvey = null;
				return;
			}
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		if (getRedirect() != null) {
			return;
		}
		if (userInfo != null && activeSurvey != null) {
			if (getContext().getRequestParameter("vote") != null) {
				byte questionIndex = 1;
				SurveyAnswerWrapper resultObject = null;
				SurveyResult resultInfo = SurveyService.getSurveyResult(activeSurvey.getId(), userInfo.getEmpid());
				if (resultInfo != null) {
					questionIndex = resultInfo.getProgress();
					resultObject = SurveyAnswerWrapper.parse(resultInfo.getResult()); // TODO check null
				} else {
					resultInfo = new SurveyResult();
					resultInfo.setSurveyId(activeSurvey.getId());
					resultInfo.setUserId(userInfo.getEmpid());;
					resultInfo.setUsername(userInfo.getName());;
					resultInfo.setDepartment(userInfo.getDepartment());;
				}
				if (resultObject == null) {
					resultObject = new SurveyAnswerWrapper();
				}
				SurveyQuestion questionInfo = SurveyService.getSurveyQuestion(activeSurvey.getId(), questionIndex);
				if (questionInfo == null) {
					GameLog.getInstance().error("Survey question not found! index : " + questionIndex);
					setRedirect(this.getClass());
					return;
				}
				String qAnswer = null;
				byte newProgress = (byte) (questionIndex + 1);
				if (questionInfo.getType() == SurveyQuestionType.OPTION_SELECT.ordinal()) {
					String strOptionList = getContext().getRequestParameter("options");
					if (strOptionList == null) {
						GameLog.getInstance().error("No option selected!");
						setRedirect(this.getClass());
						return;
					}
					if (strOptionList.isEmpty() || !strOptionList.matches("[0-9]+(,[0-9]+)*")) {
						if (questionInfo.isRequired()) {
							GameLog.getInstance().error("Option is in invalid format!");
							setRedirect(this.getClass());
							return;
						}
					} else {
						String[] arr = strOptionList.split(",");
						List<Long> ids = new ArrayList<>();
						for (String strId : arr) {
							if (CommonMethod.isValidNumeric(strId, 1, Long.MAX_VALUE)) {
								ids.add(Long.parseLong(strId));
							}
						}
						if (arr.length > questionInfo.getMaxChoice()) {
							GameLog.getInstance().error("Option list in invalid!, max_choice:" + questionInfo.getMaxChoice());
							setRedirect(this.getClass());
							return;
						}
						if (arr.length < questionInfo.getMinChoice()) {
							GameLog.getInstance().error("Option list in invalid!, min_choice:" + questionInfo.getMinChoice());
							setRedirect(this.getClass());
							return;
						}
						if (SurveyService.getCountValidSurveyOption(ids) != arr.length) {
							GameLog.getInstance().error("Option list in invalid!");
							setRedirect(this.getClass());
							return;
						}
						qAnswer = strOptionList.trim();
					}
				} else if (questionInfo.getType() == SurveyQuestionType.GIVING_ANSWER.ordinal()) {
					String strAnswer = getContext().getRequestParameter("options");
					if (strAnswer == null) {
						GameLog.getInstance().error("No answer!");
						setRedirect(this.getClass());
						return;
					}
					strAnswer = strAnswer.trim();
					int length = strAnswer.length();
					if (length == 0 && questionInfo.isRequired()) {
						GameLog.getInstance().error("Answer is required!");
						setRedirect(this.getClass());
						return;
					}
					length = strAnswer.replaceAll("\r\n", ".").replaceAll("[\r\n]", ".").length();
					if (length < questionInfo.getMinChoice() || length > questionInfo.getMaxChoice()) {
						GameLog.getInstance().error("Answer is too long : " + length);
						setRedirect(this.getClass());
						return;
					}
					qAnswer = strAnswer.trim();
				} else if (questionInfo.getType() == SurveyQuestionType.YES_NO.ordinal()) {
					String strAnswer = getContext().getRequestParameter("options");
					boolean ans = false;
					if (strAnswer != null && strAnswer.equals("on")) {
						ans = true;
					}
					if (!ans) {
						if (questionInfo.getMinChoice() > 0) {
							newProgress = (byte) questionInfo.getMinChoice();
						}
						qAnswer = SurveyService.YES_NO_OPTION_NO_TEMPL;
					} else {
						if (questionInfo.getMaxChoice() > 0) {
							newProgress = (byte) questionInfo.getMaxChoice();
						}
						qAnswer = SurveyService.YES_NO_OPTION_YES_TEMPL;
					}
				} else if (questionInfo.getType() == SurveyQuestionType.NUMERAL_LIST.ordinal()) {
					String strAnswer = getContext().getRequestParameter("options");
					if (strAnswer == null) {
						GameLog.getInstance().error("No answer!");
						setRedirect(this.getClass());
						return;
					}
					strAnswer = strAnswer.trim();
					int length = strAnswer.length();
					if (length == 0 && questionInfo.isRequired()) {
						GameLog.getInstance().error("Answer is required!");
						setRedirect(this.getClass());
						return;
					}
					if (questionInfo.getMinChoice() != 0 && questionInfo.getMaxChoice() != 0) {
						if (!CommonMethod.isValidNumeric(strAnswer, questionInfo.getMinChoice(), questionInfo.getMaxChoice())) {
							GameLog.getInstance().error("Answer is invalid!");
							setRedirect(this.getClass());
							return;
						}
					}
					qAnswer = strAnswer.trim();
				} else if (questionInfo.getType() == SurveyQuestionType.DATE_PICKER.ordinal()) {
					String strAnswer = getContext().getRequestParameter("options");
					if (strAnswer == null) {
						GameLog.getInstance().error("No answer!");
						setRedirect(this.getClass());
						return;
					}
					strAnswer = strAnswer.trim();
					int length = strAnswer.length();
					if (length == 0 && questionInfo.isRequired()) {
						GameLog.getInstance().error("Answer is required!");
						setRedirect(this.getClass());
						return;
					}
					if (!strAnswer.matches("[0-9]{4}/[0-9]{2}/[0-9]{2}")) {
						GameLog.getInstance().error("Answer is in an invalid format!");
						setRedirect(this.getClass());
						return;
					}
					Date date = CommonMethod.getDate(strAnswer, CommonDefine.DATE_FORMAT_PATTERN_WITHOUT_TIME);
					if (date == null) {
						GameLog.getInstance().error("Answer is in an invalid format of date!");
						setRedirect(this.getClass());
						return;
					}
					qAnswer = strAnswer.trim();
				} else if (questionInfo.getType() == SurveyQuestionType.OPTION_LIST.ordinal()) {
					String strAnswer = getContext().getRequestParameter("options");
					if (strAnswer == null) {
						GameLog.getInstance().error("No answer!");
						setRedirect(this.getClass());
						return;
					}
					strAnswer = strAnswer.trim();
					int length = strAnswer.length();
					if (length == 0 && questionInfo.isRequired()) {
						GameLog.getInstance().error("Answer is required!");
						setRedirect(this.getClass());
						return;
					}
					qAnswer = strAnswer.trim();
				} else {
					GameLog.getInstance().error("Question type is invalid!");
					setRedirect(this.getClass());
					return;
				}
				if (qAnswer == null) {
					qAnswer = "";
				}
				resultObject.addAnswer(questionIndex, qAnswer);
				resultInfo.setProgress(newProgress);
				resultInfo.setResult(resultObject.toString());
				resultInfo.setUpdated(CommonMethod.getFormatStringNow());
				if (resultInfo.getSurveyResultInfo().isInsert()) {
					if (CommonDaoFactory.Insert(resultInfo.getSurveyResultInfo()) <= 0) {
						GameLog.getInstance().info("Cant insert option result!");
						setRedirect(this.getClass());
						return;
					}
					GameLog.getInstance().info("Inserted option result successfully!");
				} else {
					if (CommonDaoFactory.Update(resultInfo.getSurveyResultInfo()) < 0) {
						GameLog.getInstance().info("Cant update option result!");
						setRedirect(this.getClass());
						return;
					}
					GameLog.getInstance().info("Updated option result successfully!");
				}
			}
		}
		Map<String, String> params = new HashMap<>();
		if (activeSurvey != null) {
			params.put("p", activeSurvey.getPathname());
		}
		setRedirect(this.getClass(), params);
		return;
	}

	@Override
	public void onRender() {
		super.onRender();
		if (getRedirect() != null) {
			return;
		}
		if (userInfo == null) {
			addModel("login_modal", CommonHtmlFunc.getLoginFormModal(getHostURL() + getContextPath(), getMessages()));
			return;
		} else {
			addModel("logoutForm", getLogoutForm());
		}
		renderUserInfo();
		if (activeSurvey == null) {
			renderListActiveSurvey(userInfo);
			return;
		}
		addModel("survey_path", activeSurvey.getPathname());
		addModel("survey_name", activeSurvey.getName());
		addModel("survey_desc", CommonMethod.getFormatContentHtmlForDisplaying(activeSurvey.getDescription()));
		byte questionIndex = 1;
		SurveyAnswerWrapper resultObject = null;
		SurveyResult resultInfo = SurveyService.getSurveyResult(activeSurvey.getId(), userInfo.getEmpid());
		if (resultInfo != null) {
			questionIndex = resultInfo.getProgress();
			resultObject = SurveyAnswerWrapper.parse(resultInfo.getResult()); // TODO check null
		}
		if (resultObject != null) {
			// previous question.
			renderPreviousAnswer(resultObject);
		}
		SurveyQuestion questionInfo = SurveyService.getSurveyQuestion(activeSurvey.getId(), questionIndex);
		if (questionInfo == null) {
			//GameLog.getInstance().error("Survey question not found! index : " + questionIndex);
			return;
		}
		// current question.
		renderQuestionInfo(questionInfo);
	}
	
	private void renderListActiveSurvey(User userinfo) {
		List<Survey> listActive = SurveyService.getActiveSurveyList(userinfo.getFlag());
		if (listActive == null || listActive.isEmpty()) {
			return;
		}
		List<Survey> validList = null;;
		for (Survey surveyInfo : listActive) {
			if (!VoteFuncs.hasSurveyAccess(userInfo, surveyInfo)) {
				continue;
			}
			if (validList == null) {
				validList = new ArrayList<>();
			}
			validList.add(surveyInfo);
		}
		if (validList != null) {
			addModel("active_list", validList);
		}
	}
	
	private void renderPreviousAnswer(SurveyAnswerWrapper resultObject) {
		if (resultObject == null) {
			return;
		}
		List<SurveyAnswer> answerList = resultObject.getSurvey_answer();
		if (answerList == null || answerList.isEmpty()) {
			return;
		}
		List<Byte> questionIndexs = new ArrayList<>();
		for (SurveyAnswer answer : answerList) {
			if (!questionIndexs.contains(answer.getQuestion_index())) {
				questionIndexs.add(answer.getQuestion_index());
			}
		}
		Map<Byte, SurveyQuestion> mapQuestion = SurveyService.getSurveyQuestionMapByIndexs(activeSurvey.getId(), questionIndexs);
		if (mapQuestion == null || mapQuestion.size() != answerList.size()) {
			return;
		}
		Collections.sort(answerList, new Comparator<SurveyAnswer>() {

			@Override
			public int compare(SurveyAnswer o1, SurveyAnswer o2) {
				return o1.getQuestion_index() - o2.getQuestion_index();
			}
		});
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"\">");
		for (SurveyAnswer answer : answerList) {
			sb.append("<div class=\"\" style=\"margin-bottom:1rem;\">");
			SurveyQuestion questionInfo = mapQuestion.get(answer.getQuestion_index());
			sb.append(getQuestionTitleContentDesc(questionInfo));
			SurveyQuestionType type = SurveyQuestionType.valueOf(questionInfo.getType());
			switch (type) {
			case OPTION_SELECT:
				sb.append(getAnsweredOption(answer, questionInfo));
				break;
			case GIVING_ANSWER:
			case YES_NO:
			case NUMERAL_LIST:
			case DATE_PICKER:
			case OPTION_LIST:
				sb.append("<div class=\"bg-secondary answer-content\" >");
				sb.append("<p>");
				sb.append(CommonMethod.getFormatContentHtmlForDisplaying(answer.getAnswer()));
				sb.append("</p>");
				sb.append("</div>");
				break;
			case MYSTERY_GIFT_BOX:
				sb.append(getLuckyNumberMysteryReward(answer));
				break;
			default:
				break;
			}
			sb.append("</div>");
		}
		sb.append("</div>");
		
		addModel("previous_answer", sb.toString());
	}
	
	private void renderQuestionInfo(SurveyQuestion questionInfo) {
		if (questionInfo == null) {
			GameLog.getInstance().error("Survey question not found!");
			return;
		}
		SurveyQuestionType type = SurveyQuestionType.valueOf(questionInfo.getType());
		switch (type) {
		case OPTION_SELECT:
			renderQuestionOption(questionInfo);
			break;
		case GIVING_ANSWER:
			renderQuestionAnswer(questionInfo);
			break;
		case YES_NO:
			renderQuestionYesNo(questionInfo);
			break;
		case NUMERAL_LIST:
			renderQuestionSelectList(questionInfo);
			break;
		case DATE_PICKER:
			renderQuestionSelectDate(questionInfo);
			break;
		case OPTION_LIST:
			renderQuestionListOption(questionInfo);
			break;
		case MYSTERY_GIFT_BOX:
			renderQuestionLuckyBoxList(questionInfo);
			break;
		default:
			break;
		}
	}
	
	private void renderQuestionOption(SurveyQuestion questionInfo) {
		if (questionInfo == null) {
			GameLog.getInstance().error("Survey question not found!");
			return;
		}
		List<SurveyOption> optionList = SurveyService.getSurveyOptionList(questionInfo.getId());
		if (optionList == null || optionList.isEmpty()) {
			GameLog.getInstance().error("[Survey] Survey's option list is null!");
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"\">");
		sb.append(getQuestionTitleContentDesc(questionInfo));
		sb.append("<div class=\"row justify-content-center\">");
		for (SurveyOption optionInfo : optionList) {
			sb.append("<div class=\"col-sm-6 col-md-4 col-lg-3\">");
			sb.append(getOptionPreview(optionInfo, true, false));
			sb.append("</div>");
		}
		sb.append("</div>");
		sb.append("</div>");
		sb.append("<div class=\"d-none\">");
		sb.append("<span class=\"d-none\" id=\"max_choice\">").append(questionInfo.getMaxChoice()).append("</span>");
		sb.append("<span class=\"d-none\" id=\"min_choice\">").append(questionInfo.getMinChoice()).append("</span>");
		sb.append("</div>");
		//
		sb.append(getQuestionSubmitButton(questionInfo));
		addModel("question_info", sb.toString());
	}
	
	private void renderQuestionYesNo(SurveyQuestion questionInfo) {
		if (questionInfo == null) {
			GameLog.getInstance().error("Survey question not found!");
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"\">");
		sb.append(getQuestionTitleContentDesc(questionInfo));
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"custom-control custom-radio\">");
		sb.append(String.format("<input type=\"radio\" id=\"yes_no-yes\" name=\"yes_no\" class=\"custom-control-input\" value=\"on\">", ""));
		sb.append("<label class=\"custom-control-label none-select\" for=\"yes_no-yes\" >" + SurveyService.YES_NO_OPTION_YES_TEMPL + "</label>");
		sb.append("</div>");
		sb.append("<div class=\"custom-control custom-radio\" style=\"margin-top:0.5rem;\">");
		sb.append(String.format("<input type=\"radio\" id=\"yes_no-no\" name=\"yes_no\" class=\"custom-control-input\" value=\"off\" >", ""));
		sb.append("<label class=\"custom-control-label none-select\" for=\"yes_no-no\" >" + SurveyService.YES_NO_OPTION_NO_TEMPL + "</label>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		//
		sb.append(getQuestionSubmitButton(questionInfo));
		addModel("question_info", sb.toString());
	}
	
	private void renderQuestionAnswer(SurveyQuestion questionInfo) {
		if (questionInfo == null) {
			GameLog.getInstance().error("Survey question not found!");
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"\">");
		sb.append(getQuestionTitleContentDesc(questionInfo));
		sb.append("<div class=\"form-group\">");
			sb.append("<label class=\"form-control-label font-weight-bold\">Your answer:&nbsp;").append("<span class=\"char-count\"></span>").append("</label>");
			sb.append("<textarea class=\"form-control text-answer\" rows=\"5\">").append("</textarea>");
		sb.append("</div>");
		sb.append("</div>");
		//
		sb.append(getQuestionSubmitButton(questionInfo));
		addModel("question_info", sb.toString());
	}
	
	private void renderQuestionSelectList(SurveyQuestion questionInfo) {
		if (questionInfo == null) {
			GameLog.getInstance().error("Survey question not found!");
			return;
		}
		if (questionInfo.getMinChoice() + questionInfo.getMaxChoice() == 0 || questionInfo.getMinChoice() > questionInfo.getMaxChoice()) {
			GameLog.getInstance().error("Survey question param is invalid for list select! " + questionInfo.getId());
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"\">");
		sb.append(getQuestionTitleContentDesc(questionInfo));
		sb.append("<div class=\"form-group\">");
			sb.append("<label class=\"form-control-label font-weight-bold\" for=\"select-label-" + questionInfo.getId() + "\">Your answer:&nbsp;").append("</label>");
			sb.append("<select id=\"select-label-" + questionInfo.getId() + "\" class=\"form-control select-answer\">");
			for (int i = questionInfo.getMinChoice(); i <= questionInfo.getMaxChoice(); i++) {
				sb.append(String.format("<option value=\"%d\">%d</option>", i, i));
			}
			sb.append("</select>");
		sb.append("</div>");
		sb.append("</div>");
		//
		sb.append(getQuestionSubmitButton(questionInfo));
		addModel("question_info", sb.toString());
	}
	
	private void renderQuestionSelectDate(SurveyQuestion questionInfo) {
		if (questionInfo == null) {
			GameLog.getInstance().error("Survey question not found!");
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"\">");
		sb.append(getQuestionTitleContentDesc(questionInfo));
		sb.append("<div class=\"form-group\">");
		sb.append("<label class=\"form-control-label font-weight-bold\" for=\"select-label-" + questionInfo.getId() + "\">Your answer:&nbsp;").append("</label>");
			sb.append("<input id=\"select-label-" + questionInfo.getId() + "\" class=\"form-control date-picker\" type=\"text\"/>");
		sb.append("</div>");
		sb.append("</div>");
		//
		sb.append(getQuestionSubmitButton(questionInfo));
		addModel("question_info", sb.toString());
	}
	
	private void renderQuestionListOption(SurveyQuestion questionInfo) {
		if (questionInfo == null) {
			GameLog.getInstance().error("Survey question not found!");
			return;
		}
		List<SurveyOption> optionList = SurveyService.getSurveyOptionList(questionInfo.getId());
		if (optionList == null || optionList.isEmpty()) {
			GameLog.getInstance().error("[Survey] Survey's option list is null!");
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"\">");
		sb.append(getQuestionTitleContentDesc(questionInfo));
		sb.append("<div class=\"form-group\">");
			sb.append("<label class=\"form-control-label font-weight-bold\" for=\"select-label-" + questionInfo.getId() + "\">Your answer:&nbsp;").append("</label>");
			sb.append("<select id=\"select-label-" + questionInfo.getId() + "\" class=\"form-control select-answer\">");
			for (SurveyOption option : optionList) {
				sb.append(String.format("<option value=\"%s\">%s</option>", option.getTitle(), option.getTitle()));
			}
			sb.append("</select>");
		sb.append("</div>");
		sb.append("</div>");
		//
		sb.append(getQuestionSubmitButton(questionInfo));
		addModel("question_info", sb.toString());
	}
	
	private void renderQuestionLuckyBoxList(SurveyQuestion questionInfo) {
		if (questionInfo == null) {
			GameLog.getInstance().error("Survey question not found!");
			return;
		}
		if (questionInfo.getMinChoice() <= 0 || questionInfo.getMaxChoice() < questionInfo.getMinChoice()) {
			GameLog.getInstance().error("Survey question param is invalid for lucky box select! " + questionInfo.getId());
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"\">");
		sb.append(getQuestionTitleContentDesc(questionInfo));
		sb.append("<div class=\"d-flex\" style=\"flex-flow:row wrap;justify-content:center;gap:1rem;\">");
		for (int i = 1; i <= questionInfo.getMinChoice(); i++) {
			sb.append(String.format("<div class=\"mystery-box\" data-id=\"%d\">", i));
			sb.append(String.format("<img src=\"%s\" alt=\"gift\" />", getContextPath() + "/img/vote/gift.png"));
			sb.append("</div>");
		}
		sb.append("</div>");
		sb.append("</div>");
		//
		sb.append(getQuestionSubmitButton(questionInfo));
		addModel("question_info", sb.toString());
	}
	
	private String getAnsweredOption(SurveyAnswer answer, SurveyQuestion questionInfo) {
		if (answer == null || questionInfo == null) {
			return "";
		}
		String strAnseredIds = answer.getAnswer();
		if (StringUtils.isEmpty(strAnseredIds) || !strAnseredIds.matches("[0-9]+(,[0-9]+)*")) {
			return "";
		}
		String[] arrIds = strAnseredIds.split(",");
		List<Long> optionIds = new ArrayList<>();
		for (String strId : arrIds) {
			if (CommonMethod.isValidNumeric(strId, 1, Long.MAX_VALUE)) {
				optionIds.add(Long.parseLong(strId));
			}
		}
		List<SurveyOption> optionList = SurveyService.getSurveyOptionList(optionIds);
		if (optionList == null || optionList.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"\">");
		sb.append("<div class=\"row justify-content-center\">");
		for (SurveyOption option : optionList) {
			sb.append("<div class=\"col-sm-6 col-md-4 col-lg-3\">");
			sb.append(getOptionPreview(option, false, false));
			sb.append("</div>");
		}
		sb.append("</div>");
		sb.append("</div>");
		
		return sb.toString();
	}
	
	private String getLuckyNumberMysteryReward(SurveyAnswer answer) {
		if (answer == null) {
			return "";
		}
		String strAnsered = answer.getAnswer();
		if (!strAnsered.matches("^[0-9]+$")) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"\">");
		sb.append("<div class=\"row justify-content-center\">");
			sb.append("<div class=\"col-sm-6 col-md-4 col-lg-3\">");
			sb.append("<div class=\"survey-option-wrapper\">");
				sb.append(String.format("<div style=\"position:relative;\" class=\"survey-option\" title=\"%s\">", CommonMethod.getFormatContentHtmlForTooltip(strAnsered)));
				sb.append("<div class=\"survey-opt-image none-select lucky-number\">").append(CommonMethod.getFormatContentHtmlForDisplaying(strAnsered)).append("</div>");
				sb.append("<label class=\"font-weight-bold\" style=\"text-overflow:ellipsis;overflow:hidden;white-space:nowrap;width:100%;\" >");
				sb.append("Your lucky number");
				sb.append("</label>");
				sb.append("</div>");
			sb.append("</div>");
			sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		
		return sb.toString();
	}
	
	private String getOptionPreview(SurveyOption info, boolean selectable, boolean checked) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"survey-option-wrapper" + ((info == null && !selectable) ? " result-empty" : "") + "\">");
		if (info != null) {
			if (info.getType() == SurveyOptionType.IMAGE.ordinal()) {
				sb.append(String.format("<div style=\"position:relative;\" data=\"option-%d\" class=\"survey-option option-%s\" title=\"%s\">", info.getId(), (selectable ? "select-" + info.getId() : "result-" + info.getId()), CommonMethod.getFormatContentHtmlForTooltip(info.getDescription())));
				sb.append("<img src=\"" + CommonMethod.getStaticFile(info.getContent()) + "\" alt=\"" + info.getTitle() + "\" class=\"survey-opt-image" + (checked ? " check" : "") + "\">");
				sb.append("<label class=\"font-weight-bold\" style=\"text-overflow:ellipsis;overflow:hidden;white-space:nowrap;width:100%;\" >");
				sb.append(info.getTitle());
				sb.append("</label>");
				if (selectable) {
					sb.append("<div class=\"option-checked\" style=\"display:" + (checked ? "block" : "none") + ";\">");
					sb.append("<img src=\"" + getHostURL() + getContextPath() + "/img/vote/check_icon_c.png" + "\" style=\"width:90%;\" />");
					sb.append("</div>");
				}
				sb.append("</div>");
			} else if (info.getType() == SurveyOptionType.TEXT.ordinal()) {
				sb.append(String.format("<div style=\"position:relative;\" data=\"option-%d\" class=\"survey-option option-%s\" title=\"%s\">", info.getId(), (selectable ? "select-" + info.getId() : "result-" + info.getId()), CommonMethod.getFormatContentHtmlForTooltip(info.getDescription())));
				sb.append("<div class=\"survey-opt-image none-select" + (checked ? " check" : "") + "\">").append(CommonMethod.getFormatContentHtmlForDisplaying(info.getContent())).append("</div>");
				sb.append("<label class=\"font-weight-bold\" style=\"text-overflow:ellipsis;overflow:hidden;white-space:nowrap;width:100%;\" >");
				sb.append(info.getTitle());
				sb.append("</label>");
				if (selectable) {
					sb.append("<div class=\"option-checked\" style=\"display:" + (checked ? "block" : "none") + ";\">");
					sb.append("<img src=\"" + getHostURL() + getContextPath() + "/img/vote/check_icon_c.png" + "\" style=\"width:90%;\" />");
					sb.append("</div>");
				}
				sb.append("</div>");
			}
		}
		sb.append("</div>");
		return sb.toString();
	}
	
	private String getQuestionTitleContentDesc(SurveyQuestion questionInfo) {
		if (questionInfo == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"\">");
		sb.append("<div class=\"form-group\">");
		sb.append(String.format("<h4 class=\"%s\">", (questionInfo.isRequired() ? "required-field" : ""))).append(questionInfo.getIdx()).append(". ").append(questionInfo.getTitle()).append("</h4>");
		if (StringUtils.isNotEmpty(questionInfo.getContent())) {
			sb.append("<p style=\"padding: 0 1rem 0 1rem;font-size: 1rem; font-style: italic;\">");
				sb.append(CommonMethod.getFormatContentHtmlForDisplaying(questionInfo.getContent()));
			sb.append("</p>");
		}
		sb.append("</div>");
		sb.append("</div>");
		
		return sb.toString();
	}
	
	private String getQuestionSubmitButton(SurveyQuestion questionInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"text-center\" style=\"margin-top:3rem;\">");
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-sm-12 col-lg-4\" style=\"margin:0 auto;\">");
		if (questionInfo.getType() == SurveyQuestionType.MYSTERY_GIFT_BOX.ordinal()) {
			sb.append("<button type=\"button\" class=\"btn btn-success\" onclick=\"window.location.reload();\" style=\"width:100%;\">Confirm</button>");
		} else {
			sb.append(String.format("<button type=\"submit\" data-required=\"%s\" data-max=\"%d\" data-min=\"%d\" id=\"btn-submit-vote\" class=\"btn btn-success\" style=\"width:100%%;\">Submit</button>", (questionInfo.isRequired() ? "1" : "0"), questionInfo.getMaxChoice(), questionInfo.getMinChoice()));
		}
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	private void renderUserInfo() {
		if (userInfo == null) {
			return;
		}
		String userName = userInfo.getName();
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"row\">");
			sb.append("<div class=\"col-lg-12 text-center\">");
				sb.append("<label class=\"font-weight-bold text-success\" style=\"margin-top:0.8rem;font-size:" + (userName.length() > 24 ? "1" : "1.125") + "rem;\" >");
					sb.append("Welcome, " + userName);
					sb.append("<a href=\"#\" id=\"logout\" title=\"Logout\">");
					sb.append("<img src=\"" + getContextPath() + "/img/vote/logout-3.png" + "\" style=\"transform:scale(0.5,0.5);\"/>");
					sb.append("</a>");
				sb.append("</label>");
			sb.append("</div>");
			String pageLink = null;
			if (userInfo.isVoteAdmin()) {
				pageLink = String.format("<button type=\"button\" class=\"btn btn-secondary\"><a href=\"%s\">Administration Page</a></button>", getPagePath(ManageVote.class));
			} 
//			else if (surveyData.isReadonly() && activeSurvey != null) {
//				pageLink = String.format("<button type=\"button\" class=\"btn btn-secondary\"><a href=\"%s\">Result Page</a></button>", getPagePath(ManageVote.class) + "?mode=5" + "&survey_id=" + activeSurvey.getId());
//			}
			if (pageLink != null) {
				sb.append("<div class=\"col-lg-12 text-center\">");
				sb.append("<label class=\"\" style=\"margin-bottom:1rem;\" >");
					sb.append(pageLink);
				sb.append("</label>");
				sb.append("</div>");
			}
		sb.append("</div>");
		addModel("user_info", sb.toString());
	}
	
	protected int getMenuBarIndex() {
		return MainNavBarEnum.VOTE.getIndex();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(importCss("/css/vote/vote.css"));
		headElements.add(importJs("/js/vote/vote.js"));

		return headElements;
	}
}
