package dev.boom.tbl.info;

import dev.boom.dao.core.DaoValueInfo;

public class TblDeckInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "deck_info";
	private static final String PRIMARY_KEY = "id";

	/** 部隊ID（unigned int） */
	private long id;

	/** プレイヤーID（unigned int） */
	private long player_id;

	/** 総隊長の位置（card_x） */
	private byte leader;

	/** 陣形Index */
	private byte formation_index;

	/** 武将カードIDのリンク（card_info.id)（unigned int） */
	private long card_id_1;
	private long card_id_2;
	private long card_id_3;
	private long card_id_4;
	private long card_id_5;

	/** 連携秘技Index（unsigned short） */
	private int joint_skill_index_1;
	private int joint_skill_index_2;
	private int joint_skill_index_3;
	private int joint_skill_index_4;
	private int joint_skill_index_5;

	/** 連携秘技数 */
	private byte joint_skill_count_1;
	private byte joint_skill_count_2;
	private byte joint_skill_count_3;
	private byte joint_skill_count_4;
	private byte joint_skill_count_5;

	private byte status;

	/** デッキのレベル */
	private byte level;

	public TblDeckInfo() {
		id = 0;
		player_id = 0;
		leader = 1;
		formation_index = 1;
		card_id_1 = 0;
		card_id_2 = 0;
		card_id_3 = 0;
		card_id_4 = 0;
		card_id_5 = 0;
		joint_skill_index_1 = 0;
		joint_skill_index_2 = 0;
		joint_skill_index_3 = 0;
		joint_skill_index_4 = 0;
		joint_skill_index_5 = 0;
		joint_skill_count_1 = 0;
		joint_skill_count_2 = 0;
		joint_skill_count_3 = 0;
		joint_skill_count_4 = 0;
		joint_skill_count_5 = 0;
		status = 0;
		level = 1;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(long player_id) {
		this.player_id = player_id;
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

	public long getCard_id_1() {
		return card_id_1;
	}

	public void setCard_id_1(long card_id_1) {
		this.card_id_1 = card_id_1;
	}

	public long getCard_id_2() {
		return card_id_2;
	}

	public void setCard_id_2(long card_id_2) {
		this.card_id_2 = card_id_2;
	}

	public long getCard_id_3() {
		return card_id_3;
	}

	public void setCard_id_3(long card_id_3) {
		this.card_id_3 = card_id_3;
	}

	public long getCard_id_4() {
		return card_id_4;
	}

	public void setCard_id_4(long card_id_4) {
		this.card_id_4 = card_id_4;
	}

	public long getCard_id_5() {
		return card_id_5;
	}

	public void setCard_id_5(long card_id_5) {
		this.card_id_5 = card_id_5;
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

	public byte getJoint_skill_count_1() {
		return joint_skill_count_1;
	}

	public void setJoint_skill_count_1(byte joint_skill_count_1) {
		this.joint_skill_count_1 = joint_skill_count_1;
	}

	public byte getJoint_skill_count_2() {
		return joint_skill_count_2;
	}

	public void setJoint_skill_count_2(byte joint_skill_count_2) {
		this.joint_skill_count_2 = joint_skill_count_2;
	}

	public byte getJoint_skill_count_3() {
		return joint_skill_count_3;
	}

	public void setJoint_skill_count_3(byte joint_skill_count_3) {
		this.joint_skill_count_3 = joint_skill_count_3;
	}

	public byte getJoint_skill_count_4() {
		return joint_skill_count_4;
	}

	public void setJoint_skill_count_4(byte joint_skill_count_4) {
		this.joint_skill_count_4 = joint_skill_count_4;
	}

	public byte getJoint_skill_count_5() {
		return joint_skill_count_5;
	}

	public void setJoint_skill_count_5(byte joint_skill_count_5) {
		this.joint_skill_count_5 = joint_skill_count_5;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
