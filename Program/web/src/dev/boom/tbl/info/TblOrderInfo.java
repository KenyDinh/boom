package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblOrderInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "order_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public long user_id;
		public String username;
		public long menu_id;
		public long shop_id;
		public String dish_name;
		public String dish_type;
		public long dish_price;
		public long attr_price;
		public long final_price;
		public int dish_code;
		public short voting_star;
		public String comment;
		public long quantity;
		public String size;
		public String ice;
		public String sugar;
		public String option_list;
		public int flag;
		public String created;
		public String updated;

		public Fields() {
			id = 0;
			user_id = 0;
			username = "";
			menu_id = 0;
			shop_id = 0;
			dish_name = "";
			dish_type = "";
			dish_price = 0;
			attr_price = 0;
			final_price = 0;
			dish_code = 0;
			voting_star = 0;
			comment = "";
			quantity = 0;
			size = "";
			ice = "";
			sugar = "";
			option_list = "";
			flag = 0;
			created = "";
			updated = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblOrderInfo() {
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
		fieldRead.username = fieldWrite.username;
		fieldRead.menu_id = fieldWrite.menu_id;
		fieldRead.shop_id = fieldWrite.shop_id;
		fieldRead.dish_name = fieldWrite.dish_name;
		fieldRead.dish_type = fieldWrite.dish_type;
		fieldRead.dish_price = fieldWrite.dish_price;
		fieldRead.attr_price = fieldWrite.attr_price;
		fieldRead.final_price = fieldWrite.final_price;
		fieldRead.dish_code = fieldWrite.dish_code;
		fieldRead.voting_star = fieldWrite.voting_star;
		fieldRead.comment = fieldWrite.comment;
		fieldRead.quantity = fieldWrite.quantity;
		fieldRead.size = fieldWrite.size;
		fieldRead.ice = fieldWrite.ice;
		fieldRead.sugar = fieldWrite.sugar;
		fieldRead.option_list = fieldWrite.option_list;
		fieldRead.flag = fieldWrite.flag;
		fieldRead.created = fieldWrite.created;
		fieldRead.updated = fieldWrite.updated;
	}
}

