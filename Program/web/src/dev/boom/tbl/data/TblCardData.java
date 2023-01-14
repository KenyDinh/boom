package dev.boom.tbl.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueData;
import dev.boom.dao.IDaoValue;

public class TblCardData extends DaoValueData implements IDaoValue {
	/**
	 * 
	 */
	private static final String TABLE_NAME = "card_data";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {
	
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
	
		public Fields() {
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
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblCardData() {
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
		fieldRead.card_base_id = fieldWrite.card_base_id;
		fieldRead.name = fieldWrite.name;
		fieldRead.cat_name = fieldWrite.cat_name;
		fieldRead.mobile_cat_name = fieldWrite.mobile_cat_name;
		fieldRead.yomigana = fieldWrite.yomigana;
		fieldRead.rank = fieldWrite.rank;
		fieldRead.star = fieldWrite.star;
		fieldRead.image_id = fieldWrite.image_id;
		fieldRead.property = fieldWrite.property;
		fieldRead.cost = fieldWrite.cost;
		fieldRead.atk = fieldWrite.atk;
		fieldRead.def = fieldWrite.def;
		fieldRead.spd = fieldWrite.spd;
		fieldRead.vir = fieldWrite.vir;
		fieldRead.stg = fieldWrite.stg;
		fieldRead.skill_index_1 = fieldWrite.skill_index_1;
		fieldRead.skill_index_2 = fieldWrite.skill_index_2;
		fieldRead.skill_index_3 = fieldWrite.skill_index_3;
		fieldRead.skill_index_4 = fieldWrite.skill_index_4;
		fieldRead.skill_index_5 = fieldWrite.skill_index_5;
		fieldRead.skill_index_6 = fieldWrite.skill_index_6;
		fieldRead.skill_index_7 = fieldWrite.skill_index_7;
		fieldRead.skill_index_8 = fieldWrite.skill_index_8;
		fieldRead.skill_index_9 = fieldWrite.skill_index_9;
		fieldRead.skill_index_10 = fieldWrite.skill_index_10;
		fieldRead.prop_fire = fieldWrite.prop_fire;
		fieldRead.prop_earth = fieldWrite.prop_earth;
		fieldRead.prop_wind = fieldWrite.prop_wind;
		fieldRead.prop_water = fieldWrite.prop_water;
		fieldRead.prop_sky = fieldWrite.prop_sky;
		fieldRead.init_skill = fieldWrite.init_skill;
		fieldRead.hp = fieldWrite.hp;
		fieldRead.rarity = fieldWrite.rarity;
		fieldRead.available_1 = fieldWrite.available_1;
		fieldRead.available_2 = fieldWrite.available_2;
		fieldRead.available_3 = fieldWrite.available_3;
		fieldRead.available_4 = fieldWrite.available_4;
		fieldRead.available_5 = fieldWrite.available_5;
		fieldRead.available_mix = fieldWrite.available_mix;
		fieldRead.available = fieldWrite.available;
		fieldRead.lineup_1 = fieldWrite.lineup_1;
		fieldRead.lineup_2 = fieldWrite.lineup_2;
		fieldRead.lineup_3 = fieldWrite.lineup_3;
		fieldRead.lineup_4 = fieldWrite.lineup_4;
		fieldRead.lineup_5 = fieldWrite.lineup_5;
		fieldRead.lineup_6 = fieldWrite.lineup_6;
		fieldRead.lineup_7 = fieldWrite.lineup_7;
		fieldRead.lineup_8 = fieldWrite.lineup_8;
		fieldRead.lineup_9 = fieldWrite.lineup_9;
		fieldRead.lineup_10 = fieldWrite.lineup_10;
		fieldRead.lineup_11 = fieldWrite.lineup_11;
		fieldRead.rarity_1 = fieldWrite.rarity_1;
		fieldRead.rarity_2 = fieldWrite.rarity_2;
		fieldRead.rarity_3 = fieldWrite.rarity_3;
		fieldRead.rarity_4 = fieldWrite.rarity_4;
		fieldRead.rarity_5 = fieldWrite.rarity_5;
		fieldRead.rarity_6 = fieldWrite.rarity_6;
		fieldRead.rarity_7 = fieldWrite.rarity_7;
		fieldRead.rarity_8 = fieldWrite.rarity_8;
		fieldRead.rarity_9 = fieldWrite.rarity_9;
		fieldRead.rarity_10 = fieldWrite.rarity_10;
		fieldRead.rarity_11 = fieldWrite.rarity_11;
		fieldRead.honor_random_flag = fieldWrite.honor_random_flag;
		fieldRead.novice = fieldWrite.novice;
		fieldRead.payment_flag = fieldWrite.payment_flag;
		fieldRead.personality = fieldWrite.personality;
		fieldRead.history = fieldWrite.history;
		fieldRead.slogan = fieldWrite.slogan;
		fieldRead.num = fieldWrite.num;
		fieldRead.trade_limit = fieldWrite.trade_limit;
		fieldRead.reference_id = fieldWrite.reference_id;
		fieldRead.lot_group_1 = fieldWrite.lot_group_1;
		fieldRead.lot_group_2 = fieldWrite.lot_group_2;
		fieldRead.lot_group_3 = fieldWrite.lot_group_3;
		fieldRead.vote_flag = fieldWrite.vote_flag;
		fieldRead.available_fortune = fieldWrite.available_fortune;
	}

}
