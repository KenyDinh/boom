package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblSurveyQuestionInfo;

public class SurveyQuestionService {

	private SurveyQuestionService() {
	}

	public static List<SurveyQuestion> getSurveyQuestionListAll(String option) {
		TblSurveyQuestionInfo tblInfo = new TblSurveyQuestionInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<SurveyQuestion> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyQuestion((TblSurveyQuestionInfo) dao));
		}

		return ret;
	}

	public static List<SurveyQuestion> getSurveyQuestionListAll() {
		return getSurveyQuestionListAll(null);
	}
}

