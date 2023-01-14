package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblSurveyResultInfo;

public class SurveyResultService {

	private SurveyResultService() {
	}

	public static List<SurveyResult> getSurveyResultListAll(String option) {
		TblSurveyResultInfo tblInfo = new TblSurveyResultInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<SurveyResult> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyResult((TblSurveyResultInfo) dao));
		}

		return ret;
	}

	public static List<SurveyResult> getSurveyResultListAll() {
		return getSurveyResultListAll(null);
	}
}

