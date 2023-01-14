package dev.boom.services;

import dev.boom.tbl.info.TblSurveyOptionInfo;

public class SurveyOption {
	private TblSurveyOptionInfo surveyOptionInfo;

	public SurveyOption() {
		surveyOptionInfo = new TblSurveyOptionInfo();
	}

	public SurveyOption(TblSurveyOptionInfo surveyOptionInfo) {
		this.surveyOptionInfo = surveyOptionInfo;
	}

	public TblSurveyOptionInfo getSurveyOptionInfo() {
		return surveyOptionInfo;
	}

	public long getId() {
		return (Long) surveyOptionInfo.Get("id");
	}

	public void setId(long id) {
		surveyOptionInfo.Set("id", id);
	}

	public long getQuestionId() {
		return (Long) surveyOptionInfo.Get("question_id");
	}

	public void setQuestionId(long questionId) {
		surveyOptionInfo.Set("question_id", questionId);
	}

	public byte getType() {
		return (Byte) surveyOptionInfo.Get("type");
	}

	public void setType(byte type) {
		surveyOptionInfo.Set("type", type);
	}

	public String getTitle() {
		return (String) surveyOptionInfo.Get("title");
	}

	public void setTitle(String title) {
		surveyOptionInfo.Set("title", title);
	}

	public String getContent() {
		return (String) surveyOptionInfo.Get("content");
	}

	public void setContent(String content) {
		surveyOptionInfo.Set("content", content);
	}

	public String getDescription() {
		return (String) surveyOptionInfo.Get("description");
	}

	public void setDescription(String description) {
		surveyOptionInfo.Set("description", description);
	}

	public int getParam() {
		return (Integer) surveyOptionInfo.Get("param");
	}

	public void setParam(int param) {
		surveyOptionInfo.Set("param", param);
	}

}

