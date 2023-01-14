package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblMenuInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "menu_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public String name;
		public long shop_id;
		public short sale;
		public String code;
		public long max_discount;
		public long shipping_fee;
		public String description;
		public byte status;
		public int dept;
		public int flag;
		public String created;
		public String expired;
		public String updated;

		public Fields() {
			id = 0;
			name = "";
			shop_id = 0;
			sale = 0;
			code = "";
			max_discount = 0;
			shipping_fee = 0;
			description = "";
			status = 0;
			dept = 0;
			flag = 0;
			created = "";
			expired = "";
			updated = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblMenuInfo() {
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
		fieldRead.shop_id = fieldWrite.shop_id;
		fieldRead.sale = fieldWrite.sale;
		fieldRead.code = fieldWrite.code;
		fieldRead.max_discount = fieldWrite.max_discount;
		fieldRead.shipping_fee = fieldWrite.shipping_fee;
		fieldRead.description = fieldWrite.description;
		fieldRead.status = fieldWrite.status;
		fieldRead.dept = fieldWrite.dept;
		fieldRead.flag = fieldWrite.flag;
		fieldRead.created = fieldWrite.created;
		fieldRead.expired = fieldWrite.expired;
		fieldRead.updated = fieldWrite.updated;
	}
}

