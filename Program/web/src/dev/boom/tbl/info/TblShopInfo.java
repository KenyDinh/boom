package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblShopInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "shop_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public String name;
		public String url;
		public String address;
		public String pre_image_url;
		public String image_url;
		public long opening_count;
		public long ordered_dish_count;
		public long voting_count;
		public long star_count;

		public Fields() {
			id = 0;
			name = "";
			url = "";
			address = "";
			pre_image_url = "";
			image_url = "";
			opening_count = 0;
			ordered_dish_count = 0;
			voting_count = 0;
			star_count = 0;
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblShopInfo() {
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
		fieldRead.url = fieldWrite.url;
		fieldRead.address = fieldWrite.address;
		fieldRead.pre_image_url = fieldWrite.pre_image_url;
		fieldRead.image_url = fieldWrite.image_url;
		fieldRead.opening_count = fieldWrite.opening_count;
		fieldRead.ordered_dish_count = fieldWrite.ordered_dish_count;
		fieldRead.voting_count = fieldWrite.voting_count;
		fieldRead.star_count = fieldWrite.star_count;
	}
}

