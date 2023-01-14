package dev.boom.services;

import dev.boom.tbl.info.TblSurveyUserAccessInfo;

public class SurveyUserAccess {
	private TblSurveyUserAccessInfo surveyUserAccessInfo;

	public SurveyUserAccess() {
		surveyUserAccessInfo = new TblSurveyUserAccessInfo();
	}

	public SurveyUserAccess(TblSurveyUserAccessInfo surveyUserAccessInfo) {
		this.surveyUserAccessInfo = surveyUserAccessInfo;
	}

	public TblSurveyUserAccessInfo getSurveyUserAccessInfo() {
		return surveyUserAccessInfo;
	}

	public long getId() {
		return (Long) surveyUserAccessInfo.Get("id");
	}

	public void setId(long id) {
		surveyUserAccessInfo.Set("id", id);
	}

	public long getSurveyId() {
		return (Long) surveyUserAccessInfo.Get("survey_id");
	}

	public void setSurveyId(long surveyId) {
		surveyUserAccessInfo.Set("survey_id", surveyId);
	}

	public String getUserCode() {
		return (String) surveyUserAccessInfo.Get("user_code");
	}

	public void setUserCode(String userCode) {
		surveyUserAccessInfo.Set("user_code", userCode);
	}

	public int getFlag() {
		return (Integer) surveyUserAccessInfo.Get("flag");
	}

	public void setFlag(int flag) {
		surveyUserAccessInfo.Set("flag", flag);
	}

}

