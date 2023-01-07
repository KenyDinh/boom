package dev.boom.services;

import dev.boom.tbl.data.TblCardData;

public class CardData {
	private TblCardData tblCardData;

	public CardData() {
		tblCardData = new TblCardData();
	}

	public CardData(TblCardData tblCardData) {
		this.tblCardData = tblCardData;
	}

	public TblCardData getTblCardData() {
		return this.tblCardData;
	}

	/**
	 * @return
	 */
	public int getId() {
		return (Integer) tblCardData.Get("id");
	}

	/**
	 * @return
	 */
	public int getCardBaseId() {
		return (Integer) tblCardData.Get("card_base_id");
	}

	/**
	 * @return
	 */
	public String getName() {
		return String.valueOf(tblCardData.Get("name"));
	}

	/**
	 * @return
	 */
	public String getCatName() {
		return String.valueOf(tblCardData.Get("cat_name"));
	}

	/**
	 * @return
	 */
	public String getMobileCatName() {
		return String.valueOf(tblCardData.Get("mobile_cat_name"));
	}

	/**
	 * @return
	 */
	public String getYomigana() {
		return String.valueOf(tblCardData.Get("yomigana"));
	}

	/**
	 * @return
	 */
	public byte getRank() {
		return (Byte) tblCardData.Get("rank");
	}

	public byte getRankStar() {
		return (Byte) tblCardData.Get("star");
	}

	public short getCatImageId() {
		return (Short) tblCardData.Get("image_id");
	}

	/**
	 * 初期スキル.
	 */
	public int getInitSkill() {
		return (Integer) tblCardData.Get("init_skill");
	}

	/**
	 * 入手レベル.
	 *
	 * @return
	 */
	public byte getRarity() {
		return (Byte) tblCardData.Get("rarity");
	}

	/**
	 * @return 投票出現カードかどうか.
	 */
	public boolean isVote() {
		return ((Byte) this.tblCardData.Get("vote_flag") != 0);
	}

	// used for manage tools card price: manage/pages/stastatics/DailyCardPrice.java
	public boolean isTradeable() {
		return ((Byte) tblCardData.Get("trade_limit") == 0);
	}

	public byte getProperty() {
		return (Byte) tblCardData.Get("property");
	}

	public byte getCost() {
		return (Byte) tblCardData.Get("cost");
	}

	public String getPersonality() {
		return (String) tblCardData.Get("personality");
	}

	public String getSlogan() {
		return (String) tblCardData.Get("slogan");
	}

	public String getHistory() {
		return (String) tblCardData.Get("history");
	}

	public int getSkillIndex(int i) {
		return (Integer) tblCardData.Get("skill_index_" + i);
	}

	public short getDispNum() {
		return (Short) tblCardData.Get("num");
	}

}
