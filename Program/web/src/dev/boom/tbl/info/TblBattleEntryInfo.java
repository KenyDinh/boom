package dev.boom.tbl.info;

import dev.boom.dao.core.DaoValueInfo;

public class TblBattleEntryInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "battle_entry_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	/** 戦闘演出データ（battle_direction_info）ID（unsigned int） */
	private long battle_direction_id;

	/** プレイヤーID（unsigned int） */
	private long player_id;

	/** 0：攻撃側、1：守備側 */
	private byte side;

	/** 総隊長の位置（card_x） */
	private byte leader;

	/** 陣形Index */
	private byte formation_index;

	/** 五輪奥義Index（unsigned short） */
	private int joint_skill_index_1;
	private int joint_skill_index_2;
	private int joint_skill_index_3;
	private int joint_skill_index_4;
	private int joint_skill_index_5;

	/** 武将カードIndex（unsigned short） */
	private int card_index_1;
	private int card_index_2;
	private int card_index_3;
	private int card_index_4;
	private int card_index_5;

	/** 開始兵士数 */
	private int start_hp_1;
	private int start_hp_2;
	private int start_hp_3;
	private int start_hp_4;
	private int start_hp_5;

	/** 終了兵士数 */
	private int end_hp_1;
	private int end_hp_2;
	private int end_hp_3;
	private int end_hp_4;
	private int end_hp_5;

	public TblBattleEntryInfo() {
		id = 0;
		battle_direction_id = 0;
		player_id = 0;
		side = 0;
		leader = 0;
		formation_index = 0;
		joint_skill_index_1 = 0;
		joint_skill_index_2 = 0;
		joint_skill_index_3 = 0;
		joint_skill_index_4 = 0;
		joint_skill_index_5 = 0;
		card_index_1 = 0;
		card_index_2 = 0;
		card_index_3 = 0;
		card_index_4 = 0;
		card_index_5 = 0;
		start_hp_1 = 0;
		start_hp_2 = 0;
		start_hp_3 = 0;
		start_hp_4 = 0;
		start_hp_5 = 0;
		end_hp_1 = 0;
		end_hp_2 = 0;
		end_hp_3 = 0;
		end_hp_4 = 0;
		end_hp_5 = 0;
		Sync();
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBattle_direction_id() {
		return battle_direction_id;
	}

	public void setBattle_direction_id(long battle_direction_id) {
		this.battle_direction_id = battle_direction_id;
	}

	public long getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(long player_id) {
		this.player_id = player_id;
	}

	public byte getSide() {
		return side;
	}

	public void setSide(byte side) {
		this.side = side;
	}

	public byte getLeader() {
		return leader;
	}

	public void setLeader(byte leader) {
		this.leader = leader;
	}

	public byte getFormation_index() {
		return formation_index;
	}

	public void setFormation_index(byte formation_index) {
		this.formation_index = formation_index;
	}

	public int getJoint_skill_index_1() {
		return joint_skill_index_1;
	}

	public void setJoint_skill_index_1(int joint_skill_index_1) {
		this.joint_skill_index_1 = joint_skill_index_1;
	}

	public int getJoint_skill_index_2() {
		return joint_skill_index_2;
	}

	public void setJoint_skill_index_2(int joint_skill_index_2) {
		this.joint_skill_index_2 = joint_skill_index_2;
	}

	public int getJoint_skill_index_3() {
		return joint_skill_index_3;
	}

	public void setJoint_skill_index_3(int joint_skill_index_3) {
		this.joint_skill_index_3 = joint_skill_index_3;
	}

	public int getJoint_skill_index_4() {
		return joint_skill_index_4;
	}

	public void setJoint_skill_index_4(int joint_skill_index_4) {
		this.joint_skill_index_4 = joint_skill_index_4;
	}

	public int getJoint_skill_index_5() {
		return joint_skill_index_5;
	}

	public void setJoint_skill_index_5(int joint_skill_index_5) {
		this.joint_skill_index_5 = joint_skill_index_5;
	}

	public int getCard_index_1() {
		return card_index_1;
	}

	public void setCard_index_1(int card_index_1) {
		this.card_index_1 = card_index_1;
	}

	public int getCard_index_2() {
		return card_index_2;
	}

	public void setCard_index_2(int card_index_2) {
		this.card_index_2 = card_index_2;
	}

	public int getCard_index_3() {
		return card_index_3;
	}

	public void setCard_index_3(int card_index_3) {
		this.card_index_3 = card_index_3;
	}

	public int getCard_index_4() {
		return card_index_4;
	}

	public void setCard_index_4(int card_index_4) {
		this.card_index_4 = card_index_4;
	}

	public int getCard_index_5() {
		return card_index_5;
	}

	public void setCard_index_5(int card_index_5) {
		this.card_index_5 = card_index_5;
	}

	public int getStart_hp_1() {
		return start_hp_1;
	}

	public void setStart_hp_1(int start_hp_1) {
		this.start_hp_1 = start_hp_1;
	}

	public int getStart_hp_2() {
		return start_hp_2;
	}

	public void setStart_hp_2(int start_hp_2) {
		this.start_hp_2 = start_hp_2;
	}

	public int getStart_hp_3() {
		return start_hp_3;
	}

	public void setStart_hp_3(int start_hp_3) {
		this.start_hp_3 = start_hp_3;
	}

	public int getStart_hp_4() {
		return start_hp_4;
	}

	public void setStart_hp_4(int start_hp_4) {
		this.start_hp_4 = start_hp_4;
	}

	public int getStart_hp_5() {
		return start_hp_5;
	}

	public void setStart_hp_5(int start_hp_5) {
		this.start_hp_5 = start_hp_5;
	}

	public int getEnd_hp_1() {
		return end_hp_1;
	}

	public void setEnd_hp_1(int end_hp_1) {
		this.end_hp_1 = end_hp_1;
	}

	public int getEnd_hp_2() {
		return end_hp_2;
	}

	public void setEnd_hp_2(int end_hp_2) {
		this.end_hp_2 = end_hp_2;
	}

	public int getEnd_hp_3() {
		return end_hp_3;
	}

	public void setEnd_hp_3(int end_hp_3) {
		this.end_hp_3 = end_hp_3;
	}

	public int getEnd_hp_4() {
		return end_hp_4;
	}

	public void setEnd_hp_4(int end_hp_4) {
		this.end_hp_4 = end_hp_4;
	}

	public int getEnd_hp_5() {
		return end_hp_5;
	}

	public void setEnd_hp_5(int end_hp_5) {
		this.end_hp_5 = end_hp_5;
	}

}
