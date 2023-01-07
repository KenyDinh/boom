package dev.boom.tbl.data;

import dev.boom.dao.core.DaoValueData;

public class TblSkillBaseData extends DaoValueData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "skill_base_data";
	private static final String PRIMARY_KEY = "id";

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

	public TblSkillBaseData() {
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
		Sync();
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

}
