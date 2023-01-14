package dev.boom.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.boom.tbl.data.TblCardBaseData;
import dev.boom.tbl.data.TblCardData;

public class Card {
	
	private TblCardData tblCardData;
	private TblCardBaseData tblCardBaseData;

	public enum CardRank {
		INVALID(0), 
		COMMON(1), 
		UNCOMMON(2), 
		RARE(3), 
		SUPERRARE(4), 
		TREASURE(5), 
		HONOR(6), 
		ULTRARARE(7),;

		private int rank;

		public int getRank() {
			return this.rank;
		}
		
		public String getImgName() {
			return "rare_0" + getRank() + ".png";
		}
		
		public int getMask() {
			return (1 << (getRank() - 1));
		}
		
		public boolean isValidFlag(int flag) {
			if (this.getRank() == CardRank.INVALID.getRank()) {
				return false;
			}
			return (flag & getMask()) != 0;
		}

		private CardRank(int rank) {
			this.rank = rank;
		}
		
		public static CardRank valueOf(int rank) {
			switch (rank) {
			case 1:
				return COMMON;
			case 2:
				return UNCOMMON;
			case 3:
				return RARE;
			case 4:
				return SUPERRARE;
			case 5:
				return TREASURE;
			case 6:
				return HONOR;
			case 7:
				return SUPERRARE;
			default:
				return INVALID;
			}
		}
	}

	public enum CardElement {
		NONE(-1), 
		FIRE(0), 
		EARTH(1), 
		WIND(2), 
		WATER(3), 
		SKY(4),;

		private int id;

		public int getId() {
			return id;
		}
		
		public int getMask() {
			return (1 << getId());
		}
		
		public boolean isValidFlag(int flag) {
			return (flag & getMask()) != 0;
		}

		private CardElement(int id) {
			this.id = id;
		}
		
		public String getImgName() {
			return "param_0" + getId() + ".png";
		}
	}

	public enum JobType {
		JOB1(0, "MSG_GENERAL_OCCUPATION_SAMURAI"), // 豁ｦ螢ｫ.
		JOB2(1, "MSG_GENERAL_OCCUPATION_STRATEGIST"), // 霆榊ｸｫ.
		JOB3(2, "MSG_GENERAL_OCCUPATION_MONK"), // 蜒ｧ萓ｶ.
		JOB4(3, "MSG_GENERAL_OCCUPATION_NINJA"), // 蠢崎��.
		JOB5(4, "MSG_GENERAL_OCCUPATION_SWORDSMAN"), // 蜑｣雎ｪ.
		JOB6(5, "MSG_GENERAL_OCCUPATION_AVANTGARDE"), // 蛯ｾ螂�.
		JOB7(6, "MSG_GENERAL_OCCUPATION_PRINCESS"), // 蟋ｫ.
		JOB8(7, "MSG_GENERAL_OCCUPATION_TEA_CEREMONY"), // 闌ｶ莠ｺ.
		JOB9(8, "MSG_GENERAL_OCCUPATION_MERCHANT"), // 蝠�莠ｺ.
		JOB10(9, "MSG_GENERAL_OCCUPATION_PIRATE"), // 豌ｴ霆�.
		;

		private final int job_id;

		private final String label;

		private JobType(int job_id, String label) {
			this.job_id = job_id;
			this.label = label;
		}

		public int getJobId() {
			return this.job_id;
		}

		public String getLabel() {
			return this.label;
		}

		public int getMask() {
			return (1 << getJobId());
		}
		
		public boolean isValidFlag(int flag) {
			return (flag & getMask()) != 0;
		}

		/**
		 * @param job
		 * @return
		 */
		public static JobType valueOf(int job) {
			for (JobType e : JobType.values()) {
				if (e.getJobId() == job) {
					return e;
				}
			}
			return JOB1;
		}
	}

	public enum CardCost {
		NONE(0, "0.0"),
		COST1(2, "1.0"),
		COST2(3, "1.5"),
		COST3(4, "2.0"),
		COST4(5, "2.5"),
		COST5(6, "3.0"),
		COST6(8, "4.0");
		
		private int cost;
		private String name;
		
		private CardCost(int cost, String name) {
			this.cost = cost;
			this.name = name;
		}

		public int getCost() {
			return cost;
		}

		public String getName() {
			return name;
		}
		
		public int getMask() {
			return (1 << (getCost() - 1));
		}
		
		public boolean isValidFlag(int flag) {
			return (flag & getMask()) != 0;
		}
		
		public static CardCost valueOf(int cost) {
			for (CardCost cardCost : CardCost.values()) {
				if (cardCost.getCost() == cost) {
					return cardCost;
				}
			}
			return CardCost.NONE;
		}
	}
	
	public enum CardMilType {
		SPEAR(0),
		HORSE(1),
		GUN(2);
		
		private int id;
		
		private CardMilType(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public String getImgName() {
			return "class_" + getId() + ".png";
		}
		
		public int getMask() {
			return (1 << getId());
		}
		
		public boolean isValidFlag(int flag) {
			return (flag & getMask()) != 0;
		}
		
	}
	
	public Card() {
	}

	public Card(TblCardData tblCardData, TblCardBaseData tblCardBaseData) {
		this.tblCardData = tblCardData;
		this.tblCardBaseData = tblCardBaseData;
	}
	
	public TblCardData getTblCardData() {
		return tblCardData;
	}

	public TblCardBaseData getTblCardBaseData() {
		return tblCardBaseData;
	}

	public void setTblCardData(TblCardData p) {
		tblCardData = p;
	}

	public void setTblCardBaseData(TblCardBaseData p) {
		tblCardBaseData = p;
	}
	
