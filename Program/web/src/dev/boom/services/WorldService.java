package dev.boom.services;

import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblWorldInfo;

public class WorldService {

	public static WorldInfo getWorldInfo() {
		TblWorldInfo info = new TblWorldInfo();
		info.setId(1);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new WorldInfo((TblWorldInfo) list.get(0));
	}
}
