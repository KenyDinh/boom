package dev.boom.tbl.data;

import java.util.List;

import dev.boom.dao.core.DaoValueData;

public class TblSurveyValidCodeData extends DaoValueData {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "survey_valid_code_data";
	private static final String PRIMARY_KEY = "id";

	private int id;
	private String code;
	private String name;
	
	public TblSurveyValidCodeData() {
		this.id = 0;
		this.code = "";
		this.name = "";
		Sync();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
