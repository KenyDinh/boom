package dev.boom.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.NihongoOwningInfo;
import dev.boom.entity.info.NihongoPetInfo;
import dev.boom.entity.info.NihongoUserInfo;

public class NihongoOwningService {

	public static boolean levelUpOwning(long id, long user_id) {
		NihongoOwningInfo owningInfo = getNihongoOwning(id);
		if (owningInfo == null) {
			return false;
		}
		if (owningInfo.getUser_id() != user_id) {
			return false;
		}

		Map<Long, NihongoPetInfo> petMap = NihongoPetService.getPetMap();
		if (petMap.containsKey(owningInfo.getPet_id())) {
			int maxLevel = petMap.get(owningInfo.getPet_id()).getMax_level();
			if (owningInfo.getCurrent_level() >= maxLevel) {
				return false;
			}
		}
		NihongoUserInfo nihonUser = NihongoUserService.getNihongoUserInfo(user_id);
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
		NihongoOwningInfo info = new NihongoOwningInfo();
		info.setPet_id(pet_id);
		info.setUser_id(user_id);
		info.setCurrent_level(1);
		Long id = (Long) CommonDaoService.insert(info);
		return (id != null && id > 0);
	}

	public static boolean buyOwning(long pet_id, long user_id) {
		NihongoUserInfo nihonUser = NihongoUserService.getNihongoUserInfo(user_id);
		if (nihonUser == null || nihonUser.getStar() <= 0) {
			return false;
		}
		List<DaoValue> updateList = new ArrayList<DaoValue>();
		NihongoOwningInfo info = new NihongoOwningInfo();
		info.setPet_id(pet_id);
		info.setUser_id(user_id);
		info.setCurrent_level(1);
		updateList.add(info);
		nihonUser.setStar(nihonUser.getStar() - 1);
		updateList.add(nihonUser);

		return CommonDaoService.update(updateList);
	}

	public static NihongoOwningInfo getNihongoOwning(long id) {
		NihongoOwningInfo info = new NihongoOwningInfo();
		info.setId(id);

		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}

		return (NihongoOwningInfo) list.get(0);
	}

	public static List<NihongoOwningInfo> getOwningList(long user_id) {
		NihongoOwningInfo info = new NihongoOwningInfo();
		info.setUser_id(user_id);

		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() == 0) {
			return null;
		}
		List<NihongoOwningInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((NihongoOwningInfo) dao);
		}

		return ret;
	}

}
