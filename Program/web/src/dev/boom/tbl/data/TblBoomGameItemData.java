package dev.boom.tbl.data;

import dev.boom.dao.IDaoValue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueData;

public class TblBoomGameItemData extends DaoValueData implements IDaoValue {

	private static final String TABLE_NAME = "boom_game_item_data";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {
	
		public int id;
		public int type;
		public int target_type;
		public int effect_id_1;
		public int effect_param_1;
		public int effect_duration_1;
		public int effect_id_2;
		public int effect_param_2;
		public int effect_duration_2;
		public int effect_id_3;
		public int effect_param_3;
		public int effect_duration_3;
		public int imageID;
		public int prob_rate;
		public String label_explain;
	
		public Fields() {
			this.id = 0;
			this.type = 0;
			this.target_type = 0;
			this.effect_id_1 = 0;
			this.effect_param_1 = 0;
			this.effect_duration_1 = 0;
			this.effect_id_2 = 0;
			this.effect_param_2 = 0;
			this.effect_duration_2 = 0;
			this.effect_id_3 = 0;
			this.effect_param_3 = 0;
			this.effect_duration_3 = 0;
			this.imageID = 0;
			this.prob_rate = 0;
			this.label_explain = "";
		}
	}
	
	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblBoomGameItemData() {
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
		fieldRead.type = fieldWrite.type;
		fieldRead.target_type = fieldWrite.target_type;
		fieldRead.effect_id_1 = fieldWrite.effect_id_1;
		fieldRead.effect_param_1 = fieldWrite.effect_param_1;
		fieldRead.effect_duration_1 = fieldWrite.effect_duration_1;
		fieldRead.effect_id_2 = fieldWrite.effect_id_2;
		fieldRead.effect_param_2 = fieldWrite.effect_param_2;
		fieldRead.effect_duration_2 = fieldWrite.effect_duration_2;
		fieldRead.effect_id_3 = fieldWrite.effect_id_3;
		fieldRead.effect_param_3 = fieldWrite.effect_param_3;
		fieldRead.effect_duration_3 = fieldWrite.effect_duration_3;
		fieldRead.imageID = fieldWrite.imageID;
		fieldRead.prob_rate = fieldWrite.prob_rate;
		fieldRead.label_explain = fieldWrite.label_explain;
	}

}