	public int getCardID() {
		return (Integer) tblCardData.Get("id");
	}

	public int getCardBaseID() {
		return (Integer) tblCardData.Get("card_base_id");
	}

	/**
	 * @return 繧ｫ繝ｼ繝芽｡ｨ遉ｺ逡ｪ蜿ｷ繧貞叙蠕�.
	 */
	public short getCardDispNo() {
		return (Short) tblCardData.Get("num");
	}

	public String getImgSrc(String context) {
		return context + "/img/nyaga_card/still/" + String.format("character_still_%05d.png", tblCardData.Get("image_id"));
	}

	public String getElementImg(String context) {
		return getElementImg(context, getElement());
	}

	public String getElementImg(String context, int element) {
		return context + "/img/nyaga_card/icon/elements_0" + element + ".png";
	}

	public String getRarityImg(String context) {
		byte star = (Byte) tblCardData.Get("star");
		if (star >  0) {
			return String.format("%s/img/nyaga_card/icon/rare_0%d_star0%d.png", context, getRank(), star);
		}
		return String.format("%s/img/nyaga_card/icon/rare_0%d.png", context, getRank());
	}

	public String getMilTypeImg(String context) {
		return context + "/img/nyaga_card/icon/class_" + getMilType() + ".png";
	}

	public byte getElement() {
		return (Byte) tblCardData.Get("property");
	}

	public byte getCost() {
		return (Byte) tblCardData.Get("cost");
	}

	public byte getRank() {
		return (Byte) tblCardData.Get("rank");
	}

	public String getName() {
		return tblCardData.Get("name").toString();
	}

	public String getCatName() {
		return tblCardData.Get("cat_name").toString();
	}

	public byte getRarity() {
		return (Byte) tblCardData.Get("rarity");
	}

	public byte getJob() {
		return (Byte) tblCardBaseData.Get("job");
	}

	public byte getMilitary() {
		return (Byte) tblCardBaseData.Get("mil_type");
	}

	public byte getSex() {
		return (Byte) tblCardBaseData.Get("sex");
	}

	public byte getAssignDaimyo() {
		return (Byte) tblCardBaseData.Get("assign_daimyo_index");
	}

	public int getNativeMap() {
		return (Integer) tblCardBaseData.Get("native_map_index");
	}

	public int getFather() {
		return (Integer) tblCardBaseData.Get("father_index");
	}

	public int getMother() {
		return (Integer) tblCardBaseData.Get("mother_index");
	}

	public int getMaritalPartner() {
		return (Integer) tblCardBaseData.Get("marrige_index");
	}

	public byte getMilType() {
		return (Byte) tblCardBaseData.Get("mil_type");
	}

	public List<Skill> getSkillList() {
		Set<Integer> skillIdSet = new HashSet<Integer>();
		for (int i = 1; i <= 10; i++) {
			skillIdSet.add((Integer)tblCardData.Get(String.format("skill_index_%d", i)));
		}
		skillIdSet.add((Integer)tblCardData.Get("init_skill"));
		return SkillService.getSkillList(skillIdSet);
	}

	public String getStringSkillName(String context) {
		List<Skill> skillList = getSkillList();
		StringBuilder sb = new StringBuilder();
		for (Skill skill : skillList) {
			if (skill != null) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append("<span>");
				sb.append("<img src='"+ getElementImg(context, skill.getProperty() - 1) +"'/>");
				sb.append(skill.getName());
				sb.append("</span>");
			}
		}
		return sb.toString();
	}
	
	public boolean isContainSkillId(List<Integer> skillID) {
		if (skillID == null || skillID.isEmpty()) {
			return true;
		}
		for (int i = 1; i <= 10; i++) {
			if (skillID.contains((Integer)tblCardData.Get(String.format("skill_index_%d", i)))) {
				return true;
			}
		}
		if (skillID.contains((Integer)tblCardData.Get("init_skill"))) {
			return true;
		}
		
		return false;
	}
	
	public byte getProperty() {
		return (Byte) tblCardData.Get("property");
	}
	
	public int getPropAcquirementRate() {
		return 0;
	}
	
	public int getCardIDFromBase() {
		return (Integer) tblCardBaseData.Get("id");
	}
	
	public short getImgID() {
		return (Short) tblCardData.Get("image_id");
	}
	
	public String getFaceImageMain() {
		String filename = String.format("character_still_%05d.png", getImgID());
		return String.format("/img/card/still/%s", filename);
	}

	public String getMobileFaceImage() {
		String filename = String.format("character_still_%05d.png", getImgID());
		return String.format("/img/card/still/%s", filename);
	}

	public String getSpSmallFaceImage() {
		String filename = String.format("character_still_%05d.jpg", getImgID());
		return String.format("/img/Flash/load/large_still/%s", filename);
	}
	
	public String getIllustImg(String contextPath) {
		String filename = String.format("card_chara_%05d.png", getImgID());
		return String.format("%s/img/card/illustration/%s", contextPath, filename);
	}
	
	public String getMobileCatName() {
		return tblCardData.Get("mobile_cat_name").toString();
	}

	public String getCatName(boolean mbFlag) {
		assert (false);
		return getMobileCatName();
	}
	
	public short getCardInitMaxHP() {
		return (Short) tblCardData.Get("hp");
	}
	
	@SuppressWarnings("rawtypes")
	public static String getJobFromID(Map messages, int job) {

		String strLabel = "MSG_TEAM_JOB_SAMURAI";
		if (job - 1 >= 0 && job - 1 <= JobType.values().length) {
			strLabel = JobType.valueOf(job - 1).getLabel();
		}
		return (String) messages.get(strLabel);
	}
	
}
