package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblBoomPlayerStageInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "boom_player_stage_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public long stage_id;
		public long player_id;
		public String player_name;
		public long group_id;

		public Fields() {
			id = 0;
			stage_id = 0;
			player_id = 0;
			player_name = "";
			group_id = 0;
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblBoomPlayerStageInfo() {
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
		fieldRead.stage_id = fieldWrite.stage_id;
		fieldRead.player_id = fieldWrite.player_id;
		fieldRead.player_name = fieldWrite.player_name;
		fieldRead.group_id = fieldWrite.group_id;
	}
}

