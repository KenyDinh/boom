package dev.boom.pages.vote;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.click.element.JsImport;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.VoteFuncs;
import dev.boom.common.enums.SurveyOptionType;
import dev.boom.common.enums.SurveyQuestionType;
import dev.boom.common.enums.SurveyStatus;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.services.Survey;
import dev.boom.services.SurveyOption;
import dev.boom.services.SurveyQuestion;
import dev.boom.services.SurveyService;
import dev.boom.services.SurveyUserAccess;
import dev.boom.services.json.SurveyOptionStatistics;
import dev.boom.services.json.SurveyResultObject;
import dev.boom.tbl.info.TblSurveyResultInfo;
import dev.boom.tbl.info.TblSurveyUserAccessInfo;

public class ManageVote extends VotePageBase {

	private static final long serialVersionUID = 1L;
	private static final int MODE_SURVEY_NEW = 1;
	private static final int MODE_SURVEY_EDIT = 2;
	private static final int MODE_QUESTION_NEW = 3;
	private static final int MODE_QUESTION_EDIT = 4;
	private static final int MODE_OPTION_NEW = 5;
	private static final int MODE_OPTION_EDIT = 6;
	private static final int MODE_SHOW_RESULT = 7;
	private static final int MODE_EXPORT_RESULT = 8;
	
	private Survey survey = null;
	private int mode = 0;
	private boolean error = false;

	public ManageVote() {
	}

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (userInfo == null) {
			GameLog.getInstance().error("[ManageVote] survey valid data is null!");
			setRedirect(Vote.class);
			return false;
		}
		if (!userInfo.isVoteAdmin()) {
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
		headElements.add(importJs("/js/vote/manage_vote.js"));
		
		return headElements;
	}

