package dev.boom.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.dao.fix.FixData;
import dev.boom.dao.fix.FixDataBase;
import dev.boom.tbl.data.TblCardBaseData;
import dev.boom.tbl.data.TblCardData;

public class CardService {

	public static final Card EMPTY_CARD = new Card();
	
	public static List<Card> getCardListAll() {
		List<Card> cardList = new ArrayList<Card>();
		TblCardData _value = new TblCardData();
		List<DaoValue> result = CommonDaoFactory.Select(_value);
		if (result != null && !result.isEmpty()) {
			FixData fixData = (FixData) FixDataBase.getInstance("CardBaseData");
			Map<Integer, DaoValue> cardBaseData = fixData.getData();
			for (DaoValue daoValue : result) {
				CardData cardData = new CardData((TblCardData) daoValue);
				if (cardData != null) {
					TblCardBaseData pTblCardBaseData = (TblCardBaseData) cardBaseData.get(cardData.getCardBaseId());
					if (pTblCardBaseData != null) {
						cardList.add(new Card(cardData.getTblCardData(), pTblCardBaseData));
					}
				}
			}
		}
		return cardList;
	}
	
	public static List<CardData> getCardDataList() {
		TblCardData _value = new TblCardData();
		List<DaoValue> result = CommonDaoFactory.Select(_value);
		List<CardData> cardList = new ArrayList<CardData>();
		if (result != null && !result.isEmpty()) {
			for (DaoValue daoValue : result) {
				CardData cardData = new CardData((TblCardData) daoValue);
				if (cardData != null) {
					cardList.add(cardData);
				}
			}
		}
		return cardList;
	}

	public static List<Card> getCardList(int id, short dispNum, String sName, int elemFlag, int rankFlag, int startFlag, int costFlag, int jobFlag, int miltypeFlag, String sSkill) {
		TblCardData _value = new TblCardData();
		if (sName != null && !sName.isEmpty()) {
			_value.Set("cat_name", sName);;
		}
		if (dispNum > 0) {
			_value.Set("num", dispNum);
		}
		if (id > 0) {
			_value.Set("id", id);
		}
		List<Integer> skillID = null;
		if (sSkill != null && !sSkill.isEmpty()) {
			skillID = SkillService.getSkillIdsByName(sSkill);
			if (skillID == null || skillID.isEmpty()) {
				return Collections.emptyList();
			}
		}
		List<DaoValue> result = CommonDaoFactory.Select(_value);
		if (result == null || result.isEmpty()) {
			return Collections.emptyList();
		}
		List<Card> ret = new ArrayList<>();
		FixData fixData = (FixData) FixDataBase.getInstance("CardBaseData");
		Map<Integer, DaoValue> cardBaseData = fixData.getData();
		for (DaoValue daoValue : result) {
			CardData cardData = new CardData((TblCardData) daoValue);
			if (cardData != null) {
				TblCardBaseData pTblCardBaseData = (TblCardBaseData) cardBaseData.get(cardData.getCardBaseId());
				if (pTblCardBaseData == null) {
					continue;
				}
				if (elemFlag > 0 && (elemFlag & (1 << cardData.getProperty())) == 0) {
					continue;
				}
				if (rankFlag > 0 && (rankFlag & (1 << (cardData.getRank() - 1))) == 0) {
					continue;
				}
				if (startFlag > 0 && (startFlag & (1 << (cardData.getRankStar() - 1))) == 0) {
					continue;
				}
				if (costFlag > 0 && (costFlag & ( 1 << (cardData.getCost() - 1))) == 0) {
					continue;
				}
				if (jobFlag > 0 && (jobFlag & (1 << (Integer)pTblCardBaseData.Get("job"))) == 0) {
					continue;
				}
				if (miltypeFlag > 0 && (miltypeFlag & (1 << (Integer)pTblCardBaseData.Get("mil_type"))) == 0) {
					continue;
				}
				Card card = new Card(cardData.getTblCardData(), pTblCardBaseData);
				if (!card.isContainSkillId(skillID)) {
					continue;
				}
				ret.add(card);
			}
		}
		return ret;
	}

	public static String getCardImg(String contextPath, int index) {
		TblCardData tblCardData = CardService.getTblCardData(index);

		if (tblCardData == null) {
			return "";
		}

		int image_id = (Short) tblCardData.Get("image_id");
		String still = String.format("character_still_%05d.png", image_id);

		return contextPath + "/img/nyaga_card/" + still;
	}

	public static TblCardData getTblCardData(int id) {
		FixData fixData = (FixData) FixDataBase.getInstance("CardData");
		Map<Integer, DaoValue> carddata = fixData.getData();

		return (TblCardData) carddata.get(id);
	}
	
	public static TblCardBaseData getTblCardBaseData(int id) {
		FixData fixData = (FixData) FixDataBase.getInstance("CardBaseData");
		Map<Integer, DaoValue> cardbasedata = fixData.getData();

		return (TblCardBaseData) cardbasedata.get(id);
	}
	
}
