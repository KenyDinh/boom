package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblSurveyResultInfo;

public class SurveyResult {
	private TblSurveyResultInfo surveyResultInfo;

	public SurveyResult() {
		surveyResultInfo = new TblSurveyResultInfo();
	}

	public SurveyResult(TblSurveyResultInfo surveyResultInfo) {
		this.surveyResultInfo = surveyResultInfo;
	}

	public TblSurveyResultInfo getSurveyResultInfo() {
		return surveyResultInfo;
	}

	public long getId() {
		return (Long) surveyResultInfo.Get("id");
	}

	public void setId(long id) {
		surveyResultInfo.Set("id", id);
	}

	public long getSurveyId() {
		return (Long) surveyResultInfo.Get("survey_id");
	}

	public void setSurveyId(long surveyId) {
		surveyResultInfo.Set("survey_id", surveyId);
	}

	public String getUserId() {
		return (String) surveyResultInfo.Get("user_id");
	}

	public void setUserId(String userId) {
		surveyResultInfo.Set("user_id", userId);
	}

	public String getUsername() {
		return (String) surveyResultInfo.Get("username");
	}

	public void setUsername(String username) {
		surveyResultInfo.Set("username", username);
	}

	public String getDepartment() {
		return (String) surveyResultInfo.Get("department");
	}

	public void setDepartment(String department) {
		surveyResultInfo.Set("department", department);
	}

	public String getResult() {
		return (String) surveyResultInfo.Get("result");
	}

	public void setResult(String result) {
		surveyResultInfo.Set("result", result);
	}

	public byte getProgress() {
		return (Byte) surveyResultInfo.Get("progress");
	}

	public void setProgress(byte progress) {
		surveyResultInfo.Set("progress", progress);
	}

	public String getUpdated() {
		return (String) surveyResultInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		surveyResultInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}

}

