package dev.boom.tbl.data;

import dev.boom.dao.core.DaoValueData;

public class TblQuizOptionData extends DaoValueData {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "survey_valid_code_data";
	private static final String PRIMARY_KEY = "id";

	private int id;
	private int question_id;
	private String option_1;
	private String option_2;
	private String option_3;
	private String option_4;
	private String option_5;
	private String description;

	public TblQuizOptionData() {
		this.id = 0;
		this.question_id = 0;
		this.option_1 = "";
		this.option_2 = "";
		this.option_3 = "";
		this.option_4 = "";
		this.option_5 = "";
		this.description = "";
	}

	public int getId() {
		return id;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public String getOption_1() {
		return option_1;
	}

	public String getOption_2() {
		return option_2;
	}

	public String getOption_3() {
		return option_3;
	}

	public String getOption_4() {
		return option_4;
	}

	public String getOption_5() {
		return option_5;
	}

	public String getDescription() {
		return description;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

}
