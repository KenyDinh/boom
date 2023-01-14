package dev.boom.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblNihongoPetInfo;

public class NihongoPetService {

	private NihongoPetService() {
	}

	public static Map<Long, TblNihongoPetInfo> getPetMap() {
		Map<Long, TblNihongoPetInfo> ret = new HashMap<Long, TblNihongoPetInfo>();
		TblNihongoPetInfo info = new TblNihongoPetInfo();
		
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return ret;
		}
		for (Iterator<DaoValue> it = list.iterator(); it.hasNext();) {
			TblNihongoPetInfo _value = (TblNihongoPetInfo) it.next();
			ret.put((Long)_value.Get("id"), _value);
		}

		return ret;
	}
	
	public static Map<Long, Map<String, Object>> getPetMapObject() {
		Map<Long, Map<String, Object>> ret = new HashMap<>();
		TblNihongoPetInfo info = new TblNihongoPetInfo();
		
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return ret;
		}
		for (Iterator<DaoValue> it = list.iterator(); it.hasNext();) {
			TblNihongoPetInfo _value = (TblNihongoPetInfo) it.next();
			ret.put((Long)_value.Get("id"), _value.toMapObject());
		}

		return ret;
	}
	
}

