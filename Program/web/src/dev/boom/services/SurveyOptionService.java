package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblSurveyOptionInfo;

public class SurveyOptionService {

	private SurveyOptionService() {
	}

	public static List<SurveyOption> getSurveyOptionListAll(String option) {
		TblSurveyOptionInfo tblInfo = new TblSurveyOptionInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<SurveyOption> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyOption((TblSurveyOptionInfo) dao));
		}

		return ret;
	}

	public static List<SurveyOption> getSurveyOptionListAll() {
		return getSurveyOptionListAll(null);
	}
}

