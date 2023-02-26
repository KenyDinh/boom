package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblBoomGroupInfo;

public class BoomGroupService {

	private BoomGroupService() {
	}

	public static List<BoomGroup> getBoomGroupListAll(String option) {
		TblBoomGroupInfo tblInfo = new TblBoomGroupInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<BoomGroup> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new BoomGroup((TblBoomGroupInfo) dao));
		}

		return ret;
	}

	public static List<BoomGroup> getBoomGroupListAll() {
		return getBoomGroupListAll(null);
	}
	
	public static BoomGroup getBoomGroup(long id) {
		TblBoomGroupInfo tblInfo = new TblBoomGroupInfo();
		tblInfo.Set("id", id);

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		return new BoomGroup((TblBoomGroupInfo) list.get(0));
	}
}

