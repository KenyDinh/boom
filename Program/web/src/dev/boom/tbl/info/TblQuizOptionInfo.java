package dev.boom.tbl.info;

import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblQuizOptionInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "quiz_option_info";
	private static final String PRIMARY_KEY = "id";

	public int id;
	public int question_id;
	public String option;
	public byte correct;

	public TblQuizOptionInfo() {
		this.id = 0;
		this.question_id = 0;
		this.option = "";
		this.correct = 0;
		Sync();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public byte getCorrect() {
		return correct;
	}

	public void setCorrect(byte correct) {
		this.correct = correct;
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
