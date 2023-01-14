package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblSurveyInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "survey_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public String name;
		public String pathname;
		public byte status;
		public byte type;
		public int flag;
		public String description;
		public String created;
		public String expired;
		public String updated;

		public Fields() {
			id = 0;
			name = "";
			pathname = "";
			status = 0;
			type = 0;
			flag = 0;
			description = "";
			created = "";
			expired = "";
			updated = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblSurveyInfo() {
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
		fieldRead.name = fieldWrite.name;
		fieldRead.pathname = fieldWrite.pathname;
		fieldRead.status = fieldWrite.status;
		fieldRead.type = fieldWrite.type;
		fieldRead.flag = fieldWrite.flag;
		fieldRead.description = fieldWrite.description;
		fieldRead.created = fieldWrite.created;
		fieldRead.expired = fieldWrite.expired;
		fieldRead.updated = fieldWrite.updated;
	}
}

