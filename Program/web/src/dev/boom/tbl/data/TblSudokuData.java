package dev.boom.tbl.data;

import dev.boom.dao.core.DaoValueData;

public class TblSudokuData extends DaoValueData {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "sudoku_data";
	private static final String PRIMARY_KEY = "id";

	public int id;
	public byte level;
	public String data;

	public TblSudokuData() {
		this.id = 0;
		this.level = 0;
		this.data = "";
		Sync();
	}

	public int getId() {
		return id;
	}

	public byte getLevel() {
		return level;
	}
	
	public String getData() {
		return data;
	}

	public String getLabel() {
		return data;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

}
