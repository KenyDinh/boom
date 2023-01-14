package dev.boom.tbl.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueData;
import dev.boom.dao.IDaoValue;

public class TblCardBaseData extends DaoValueData implements IDaoValue {
	
	private static final String TABLE_NAME = "card_base_data";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {
		
		/** 武将ID（unsigned short） */
		public int id;
	
		/** 猫種 */
		public byte cat_type;
	
		/** 性別 */
		public byte sex;
	
		/** 所属大名 */
		public byte assign_daimyo_index;
	
		/** 出身地 */
		public int native_map_index;
	
		/** 職業 */
		public byte job;
	
		/** 成長タイプ（0：早熟、1：普通（早め）、2：普通（遅め）、3：晩成） */
		public byte growth;
	
		/** 父親Index（unsigned short） */
		public int father_index;
	
		/** 母親Index（unsigned short） */
		public int mother_index;
	
		/** 配偶者Index（unsigned short） */
		public int marrige_index;
	
		/** 相性 */
		public byte affinity;
	
		/** 兵科 */
		public byte mil_type;
	
		/** 好きな武将（unsigned short） */
		public int like_index_1;
		public int like_index_2;
		public int like_index_3;
		public int like_index_4;
		public int like_index_5;
	
		/** 嫌いな武将（unsigned short） */
		public int dislike_index_1;
		public int dislike_index_2;
		public int dislike_index_3;
		public int dislike_index_4;
		public int dislike_index_5;
	
		/** 性格（0:猪突、1:大胆、2:普通、3:沈着、4:慎重（数字が大きいほど冷静）） */
		public byte ego;
	
		/** 武将のタイプ（0：不明、1：猛将、2：守将、3：智将、4：医将） */
		public byte type;
	
		/**
		 *
		 */
		public Fields() {
			id = 0;
			cat_type = -1; // 0も有意なため.
			sex = 0;
			assign_daimyo_index = 0;
			native_map_index = 0;
			job = 0;
			growth = 0;
			father_index = 0;
			mother_index = 0;
			marrige_index = 0;
			affinity = 0;
			mil_type = 0;
			like_index_1 = 0;
			like_index_2 = 0;
			like_index_3 = 0;
			like_index_4 = 0;
			like_index_5 = 0;
			dislike_index_1 = 0;
			dislike_index_2 = 0;
			dislike_index_3 = 0;
			dislike_index_4 = 0;
			dislike_index_5 = 0;
			ego = 0;
			type = 0;
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblCardBaseData() {
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
		fieldRead.cat_type = fieldWrite.cat_type;
		fieldRead.sex = fieldWrite.sex;
		fieldRead.assign_daimyo_index = fieldWrite.assign_daimyo_index;
		fieldRead.native_map_index = fieldWrite.native_map_index;
		fieldRead.job = fieldWrite.job;
		fieldRead.growth = fieldWrite.growth;
		fieldRead.father_index = fieldWrite.father_index;
		fieldRead.mother_index = fieldWrite.mother_index;
		fieldRead.marrige_index = fieldWrite.marrige_index;
		fieldRead.affinity = fieldWrite.affinity;
		fieldRead.mil_type = fieldWrite.mil_type;
		fieldRead.like_index_1 = fieldWrite.like_index_1;
		fieldRead.like_index_2 = fieldWrite.like_index_2;
		fieldRead.like_index_3 = fieldWrite.like_index_3;
		fieldRead.like_index_4 = fieldWrite.like_index_4;
		fieldRead.like_index_5 = fieldWrite.like_index_5;
		fieldRead.dislike_index_1 = fieldWrite.dislike_index_1;
		fieldRead.dislike_index_2 = fieldWrite.dislike_index_2;
		fieldRead.dislike_index_3 = fieldWrite.dislike_index_3;
		fieldRead.dislike_index_4 = fieldWrite.dislike_index_4;
		fieldRead.dislike_index_5 = fieldWrite.dislike_index_5;
		fieldRead.ego = fieldWrite.ego;
		fieldRead.type = fieldWrite.type;
	}
	
}
