package dev.boom.tbl.info;

import dev.boom.dao.core.DaoValueInfo;

public class TblCardInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "card_info";
	private static final String PRIMARY_KEY = "id";

	/** 武将ID（unsigned int） */
	private long id;

	/** プレイヤーID（unsigned int） */
	private long player_id;

	/** カードデータIndex（unsigned short） */
	private int card_index;

	/** 武将データIndex（unsigned short） */
	private int card_base_index;

	/** 勲功（耐久度）（unsigned tinyint） */
	private int deed;

	/** 現在兵士数） */
	private short hp;

	/** 攻撃 */
	private int atk;

	/** 防御 */
	private int def;

	/** 速力 */
	private int spd;

	/** 徳 */
	private int vir;

	/** 策 */
	private int stg;

	/** （火）修練を重ねた回数 */
	private short atk_level;

	/** （地）修練を重ねた回数 */
	private short def_level;

	/** （風）修練を重ねた回数 */
	private short spd_level;

	/** （水）修練を重ねた回数 */
	private short vir_level;

	/** （空）修練を重ねた回数 */
	private short stg_level;

	/** 秘技（unsigned short） */
	private int skill_index_1;
	private int skill_index_2;
	private int skill_index_3;

	public TblCardInfo() {
		id = 0;
		player_id = 0;
		card_index = 0;
		card_base_index = 0;
		deed = 0;
		hp = 0;
		atk = 0;
		def = 0;
		spd = 0;
		vir = 0;
		stg = 0;
		atk_level = 0;
		def_level = 0;
		spd_level = 0;
		vir_level = 0;
		stg_level = 0;
		skill_index_1 = 0;
		skill_index_2 = 0;
		skill_index_3 = 0;
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

	public int getCard_index() {
		return card_index;
	}

	public void setCard_index(int card_index) {
		this.card_index = card_index;
	}

	public int getCard_base_index() {
		return card_base_index;
	}

	public void setCard_base_index(int card_base_index) {
		this.card_base_index = card_base_index;
	}

	public int getDeed() {
		return deed;
	}

	public void setDeed(int deed) {
		this.deed = deed;
	}

	public short getHp() {
		return hp;
	}

	public void setHp(short hp) {
		this.hp = hp;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getSpd() {
		return spd;
	}

	public void setSpd(int spd) {
		this.spd = spd;
	}

	public int getVir() {
		return vir;
	}

	public void setVir(int vir) {
		this.vir = vir;
	}

	public int getStg() {
		return stg;
	}

	public void setStg(int stg) {
		this.stg = stg;
	}

	public short getAtk_level() {
		return atk_level;
	}

	public void setAtk_level(short atk_level) {
		this.atk_level = atk_level;
	}

	public short getDef_level() {
		return def_level;
	}

	public void setDef_level(short def_level) {
		this.def_level = def_level;
	}

	public short getSpd_level() {
		return spd_level;
	}

	public void setSpd_level(short spd_level) {
		this.spd_level = spd_level;
	}

	public short getVir_level() {
		return vir_level;
	}

	public void setVir_level(short vir_level) {
		this.vir_level = vir_level;
	}

	public short getStg_level() {
		return stg_level;
	}

	public void setStg_level(short stg_level) {
		this.stg_level = stg_level;
	}

	public int getSkill_index_1() {
		return skill_index_1;
	}

	public void setSkill_index_1(int skill_index_1) {
		this.skill_index_1 = skill_index_1;
	}

	public int getSkill_index_2() {
		return skill_index_2;
	}

	public void setSkill_index_2(int skill_index_2) {
		this.skill_index_2 = skill_index_2;
	}

	public int getSkill_index_3() {
		return skill_index_3;
	}

	public void setSkill_index_3(int skill_index_3) {
		this.skill_index_3 = skill_index_3;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
