package dev.boom.tbl.data;

import dev.boom.dao.core.DaoValueData;

public class TblQuizProgrammingData extends DaoValueData {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "quiz_programming_data";
	private static final String PRIMARY_KEY = "id";

	public int id;
	public short level;
	public byte type;
	public String label;

	public TblQuizProgrammingData() {
		this.id = 0;
		this.level = 0;
		this.type = 0;
		this.label = "";
		Sync();
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
