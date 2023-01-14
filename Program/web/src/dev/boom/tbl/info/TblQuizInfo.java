package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblQuizInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "quiz_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public long host;
		public String name;
		public byte subject;
		public byte level;
		public byte max_player;
		public byte question_num;
		public long time_per_question;
		public byte status;
		public byte retry;
		public byte flag;
		public byte player_num;
		public byte current_question;
		public String current_option_order;
		public String question_data;
		public String created;
		public String expired;
		public String updated;

		public Fields() {
			id = 0;
			host = 0;
			name = "";
			subject = 0;
			level = 0;
			max_player = 0;
			question_num = 0;
			time_per_question = 0;
			status = 0;
			retry = 0;
			flag = 0;
			player_num = 0;
			current_question = 0;
			current_option_order = "";
			question_data = "";
			created = "";
			expired = "";
			updated = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblQuizInfo() {
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
		fieldRead.host = fieldWrite.host;
		fieldRead.name = fieldWrite.name;
		fieldRead.subject = fieldWrite.subject;
		fieldRead.level = fieldWrite.level;
		fieldRead.max_player = fieldWrite.max_player;
		fieldRead.question_num = fieldWrite.question_num;
		fieldRead.time_per_question = fieldWrite.time_per_question;
		fieldRead.status = fieldWrite.status;
		fieldRead.retry = fieldWrite.retry;
		fieldRead.flag = fieldWrite.flag;
		fieldRead.player_num = fieldWrite.player_num;
		fieldRead.current_question = fieldWrite.current_question;
		fieldRead.current_option_order = fieldWrite.current_option_order;
		fieldRead.question_data = fieldWrite.question_data;
		fieldRead.created = fieldWrite.created;
		fieldRead.expired = fieldWrite.expired;
		fieldRead.updated = fieldWrite.updated;
	}
}

