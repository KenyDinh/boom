package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblNihongoWordInfo;

public class NihongoWordService {

	public static List<TblNihongoWordInfo> getWordList() {
		TblNihongoWordInfo info = new TblNihongoWordInfo();

		List<DaoValue> list = CommonDaoService.select(info);
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
