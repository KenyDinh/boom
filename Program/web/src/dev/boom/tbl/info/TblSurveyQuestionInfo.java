package dev.boom.tbl.info;

import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblSurveyQuestionInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "survey_question_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long survey_id;
	private byte idx;
	private byte type;
	private String title;
	private String content;
	private String description;
	private int min_choice;
	private int max_choice;
	private byte optional;

	public TblSurveyQuestionInfo() {
		this.id = 0;
		this.survey_id = 0;
		this.idx = 0;
		this.type = 0;
		this.title = "";
		this.content = "";
		this.description = "";
		this.min_choice = 0;
		this.max_choice = 0;
		this.optional = 0;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSurvey_id() {
		return survey_id;
	}

	public void setSurvey_id(long survey_id) {
		this.survey_id = survey_id;
	}

	public byte getIdx() {
		return idx;
	}

	public void setIdx(byte idx) {
		this.idx = idx;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMin_choice() {
		return min_choice;
	}

	public void setMin_choice(int min) {
		this.min_choice = min;
	}

	public int getMax_choice() {
		return max_choice;
	}

	public void setMax_choice(int max) {
		this.max_choice = max;
	}

	public byte getOptional() {
		return optional;
	}

	public void setOptional(byte optional) {
		this.optional = optional;
	}

	public boolean isRequired() {
		return (getOptional() == 0);
	}

	public List<String> getSubKey() {
		return null;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

}
