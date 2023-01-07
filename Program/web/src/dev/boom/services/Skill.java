package dev.boom.services;

import dev.boom.tbl.data.TblSkillBaseData;
import dev.boom.tbl.data.TblSkillData;

public class Skill {
	private TblSkillData tblSkillData;
	private TblSkillBaseData tblSkillBaseData;
	public static final int MAX_EFFECT_IN_SKILL = 3;

	public Skill(TblSkillData skillData, TblSkillBaseData skillBaseData) {
		tblSkillData = skillData;
		tblSkillBaseData = skillBaseData;
	}

	public TblSkillData getTblSkillData() {
		return tblSkillData;
	}

	public TblSkillBaseData getTblSkillBaseData() {
		return tblSkillBaseData;
	}

	public int getSkillBaseId() {
		return (Integer) tblSkillBaseData.Get("id");
	}

	/**
	 * @return スキル名.
	 */
	public String getName() {
		return tblSkillBaseData.Get("name").toString();
	}

	/**
	 * @return モバイル用スキル名.
	 */
	public String getMobileName() {
		return tblSkillBaseData.Get("mobile_name").toString();
	}

	/**
	 * @return スキルの説明.
	 */
	public String getComment() {
		return tblSkillBaseData.Get("comments").toString();
	}

	/**
	 * @return 五輪属性.
	 */
	public byte getProperty() {
		return (Byte) tblSkillBaseData.Get("property");
	}

	/**
	 * @return 五輪属性.
	 */
	public byte getSystem() {
		return (Byte) tblSkillBaseData.Get("system");
	}

	/**
	 * @return 分類（0:特技、1:奥義、2:超奥義）.
	 */
	public byte getKind() {
		return (Byte) tblSkillBaseData.Get("kind");
	}


	/**
	 * @return 戦闘演出の表現方法のID..
	 */
	public int getDirectionId() {
		return (Integer) tblSkillBaseData.Get("direction_id");
	}

	/**
	 * @return アピールID.
	 */
	public int getSkillAppealId() {
		return (Integer) tblSkillBaseData.Get("appeal_id");
	}

	public short getJointSkillCost() {
		return (Short) tblSkillBaseData.Get("cast_cost");
	}

	/**
	 * @return スキルフラグ(1:固有技能、2:購入可、4:開発入手、8:クエスト入手、16:大名入手、32:合戦専用).
	 */
	public byte getSkillFlag() {
		return (Byte) tblSkillBaseData.Get("skill_flag");
	}

	public String getActiveConditionTag(int nIndex) {
		int nConditionID = (Byte) getTblSkillBaseData().Get(("active_condition_explain_") + nIndex);
		if (nConditionID != 0) {
			return ("MSG_TEAM_SKILL_CONDITION_" + nConditionID);
		}

		return "";
	}

	/**
	 * @return 合成判定兵科（ビット管理、1:足軽、2:騎馬、4:鉄砲）..
	 */
	public byte getReinforceMilitary() {
		return (Byte) tblSkillBaseData.Get("reinforce_military");
	}

	/**
	 * @return 合成判定属性..
	 */
	public byte getReinforceProperty() {
		return (Byte) tblSkillBaseData.Get("reinforce_property");
	}

	/**
	 * @return 合成判定職業..
	 */
	public byte getReinforceJob() {
		return (Byte) tblSkillBaseData.Get("reinforce_job");
	}

	/**
	 * 奥義レシピ解錠系
	 * @return
	 */
	public boolean isUnlockableJointSkill() {
		return (Integer) tblSkillBaseData.Get("joint_skill_unlock_id") > 0;
	}

	/**
	 * 奥義レシピ解錠ID
	 * @return
	 */
	public int getJointSkillUnlockId() {
		return (Integer) tblSkillBaseData.Get("joint_skill_unlock_id");
	}

	/** TblSkillData **/

	/**
	 * @return スキルID.
	 */
	public int getSkillId() {
		return (Integer) tblSkillData.Get("id");
	}

	/**
	 * @return スキルレベル.
	 */
	public byte getLevel() {
		return (Byte) tblSkillData.Get("level");
	}

	/**
	 * @return ウエイト.
	 */
	public byte getWeight() {
		return (Byte) tblSkillData.Get("weight");
	}

	/**
	 * @return 効果ID.
	 */
	public int getEffectId(int index) {
		if (index < 1 || index > MAX_EFFECT_IN_SKILL) {
			return 0;
		}
		return (Integer) tblSkillData.Get("effect_id_" + index);
	}

	/**
	 * @return 効果値.
	 */
	public short getEffectMain(int index) {
		if (index < 1 || index > MAX_EFFECT_IN_SKILL) {
			return 0;
		}
		return (Short) tblSkillData.Get("effect_data_main_" + index);
	}

	/**
	 * @return 効果サブ値.
	 */
	public short getEffectSub(int index) {
		if (index < 1 || index > MAX_EFFECT_IN_SKILL) {
			return 0;
		}
		return (Short) tblSkillData.Get("effect_data_sub_" + index);
	}

	/**
	 * ソート順番を取得.
	 *
	 * @return
	 */
	public int getSortOrder() {
		return (Integer) tblSkillData.Get("sort_order");
	}
}
