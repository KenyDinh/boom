package dev.boom.services;

import dev.boom.tbl.data.TblCardBaseData;

public class CardBaseData {
	/** 武将ID（unsigned short） */
	private int id;

	/** 猫種 */
	public byte cat_type;

	/** 性別 */
	private byte sex;

	/** 所属大名 */
	private byte assign_daimyo_index;

	/** 出身地 */
	private int native_map_index;

	/** 職業 */
	private byte job;

	/** 成長タイプ（0：早熟、1：普通（早め）、2：普通（遅め）、3：晩成） */
	private byte growth;

	/** 列伝（80 * 3） */
	private String history;

	/** 父親Index（unsigned short） */
	private int father_index;

	/** 母親Index（unsigned short） */
	private int mother_index;

	/** 配偶者Index（unsigned short） */
	private int marrige_index;

	/** 相性 */
	private byte affinity;

	/** 兵科 */
	private byte mil_type;

	/** 好きな武将（unsigned short） */
	private int[] like_index;

	/** 嫌いな武将（unsigned short） */
	private int[] dislike_index;

	/** 性格（0:猪突、1:大胆、2:普通、3:沈着、4:慎重（数字が大きいほど冷静）） */
	private byte ego;

	/** 武将のタイプ（0：不明、1：猛将、2：守将、3：智将、4：医将） */
	private byte type;

	/**
	 * @param _data
	 */
	public CardBaseData(TblCardBaseData _data) {

		this.id = ((Integer) _data.Get("id"));
		this.cat_type = ((Byte) _data.Get("cat_type"));
		this.sex = ((Byte) _data.Get("sex"));
		this.assign_daimyo_index = ((Byte) _data.Get("assign_daimyo_index"));
		this.native_map_index = ((Integer) _data.Get("native_map_index"));
		this.job = ((Byte) _data.Get("job"));
		this.growth = ((Byte) _data.Get("growth"));
		this.father_index = ((Integer) _data.Get("father_index"));
		this.mother_index = ((Integer) _data.Get("mother_index"));
		this.marrige_index = ((Integer) _data.Get("marrige_index"));
		this.affinity = ((Byte) _data.Get("affinity"));
		this.mil_type = ((Byte) _data.Get("mil_type"));
		int[] like_index = { (Integer) _data.Get("like_index_1"), //
				(Integer) _data.Get("like_index_2"), //
				(Integer) _data.Get("like_index_3"), //
				(Integer) _data.Get("like_index_4"), //
				(Integer) _data.Get("like_index_5") };
		this.like_index = like_index;
		int[] dislike_index = { (Integer) _data.Get("dislike_index_1"), //
				(Integer) _data.Get("dislike_index_2"), //
				(Integer) _data.Get("dislike_index_3"), //
				(Integer) _data.Get("dislike_index_4"), //
				(Integer) _data.Get("dislike_index_5") };
		this.dislike_index = dislike_index;
		this.ego = ((Byte) _data.Get("ego"));
		this.type = ((Byte) _data.Get("type"));
	}

	public TblCardBaseData getTblCardBaseData() {
		TblCardBaseData _data = new TblCardBaseData();
		_data.Set("id", this.id);
		_data.Set("cat_type", this.cat_type);
		_data.Set("sex", this.sex);
		_data.Set("assign_daimyo_index", this.assign_daimyo_index);
		_data.Set("native_map_index", this.native_map_index);
		_data.Set("job", this.job);
		_data.Set("growth", this.growth);
		_data.Set("father_index", this.father_index);
		_data.Set("mother_index", this.mother_index);
		_data.Set("marrige_index", this.marrige_index);
		_data.Set("affinity", this.affinity);
		_data.Set("mil_type", this.mil_type);
		_data.Set("like_index_1", this.like_index[0]);
		_data.Set("like_index_2", this.like_index[1]);
		_data.Set("like_index_3", this.like_index[2]);
		_data.Set("like_index_4", this.like_index[3]);
		_data.Set("like_index_5", this.like_index[4]);
		_data.Set("dislike_index_1", this.dislike_index[0]);
		_data.Set("dislike_index_2", this.dislike_index[1]);
		_data.Set("dislike_index_3", this.dislike_index[2]);
		_data.Set("dislike_index_4", this.dislike_index[3]);
		_data.Set("dislike_index_5", this.dislike_index[4]);
		_data.Set("ego", this.ego);
		_data.Set("type", this.type);
		return _data;
	}

	/**
	 * @return id
	 */
	public int getID() {
		return id;
	}

	/**
	 * @return cat_type
	 */
	public byte getCatType() {
		return cat_type;
	}

	/**
	 * @return sex
	 */
	public byte getSex() {
		return sex;
	}

	/**
	 * @return assign_daimyo_index
	 */
	public byte getAssignDaimyoIndex() {
		return assign_daimyo_index;
	}

	/**
	 * @return native_map_index
	 */
	public int getNativeMapIndex() {
		return native_map_index;
	}

	/**
	 * @return job
	 */
	public byte getJob() {
		return job;
	}

	/**
	 * @return growth
	 */
	public byte getGrowth() {
		return growth;
	}

	/**
	 * @return history
	 */
	public String getHistory() {
		return history;
	}

	/**
	 * @return father_index
	 */
	public int getFatherIndex() {
		return father_index;
	}

	/**
	 * @return mother_index
	 */
	public int getMotherIndex() {
		return mother_index;
	}

	/**
	 * @return marrige_index
	 */
	public int getMarrigeIndex() {
		return marrige_index;
	}

	/**
	 * @return affinity
	 */
	public byte getAffinity() {
		return affinity;
	}

	/**
	 * @return mil_type
	 */
	public byte getMilType() {
		return mil_type;
	}

	/**
	 * @return like_index
	 */
	public int[] getLikeIndex() {
		return like_index;
	}

	/**
	 *
	 * @param index
	 * @return like_index[index]
	 */
	public int getLikeIndex(int index) {
		return like_index[index];
	}

	/**
	 * @return dislike_index
	 */
	public int[] getDislikeIndex() {
		return dislike_index;
	}

	/**
	 *
	 * @param index
	 * @return dislike_index[index]
	 */
	public int getDislikeIndex(int index) {
		return dislike_index[index];
	}

	/**
	 * @return ego
	 */
	public byte getEgo() {
		return ego;
	}

	/**
	 * @return type
	 */
	public byte getType() {
		return type;
	}
}
