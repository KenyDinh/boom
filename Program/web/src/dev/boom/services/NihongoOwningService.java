package dev.boom.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.boom.common.CommonMethod;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblNihongoOwningInfo;
import dev.boom.tbl.info.TblNihongoPetInfo;
import dev.boom.tbl.info.TblNihongoUserInfo;

public class NihongoOwningService {

	private NihongoOwningService() {
	}
	
	public static boolean levelUpOwning(long id, long user_id) {
		TblNihongoOwningInfo owningInfo = getNihongoOwning(id);
		if (owningInfo == null) {
			return false;
		}
		if ((Long)owningInfo.Get("user_id") != user_id) {
			return false;
		}

		Map<Long, TblNihongoPetInfo> petMap = NihongoPetService.getPetMap();
		long petID = (Long)owningInfo.Get("pet_id");
		if (petMap.containsKey(petID)) {
			int maxLevel = (Integer)petMap.get(petID).Get("max_level");
			if ((Integer)owningInfo.Get("current_level") >= maxLevel) {
				return false;
			}
		}
		TblNihongoUserInfo nihonUser = NihongoUserService.getNihongoUserInfo(user_id);
		if ((Integer)nihonUser.Get("star") <= 0) {
			return false;
		}
		owningInfo.Set("current_level", (Integer)owningInfo.Get("current_level") + 1);
		owningInfo.Set("updated", CommonMethod.getFormatStringNow());
		nihonUser.Set("star", (Integer)nihonUser.Get("star") - 1);
		List<DaoValue> updateList = new ArrayList<DaoValue>();
		updateList.add(owningInfo);
		updateList.add(nihonUser);

		return (CommonDaoFactory.Update(updateList) > 0);
	}

	public static boolean insertOwning(long pet_id, long user_id) {
		TblNihongoOwningInfo info = new TblNihongoOwningInfo();
		info.Set("pet_id", pet_id);
		info.Set("user_id", user_id);
		info.Set("current_level", 1);
		return (CommonDaoFactory.Insert(info) > 0);
	}

	public static boolean buyOwning(long pet_id, long user_id) {
		TblNihongoUserInfo nihonUser = NihongoUserService.getNihongoUserInfo(user_id);
		if (nihonUser == null || (Integer)nihonUser.Get("star") <= 0) {
			return false;
		}
		List<DaoValue> updateList = new ArrayList<DaoValue>();
		TblNihongoOwningInfo info = new TblNihongoOwningInfo();
		info.Set("pet_id", pet_id);
		info.Set("user_id", user_id);
		info.Set("current_level", 1);
		updateList.add(info);
		nihonUser.Set("star", (Integer)nihonUser.Get("star") - 1);
		updateList.add(nihonUser);

		return (CommonDaoFactory.Update(updateList) > 0);
	}

	public static TblNihongoOwningInfo getNihongoOwning(long id) {
		TblNihongoOwningInfo info = new TblNihongoOwningInfo();
		info.Set("id", id);

		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() != 1) {
			return null;
		}

		return (TblNihongoOwningInfo) list.get(0);
	}

	public static List<TblNihongoOwningInfo> getOwningList(long user_id) {
		TblNihongoOwningInfo info = new TblNihongoOwningInfo();
		info.Set("user_id", user_id);

		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() == 0) {
			return null;
		}
		List<TblNihongoOwningInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblNihongoOwningInfo) dao);
		}

		return ret;
	}
	
}

