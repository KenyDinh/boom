package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblBoomStageInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "boom_stage_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public long season_id;
		public String name;
		public int max_player;

		public Fields() {
			id = 0;
			season_id = 0;
			name = "";
			max_player = 0;
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblBoomStageInfo() {
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
		fieldRead.season_id = fieldWrite.season_id;
		fieldRead.name = fieldWrite.name;
		fieldRead.max_player = fieldWrite.max_player;
	}
}

