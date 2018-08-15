package dev.boom.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblNihongoOwningInfo;
import dev.boom.tbl.info.TblNihongoPetInfo;
import dev.boom.tbl.info.TblNihongoUserInfo;

public class NihongoOwningService {

	public static boolean levelUpOwning(long id, long user_id) {
		TblNihongoOwningInfo owningInfo = getNihongoOwning(id);
		if (owningInfo == null) {
			return false;
		}
		if (owningInfo.getUser_id() != user_id) {
			return false;
		}

		Map<Long, TblNihongoPetInfo> petMap = NihongoPetService.getPetMap();
		if (petMap.containsKey(owningInfo.getPet_id())) {
			int maxLevel = petMap.get(owningInfo.getPet_id()).getMax_level();
			if (owningInfo.getCurrent_level() >= maxLevel) {
				return false;
			}
		}
		TblNihongoUserInfo nihonUser = NihongoUserService.getNihongoUserInfo(user_id);
		if (nihonUser.getStar() <= 0) {
			return false;
		}
		owningInfo.setCurrent_level(owningInfo.getCurrent_level() + 1);
		owningInfo.setUpdated(new Date());
		nihonUser.setStar(nihonUser.getStar() - 1);
		List<DaoValue> updateList = new ArrayList<DaoValue>();
		updateList.add(owningInfo);
		updateList.add(nihonUser);

		return CommonDaoService.update(updateList);
	}

	public static boolean insertOwning(long pet_id, long user_id) {
		TblNihongoOwningInfo info = new TblNihongoOwningInfo();
		info.setPet_id(pet_id);
		info.setUser_id(user_id);
		info.setCurrent_level(1);
		Long id = (Long) CommonDaoService.insert(info);
		return (id != null && id > 0);
	}

	public static boolean buyOwning(long pet_id, long user_id) {
		TblNihongoUserInfo nihonUser = NihongoUserService.getNihongoUserInfo(user_id);
		if (nihonUser == null || nihonUser.getStar() <= 0) {
			return false;
		}
		List<DaoValue> updateList = new ArrayList<DaoValue>();
		TblNihongoOwningInfo info = new TblNihongoOwningInfo();
		info.setPet_id(pet_id);
		info.setUser_id(user_id);
		info.setCurrent_level(1);
		updateList.add(info);
		nihonUser.setStar(nihonUser.getStar() - 1);
		updateList.add(nihonUser);

		return CommonDaoService.update(updateList);
	}

	public static TblNihongoOwningInfo getNihongoOwning(long id) {
		TblNihongoOwningInfo info = new TblNihongoOwningInfo();
		info.setId(id);

		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}

		return (TblNihongoOwningInfo) list.get(0);
	}

	public static List<TblNihongoOwningInfo> getOwningList(long user_id) {
		TblNihongoOwningInfo info = new TblNihongoOwningInfo();
		info.setUser_id(user_id);

		List<DaoValue> list = CommonDaoService.select(info);
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
