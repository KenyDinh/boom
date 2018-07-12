package dev.boom.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.NihongoPetInfo;

public class NihongoPetService {

	public static Map<Long, NihongoPetInfo> getPetMap() {
		Map<Long, NihongoPetInfo> ret = new HashMap<Long, NihongoPetInfo>();
		NihongoPetInfo info = new NihongoPetInfo();
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return ret;
		}
		for (Iterator<DaoValue> it = list.iterator(); it.hasNext();) {
			NihongoPetInfo _value = (NihongoPetInfo) it.next();
			ret.put(_value.getId(), _value);
		}

		return ret;
	}
	
	public static Map<Long, Map<String, Object>> getPetMapObject() {
		Map<Long, Map<String, Object>> ret = new HashMap<>();
		NihongoPetInfo info = new NihongoPetInfo();
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return ret;
		}
		for (Iterator<DaoValue> it = list.iterator(); it.hasNext();) {
			NihongoPetInfo _value = (NihongoPetInfo) it.next();
			ret.put(_value.getId(), _value.toMapObject());
		}

		return ret;
	}
}
