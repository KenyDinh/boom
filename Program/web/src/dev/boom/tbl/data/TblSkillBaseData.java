package dev.boom.tbl.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueData;
import dev.boom.dao.IDaoValue;

public class TblSkillBaseData extends DaoValueData implements IDaoValue {
	/**
	 * 
	 */
	private static final String TABLE_NAME = "skill_base_data";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {
	
		/** 技能ベースID（unsigned short） */
		public int id;
	
		/** 技能名（6 * 3） */
		public String name;
	
		/** Mobile技能名 */
		public String mobile_name;
	
		/** ふりがな（18 * 3） */
		public String furigana;
	
		/** 技能説明（48 * 3） */
		public String comments;
	
		/** 五輪属性（0:なし、1:火、2:地、3:風、4:水、5:空） */
		public byte property;
	
		/** 系統（0:不明、1:攻撃、2:守備、3:支援、4:回復） */
		public byte system;
	
		/** 技能タイプ（0:不明、1:近接物理系、2:間接物理系、3:術系、4:その他） */
		public byte type;
	
		/** 分類（0:特技、1:奥義、2:超奥義） */
		public byte kind;
	
		/** アピールID（unsigned short） */
		public int appeal_id;
	
		/** スキルフラグ(1:固有技能、2:購入可、4:開発入手、8:クエスト入手、16:大名入手) */
		public byte skill_flag;
	
		/** 発動方法（0:常時スキル、1:自動発動、2:通常、 3:常時） */
		public byte active_type;
	
		/** 追加条件の種類を示すID（unsigned tinyint）とその数値 */
		public int active_condition_1;
		public short active_value_1;
	
		public int active_condition_2;
		public short active_value_2;
	
		public int active_condition_3;
		public short active_value_3;
	
		public int active_condition_4;
		public short active_value_4;
	
		public int active_condition_5;
		public short active_value_5;
	
		/** 対象決定パターン（unsigned short）（アルゴで使用） */
		public int target_decide;
	
		/** 戦闘演出の表現方法のID（unsigned short） */
		public int direction_id;
	
		/** 合成判定兵科（ビット管理、1:足軽、2:騎馬、4:鉄砲） */
		public byte reinforce_military;
	
		/** 合成判定属性 */
		public byte reinforce_property;
	
		/** 合成判定職業 */
		public byte reinforce_job;
	
		/** 発動条件のメッセージindex */
		public byte active_condition_explain_1;
		public byte active_condition_explain_2;
		public byte active_condition_explain_3;
	
		/** 消費数cost */
		public short cast_cost;
	
		/** 解錠ID */
		public int joint_skill_unlock_id;
	
		public Fields() {
			id = 0;
			name = "";
			mobile_name = "";
			furigana = "";
			comments = "";
			property = 0;
			system = 0;
			type = 0;
			kind = 0;
			skill_flag = 0;
			appeal_id = 0;
			active_type = 0;
			active_condition_1 = 0;
			active_condition_2 = 0;
			active_condition_3 = 0;
			active_condition_4 = 0;
			active_condition_5 = 0;
			active_value_1 = 0;
			active_value_2 = 0;
			active_value_3 = 0;
			active_value_4 = 0;
			active_value_5 = 0;
			target_decide = 0;
			direction_id = 0;
			reinforce_military = 0;
			reinforce_property = 0;
			reinforce_job = 0;
			active_condition_explain_1 = 0;
			active_condition_explain_2 = 0;
			active_condition_explain_3 = 0;
			cast_cost = 0;
			joint_skill_unlock_id = 0;
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblSkillBaseData() {
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
		fieldRead.mobile_name = fieldWrite.mobile_name;
		fieldRead.furigana = fieldWrite.furigana;
		fieldRead.comments = fieldWrite.comments;
		fieldRead.property = fieldWrite.property;
		fieldRead.system = fieldWrite.system;
		fieldRead.type = fieldWrite.type;
		fieldRead.kind = fieldWrite.kind;
		fieldRead.skill_flag = fieldWrite.skill_flag;
		fieldRead.appeal_id = fieldWrite.appeal_id;
		fieldRead.active_type = fieldWrite.active_type;
		fieldRead.active_condition_1 = fieldWrite.active_condition_1;
		fieldRead.active_condition_2 = fieldWrite.active_condition_2;
		fieldRead.active_condition_3 = fieldWrite.active_condition_3;
		fieldRead.active_condition_4 = fieldWrite.active_condition_4;
		fieldRead.active_condition_5 = fieldWrite.active_condition_5;
		fieldRead.active_value_1 = fieldWrite.active_value_1;
		fieldRead.active_value_2 = fieldWrite.active_value_2;
		fieldRead.active_value_3 = fieldWrite.active_value_3;
		fieldRead.active_value_4 = fieldWrite.active_value_4;
		fieldRead.active_value_5 = fieldWrite.active_value_5;
		fieldRead.target_decide = fieldWrite.target_decide;
		fieldRead.direction_id = fieldWrite.direction_id;
		fieldRead.reinforce_military = fieldWrite.reinforce_military;
		fieldRead.reinforce_property = fieldWrite.reinforce_property;
		fieldRead.reinforce_job = fieldWrite.reinforce_job;
		fieldRead.active_condition_explain_1 = fieldWrite.active_condition_explain_1;
		fieldRead.active_condition_explain_2 = fieldWrite.active_condition_explain_2;
		fieldRead.active_condition_explain_3 = fieldWrite.active_condition_explain_3;
		fieldRead.cast_cost = fieldWrite.cast_cost;
		fieldRead.joint_skill_unlock_id = fieldWrite.joint_skill_unlock_id;
	}

}
