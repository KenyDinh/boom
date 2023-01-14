package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblSurveyUserAccessInfo;

public class SurveyUserAccessService {

	private SurveyUserAccessService() {
	}

	public static List<SurveyUserAccess> getSurveyUserAccessListAll(String option) {
		TblSurveyUserAccessInfo tblInfo = new TblSurveyUserAccessInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<SurveyUserAccess> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new SurveyUserAccess((TblSurveyUserAccessInfo) dao));
		}

		return ret;
	}

	public static List<SurveyUserAccess> getSurveyUserAccessListAll() {
		return getSurveyUserAccessListAll(null);
	}
}

