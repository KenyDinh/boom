package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblWorldInfo;

public class WorldService {

	private WorldService() {
	}

	public static List<World> getWorldListAll(String option) {
		TblWorldInfo tblInfo = new TblWorldInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<World> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new World((TblWorldInfo) dao));
		}

		return ret;
	}

	public static List<World> getWorldListAll() {
		return getWorldListAll(null);
	}
	
	public static World getWorldInfo() {
		TblWorldInfo info = new TblWorldInfo();
		info.Set("id", 1);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new World((TblWorldInfo) list.get(0));
	}
	
}

