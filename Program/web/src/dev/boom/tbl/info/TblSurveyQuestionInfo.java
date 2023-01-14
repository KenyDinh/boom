package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblSurveyQuestionInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "survey_question_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public long survey_id;
		public byte idx;
		public byte type;
		public String title;
		public String content;
		public String description;
		public int min_choice;
		public int max_choice;
		public byte optional;

		public Fields() {
			id = 0;
			survey_id = 0;
			idx = 0;
			type = 0;
			title = "";
			content = "";
			description = "";
			min_choice = 0;
			max_choice = 0;
			optional = 0;
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblSurveyQuestionInfo() {
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
		fieldRead.survey_id = fieldWrite.survey_id;
		fieldRead.idx = fieldWrite.idx;
		fieldRead.type = fieldWrite.type;
		fieldRead.title = fieldWrite.title;
		fieldRead.content = fieldWrite.content;
		fieldRead.description = fieldWrite.description;
		fieldRead.min_choice = fieldWrite.min_choice;
		fieldRead.max_choice = fieldWrite.max_choice;
		fieldRead.optional = fieldWrite.optional;
	}
}

