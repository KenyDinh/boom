package dev.boom.tbl.data;

import dev.boom.dao.core.DaoValueData;

public class TblSkillData extends DaoValueData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "skill_data";
	private static final String PRIMARY_KEY = "id";

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

	public TblSkillData() {
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
		Sync();
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getHidden_flag() {
		return hidden_flag;
	}

	public void setHidden_flag(byte hidden_flag) {
		this.hidden_flag = hidden_flag;
	}

	public int getSkill_base_id() {
		return skill_base_id;
	}

	public void setSkill_base_id(int skill_base_id) {
		this.skill_base_id = skill_base_id;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public byte getWeight() {
		return weight;
	}

	public void setWeight(byte weight) {
		this.weight = weight;
	}

	public byte getTarget_type_index() {
		return target_type_index;
	}

	public void setTarget_type_index(byte target_type_index) {
		this.target_type_index = target_type_index;
	}

	public byte getActive_rate() {
		return active_rate;
	}

	public void setActive_rate(byte active_rate) {
		this.active_rate = active_rate;
	}

	public short getRegist_fix_value() {
		return regist_fix_value;
	}

	public void setRegist_fix_value(short regist_fix_value) {
		this.regist_fix_value = regist_fix_value;
	}

	public byte getEffect_term() {
		return effect_term;
	}

	public void setEffect_term(byte effect_term) {
		this.effect_term = effect_term;
	}

	public int getEffect_id_1() {
		return effect_id_1;
	}

	public void setEffect_id_1(int effect_id_1) {
		this.effect_id_1 = effect_id_1;
	}

	public int getEffect_type_1() {
		return effect_type_1;
	}

	public void setEffect_type_1(int effect_type_1) {
		this.effect_type_1 = effect_type_1;
	}

	public short getEffect_data_main_1() {
		return effect_data_main_1;
	}

	public void setEffect_data_main_1(short effect_data_main_1) {
		this.effect_data_main_1 = effect_data_main_1;
	}

	public short getEffect_data_sub_1() {
		return effect_data_sub_1;
	}

	public void setEffect_data_sub_1(short effect_data_sub_1) {
		this.effect_data_sub_1 = effect_data_sub_1;
	}

	public int getEffect_id_2() {
		return effect_id_2;
	}

	public void setEffect_id_2(int effect_id_2) {
		this.effect_id_2 = effect_id_2;
	}

	public int getEffect_type_2() {
		return effect_type_2;
	}

	public void setEffect_type_2(int effect_type_2) {
		this.effect_type_2 = effect_type_2;
	}

	public short getEffect_data_main_2() {
		return effect_data_main_2;
	}

	public void setEffect_data_main_2(short effect_data_main_2) {
		this.effect_data_main_2 = effect_data_main_2;
	}

	public short getEffect_data_sub_2() {
		return effect_data_sub_2;
	}

	public void setEffect_data_sub_2(short effect_data_sub_2) {
		this.effect_data_sub_2 = effect_data_sub_2;
	}

	public int getEffect_id_3() {
		return effect_id_3;
	}

	public void setEffect_id_3(int effect_id_3) {
		this.effect_id_3 = effect_id_3;
	}

	public int getEffect_type_3() {
		return effect_type_3;
	}

	public void setEffect_type_3(int effect_type_3) {
		this.effect_type_3 = effect_type_3;
	}

	public short getEffect_data_main_3() {
		return effect_data_main_3;
	}

	public void setEffect_data_main_3(short effect_data_main_3) {
		this.effect_data_main_3 = effect_data_main_3;
	}

	public short getEffect_data_sub_3() {
		return effect_data_sub_3;
	}

	public void setEffect_data_sub_3(short effect_data_sub_3) {
		this.effect_data_sub_3 = effect_data_sub_3;
	}

	public int getProp_reward() {
		return prop_reward;
	}

	public void setProp_reward(int prop_reward) {
		this.prop_reward = prop_reward;
	}

	public int getMenace() {
		return menace;
	}

	public void setMenace(int menace) {
		this.menace = menace;
	}

	public int getSort_order() {
		return sort_order;
	}

	public void setSort_order(int sort_order) {
		this.sort_order = sort_order;
	}

}
