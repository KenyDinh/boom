package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblDishRatingInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "dish_rating_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public long shop_id;
		public String name;
		public int code;
		public long order_count;
		public long star_count;
		public String updated;

		public Fields() {
			id = 0;
			shop_id = 0;
			name = "";
			code = 0;
			order_count = 0;
			star_count = 0;
			updated = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblDishRatingInfo() {
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
		fieldRead.shop_id = fieldWrite.shop_id;
		fieldRead.name = fieldWrite.name;
		fieldRead.code = fieldWrite.code;
		fieldRead.order_count = fieldWrite.order_count;
		fieldRead.star_count = fieldWrite.star_count;
		fieldRead.updated = fieldWrite.updated;
	}
}

