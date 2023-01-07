package dev.boom.tbl.data;

import dev.boom.dao.core.DaoValueData;

public class TblCardData extends DaoValueData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "card_data";
	private static final String PRIMARY_KEY = "id";

	/** カードID（unsigned short） */
	public int id;

	/** 武将ID（unsigned short） */
	public int card_base_id;

	/** 名前（10 * 3） */
	public String name;

	/** 猫名（10 * 3） */
	public String cat_name;

	/** Mobile猫名（10 * 3） */
	public String mobile_cat_name;

	/** よみがな（10 * 3） */
	public String yomigana;

	/** カードのランク */
	public byte rank;
	
	/** ☆数 */
	public byte star;

	/** CGのID */
	public short image_id;

	/** 初期属性 */
	public byte property;

	/** 知行高（コスト） */
	public byte cost;

	/** 初期：攻撃 */
	public int atk;

	/** 初期：防御 */
	public int def;

	/** 初期：速力 */
	public int spd;

	/** 初期：徳 */
	public int vir;

	/** 初期：軍略 */
	public int stg;

	/** 潜在特技（unsigned short） */
	public int skill_index_1;
	public int skill_index_2;
	public int skill_index_3;
	public int skill_index_4;
	public int skill_index_5;
	public int skill_index_6;
	public int skill_index_7;
	public int skill_index_8;
	public int skill_index_9;
	public int skill_index_10;

	/** 五輪玉（火）伸び率％ */
	public byte prop_fire;

	/** 五輪玉（地）伸び率％ */
	public byte prop_earth;

	/** 五輪玉（風）伸び率％ */
	public byte prop_wind;

	/** 五輪玉（水）伸び率％ */
	public byte prop_water;

	/** 五輪玉（空）伸び率％ */
	public byte prop_sky;

	/** 初期スキル */
	public int init_skill;

	/** 初期：兵士数 */
	public short hp;

	/** 入手レベル（1 ～ 4） */
	public byte rarity;

	/** 入手可能フラグ */
	public byte available_1; // 通.
	public byte available_2; // 改.
	public byte available_3; // 特.
	public byte available_4; // 吉.
	public byte available_5; // 福.
	public byte available_mix; // 練成ガチャ.
	/** 入手可能フラグ */
	public byte available;
	public byte lineup_1;	 // 内容1
	public byte lineup_2;	 // 内容2
	public byte lineup_3;	 // 内容3
	public byte lineup_4;	 // 内容4
	public byte lineup_5;	 // 内容5
	public byte lineup_6;	 // 内容6
	public byte lineup_7;	 // 内容7
	public byte lineup_8;	 // 内容8
	public byte lineup_9;	 // 内容9
	public byte lineup_10;	 // 内容10
	public byte lineup_11;	 // 内容11
	public byte rarity_1;	 // 内容1入手レベル
	public byte rarity_2;	 // 内容2入手レベル
	public byte rarity_3;	 // 内容3入手レベル
	public byte rarity_4;	 // 内容4入手レベル
	public byte rarity_5;	 // 内容5入手レベル
	public byte rarity_6;	 // 内容6入手レベル
	public byte rarity_7;	 // 内容7入手レベル
	public byte rarity_8;	 // 内容8入手レベル
	public byte rarity_9;	 // 内容9入手レベル
	public byte rarity_10;	 // 内容10入手レベル
	public byte rarity_11;	 // 内容11入手レベル
/*		public byte available_6; // 祭.
	public byte available_7; // 一撃.
	public byte available_8; // 宝船.*/


	/** 祭用入手レベル（1 ～ 4） */
//	public byte rarity_maturi;

	/** 一撃くじ入手レベル（1～4）*/
//	public byte rarity_onecoin;

	/** 宝船用入手レベル*/
