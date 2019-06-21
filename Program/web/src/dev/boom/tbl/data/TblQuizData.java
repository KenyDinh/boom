package dev.boom.tbl.data;

import dev.boom.dao.core.DaoValueData;

public class TblQuizData extends DaoValueData {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "survey_valid_code_data";
	private static final String PRIMARY_KEY = "id";

	private int id;
	private short level;
	private byte type;
	private String label;

	public TblQuizData() {
		this.id = 0;
		this.level = 0;
		this.type = 0;
		this.label = "";
	}

	public int getId() {
		return id;
	}

	public short getLevel() {
		return level;
	}

	public byte getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

}
