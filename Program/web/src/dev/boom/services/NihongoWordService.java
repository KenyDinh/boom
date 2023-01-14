package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblNihongoWordInfo;

public class NihongoWordService {

	private NihongoWordService() {
	}

	public static List<NihongoWord> getNihongoWordListAll(String option) {
		TblNihongoWordInfo tblInfo = new TblNihongoWordInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<NihongoWord> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new NihongoWord((TblNihongoWordInfo) dao));
		}

		return ret;
	}

	public static List<NihongoWord> getNihongoWordListAll() {
		return getNihongoWordListAll(null);
	}
	
	public static List<TblNihongoWordInfo> getWordList() {
		TblNihongoWordInfo info = new TblNihongoWordInfo();

		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<TblNihongoWordInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblNihongoWordInfo)dao);
		}
		
		return ret;
	}
	
}

