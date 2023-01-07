package dev.boom.tbl.data;

import dev.boom.dao.core.DaoValueData;

public class TblCardBaseData extends DaoValueData {
	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "card_base_data";
	private static final String PRIMARY_KEY = "id";

	/** 武将ID（unsigned short） */
	public int id;

	/** 猫種 */
	public byte cat_type;

	/** 性別 */
	public byte sex;

	/** 所属大名 */
	public byte assign_daimyo_index;

	/** 出身地 */
	public int native_map_index;

	/** 職業 */
	public byte job;

	/** 成長タイプ（0：早熟、1：普通（早め）、2：普通（遅め）、3：晩成） */
	public byte growth;

	/** 父親Index（unsigned short） */
	public int father_index;

	/** 母親Index（unsigned short） */
	public int mother_index;

	/** 配偶者Index（unsigned short） */
	public int marrige_index;

	/** 相性 */
	public byte affinity;

	/** 兵科 */
	public byte mil_type;

	/** 好きな武将（unsigned short） */
	public int like_index_1;
	public int like_index_2;
	public int like_index_3;
	public int like_index_4;
	public int like_index_5;

	/** 嫌いな武将（unsigned short） */
	public int dislike_index_1;
	public int dislike_index_2;
	public int dislike_index_3;
	public int dislike_index_4;
	public int dislike_index_5;

	/** 性格（0:猪突、1:大胆、2:普通、3:沈着、4:慎重（数字が大きいほど冷静）） */
	public byte ego;

	/** 武将のタイプ（0：不明、1：猛将、2：守将、3：智将、4：医将） */
	public byte type;

	/**
	 *
	 */
	public TblCardBaseData() {
		id = 0;
		cat_type = -1; // 0も有意なため.
		sex = 0;
		assign_daimyo_index = 0;
		native_map_index = 0;
		job = 0;
		growth = 0;
		father_index = 0;
		mother_index = 0;
		marrige_index = 0;
		affinity = 0;
		mil_type = 0;
		like_index_1 = 0;
		like_index_2 = 0;
		like_index_3 = 0;
		like_index_4 = 0;
		like_index_5 = 0;
		dislike_index_1 = 0;
		dislike_index_2 = 0;
		dislike_index_3 = 0;
		dislike_index_4 = 0;
		dislike_index_5 = 0;
		ego = 0;
		type = 0;
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

	public byte getCat_type() {
		return cat_type;
	}

	public void setCat_type(byte cat_type) {
		this.cat_type = cat_type;
	}

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

	public byte getAssign_daimyo_index() {
		return assign_daimyo_index;
	}

	public void setAssign_daimyo_index(byte assign_daimyo_index) {
		this.assign_daimyo_index = assign_daimyo_index;
	}

	public int getNative_map_index() {
		return native_map_index;
	}

	public void setNative_map_index(int native_map_index) {
		this.native_map_index = native_map_index;
	}

	public byte getJob() {
		return job;
	}

	public void setJob(byte job) {
		this.job = job;
	}

	public byte getGrowth() {
		return growth;
	}

	public void setGrowth(byte growth) {
		this.growth = growth;
	}

	public int getFather_index() {
		return father_index;
	}

	public void setFather_index(int father_index) {
		this.father_index = father_index;
	}

	public int getMother_index() {
		return mother_index;
	}

	public void setMother_index(int mother_index) {
		this.mother_index = mother_index;
	}

	public int getMarrige_index() {
		return marrige_index;
	}

	public void setMarrige_index(int marrige_index) {
		this.marrige_index = marrige_index;
	}

	public byte getAffinity() {
		return affinity;
	}

	public void setAffinity(byte affinity) {
		this.affinity = affinity;
	}

	public byte getMil_type() {
		return mil_type;
	}

	public void setMil_type(byte mil_type) {
		this.mil_type = mil_type;
	}

	public int getLike_index_1() {
		return like_index_1;
	}

	public void setLike_index_1(int like_index_1) {
		this.like_index_1 = like_index_1;
	}

	public int getLike_index_2() {
		return like_index_2;
	}

	public void setLike_index_2(int like_index_2) {
		this.like_index_2 = like_index_2;
	}

	public int getLike_index_3() {
		return like_index_3;
	}

	public void setLike_index_3(int like_index_3) {
		this.like_index_3 = like_index_3;
	}

	public int getLike_index_4() {
		return like_index_4;
	}

	public void setLike_index_4(int like_index_4) {
		this.like_index_4 = like_index_4;
	}

	public int getLike_index_5() {
		return like_index_5;
	}

	public void setLike_index_5(int like_index_5) {
		this.like_index_5 = like_index_5;
	}

	public int getDislike_index_1() {
		return dislike_index_1;
	}

	public void setDislike_index_1(int dislike_index_1) {
		this.dislike_index_1 = dislike_index_1;
	}

	public int getDislike_index_2() {
		return dislike_index_2;
	}

	public void setDislike_index_2(int dislike_index_2) {
		this.dislike_index_2 = dislike_index_2;
	}

	public int getDislike_index_3() {
		return dislike_index_3;
	}

	public void setDislike_index_3(int dislike_index_3) {
		this.dislike_index_3 = dislike_index_3;
	}

	public int getDislike_index_4() {
		return dislike_index_4;
	}

	public void setDislike_index_4(int dislike_index_4) {
		this.dislike_index_4 = dislike_index_4;
	}

	public int getDislike_index_5() {
		return dislike_index_5;
	}

	public void setDislike_index_5(int dislike_index_5) {
		this.dislike_index_5 = dislike_index_5;
	}

	public byte getEgo() {
		return ego;
	}

	public void setEgo(byte ego) {
		this.ego = ego;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

}
