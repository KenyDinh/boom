package dev.boom.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblNihongoPetInfo;

public class NihongoPetService {

	public static Map<Long, TblNihongoPetInfo> getPetMap() {
		Map<Long, TblNihongoPetInfo> ret = new HashMap<Long, TblNihongoPetInfo>();
		TblNihongoPetInfo info = new TblNihongoPetInfo();
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return ret;
		}
		for (Iterator<DaoValue> it = list.iterator(); it.hasNext();) {
			TblNihongoPetInfo _value = (TblNihongoPetInfo) it.next();
			ret.put(_value.getId(), _value);
		}

		return ret;
	}
	
	public static Map<Long, Map<String, Object>> getPetMapObject() {
		Map<Long, Map<String, Object>> ret = new HashMap<>();
		TblNihongoPetInfo info = new TblNihongoPetInfo();
		
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return ret;
		}
		for (Iterator<DaoValue> it = list.iterator(); it.hasNext();) {
			TblNihongoPetInfo _value = (TblNihongoPetInfo) it.next();
			ret.put(_value.getId(), _value.toMapObject());
		}

		return ret;
	}
}