//	public byte rarity_tenpack_takara;

	/** 誉ランダム報酬用 排出フラグ */
	public byte honor_random_flag;

	/** 新米フラグ */
	public byte novice;

	/** 課金のみフラグ */
	public byte payment_flag;

	/** 人物評価 */
	public String personality;

	/** 列伝 */
	public String history;

	/** 称呼 */
	public String slogan;

	/** カードNo */
	public short num;

	/** 交換不可 */
	public byte trade_limit;
	
	/** 制御 */
	public int reference_id;
	
	/** 排出制御フラグ */
	public byte lot_group_1;
	public byte lot_group_2;
	public byte lot_group_3;

	/** 投票出現フラグ */
	public byte vote_flag;

	public byte available_fortune; // 占い.

	public TblCardData() {
		id					= 0;
		card_base_id		= 0;
		name				= "";
		cat_name			= "";
		mobile_cat_name		= "";
		yomigana			= "";
		rank				= 0;
		star				= 0;
		image_id			= 0;
		property			= 0;
		cost				= 0;
		atk					= 0;
		def					= 0;
		spd					= 0;
		vir					= 0;
		stg					= 0;
		skill_index_1		= 0;
		skill_index_2		= 0;
		skill_index_3		= 0;
		skill_index_4		= 0;
		skill_index_5		= 0;
		skill_index_6		= 0;
		skill_index_7		= 0;
		skill_index_8		= 0;
		skill_index_9		= 0;
		skill_index_10		= 0;
		prop_fire			= 0;
		prop_earth			= 0;
		prop_wind			= 0;
		prop_water			= 0;
		prop_sky			= 0;
		init_skill			= 0;
		hp					= 0;
		rarity				= 0;
		available_1			= 0;
		available_2			= 0;
		available_3			= 0;
		available_4			= 0;
		available_5			= 0;
		available_mix		= 0;
		available			= 0;
		lineup_1			= 0;
		lineup_2			= 0;
		lineup_3			= 0;
		lineup_4			= 0;
		lineup_5			= 0;
		lineup_6			= 0;
		lineup_7			= 0;
		lineup_8			= 0;
		lineup_9			= 0;
		lineup_10			= 0;
		lineup_11			= 0;
		rarity_1			= 0;
		rarity_2			= 0;
		rarity_3			= 0;
		rarity_4			= 0;
		rarity_5			= 0;
		rarity_6			= 0;
		rarity_7			= 0;
		rarity_8			= 0;
		rarity_9			= 0;
		rarity_10			= 0;
		rarity_11			= 0;
//		rarity_maturi		= 0;
//		rarity_onecoin		= 0;
//		rarity_tenpack_takara = 0;
		honor_random_flag	= 0;
		novice				= 0;
		payment_flag		= 0;
		personality			= "";
		history				= "";
		slogan				= "";
		num					= 0;
		trade_limit			= 0;
		reference_id 		= 0;
		lot_group_1			= 0;
		lot_group_2			= 0;
		lot_group_3			= 0;
		vote_flag			= 0;
		available_fortune	= 0;
		Sync();
	}

	public int getId() {
		return id;
	}

	public int getCard_base_id() {
		return card_base_id;
	}

	public String getName() {
		return name;
	}

	public String getCat_name() {
		return cat_name;
	}

	public String getMobile_cat_name() {
		return mobile_cat_name;
	}

	public String getYomigana() {
		return yomigana;
	}

	public byte getRank() {
		return rank;
	}

	public byte getStar() {
		return star;
	}

	public short getImage_id() {
		return image_id;
	}

	public byte getProperty() {
		return property;
	}

	public byte getCost() {
		return cost;
	}

	public int getAtk() {
		return atk;
	}

	public int getDef() {
		return def;
	}

	public int getSpd() {
		return spd;
	}

	public int getVir() {
		return vir;
	}

	public int getStg() {
		return stg;
	}

	public int getSkillIndex(int i) {
		switch (i) {
		case 1:
			return skill_index_1;
		case 2:
			return skill_index_2;
		case 3:
			return skill_index_3;
		case 4:
			return skill_index_4;
		case 5:
			return skill_index_5;
		case 6:
			return skill_index_6;
		case 7:
			return skill_index_7;
		case 8:
			return skill_index_8;
		case 9:
			return skill_index_9;
		case 10:
			return skill_index_10;
		default:
			return 0;
		}
	}

	public int getSkill_index_1() {
		return skill_index_1;
	}

	public int getSkill_index_2() {
		return skill_index_2;
	}

	public int getSkill_index_3() {
		return skill_index_3;
	}

	public int getSkill_index_4() {
		return skill_index_4;
	}

	public int getSkill_index_5() {
		return skill_index_5;
	}

	public int getSkill_index_6() {
		return skill_index_6;
	}

	public int getSkill_index_7() {
		return skill_index_7;
	}

	public int getSkill_index_8() {
		return skill_index_8;
	}

	public int getSkill_index_9() {
		return skill_index_9;
	}

	public int getSkill_index_10() {
		return skill_index_10;
	}

	public byte getProp_fire() {
		return prop_fire;
	}

	public byte getProp_earth() {
		return prop_earth;
	}

	public byte getProp_wind() {
		return prop_wind;
	}

	public byte getProp_water() {
		return prop_water;
	}

	public byte getProp_sky() {
		return prop_sky;
	}

	public int getInit_skill() {
		return init_skill;
	}

	public short getHp() {
		return hp;
	}

	public byte getRarity() {
		return rarity;
	}

	public byte getAvailable_1() {
		return available_1;
	}

	public byte getAvailable_2() {
		return available_2;
	}

	public byte getAvailable_3() {
		return available_3;
	}

	public byte getAvailable_4() {
		return available_4;
	}

	public byte getAvailable_5() {
		return available_5;
	}

	public byte getAvailable_mix() {
		return available_mix;
	}

	public byte getAvailable() {
		return available;
	}

	public byte getLineup_1() {
		return lineup_1;
	}

	public byte getLineup_2() {
		return lineup_2;
	}

	public byte getLineup_3() {
		return lineup_3;
	}

	public byte getLineup_4() {
		return lineup_4;
	}

	public byte getLineup_5() {
		return lineup_5;
	}

	public byte getLineup_6() {
		return lineup_6;
	}

	public byte getLineup_7() {
		return lineup_7;
	}

	public byte getLineup_8() {
		return lineup_8;
	}

	public byte getLineup_9() {
		return lineup_9;
	}

	public byte getLineup_10() {
		return lineup_10;
	}

	public byte getLineup_11() {
		return lineup_11;
	}

	public byte getRarity_1() {
		return rarity_1;
	}

	public byte getRarity_2() {
		return rarity_2;
	}

	public byte getRarity_3() {
		return rarity_3;
	}

	public byte getRarity_4() {
		return rarity_4;
	}

	public byte getRarity_5() {
		return rarity_5;
	}

	public byte getRarity_6() {
		return rarity_6;
	}

	public byte getRarity_7() {
		return rarity_7;
	}

	public byte getRarity_8() {
		return rarity_8;
	}

	public byte getRarity_9() {
		return rarity_9;
	}

	public byte getRarity_10() {
		return rarity_10;
	}

	public byte getRarity_11() {
		return rarity_11;
	}

	public byte getHonor_random_flag() {
		return honor_random_flag;
	}

	public byte getNovice() {
		return novice;
	}

	public byte getPayment_flag() {
		return payment_flag;
	}

	public String getPersonality() {
		return personality;
	}

	public String getHistory() {
		return history;
	}

	public String getSlogan() {
		return slogan;
	}

	public short getNum() {
		return num;
	}

	public byte getTrade_limit() {
		return trade_limit;
	}

	public byte getLot_group_1() {
		return lot_group_1;
	}

	public byte getLot_group_2() {
		return lot_group_2;
	}

	public byte getLot_group_3() {
		return lot_group_3;
	}

	public byte getVote_flag() {
		return vote_flag;
	}

	public byte getAvailable_fortune() {
		return available_fortune;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

}
