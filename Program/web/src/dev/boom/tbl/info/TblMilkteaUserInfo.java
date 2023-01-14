package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblMilkteaUserInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "milktea_user_info";
	private static final String PRIMARY_KEY = "user_id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long user_id;
		public String username;
		public long dish_count;
		public long order_count;
		public long total_money;
		public long total_sugar;
		public long total_ice;
		public long total_topping;
		public byte free_ticket;
		public long latest_order_id;

		public Fields() {
			user_id = 0;
			username = "";
			dish_count = 0;
			order_count = 0;
			total_money = 0;
			total_sugar = 0;
			total_ice = 0;
			total_topping = 0;
			free_ticket = 0;
			latest_order_id = 0;
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblMilkteaUserInfo() {
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
		fieldRead.dish_count = fieldWrite.dish_count;
		fieldRead.order_count = fieldWrite.order_count;
		fieldRead.total_money = fieldWrite.total_money;
		fieldRead.total_sugar = fieldWrite.total_sugar;
		fieldRead.total_ice = fieldWrite.total_ice;
		fieldRead.total_topping = fieldWrite.total_topping;
		fieldRead.free_ticket = fieldWrite.free_ticket;
		fieldRead.latest_order_id = fieldWrite.latest_order_id;
	}
}

