package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblQuizPlayerInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "quiz_player_info";
	private static final String PRIMARY_KEY = "user_id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long user_id;
		public String username;
		public long quiz_id;
		public byte status;
		public byte retry;
		public String answer;
		public byte correct_count;
		public int correct_point;
		public String updated;

		public Fields() {
			user_id = 0;
			username = "";
			quiz_id = 0;
			status = 0;
			retry = 0;
			answer = "";
			correct_count = 0;
			correct_point = 0;
			updated = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblQuizPlayerInfo() {
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
		fieldRead.user_id = fieldWrite.user_id;
		fieldRead.username = fieldWrite.username;
		fieldRead.quiz_id = fieldWrite.quiz_id;
		fieldRead.status = fieldWrite.status;
		fieldRead.retry = fieldWrite.retry;
		fieldRead.answer = fieldWrite.answer;
		fieldRead.correct_count = fieldWrite.correct_count;
		fieldRead.correct_point = fieldWrite.correct_point;
		fieldRead.updated = fieldWrite.updated;
	}
}

