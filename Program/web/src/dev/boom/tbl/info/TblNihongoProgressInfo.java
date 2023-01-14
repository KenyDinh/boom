package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblNihongoProgressInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "nihongo_progress_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public long user_id;
		public int test_id;
		public int progress;
		public String created;
		public String updated;

		public Fields() {
			id = 0;
			user_id = 0;
			test_id = 0;
			progress = 0;
			created = "";
			updated = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblNihongoProgressInfo() {
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
		fieldRead.user_id = fieldWrite.user_id;
		fieldRead.test_id = fieldWrite.test_id;
		fieldRead.progress = fieldWrite.progress;
		fieldRead.created = fieldWrite.created;
		fieldRead.updated = fieldWrite.updated;
	}
}

