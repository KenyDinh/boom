package dev.boom.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dev.boom.dao.DaoValue;
import dev.boom.dao.fix.FixData;
import dev.boom.dao.fix.FixDataBase;
import dev.boom.tbl.data.TblSkillBaseData;
import dev.boom.tbl.data.TblSkillData;

public class SkillService {

	public static List<Skill> getSkillByCardInfo(Card card) {
		List<Skill> retValue = new ArrayList<Skill>();

		return retValue;
	}

	public static Skill getSkillBySkillID(int nSkillID) {
		TblSkillData tblSkillData = getSkillData(nSkillID);
		if (tblSkillData == null) {
			return null;
		}
		TblSkillBaseData tblSkillBaseData = getSkillBaseData(nSkillID);
		if (tblSkillBaseData == null) {
			return null;
		}
		Skill skill = new Skill(tblSkillData, tblSkillBaseData);
		return skill;
	}

	static public TblSkillData getSkillData(int skillID) {
		FixData fixData = (FixData) FixDataBase.getInstance("SkillData");
		Map<Integer, DaoValue> mapSkillData = fixData.getData();
		return (TblSkillData) mapSkillData.get(skillID);
	}

	static public TblSkillBaseData getSkillBaseData(int skillID) {
		int skillBaseID = getSkillBaseID(skillID);
		if (skillBaseID == 0) {
			return null;
		}
		FixData fixData = (FixData) FixDataBase.getInstance("SkillBaseData");
		Map<Integer, DaoValue> sbData = fixData.getData();
		return (TblSkillBaseData) sbData.get(skillBaseID);
	}

	static public int getSkillBaseID(int skillID) {
		TblSkillData tblSkillData = getSkillData(skillID);
		if (tblSkillData == null) {
			return 0;
		}
		return (Integer) tblSkillData.Get("skill_base_id");
	}

	static public List<Skill> getSkillList(Set<Integer> setSkillId) {
		List<Skill> res = new ArrayList<Skill>();
		for (int id : setSkillId) {
			if (id > 0) {
				Skill skill = getSkillBySkillID(id);
				if (skill != null) {
					res.add(skill);
				}
			}
		}
		return res;
	}
	
	public static List<Integer> getSkillIdsByName(String sSkill) {
		if (sSkill == null || sSkill.isEmpty()) {
			return null;
		}
		String[] arrSkill = sSkill.split(",|\\s+");
		FixData fixData = (FixData) FixDataBase.getInstance("SkillBaseData");
		Map<Integer, DaoValue> sbData = fixData.getData();
		Set<Integer> baseIDs = new HashSet<>();
		for (Integer id : sbData.keySet()) {
			DaoValue dao = sbData.get(id);
			String name = (String) dao.Get("name");
			for (String skillName : arrSkill) {
				skillName = skillName.trim();
				if (skillName.length() > 0 && name.contains(skillName)) {
					baseIDs.add(id);
				}
			}
		}
		if (baseIDs.isEmpty()) {
			return null;
		}
		List<Integer> ret = new ArrayList<>();
		FixData fixSkillData = (FixData) FixDataBase.getInstance("SkillData");
		Map<Integer, DaoValue> mapSkillData = fixSkillData.getData();
		for (Integer sID : mapSkillData.keySet()) {
			int baseID = (Integer)mapSkillData.get(sID).Get("skill_base_id");
			if (baseIDs.contains(baseID)) {
				ret.add(sID);
			}
		}
		return ret;
	}
}
