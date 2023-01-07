package dev.boom.tbl.data;

import dev.boom.dao.core.DaoValueData;

public class TblQuizOptionProgrammingData extends DaoValueData {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "quiz_option_programming_data";
	private static final String PRIMARY_KEY = "id";

	public int id;
	public int quiz_data_id;
	public String option;
	public int correct;

	public TblQuizOptionProgrammingData() {
		this.id = 0;
		this.quiz_data_id = 0;
		this.option = "";
		this.correct = 0;
		Sync();
	}

	public int getId() {
		return id;
	}

	public int getQuiz_data_id() {
		return quiz_data_id;
	}

	public String getOption() {
		return option;
	}

	public int getCorrect() {
		return correct;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

}
