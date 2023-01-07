package dev.boom.tbl.info;

import java.util.Date;

import dev.boom.dao.core.DaoValueInfo;

public class TblBattleInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "battle_info";
	private static final String PRIMARY_KEY = "id";

	/** 戦闘結果ID（unsigned int） */
	private long id;

	/** 所有者のプレイヤーID（unsigned int） */
	private long keeper_id;

	/** 戦闘演出ID（unsigned int） */
	private long battle_direction_id;

	/** 戦闘ログデータのインデックス。1～100番目のどのファイルに該当するかどうか(unsigned tinyint). */
	private short battle_log_index;

	/** 拡張フラグ（unsigned tinyint） */
	private int expansion_flag;

	/** 使用：スキルIndex（unsigned smallint） */
	private int use_skill_index_1;
	private int use_skill_index_2;
	private int use_skill_index_3;
	private int use_skill_index_4;
	private int use_skill_index_5;

	/** 使用：スキル数（unsigned tinyint） */
	private int use_skill_spent_1;
	private int use_skill_spent_2;
	private int use_skill_spent_3;
	private int use_skill_spent_4;
	private int use_skill_spent_5;

	/** 報酬：戦闘中に獲得した奥義ID（unsigned smallint） */
	private int reward_skill_id;

	/** 獲得ニャポ */
	private byte reward_nyapo;

	/** 戦闘実行時間(サーバタイム) */
	private Date created;

	public TblBattleInfo() {
		id = 0;
		keeper_id = 0;
		battle_direction_id = 0;
		battle_log_index = 0;
		expansion_flag = 0;
		use_skill_index_1 = 0;
		use_skill_index_2 = 0;
		use_skill_index_3 = 0;
		use_skill_index_4 = 0;
		use_skill_index_5 = 0;
		use_skill_spent_1 = 0;
		use_skill_spent_2 = 0;
		use_skill_spent_3 = 0;
		use_skill_spent_4 = 0;
		use_skill_spent_5 = 0;
		reward_skill_id = 0;
		reward_nyapo = 0;
		created = new Date();
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getKeeper_id() {
		return keeper_id;
	}

	public void setKeeper_id(long keeper_id) {
		this.keeper_id = keeper_id;
	}

	public long getBattle_direction_id() {
		return battle_direction_id;
	}

	public void setBattle_direction_id(long battle_direction_id) {
		this.battle_direction_id = battle_direction_id;
	}

	public short getBattle_log_index() {
		return battle_log_index;
	}

	public void setBattle_log_index(short battle_log_index) {
		this.battle_log_index = battle_log_index;
	}

	public int getExpansion_flag() {
		return expansion_flag;
	}

	public void setExpansion_flag(int expansion_flag) {
		this.expansion_flag = expansion_flag;
	}

	public int getUse_skill_index_1() {
		return use_skill_index_1;
	}

	public void setUse_skill_index_1(int use_skill_index_1) {
		this.use_skill_index_1 = use_skill_index_1;
	}

	public int getUse_skill_index_2() {
		return use_skill_index_2;
	}

	public void setUse_skill_index_2(int use_skill_index_2) {
		this.use_skill_index_2 = use_skill_index_2;
	}

	public int getUse_skill_index_3() {
		return use_skill_index_3;
	}

	public void setUse_skill_index_3(int use_skill_index_3) {
		this.use_skill_index_3 = use_skill_index_3;
	}

	public int getUse_skill_index_4() {
		return use_skill_index_4;
	}

	public void setUse_skill_index_4(int use_skill_index_4) {
		this.use_skill_index_4 = use_skill_index_4;
	}

	public int getUse_skill_index_5() {
		return use_skill_index_5;
	}

	public void setUse_skill_index_5(int use_skill_index_5) {
		this.use_skill_index_5 = use_skill_index_5;
	}

	public int getUse_skill_spent_1() {
		return use_skill_spent_1;
	}

	public void setUse_skill_spent_1(int use_skill_spent_1) {
		this.use_skill_spent_1 = use_skill_spent_1;
	}

	public int getUse_skill_spent_2() {
		return use_skill_spent_2;
	}

	public void setUse_skill_spent_2(int use_skill_spent_2) {
		this.use_skill_spent_2 = use_skill_spent_2;
	}

	public int getUse_skill_spent_3() {
		return use_skill_spent_3;
	}

	public void setUse_skill_spent_3(int use_skill_spent_3) {
		this.use_skill_spent_3 = use_skill_spent_3;
	}

	public int getUse_skill_spent_4() {
		return use_skill_spent_4;
	}

	public void setUse_skill_spent_4(int use_skill_spent_4) {
		this.use_skill_spent_4 = use_skill_spent_4;
	}

	public int getUse_skill_spent_5() {
		return use_skill_spent_5;
	}

	public void setUse_skill_spent_5(int use_skill_spent_5) {
		this.use_skill_spent_5 = use_skill_spent_5;
	}

	public int getReward_skill_id() {
		return reward_skill_id;
	}

	public void setReward_skill_id(int reward_skill_id) {
		this.reward_skill_id = reward_skill_id;
	}

	public byte getReward_nyapo() {
		return reward_nyapo;
	}

	public void setReward_nyapo(byte reward_nyapo) {
		this.reward_nyapo = reward_nyapo;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
