package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblDeviceInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "device_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public int id;
		public String name;
		public String serial;
		public String image;
		public byte dept;
		public byte type;
		public byte status;
		public String buy_date;
		public String note;
		public String hold_date;
		public String release_date;
		public String extend_date;
		public long user_id;
		public String username;
		public byte available;
		public int flag;
		public int regist_count;
		public String updated;

		public Fields() {
			id = 0;
			name = "";
			serial = "";
			image = "";
			dept = 0;
			type = 0;
			status = 0;
			buy_date = "";
			note = "";
			hold_date = "";
			release_date = "";
			extend_date = "";
			user_id = 0;
			username = "";
			available = 0;
			flag = 0;
			regist_count = 0;
			updated = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblDeviceInfo() {
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
		fieldRead.serial = fieldWrite.serial;
		fieldRead.image = fieldWrite.image;
		fieldRead.dept = fieldWrite.dept;
		fieldRead.type = fieldWrite.type;
		fieldRead.status = fieldWrite.status;
		fieldRead.buy_date = fieldWrite.buy_date;
		fieldRead.note = fieldWrite.note;
		fieldRead.hold_date = fieldWrite.hold_date;
		fieldRead.release_date = fieldWrite.release_date;
		fieldRead.extend_date = fieldWrite.extend_date;
		fieldRead.user_id = fieldWrite.user_id;
		fieldRead.username = fieldWrite.username;
		fieldRead.available = fieldWrite.available;
		fieldRead.flag = fieldWrite.flag;
		fieldRead.regist_count = fieldWrite.regist_count;
		fieldRead.updated = fieldWrite.updated;
	}
}