	@Override
	public void onInit() {
		super.onInit();
		String strSurveyId = getContext().getRequestParameter("survey_id");
		if (CommonMethod.isValidNumeric(strSurveyId, 1, Long.MAX_VALUE)) {
			survey = SurveyService.getSurveyInfoById(Long.parseLong(strSurveyId));
		}
		if (survey != null) {
			addModel("survey", survey);
		}
		String strMode = getContext().getRequestParameter("mode");
		if (CommonMethod.isValidNumeric(strMode, 1, Integer.MAX_VALUE)) {
			mode = Integer.parseInt(strMode);
		}
		addModel("mode", mode);
		if (userInfo.isVoteAdmin()) {
			addModel("admin", 1);
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
		{
			Survey surveyInfo = null;
			if (mode == MODE_SURVEY_EDIT) {
				surveyInfo = survey;
			} else {
				surveyInfo = new Survey();
				Date now = new Date();
				surveyInfo.setCreated(CommonMethod.getFormatDateString(now));
				surveyInfo.setUpdated(CommonMethod.getFormatDateString(now));
				surveyInfo.setExpired(CommonMethod.getFormatDateString(new Date(now.getTime() + CommonDefine.MILLION_SECOND_DAY)));
			}
			if (surveyInfo == null) {
				GameLog.getInstance().error("[ManageVote] Survey is null!");
				error = true;
				return;
			}
			if (mode == MODE_SURVEY_EDIT && getContext().getRequestParameter("delete") != null) {
				String sql = "DELETE FROM survey_result_info WHERE survey_id = " + surveyInfo.getId();
				CommonDaoFactory.functionTransaction((Connection conn) -> {
					CommonDaoFactory.executeUpdate(conn, sql);
					return true;
				});
				VoteFuncs.reInitAllRewardId();
			} else {
				String strName = getContext().getRequestParameter("name");
				if (strName != null && strName.length() > 0 && !surveyInfo.getName().equals(strName)) {
					surveyInfo.setName(strName);
					update = true;
				}
				String strStatus = getContext().getRequestParameter("status");
				if (CommonMethod.isValidNumeric(strStatus, 0, Byte.MAX_VALUE)) {
					SurveyStatus status = SurveyStatus.valueOf(Byte.parseByte(strStatus));
					byte stt = (byte) status.ordinal();
					if (surveyInfo.getStatus() != stt) {
						surveyInfo.setStatus(stt);
						update = true;
					}
				}
				String strDescription = getContext().getRequestParameter("desc");
				if (strDescription != null && strDescription.length() > 0) {
					if (!surveyInfo.getDescription().equals(strDescription)) {
						surveyInfo.setDescription(strDescription);
						update = true;
					}
				}
				String strExpiration = getContext().getRequestParameter("expired");
				if (strExpiration != null && strExpiration.matches(CommonDefine.DATE_REGEX_PATTERN)) {
					Date expired = CommonMethod.getDate(strExpiration.replace("/", "-"));
					if (expired != null && expired.getTime() != surveyInfo.getExpiredDate().getTime()) {
						surveyInfo.setExpired(CommonMethod.getFormatDateString(expired));
						update = true;
					}
				}
				FileItem file = getContext().getFileItem("access_list");
				List<DaoValue> listUserAccess = null;
				if (file != null && StringUtils.isNotBlank(file.getName())) {
					listUserAccess = getSurveyUserAccessList(file);
					int nFlag = 0;
					if (listUserAccess != null && !listUserAccess.isEmpty()) {
						nFlag = 1;
					}
					if (nFlag != surveyInfo.getFlag()) {
						surveyInfo.setFlag(nFlag);
						update = true;
					}
				}
				long surveyID = surveyInfo.getId();
				if (mode == MODE_SURVEY_NEW) {
					surveyInfo.setPathname(UUID.randomUUID().toString().replace("-", ""));
					surveyID = (Integer) CommonDaoFactory.Insert(surveyInfo.getSurveyInfo());
					if (surveyID <= 0) {
						GameLog.getInstance().error("[ManageVote] insert survey fail!");
						error = true;
						return;
					}
				} else if (update) {
					if (CommonDaoFactory.Update(surveyInfo.getSurveyInfo()) < 0) {
						GameLog.getInstance().error("[ManageVote] update survey fail!");
						error = true;
						return;
					}
				}
				updateSurveyAccessList(surveyID, listUserAccess);
			}
		}
			break;
		case MODE_QUESTION_NEW:
		case MODE_QUESTION_EDIT:
		{
			if (survey == null) {
				GameLog.getInstance().error("[ManageVote] no survey selected!");
				error = true;
				return;
			}
			SurveyQuestion questionInfo = null;
			if (mode == MODE_QUESTION_EDIT) {
				String strQuestionId = getContext().getRequestParameter("question_id");
				if (!CommonMethod.isValidNumeric(strQuestionId, 1, Long.MAX_VALUE)) {
					GameLog.getInstance().error("[ManageVote] invalid question id!");
					error = true;
					return;
				}
				questionInfo = SurveyService.getSurveyQuestionById(Long.parseLong(strQuestionId));
			} else {
				questionInfo = new SurveyQuestion();
				questionInfo.setSurveyId(survey.getId());
			}
			if (questionInfo == null || questionInfo.getSurveyId() != survey.getId()) {
				GameLog.getInstance().error("[ManageVote] Question is null!");
				error = true;
				return;
			}
			if (mode == MODE_QUESTION_EDIT && getContext().getRequestParameter("delete") != null) {
				if (CommonDaoFactory.Delete(questionInfo.getSurveyQuestionInfo()) < 0) {
					GameLog.getInstance().error("[ManageVote] delete question failed!");
					error = true;
					return;
				}
				VoteFuncs.removeExistingRewardID(survey.getId(), questionInfo.getIdx());
			} else {
				String strType = getContext().getRequestParameter("type");
				if (CommonMethod.isValidNumeric(strType, 0, Byte.MAX_VALUE)) {
					SurveyQuestionType qType = SurveyQuestionType.valueOf(Byte.parseByte(strType));
					if (qType.ordinal() != questionInfo.getType()) {
						questionInfo.setType((byte)qType.ordinal());
						update = true;
					}
				}
				String strTitle = getContext().getRequestParameter("title");
				if (strTitle != null && strTitle.length() > 0 && !questionInfo.getTitle().equals(strTitle)) {
					questionInfo.setTitle(strTitle);
					update = true;
				}
				String strContent = getContext().getRequestParameter("content");
				if (strContent != null && !questionInfo.getContent().equals(strContent)) {
					questionInfo.setContent(strContent);
					update = true;
				}
				String strMin = getContext().getRequestParameter("min");
				if (CommonMethod.isValidNumeric(strMin, 0, Integer.MAX_VALUE)) {
					int min = Integer.parseInt(strMin);
					if (questionInfo.getMinChoice() != min) {
						questionInfo.setMinChoice(min);
						update = true;
					}
				}
				String strMax = getContext().getRequestParameter("max");
				if (CommonMethod.isValidNumeric(strMax, 0, Integer.MAX_VALUE)) {
					int max = Integer.parseInt(strMax);
					if (questionInfo.getMaxChoice() != max) {
						questionInfo.setMaxChoice(max);
						update = true;
					}
				}
				questionInfo.setOptional((byte) 0);
				String strOptional = getContext().getRequestParameter("optional");
				if (strOptional != null && strOptional.equals("on")) {
					questionInfo.setOptional((byte) 1);
					update = true;
				}
				if (mode == MODE_QUESTION_NEW) {
					long nCount = SurveyService.getCountSurveyQuestion(survey.getId());
					byte nIndex = (byte) (nCount + 1);
					questionInfo.setIdx(nIndex);
					if (CommonDaoFactory.Insert(questionInfo.getSurveyQuestionInfo()) <= 0) {
						GameLog.getInstance().error("[ManageVote] insert question fail!");
						error = true;
						return;
					}
				} else if (update){
					if (CommonDaoFactory.Update(questionInfo.getSurveyQuestionInfo()) < 0) {
						GameLog.getInstance().error("[ManageVote] update question fail!");
						error = true;
						return;
					}
				}
			
			}
		}
			break;
		case MODE_OPTION_NEW:
		case MODE_OPTION_EDIT:
		{
			if (survey == null) {
				GameLog.getInstance().error("[ManageVote] no survey selected!");
				error = true;
				return;
			}
			String strQuestionId = getContext().getRequestParameter("question_id");
			if (!CommonMethod.isValidNumeric(strQuestionId, 1, Long.MAX_VALUE)) {
				GameLog.getInstance().error("[ManageVote] invalid question id!");
				error = true;
				return;
			}
			SurveyQuestion questionInfo = SurveyService.getSurveyQuestionById(Long.parseLong(strQuestionId));
			if (questionInfo == null) {
				GameLog.getInstance().error("[ManageVote] no question selected!");
				error = true;
				return;
			}
			SurveyOption surveyOption = null;
			if (mode == MODE_OPTION_EDIT) {
				String strOptionId = getContext().getRequestParameter("option_id");
				if (!CommonMethod.isValidNumeric(strOptionId, 1, Long.MAX_VALUE)) {
					GameLog.getInstance().error("[ManageVote] invalid option id!");
					error = true;
					return;
				}
				surveyOption = SurveyService.getSurveyOptionById(Long.parseLong(strOptionId));
			} else {
				surveyOption = new SurveyOption();
				surveyOption.setQuestionId(questionInfo.getId());
			}
			if (surveyOption == null || surveyOption.getQuestionId() != questionInfo.getId()) {
				GameLog.getInstance().error("[ManageVote] Option is null!");
				error = true;
				return;
			}
			if (mode == MODE_OPTION_EDIT && getContext().getRequestParameter("delete") != null) {
				if (CommonDaoFactory.Delete(surveyOption.getSurveyOptionInfo()) < 0) {
					GameLog.getInstance().error("[ManageVote] delete option fail!");
					error = true;
					return;
				}
			} else {
				SurveyOptionType oldType = SurveyOptionType.valueOf(surveyOption.getType());
				String strType = getContext().getRequestParameter("type");
				if (CommonMethod.isValidNumeric(strType, 0, Byte.MAX_VALUE)) {
					SurveyOptionType oType = SurveyOptionType.valueOf(Byte.parseByte(strType));
					if (oType.ordinal() != surveyOption.getType()) {
						surveyOption.setType((byte)oType.ordinal());
						update = true;
					}
				}
				String strOptName = getContext().getRequestParameter("title");
				if (strOptName != null && strOptName.length() > 0 && !surveyOption.getTitle().equals(strOptName)) {
					surveyOption.setTitle(strOptName);
					update = true;
				}
				String strDesc = getContext().getRequestParameter("desc");
				if (strDesc != null && !surveyOption.getDescription().equals(strDesc)) {
					surveyOption.setDescription(strDesc);
					update = true;
				}
				String oldContent = surveyOption.getContent();
				if (surveyOption.getType() == SurveyOptionType.IMAGE.ordinal()) {
					FileItem image = getContext().getFileItem("content");
					if (image != null) {
						String imgName = image.getName();
						if (StringUtils.isNotBlank(imgName)) {
							GameLog.getInstance().info("[ManageVote] getImage: " + image.getName());
							String imgExt = imgName.substring(imgName.lastIndexOf(".") + 1);
							String newImgName = "";
//							do {
								newImgName = RandomStringUtils.randomAlphanumeric(12) + "." + imgExt;
//							} while (SurveyService.isExistOptionImgFileName(newImgName));
							surveyOption.setContent(newImgName);
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
				} else if (surveyOption.getType() == SurveyOptionType.TEXT.ordinal()){
					String strContent = getContext().getRequestParameter("content");
					if (strContent != null && !surveyOption.getContent().equals(strContent)) {
						surveyOption.setContent(strContent);
						update = true;
					}
				}
				if (mode == MODE_OPTION_NEW) {
					if (CommonDaoFactory.Insert(surveyOption.getSurveyOptionInfo()) <= 0) {
						GameLog.getInstance().error("[ManageVote] insert option fail!");
						error = true;
						return;
					}
				} else if (update){
					if (CommonDaoFactory.Update(surveyOption.getSurveyOptionInfo()) < 0) {
						GameLog.getInstance().error("[ManageVote] update option fail!");
						error = true;
						return;
					}
					if (oldType == SurveyOptionType.IMAGE) {
						if (oldContent != null || !surveyOption.getContent().equals(oldContent)) {
							File oldFile = new File(getFileUploadDir(), oldContent);
							if (oldFile.exists()) {
								if (!oldFile.delete()) {
									GameLog.getInstance().error("[ManageVote] cannot delete old image file: " + oldContent);
								}
							}
						}
					}
				}
			}
		}
			break;
		case MODE_SHOW_RESULT:
		{
			if (survey != null && getContext().getRequestParameter("delete") != null) {
				String strID = getContext().getRequestParameter("id");
				if (CommonMethod.isValidNumeric(strID, 1, Long.MAX_VALUE)) {
					TblSurveyResultInfo info = new TblSurveyResultInfo();
					info.Set("id", Long.parseLong(strID));
					info.Set("survey_id", survey.getId());
					List<DaoValue> listRet = CommonDaoFactory.Select(info);
					if (listRet == null || listRet.size() != 1) {
						GameLog.getInstance().error("[ManageVote] More than one result record to delete!");
						error = true;
						return;
					} else {
						if (CommonDaoFactory.Delete(listRet.get(0)) < 0) {
							GameLog.getInstance().error("[ManageVote] Failed to delete result record!");
							error = true;
							return;
						}
						VoteFuncs.reInitAllRewardId();
						Map<String, String> params = new HashMap<>();
						params.put("mode", String.valueOf(mode));
						params.put("survey_id", String.valueOf(survey.getId()));
						setRedirect(this.getClass(), params);
						return;
					}
				}
			}
		}
			break;
		case MODE_EXPORT_RESULT: 
		{
			if (exportExcelFile()) {
				setPath(null);
				return;
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
		{
			Survey surveyInfo = null;
			String strSurveyId = getContext().getRequestParameter("survey_id");
			if (mode == MODE_SURVEY_EDIT) {
				if (!CommonMethod.isValidNumeric(strSurveyId, 1, Long.MAX_VALUE)) {
					GameLog.getInstance().error("[ManageVote] invalid servey id!");
					error = true;
					return;
				}
				surveyInfo = SurveyService.getSurveyInfoById(Long.parseLong(strSurveyId));
				addModel("title", "Edit Survey");
			} else {
				surveyInfo = new Survey();
				Date now = new Date();
				surveyInfo.setCreated(CommonMethod.getFormatDateString(now));
				surveyInfo.setUpdated(CommonMethod.getFormatDateString(now));
				surveyInfo.setExpired(CommonMethod.getFormatDateString(new Date(now.getTime() + CommonDefine.MILLION_SECOND_DAY)));
				addModel("title", "New Survey");
			}
			if (surveyInfo == null) {
				GameLog.getInstance().error("[ManageVote] Survey is null!");
				error = true;
				return;
			}
			StringBuilder optionSelects = new StringBuilder();
			optionSelects.append("<select class=\"form-control\" id=\"status\" name=\"status\">");
			for (SurveyStatus stt : SurveyStatus.values()) {
				optionSelects.append("<option value=\"").append(stt.ordinal()).append("\" ").append((stt.ordinal() == surveyInfo.getStatus()? "selected" : "")).append(">");
				optionSelects.append(stt.name()).append("</option>");
			}
			optionSelects.append("</select>");
			addModel("status_list", optionSelects.toString());
			addModel("expired", (surveyInfo.getExpired()));
			addModel("survey_form", surveyInfo);
		}
			break;
		case MODE_QUESTION_NEW:
		case MODE_QUESTION_EDIT:
		{
			if (survey == null) {
				GameLog.getInstance().error("[ManageVote] no survey selected!");
				error = true;
				return;
			}
			SurveyQuestion questionInfo = null;
			if (mode == MODE_QUESTION_EDIT) {
				String strQuestionId = getContext().getRequestParameter("question_id");
				if (!CommonMethod.isValidNumeric(strQuestionId, 1, Long.MAX_VALUE)) {
					GameLog.getInstance().error("[ManageVote] invalid question id!");
					error = true;
					return;
				}
				questionInfo = SurveyService.getSurveyQuestionById(Long.parseLong(strQuestionId));
				addModel("title", "Edit Survey Question");
			} else {
				questionInfo = new SurveyQuestion();
				questionInfo.setSurveyId(survey.getId());
				addModel("title", "New Survey Question");
			}
			if (questionInfo == null) {
				GameLog.getInstance().error("[ManageVote] Question is null!");
				error = true;
				return;
			}
			StringBuilder optionSelects = new StringBuilder();
			optionSelects.append("<select class=\"form-control\" id=\"type\" name=\"type\">");
			for (SurveyQuestionType questionType : SurveyQuestionType.values()) {
				optionSelects.append("<option value=\"").append(questionType.ordinal()).append("\" ").append((questionType.ordinal() == questionInfo.getType()? "selected" : "")).append(">");
				optionSelects.append(questionType.name()).append("</option>");
			}
			optionSelects.append("</select>");
			addModel("type_list", optionSelects.toString());
			addModel("question_form", questionInfo);
		}
			break;
		case MODE_OPTION_NEW:
		case MODE_OPTION_EDIT:
		{
			if (survey == null) {
				GameLog.getInstance().error("[ManageVote] no survey selected!");
				error = true;
				return;
			}
			String strQuestionId = getContext().getRequestParameter("question_id");
			if (!CommonMethod.isValidNumeric(strQuestionId, 1, Long.MAX_VALUE)) {
				GameLog.getInstance().error("[ManageVote] invalid question id!");
				error = true;
				return;
			}
			SurveyQuestion questionInfo = SurveyService.getSurveyQuestionById(Long.parseLong(strQuestionId));
			if (questionInfo == null) {
				GameLog.getInstance().error("[ManageVote] no question selected!");
				error = true;
				return;
			}
			SurveyOption surveyOption = null;
			if (mode == MODE_OPTION_EDIT) {
				String strOptionId = getContext().getRequestParameter("option_id");
				if (!CommonMethod.isValidNumeric(strOptionId, 1, Long.MAX_VALUE)) {
					GameLog.getInstance().error("[ManageVote] invalid option id!");
					error = true;
					return;
				}
				surveyOption = SurveyService.getSurveyOptionById(Long.parseLong(strOptionId));
				addModel("title", "Edit Survey Option");
			} else {
				surveyOption = new SurveyOption();
				surveyOption.setQuestionId(questionInfo.getId());
				addModel("title", "New Survey Option");
			}
			if (surveyOption == null) {
				GameLog.getInstance().error("[ManageVote] Option is null!");
				error = true;
				return;
			}
			StringBuilder optionSelects = new StringBuilder();
			optionSelects.append("<select class=\"form-control\" id=\"type\" name=\"type\">");
			for (SurveyOptionType optionType : SurveyOptionType.values()) {
				if (SurveyOptionType.TEXT == optionType && questionInfo.getType() != SurveyQuestionType.OPTION_LIST.ordinal() && questionInfo.getType() != SurveyQuestionType.OPTION_SELECT.ordinal()) {
					continue;
				}
				optionSelects.append(String.format("<option class=\"%s\" value=\"", (optionType == SurveyOptionType.IMAGE ? "image-included" : ""))).append(optionType.ordinal()).append("\" ").append((optionType.ordinal() == surveyOption.getType()? "selected" : "")).append(">");
				optionSelects.append(optionType.name()).append("</option>");
			}
			optionSelects.append("</select>");
			addModel("type_list", optionSelects.toString());
			addModel("option_form", surveyOption);
			addModel("question_id", questionInfo.getId());
		}
			break;
		case MODE_SHOW_RESULT:
		{
			showQuestionTableResult();
		}
			break;
		default:
			showQuestionListDetail();
			break;
		}
	}
	
	private boolean exportExcelFile() {
		if (survey == null) {
			return false;
		}
		List<SurveyQuestion> questionList = SurveyService.getSurveyQuestionList(survey.getId());
		if (questionList == null || questionList.isEmpty()) {
			return false;
		}
		SurveyQuestion targetQuestion = null;
		String strQuestionId = getContext().getRequestParameter("question_id");
		if (CommonMethod.isValidNumeric(strQuestionId, 1, Long.MAX_VALUE)) {
			long questionId = Long.parseLong(strQuestionId);
			for (SurveyQuestion questionInfo : questionList) {
				if (questionInfo.getId() == questionId) {
					targetQuestion = questionInfo;
					break;
				}
			}
		}
		String filename = "SurveyResults";
		if (targetQuestion == null) {
			filename += "_Overrral";
		} else {
			filename += String.format("_Question_%d", targetQuestion.getIdx());
		}
		HSSFWorkbook workbook = getExcelFile(questionList, targetQuestion);
		HttpServletResponse response = getContext().getResponse();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".xls");
		boolean ret = true;
		try {
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ret = false;
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ret = false;
			}
		}
		return ret;
	}
	
	private HSSFWorkbook getExcelFile(List<SurveyQuestion> questionList, SurveyQuestion targetQuestion) {
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Survey Results");
		CellStyle headerStyle = workbook.createCellStyle();
		HSSFFont headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontName(HSSFFont.FONT_ARIAL);
		headerFont.setFontHeightInPoints((short) 11);
		headerStyle.setFont(headerFont);
		CellStyle cellStyle = workbook.createCellStyle();
		HSSFFont cellFont = workbook.createFont();
		cellFont.setFontName(HSSFFont.FONT_ARIAL);
		cellFont.setFontHeightInPoints((short) 11);
		cellStyle.setFont(cellFont);
		int row = 0, column = 0;
		HSSFRow excelRow;
		HSSFCell cell;
		
		excelRow = sheet.createRow(row++);
		if (targetQuestion == null) {
			//
			cell = excelRow.createCell(column++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("ID");
			//
			cell = excelRow.createCell(column++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Name");
			//
			cell = excelRow.createCell(column++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Dep");
			//
			for (SurveyQuestion question : questionList) {
				//
				cell = excelRow.createCell(column++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(String.format("Question %d", question.getIdx()));
			}
			
			List<Map<String, Object>> listOverralResult = SurveyService.calcSurveyOverralResult(questionList, survey);
			if (listOverralResult != null && !listOverralResult.isEmpty()) {
				for (Map<String, Object> data : listOverralResult) {
					excelRow = sheet.createRow(row++);
					column = 0;
					//
					cell = excelRow.createCell(column++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(data.get("user_id").toString());
					//
					cell = excelRow.createCell(column++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(StringEscapeUtils.unescapeHtml(data.get("username").toString()));
					//
					cell = excelRow.createCell(column++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(StringEscapeUtils.unescapeHtml(data.get("dep").toString()));
					//
					for (SurveyQuestion question : questionList) {
						String key = String.format("question_%d", question.getIdx());
						cell = excelRow.createCell(column++);
						cell.setCellStyle(cellStyle);
						if (data.containsKey(key)) {
							cell.setCellValue(StringEscapeUtils.unescapeHtml(data.get(key).toString()));
						}
					}
				}
			}
		} else {
			//
			cell = excelRow.createCell(column++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("ID");
			//
			cell = excelRow.createCell(column++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Name");
			//
			cell = excelRow.createCell(column++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Dep");
			//
			cell = excelRow.createCell(column++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue("Answer");
			
			byte questionIndex = targetQuestion.getIdx();
			Map<Byte, List<SurveyResultObject>> mapResultObject = SurveyService.calcSurveyQuestResult(questionList, survey);
			if (mapResultObject != null && mapResultObject.size() > 0) {
				for (SurveyResultObject surveyResultObject : mapResultObject.get(questionIndex)) {
					excelRow = sheet.createRow(row++);
					column = 0;
					//
					cell = excelRow.createCell(column++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(surveyResultObject.getUser_id());
					//
					cell = excelRow.createCell(column++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(StringEscapeUtils.unescapeHtml(surveyResultObject.getUsername()));
					//
					cell = excelRow.createCell(column++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(StringEscapeUtils.unescapeHtml(surveyResultObject.getDeparment()));
					//
					cell = excelRow.createCell(column++);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(StringEscapeUtils.unescapeHtml(surveyResultObject.getResult()));
				}
			}
		}
		
		for (int i = 0; i < column; i++) {
			if (i <= 1) {
				sheet.autoSizeColumn(i);
			} else {
				sheet.setColumnWidth(i, 8120);
			}
		}
		
		return workbook;
	}
	
	private void showQuestionTableResult() {
		if (survey == null) {
			return;
		}
		long targetQuestionId = 0;
		String strQuestionId = getContext().getRequestParameter("question_id");
		if (CommonMethod.isValidNumeric(strQuestionId, 1, Long.MAX_VALUE)) {
			targetQuestionId = Long.parseLong(strQuestionId);
		}
		StringBuilder sb = new StringBuilder();
		List<SurveyQuestion> questionList = SurveyService.getSurveyQuestionList(survey.getId());
		sb.append("<div style=\"text-align:center;font-size:1.5rem;margin-top:2rem;\">");
		sb.append("<label class=\"font-weight-bold text-info\">Question List</label>");
		sb.append("</div>");
		sb.append("<a href=\"").append(getPagePath(this.getClass()) + (survey != null ? "?survey_id=" + survey.getId() : "") + "\">");
		sb.append("<button class=\"btn btn-secondary\">Back</button>");
		sb.append("</a>");
		sb.append("<div id=\"\" class=\"\">");
		sb.append("<table id=\"question_table\" class=\"table\" style=\"width:100%;\">");
		sb.append("<thead>");
			sb.append("<tr class=\"text-info\">");
				sb.append("<th style=\"width:20%;\">");
					sb.append("Index");
				sb.append("</th>");
				sb.append("<th style=\"width:40%;\">");
					sb.append("Title");
				sb.append("</th>");
				sb.append("<th style=\"width:40%;\">");
					sb.append("Content");
				sb.append("</th>");
			sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		if (questionList == null || questionList.isEmpty()) {
			sb.append("<tr>");
				sb.append("<td id=\"no-question-record\" colspan=\"3\">");
				sb.append("No record found!");
				sb.append("</td>");
			sb.append("</tr>");
		} else {
			for (SurveyQuestion question : questionList) {
				sb.append(String.format("<tr style=\"%s\">", (targetQuestionId == question.getId() ? "background-color:#903b76;" : "")));
					sb.append("<td class=\"index\">");
						sb.append(String.format("<a href=\"%s?mode=%d&survey_id=%d&question_id=%d\" >%d</a>", getPagePath(this.getClass()), MODE_SHOW_RESULT, survey.getId(), question.getId(), question.getIdx()));
					sb.append("</td>");
					sb.append("<td class=\"title\">");
						sb.append(question.getTitle());
					sb.append("</td>");
					sb.append("<td class=\"content\">");
						sb.append(CommonMethod.getFormatContentHtmlForDisplaying(question.getContent()));
					sb.append("</td>");
				sb.append("</tr>");
			}
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		///
		if (questionList != null && questionList.size() > 0) {
			SurveyQuestion targetQuestion = null;
			if (targetQuestionId > 0) {
				for (SurveyQuestion questionInfo : questionList) {
					if (questionInfo.getId() == targetQuestionId) {
						targetQuestion = questionInfo;
						break;
					}
				}
			}
			sb.append("<div style=\"text-align:center;font-size:1.5rem;margin-top:2rem;\">");
			sb.append("<label class=\"font-weight-bold text-info\">Results</label>");
			sb.append("</div>");
			if (targetQuestion == null) {
				sb.append(getOverralResult(questionList));
			} else {
				sb.append(getQuestionResult(questionList, targetQuestion));
			}
		}
		addModel("result", sb.toString());
	}
	
	private String getQuestionResult(List<SurveyQuestion> questionList, SurveyQuestion targetQuestion) {
		if (survey == null || questionList == null || questionList.isEmpty() || targetQuestion == null) {
			return "";
		}
		byte questionIndex = targetQuestion.getIdx();
		Map<Long, SurveyOptionStatistics> statistics = new HashMap<>();
		Map<Byte, List<SurveyResultObject>> mapResultObject = SurveyService.calcSurveyQuestResult(questionList, survey, statistics);
		int totalCount = 0;
		if (statistics.size() > 0) {
			for (long optionId : statistics.keySet()) {
				SurveyOptionStatistics sos = statistics.get(optionId);
				if (sos.getQuestId() != targetQuestion.getId()) {
					continue;
				}
				totalCount += sos.getCount();
			}
		}
		StringBuilder sb = new StringBuilder();
		if (totalCount > 0) {
			sb.append("<div class=\"\">");
			sb.append("<table id=\"rate_table\" class=\"table\" style=\"width:100%;\">");
			sb.append("<thead>");
			sb.append("<tr class=\"text-info\">");
			sb.append("<th style=\"width:20%;\">");
			sb.append("Index");
			sb.append("</th>");
			sb.append("<th style=\"width:40%;\">");
			sb.append("Title");
			sb.append("</th>");
			sb.append("<th style=\"width:20%;\">");
			sb.append("Count");
			sb.append("</th>");
			sb.append("<th style=\"width:20%;\">");
			sb.append("Rate");
			sb.append("</th>");
			sb.append("</tr>");
			sb.append("</thead>");
			sb.append("<tbody>");
			if (statistics == null || statistics.isEmpty()) {
				sb.append("<tr>");
				sb.append("<td id=\"no-rate-record\" colspan=\"4\">");
				sb.append("No record found!");
				sb.append("</td>");
				sb.append("</tr>");
			} else {
				int index = 1;
				for (long optionId : statistics.keySet()) {
					SurveyOptionStatistics sos = statistics.get(optionId);
					if (sos.getQuestId() != targetQuestion.getId()) {
						continue;
					}
					sb.append("<tr>");
					sb.append("<td class=\"index\">");
					sb.append(index++);
					sb.append("</td>");
					sb.append("<td class=\"title\">");
					sb.append(sos.getTitle());
					sb.append("</td>");
					sb.append("<td class=\"count\">");
					sb.append(sos.getCount());
					sb.append("</td>");
					sb.append("<td class=\"count\">");
					sb.append(sos.getRate() + "%");
					sb.append("</td>");
					sb.append("</tr>");
				}
			}
			sb.append("</tbody>");
			sb.append("</table>");
			sb.append("</div>");
			sb.append("<div style=\"text-align:center;font-size:1.5rem;margin-top:2rem;\">");
			sb.append("<label class=\"font-weight-bold text-info\">Detail</label>");
			sb.append("</div>");
		}
		//
		if (mapResultObject != null && mapResultObject.size() > 0) {
			sb.append(getExcelExportForm(targetQuestion));
		}
		sb.append("<div class=\"\">");
		sb.append("<table id=\"result_table\" class=\"table\" style=\"width:100%;\">");
		sb.append("<thead>");
			sb.append("<tr class=\"text-info\">");
				sb.append("<th style=\"width:20%;\">");
					sb.append("ID");
				sb.append("</th>");
				sb.append("<th style=\"width:30%;\">");
					sb.append("Username");
				sb.append("</th>");
				sb.append("<th style=\"width:50%;\">");
					sb.append("Answer");
				sb.append("</th>");
			sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		if (mapResultObject == null || mapResultObject.isEmpty() || !mapResultObject.containsKey(questionIndex)) {
			sb.append("<tr>");
				sb.append("<td id=\"no-result-record\" colspan=\"3\">");
				sb.append("No record found!");
				sb.append("</td>");
			sb.append("</tr>");
		} else {
			for (SurveyResultObject surveyResultObject : mapResultObject.get(questionIndex)) {
				sb.append("<tr>");
					sb.append("<td class=\"user-id\">");
						sb.append(surveyResultObject.getUser_id());
					sb.append("</td>");
					sb.append("<td class=\"username\">");
						sb.append(surveyResultObject.getUsername());
					sb.append("</td>");
					sb.append("<td class=\"username\">");
						sb.append(CommonMethod.getFormatContentHtmlForDisplaying(surveyResultObject.getResult()));
					sb.append("</td>");
				sb.append("</tr>");
			}
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		
		return sb.toString();
	}
	
	private String getOverralResult(List<SurveyQuestion> questionList) {
		if (survey == null || questionList == null || questionList.isEmpty()) {
			return "";
		}
		List<Map<String, Object>> listOverralResult = SurveyService.calcSurveyOverralResult(questionList, survey);
		StringBuilder sb = new StringBuilder();
		if (listOverralResult != null && listOverralResult.size() > 0) {
			sb.append(getExcelExportForm(null));
		}
		sb.append("<div class=\"\">");
		sb.append("<table id=\"result_table\" class=\"table\" style=\"width:100%;\">");
		sb.append("<thead>");
			sb.append("<tr class=\"text-info\">");
				sb.append("<th style=\"width:7%;\">");
					sb.append("ID");
				sb.append("</th>");
				sb.append("<th style=\"width:13%;\">");
					sb.append("Username");
				sb.append("</th>");
				for (SurveyQuestion questionInfo : questionList) {
					sb.append(String.format("<th style=\"width:%d%%;\">", (int)(75 / questionList.size())));
						sb.append(String.format("Question %d", questionInfo.getIdx()));
					sb.append("</th>");
				}
				sb.append("<th style=\"width:5%;\">");
					sb.append("Edit");
				sb.append("</th>");
			sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		if (listOverralResult == null || listOverralResult.isEmpty()) {
			sb.append("<tr>");
				sb.append(String.format("<td id=\"no-result-record\" colspan=\"%d\">", (questionList.size() + 2)));
				sb.append("No record found!");
				sb.append("</td>");
			sb.append("</tr>");
		} else {
			for (Map<String, Object> data : listOverralResult) {
				sb.append("<tr>");
					sb.append("<td class=\"user-id\">");
						sb.append(data.get("user_id"));
					sb.append("</td>");
					sb.append("<td class=\"username\">");
						sb.append(data.get("username").toString());
					sb.append("</td>");
					for (SurveyQuestion questionInfo : questionList) {
						String key = String.format("question_%d", questionInfo.getIdx());
						sb.append("<td class=\"question\">");
						if (data.containsKey(key)) {
							sb.append(CommonMethod.getFormatContentHtmlForDisplaying(data.get(key).toString()));
						}
						sb.append("</td>");
					}
					sb.append("<td class=\"edit\">");
						sb.append(String.format("<form method=\"post\" action=\"%s?mode=%d&survey_id=%d\">", getPagePath(this.getClass()), MODE_SHOW_RESULT, survey.getId()));
						sb.append(String.format("<input type=\"hidden\" name=\"id\" value=\"%s\" />", data.get("id").toString()));
						sb.append("<button type=\"button\" class=\"btn btn-danger delete-record\" style=\"width:100%;\" title=\"Delete this record\">Del</button>");
						sb.append("</form>");
					sb.append("</td>");
				sb.append("</tr>");
			}
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}
	
	private String getExcelExportForm(SurveyQuestion targetQuestion) {
		if (survey == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<form action=\"%s\" method=\"post\">", getPagePath(this.getClass())));
		sb.append(String.format("<input type=\"hidden\" name=\"mode\" value=\"%d\" />", MODE_EXPORT_RESULT));
		sb.append(String.format("<input type=\"hidden\" name=\"survey_id\" value=\"%d\" />", survey.getId()));
		sb.append(String.format("<input type=\"hidden\" name=\"question_id\" value=\"%d\" />", (targetQuestion == null ? 0 : targetQuestion.getId())));
		sb.append("<button type=\"submit\" class=\"btn btn-danger\">Export Results</button>");
		sb.append("</form>");
		return sb.toString();
	}
	
	private void showQuestionListDetail() {
		StringBuilder sb = new StringBuilder();
		sb.append(getSurveyListTable());
		if (survey != null) {
			List<SurveyQuestion> questionList = SurveyService.getSurveyQuestionList(survey.getId());
			sb.append("<div style=\"text-align:center;font-size:1.5rem;margin-top:2rem;\">");
			sb.append("<label class=\"font-weight-bold text-info\">Question List</label>");
			sb.append("</div>");
			sb.append("<a href=\"").append(getPagePath(this.getClass()) + "?mode=" + MODE_QUESTION_NEW + (survey != null ? "&survey_id=" + survey.getId() : "") + "\">");
			sb.append("<button class=\"btn btn-info\">New Question</button>");
			sb.append("</a>");
			if (survey != null) {
				sb.append("<a style=\"margin-left:1rem;\" href=\"").append(getPagePath(this.getClass()) + "?mode=" + MODE_SHOW_RESULT + "&survey_id=" + survey.getId() +  "\">");
				sb.append("<button class=\"btn btn-warning text-white\">Show Result</button>");
				sb.append("</a>");
			}
			sb.append("<div id=\"\" class=\"\">");
			sb.append("<table id=\"question_table\" class=\"table\" style=\"width:100%;\">");
			sb.append("<thead>");
				sb.append("<tr class=\"text-info\">");
					sb.append("<th style=\"width:10%;\">");
						sb.append("Index");
					sb.append("</th>");
					sb.append("<th style=\"width:25%;\">");
						sb.append("Title");
					sb.append("</th>");
					sb.append("<th style=\"width:25%;\">");
						sb.append("Content");
					sb.append("</th>");
					sb.append("<th title='Max option, max length, next question if Yes' style=\"width:10%;\">");
						sb.append("Param 1");
					sb.append("</th>");
					sb.append("<th title='Min option, min length, next question if No' style=\"width:10%;\">");
						sb.append("Param 2");
					sb.append("</th>");
					sb.append("<th style=\"width:10%;\">");
						sb.append("Optionality");
					sb.append("</th>");
					sb.append("<th style=\"width:10%;\">");
						sb.append("Edit");
					sb.append("</th>");
				sb.append("</tr>");
			sb.append("</thead>");
			sb.append("<tbody>");
			if (questionList == null || questionList.isEmpty()) {
				sb.append("<tr>");
					sb.append("<td id=\"no-question-record\" colspan=\"7\">");
					sb.append("No record found!");
					sb.append("</td>");
				sb.append("</tr>");
			} else {
				for (SurveyQuestion question : questionList) {
					sb.append("<tr>");
						sb.append("<td class=\"index\">");
						if (question.getType() != SurveyQuestionType.OPTION_SELECT.ordinal() && question.getType() != SurveyQuestionType.OPTION_LIST.ordinal()) {
							sb.append(question.getIdx());
						} else {
							sb.append(String.format("<a href=\"javascript:void(0);\" class=\"list-question-option\" data-id=\"%d\">%d</a>", question.getId(), question.getIdx()));
						}
						sb.append("</td>");
						sb.append("<td class=\"title\">");
							sb.append(question.getTitle());
						sb.append("</td>");
						sb.append("<td class=\"content\">");
							sb.append(CommonMethod.getFormatContentHtmlForDisplaying(question.getContent()));
						sb.append("</td>");
						sb.append("<td class=\"max-choice\">");
							sb.append(question.getMaxChoice());
						sb.append("</td>");
						sb.append("<td class=\"min-choice\">");
							sb.append(question.getMinChoice());
						sb.append("</td>");
						sb.append("<td class=\"optional\">");
							sb.append((question.isRequired() ? "Mandatory" : "Optional"));
						sb.append("</td>");
						sb.append("<td class=\"edit\">");
							sb.append(String.format("<a href=\"%s\">Edit</a>", getPagePath(this.getClass()) + "?mode=" + MODE_QUESTION_EDIT + "&survey_id=" + question.getSurveyId() + "&question_id=" + question.getId()));
						sb.append("</td>");
					sb.append("</tr>");
				}
			}
			sb.append("</tbody>");
			sb.append("</table>");
			sb.append("</div>");
			if (questionList != null && !questionList.isEmpty()) {
				for (SurveyQuestion question : questionList) {
					String strOptionTable = getQuestionListTable(question);
					if (strOptionTable != null) {
						sb.append(strOptionTable);
					}
				}
			}
		}
		addModel("detail", sb.toString());
	}
	
	private String getQuestionListTable(SurveyQuestion questionInfo) {
		if (questionInfo == null || (questionInfo.getType() != SurveyQuestionType.OPTION_SELECT.ordinal() && questionInfo.getType() != SurveyQuestionType.OPTION_LIST.ordinal())) {
			return null;
		}
		List<SurveyOption> optionList = SurveyService.getSurveyOptionList(questionInfo.getId());
		StringBuilder sb = new StringBuilder();
		
		sb.append("<div class=\"question-option-list collapse\" data-id=\"" + questionInfo.getId() + "\">");
		sb.append("<div style=\"text-align:center;font-size:1.5rem;margin-top:2rem;\">");
		sb.append("<label class=\"font-weight-bold text-info\">Option List</label>");
		sb.append("</div>");
		sb.append("<a href=\"").append(getPagePath(this.getClass()) + "?mode=" + MODE_OPTION_NEW + (survey != null ? "&survey_id=" + survey.getId() : "") + "&question_id=" + questionInfo.getId() + "\">");
		sb.append("<button class=\"btn btn-info\">New Option</button>");
		sb.append("</a>");
		sb.append("<div id=\"\" class=\"\">");
		sb.append("<table class=\"table option-table\" style=\"width:100%;\">");
		sb.append("<thead>");
			sb.append("<tr class=\"text-info\">");
				sb.append("<th style=\"width:30%;\">");
					sb.append("Title");
				sb.append("</th>");
				sb.append("<th style=\"width:30%;\">");
					sb.append("Content");
				sb.append("</th>");
				sb.append("<th style=\"width:30%;\">");
					sb.append("Description");
				sb.append("</th>");
				sb.append("<th style=\"width:10%;\">");
					sb.append("Edit");
				sb.append("</th>");
			sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		if (optionList != null && optionList.size() > 0) {
			for (SurveyOption option : optionList) {
				sb.append("<tr>");
				sb.append("<td class=\"title\">");
				sb.append(option.getTitle());
				sb.append("</td>");
				sb.append("<td class=\"content\">");
				if (option.getType() == SurveyOptionType.IMAGE.ordinal()) {
					sb.append(String.format("<img src=\"%s\" class=\"survey-option-image\" alt=\"%s\"/>", CommonMethod.getStaticFile(option.getContent()), CommonMethod.getFormatContentHtmlForTooltip(option.getDescription())));
				} else if (option.getType() == SurveyOptionType.TEXT.ordinal()){
					sb.append(CommonMethod.getFormatContentHtmlForDisplaying(option.getContent()));
				}
				sb.append("</td>");
				sb.append("<td class=\"description\">");
				sb.append(CommonMethod.getFormatContentHtmlForDisplaying(option.getDescription()));
				sb.append("</td>");
				sb.append("<td class=\"edit\">");
				sb.append(String.format("<a href=\"%s\">Edit</a>", getPagePath(this.getClass()) + "?mode=" + MODE_OPTION_EDIT + (survey != null ? "&survey_id=" + survey.getId() : "") + "&question_id=" + questionInfo.getId() + "&option_id=" + option.getId()));
				sb.append("</td>");
				sb.append("</tr>");
			}
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	private String getSurveyListTable() {
		String strShowCompleted = getContext().getRequestParameter("completed");
		boolean isCompleted = false;
		if (StringUtils.isNotBlank(strShowCompleted)) {
			isCompleted = true;
		}
		List<Survey> listSurvey = SurveyService.getSurveyInfoListForDisplay(isCompleted);
		StringBuilder sb = new StringBuilder();
		sb.append("<div style=\"text-align:center;font-size:1.5rem;margin-top:2rem;\">");
		sb.append("<label class=\"font-weight-bold text-info\">Survey List</label>");
		sb.append("</div>");
		sb.append("<div class=\"d-flex\" style=\"align-items:center;\">");
		sb.append("<a href=\"").append(getPagePath(this.getClass()) + "?mode=" + MODE_SURVEY_NEW +"\">");
		sb.append("<button class=\"btn btn-info\">New Survey</button>");
		sb.append("</a>");
		sb.append("<a href=\"").append(getPagePath(Vote.class) + "\" style=\"margin-left:1rem;\">");
		sb.append("<button class=\"btn btn-secondary\">&nbsp;Back&nbsp;</button>");
		sb.append("</a>");
		sb.append(String.format("<div class=\"custom-control custom-checkbox ml-auto\"><input type=\"checkbox\" class=\"custom-control-input\" id=\"show-completed-survey\" %s><label class=\"custom-control-label\" for=\"show-completed-survey\">Show Completed</label></div>", (isCompleted ? "checked" : "")));
		sb.append("</div>");
		sb.append("<div id=\"\" class=\"\">");
		sb.append("<table id=\"survey_table\" class=\"table\" style=\"width:100%;\">");
		sb.append("<thead>");
			sb.append("<tr class=\"text-info\">");
				sb.append("<th style=\"width:20%;\">");
					sb.append("Name");
				sb.append("</th>");
				sb.append("<th style=\"width:10%;\">");
					sb.append("Status");
				sb.append("</th>");
				sb.append("<th style=\"width:45%;\">");
					sb.append("Description");
				sb.append("</th>");
				sb.append("<th style=\"width:10%;\">");
					sb.append("Created Date");
				sb.append("</th>");
				sb.append("<th style=\"width:10%;\">");
					sb.append("Expire Date");
				sb.append("</th>");
				sb.append("<th style=\"width:5%;\">");
					sb.append("Edit");
				sb.append("</th>");
			sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		if (listSurvey != null && !listSurvey.isEmpty()) {
			for (Survey survey : listSurvey) {
				String highlight = ((this.survey != null && this.survey.getId() == survey.getId()) ? "style=\"background-color:#903b76;\"" : "");
				sb.append(String.format("<tr %s>", highlight));
					sb.append("<td>");
						sb.append(String.format("<a href=\"%s\">%s</a>", getPagePath(this.getClass()) + "?survey_id=" + survey.getId(), survey.getName()));
					sb.append("</td>");
					sb.append("<td>");
						sb.append(SurveyStatus.valueOf(survey.getStatus()).name());
					sb.append("</td>");
					sb.append("<td>");
						sb.append(CommonMethod.getFormatContentHtmlForDisplaying(survey.getDescription()));
					sb.append("</td>");
					sb.append("<td>");
						sb.append((survey.getCreated()));
					sb.append("</td>");
					sb.append("<td>");
						sb.append((survey.getExpired()));
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
	
	private List<DaoValue> getSurveyUserAccessList(FileItem file) {
		if (file == null) {
			return null;
		}
		List<DaoValue> listUpdate = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String line;
			listUpdate = new ArrayList<>();
			while((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#")) {
					continue;
				}
				if (!line.matches("^[0-9]+,\\s*[0-9]+$")) {
					continue;
				}
				String[] arr = line.split(",");
				if (arr.length != 2) {
					continue;
				}
				SurveyUserAccess sas = new SurveyUserAccess();
				sas.setUserCode(arr[0].trim());
				sas.setFlag(Integer.parseInt(arr[1].trim()));
				listUpdate.add(sas.getSurveyUserAccessInfo());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return listUpdate;
	}

	private void updateSurveyAccessList(long surveyID, List<DaoValue> listUpdate) {
		if (surveyID <= 0 || listUpdate == null || listUpdate.isEmpty()) {
			return;
		}
		TblSurveyUserAccessInfo deleteInfo = new TblSurveyUserAccessInfo();
		CommonDaoFactory.executeUpdate(String.format("DELETE FROM %s WHERE survey_id = %d", deleteInfo.getTblName(), surveyID));
		for (DaoValue dao : listUpdate) {
			dao.Set("survey_id", surveyID);
		}	
		CommonDaoFactory.Update(listUpdate);
	}
	
}
