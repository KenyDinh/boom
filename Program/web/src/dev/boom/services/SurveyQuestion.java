package dev.boom.services;

import dev.boom.tbl.info.TblSurveyQuestionInfo;

public class SurveyQuestion {
	private TblSurveyQuestionInfo surveyQuestionInfo;

	public SurveyQuestion() {
		surveyQuestionInfo = new TblSurveyQuestionInfo();
	}

	public SurveyQuestion(TblSurveyQuestionInfo surveyQuestionInfo) {
		this.surveyQuestionInfo = surveyQuestionInfo;
	}

	public TblSurveyQuestionInfo getSurveyQuestionInfo() {
		return surveyQuestionInfo;
	}

	public long getId() {
		return (Long) surveyQuestionInfo.Get("id");
	}

	public void setId(long id) {
		surveyQuestionInfo.Set("id", id);
	}

	public long getSurveyId() {
		return (Long) surveyQuestionInfo.Get("survey_id");
	}

	public void setSurveyId(long surveyId) {
		surveyQuestionInfo.Set("survey_id", surveyId);
	}

	public byte getIdx() {
		return (Byte) surveyQuestionInfo.Get("idx");
	}

	public void setIdx(byte idx) {
		surveyQuestionInfo.Set("idx", idx);
	}

	public byte getType() {
		return (Byte) surveyQuestionInfo.Get("type");
	}

	public void setType(byte type) {
		surveyQuestionInfo.Set("type", type);
	}

	public String getTitle() {
		return (String) surveyQuestionInfo.Get("title");
	}

	public void setTitle(String title) {
		surveyQuestionInfo.Set("title", title);
	}

	public String getContent() {
		return (String) surveyQuestionInfo.Get("content");
	}

	public void setContent(String content) {
		surveyQuestionInfo.Set("content", content);
	}

	public String getDescription() {
		return (String) surveyQuestionInfo.Get("description");
	}

	public void setDescription(String description) {
		surveyQuestionInfo.Set("description", description);
	}

	public int getMinChoice() {
		return (Integer) surveyQuestionInfo.Get("min_choice");
	}

	public void setMinChoice(int minChoice) {
		surveyQuestionInfo.Set("min_choice", minChoice);
	}

	public int getMaxChoice() {
		return (Integer) surveyQuestionInfo.Get("max_choice");
	}

	public void setMaxChoice(int maxChoice) {
		surveyQuestionInfo.Set("max_choice", maxChoice);
	}

	public byte getOptional() {
		return (Byte) surveyQuestionInfo.Get("optional");
	}

	public void setOptional(byte optional) {
		surveyQuestionInfo.Set("optional", optional);
	}
	
	public boolean isRequired() {
		return (getOptional() == 0);
	}

}

