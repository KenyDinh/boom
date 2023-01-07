package dev.boom.tbl.info;

import dev.boom.dao.core.DaoValueInfo;

public class TblBattleDirectionInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "battle_direction_info";
	private static final String PRIMARY_KEY = "id";

	/** 戦闘ID（unsigned int） */
	private long id;

	/**
	 * 戦闘タイプ.
	 *
	 * @see jp.nobunyaga.battle.BattleType
	 */
	private byte type;

	/** NPCデッキデータID（unsigned int） */
	private long target_npc_id;

	/** 地形 */
	private byte land_type;

	/** マップポイントへのIndex */
	private short map_point_index;

	/** 天気 */
	private byte weather;

	/** 勝敗結果（0：未決定、1：攻撃側勝利、2：守備側勝利、3：引き分け） */
	private byte result;

	/** 保持フラグ */
	private byte save_flag;

	/** 総ターン数 */
	private byte total_turn;

	/** 勝敗理由（0:理由なし、1:総大将敗走、2:時間切れ） */
	private byte reason;

	/** 最多討取武将（0:該当なし、1-5:①側INDEX、6-10:②側INDEX） */
	private byte max_kill_index;

	/** 最多守衛武将 */
	private byte max_defence_index;

	/** 敗退武将（敗退していたら該当INDEXにフラグがたつ。①側と②側で２バイト */
	private byte retreat_flag_1;
	private byte retreat_flag_2;

	/** 攻撃数（①側と②側の２データ） */
	private short attack_count_1;
	private short attack_count_2;

	/** 総大将攻撃数（①側と②側の２データ） */
	private short leader_attack_count_1;
	private short leader_attack_count_2;

	/** 特技発動数（①側と②側の２データ）(tinyint unsigned) */
	private int skill_active_count_1;
	private int skill_active_count_2;

	/** 奥義発動数（①側と②側の２データ） */
	private short joint_skill_count_1;
	private short joint_skill_count_2;

	public TblBattleDirectionInfo() {
		id = 0;
		type = 0;
		target_npc_id = 0;
		land_type = 0;
		weather = 0;
		result = 0;
		save_flag = 0;
		total_turn = 0;
		reason = 0;
		max_kill_index = 0;
		max_defence_index = 0;
		retreat_flag_1 = 0;
		retreat_flag_2 = 0;
		attack_count_1 = 0;
		attack_count_2 = 0;
		leader_attack_count_1 = 0;
		leader_attack_count_2 = 0;
		skill_active_count_1 = 0;
		skill_active_count_2 = 0;
		joint_skill_count_1 = 0;
		joint_skill_count_2 = 0;
		map_point_index = 0;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public long getTarget_npc_id() {
		return target_npc_id;
	}

	public void setTarget_npc_id(long target_npc_id) {
		this.target_npc_id = target_npc_id;
	}

	public byte getLand_type() {
		return land_type;
	}

	public void setLand_type(byte land_type) {
		this.land_type = land_type;
	}

	public short getMap_point_index() {
		return map_point_index;
	}

	public void setMap_point_index(short map_point_index) {
		this.map_point_index = map_point_index;
	}

	public byte getWeather() {
		return weather;
	}

	public void setWeather(byte weather) {
		this.weather = weather;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public byte getSave_flag() {
		return save_flag;
	}

	public void setSave_flag(byte save_flag) {
		this.save_flag = save_flag;
	}

	public byte getTotal_turn() {
		return total_turn;
	}

	public void setTotal_turn(byte total_turn) {
		this.total_turn = total_turn;
	}

	public byte getReason() {
		return reason;
	}

	public void setReason(byte reason) {
		this.reason = reason;
	}

	public byte getMax_kill_index() {
		return max_kill_index;
	}

	public void setMax_kill_index(byte max_kill_index) {
		this.max_kill_index = max_kill_index;
	}

	public byte getMax_defence_index() {
		return max_defence_index;
	}

	public void setMax_defence_index(byte max_defence_index) {
		this.max_defence_index = max_defence_index;
	}

	public byte getRetreat_flag_1() {
		return retreat_flag_1;
	}

	public void setRetreat_flag_1(byte retreat_flag_1) {
		this.retreat_flag_1 = retreat_flag_1;
	}

	public byte getRetreat_flag_2() {
		return retreat_flag_2;
	}

	public void setRetreat_flag_2(byte retreat_flag_2) {
		this.retreat_flag_2 = retreat_flag_2;
	}

	public short getAttack_count_1() {
		return attack_count_1;
	}

	public void setAttack_count_1(short attack_count_1) {
		this.attack_count_1 = attack_count_1;
	}

	public short getAttack_count_2() {
		return attack_count_2;
	}

	public void setAttack_count_2(short attack_count_2) {
		this.attack_count_2 = attack_count_2;
	}

	public short getLeader_attack_count_1() {
		return leader_attack_count_1;
	}

	public void setLeader_attack_count_1(short leader_attack_count_1) {
		this.leader_attack_count_1 = leader_attack_count_1;
	}

	public short getLeader_attack_count_2() {
		return leader_attack_count_2;
	}

	public void setLeader_attack_count_2(short leader_attack_count_2) {
		this.leader_attack_count_2 = leader_attack_count_2;
	}

	public int getSkill_active_count_1() {
		return skill_active_count_1;
	}

	public void setSkill_active_count_1(int skill_active_count_1) {
		this.skill_active_count_1 = skill_active_count_1;
	}

	public int getSkill_active_count_2() {
		return skill_active_count_2;
	}

	public void setSkill_active_count_2(int skill_active_count_2) {
		this.skill_active_count_2 = skill_active_count_2;
	}

	public short getJoint_skill_count_1() {
		return joint_skill_count_1;
	}

	public void setJoint_skill_count_1(short joint_skill_count_1) {
		this.joint_skill_count_1 = joint_skill_count_1;
	}

	public short getJoint_skill_count_2() {
		return joint_skill_count_2;
	}

	public void setJoint_skill_count_2(short joint_skill_count_2) {
		this.joint_skill_count_2 = joint_skill_count_2;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
