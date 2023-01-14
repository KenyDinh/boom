package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblAuthTokenInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "auth_token_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public String token;
		public String validator;
		public long user_id;
		public String expired;

		public Fields() {
			id = 0;
			token = "";
			validator = "";
			user_id = 0;
			expired = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblAuthTokenInfo() {
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
		fieldRead.token = fieldWrite.token;
		fieldRead.validator = fieldWrite.validator;
		fieldRead.user_id = fieldWrite.user_id;
		fieldRead.expired = fieldWrite.expired;
	}
}

