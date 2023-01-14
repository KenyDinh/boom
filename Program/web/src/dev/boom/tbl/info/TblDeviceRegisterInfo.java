package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblDeviceRegisterInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "device_register_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public int device_id;
		public String device_name;
		public long user_id;
		public String username;
		public byte expired;
		public String start_date;
		public String end_date;
		public String updated;

		public Fields() {
			id = 0;
			device_id = 0;
			device_name = "";
			user_id = 0;
			username = "";
			expired = 0;
			start_date = "";
			end_date = "";
			updated = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblDeviceRegisterInfo() {
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
		fieldRead.device_id = fieldWrite.device_id;
		fieldRead.device_name = fieldWrite.device_name;
		fieldRead.user_id = fieldWrite.user_id;
		fieldRead.username = fieldWrite.username;
		fieldRead.expired = fieldWrite.expired;
		fieldRead.start_date = fieldWrite.start_date;
		fieldRead.end_date = fieldWrite.end_date;
		fieldRead.updated = fieldWrite.updated;
	}
}

