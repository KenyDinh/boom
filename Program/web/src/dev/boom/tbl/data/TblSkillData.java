package dev.boom.tbl.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueData;
import dev.boom.dao.IDaoValue;

public class TblSkillData extends DaoValueData implements IDaoValue {
	/**
	 * 
	 */
	private static final String TABLE_NAME = "skill_data";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {
	
		public int id;
	
		/** 未登場状態ならTRUE */
		public byte hidden_flag;
	
		/** 技能ベースID（unsigned short） */
		public int skill_base_id;
	
		/** レベル */
		public byte level;
	
		/** 技能をセットしたときに加算されるウエイト値。-40～40 */
		public byte weight;
	
		/** 対象の種類（自分、隣の見方など） */
		public byte target_type_index;
	
		/** 発動確率 */
		public byte active_rate;
	
		/** レジスト＝判定式＋補正値。-100～100 */
		public short regist_fix_value;
	
		/** 効果時間（何ターン、永久など） */
		public byte effect_term;
	
		/** 効果ID（unsigned short） */
		public int effect_id_1;
		public int effect_type_1;
		public short effect_data_main_1;
		public short effect_data_sub_1;
	
		public int effect_id_2;
		public int effect_type_2;
		public short effect_data_main_2;
		public short effect_data_sub_2;
	
		public int effect_id_3;
		public int effect_type_3;
		public short effect_data_main_3;
		public short effect_data_sub_3;
	
		/** 取得：五輪玉（unsigned short） */
		public int prop_reward;
	
		/** 脅威度. 0～50.デッキ */
		public int menace;
	
		/** ソート順序 */
		public int sort_order;
	
		public Fields() {
			id = 0;
			hidden_flag = 0;
			skill_base_id = 0;
			level = 0;
			weight = 0;
			target_type_index = 0;
			active_rate = 0;
			regist_fix_value = 0;
			effect_term = 0;
			effect_id_1 = 0;
			effect_id_2 = 0;
			effect_id_3 = 0;
			effect_type_1 = 0;
			effect_type_2 = 0;
			effect_type_3 = 0;
			effect_data_main_1 = 0;
			effect_data_main_2 = 0;
			effect_data_main_3 = 0;
			effect_data_sub_1 = 0;
			effect_data_sub_2 = 0;
			effect_data_sub_3 = 0;
			prop_reward = 0;
			menace = 0;
			sort_order = 0;
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblSkillData() {
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
		fieldRead.hidden_flag = fieldWrite.hidden_flag;
		fieldRead.skill_base_id = fieldWrite.skill_base_id;
		fieldRead.level = fieldWrite.level;
		fieldRead.weight = fieldWrite.weight;
		fieldRead.target_type_index = fieldWrite.target_type_index;
		fieldRead.active_rate = fieldWrite.active_rate;
		fieldRead.regist_fix_value = fieldWrite.regist_fix_value;
		fieldRead.effect_term = fieldWrite.effect_term;
		fieldRead.effect_id_1 = fieldWrite.effect_id_1;
		fieldRead.effect_id_2 = fieldWrite.effect_id_2;
		fieldRead.effect_id_3 = fieldWrite.effect_id_3;
		fieldRead.effect_type_1 = fieldWrite.effect_type_1;
		fieldRead.effect_type_2 = fieldWrite.effect_type_2;
		fieldRead.effect_type_3 = fieldWrite.effect_type_3;
		fieldRead.effect_data_main_1 = fieldWrite.effect_data_main_1;
		fieldRead.effect_data_main_2 = fieldWrite.effect_data_main_2;
		fieldRead.effect_data_main_3 = fieldWrite.effect_data_main_3;
		fieldRead.effect_data_sub_1 = fieldWrite.effect_data_sub_1;
		fieldRead.effect_data_sub_2 = fieldWrite.effect_data_sub_2;
		fieldRead.effect_data_sub_3 = fieldWrite.effect_data_sub_3;
		fieldRead.prop_reward = fieldWrite.prop_reward;
		fieldRead.menace = fieldWrite.menace;
		fieldRead.sort_order = fieldWrite.sort_order;
	}

}
