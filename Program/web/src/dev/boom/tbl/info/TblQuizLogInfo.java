package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblQuizLogInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "quiz_log_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public int id;
		public long quiz_id;
		public byte question_index;
		public long user_id;
		public String username;
		public String correct_answer;
		public String player_answer;

		public Fields() {
			id = 0;
			quiz_id = 0;
			question_index = 0;
			user_id = 0;
			username = "";
			correct_answer = "";
			player_answer = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblQuizLogInfo() {
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
		fieldRead.quiz_id = fieldWrite.quiz_id;
		fieldRead.question_index = fieldWrite.question_index;
		fieldRead.user_id = fieldWrite.user_id;
		fieldRead.username = fieldWrite.username;
		fieldRead.correct_answer = fieldWrite.correct_answer;
		fieldRead.player_answer = fieldWrite.player_answer;
	}
}

