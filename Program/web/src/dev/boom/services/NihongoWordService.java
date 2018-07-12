package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.NihongoWordInfo;

public class NihongoWordService {

	public static List<NihongoWordInfo> getWordList() {
		NihongoWordInfo info = new NihongoWordInfo();

		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<NihongoWordInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((NihongoWordInfo)dao);
		}
		
		return ret;
	}
}
