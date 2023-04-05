package dev.boom.tbl.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueData;
import dev.boom.dao.IDaoValue;

public class TblKillerSudokuData extends DaoValueData implements IDaoValue {

	private static final String TABLE_NAME = "killer_sudoku_data";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {
	
		public int id;
		public byte level;
		public String data;
		public String cage_data;
	
		public Fields() {
			this.id = 0;
			this.level = 0;
			this.data = "";
			this.cage_data = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblKillerSudokuData() {
		fieldRead = new Fields();
		fieldWrite = new Fields();

		if (fields == null) {
			fields = fieldRead.getClass().getFields();
		}
	}

	public String getTblName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getSubKey() {
		return SUB_KEY;
	}

	public String getForeignKey(String strKey) {
		return mapForeignKey.get(strKey);
	}

	public Field[] getClassField() {
		return fields;
	}

	public Object getFieldRead() {
		return (Object) fieldRead;
	}

	public Object getFieldWrite() {
		return (Object) fieldWrite;
	}

	public Fields getInstance() {
		return fieldWrite;
	}

	public void Sync() {
		fieldRead.id = fieldWrite.id;
		fieldRead.level = fieldWrite.level;
		fieldRead.data = fieldWrite.data;
		fieldRead.cage_data = fieldWrite.cage_data;
	}

}
