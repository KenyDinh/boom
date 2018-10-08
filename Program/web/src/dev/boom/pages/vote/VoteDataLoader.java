package dev.boom.pages.vote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.common.CommonMethod;
import dev.boom.core.GameLog;
import dev.boom.core.SurveySession;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.SurveyInfo;
import dev.boom.services.SurveyOptionInfo;
import dev.boom.services.SurveyResultInfo;
import dev.boom.services.SurveyService;
import dev.boom.services.SurveyValidCodeData;

public class VoteDataLoader extends JsonPageBase {

	private static final long serialVersionUID = 1L;
	private static final String SURVEY_SESSION = "servey_session";
	
	private SurveySession surveySession = null;
	private SurveyValidCodeData data = null;
	private SurveyInfo survey = null;
	
	public VoteDataLoader() {
	}
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!getContext().isAjaxRequest()) {
			return false;
		}
		if (!getContext().isPost()) {
			return false;
		}
		surveySession = (SurveySession) getContext().getSessionAttribute(SURVEY_SESSION);
		if (surveySession == null) {
			GameLog.getInstance().error("[ManageVote] survey session is null!");
			return false;
		}
		data = SurveyService.getSurveyValidData(surveySession.getCode());
		if (data == null || (!data.isEditable() && !data.isReadonly())) {
			GameLog.getInstance().error("[ManageVote] survey valid data is null or not allowed to access!");
			return false;
		}
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
		String strSurveyId = getContext().getRequestParameter("survey_id");
		if (CommonMethod.isValidNumeric(strSurveyId, 1, Long.MAX_VALUE)) {
			survey = SurveyService.getSurveyById(Long.parseLong(strSurveyId));
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		if (survey == null) {
			return;
		}
		String strMode = getContext().getRequestParameter("mode");
		if (strMode != null && strMode.equals("result_detail")) {
			getResultDetail();
		} else {
			getStatisticData();
		}
	}

	@Override
	public void onRender() {
		super.onRender();
	}
	
	private void getResultDetail() {
		List<SurveyOptionInfo> listOptions = SurveyService.getSurveyOptionList(survey.getId());
		if (listOptions == null || listOptions.isEmpty()) {
			return;
		}
		List<SurveyResultInfo> listResult = SurveyService.getSurveyResultBySurveyId(survey.getId());
		if (listResult == null || listResult.isEmpty()) {
			return;
		}
		List<Map<String, String>> listData = new ArrayList<>();
		for (SurveyResultInfo result : listResult) {
			Map<String, String> data = new HashMap<>();
			data.put("user", result.getUser());
			data.put("info", result.getUserInfo());
			String rs = "";
			if (!result.getResult().isEmpty()) {
				for (String strId : result.getResult().split(",")) {
					if (!CommonMethod.isValidNumeric(strId, 1, Long.MAX_VALUE)) {
						GameLog.getInstance().error("[VoteDateLoader] invalid option id: " + strId);
						continue;
					}
					long id = Long.parseLong(strId);
					for (SurveyOptionInfo option : listOptions) {
						if (option.getId() == id) {
							if (!rs.isEmpty()) {
								rs += ",";
							}
							rs += option.getName();
							break;
						}
					}
				}
			}
			data.put("result", rs);
			listData.add(data);
		}
		if (listData.isEmpty()) {
			return;
		}
		putJsonData("result_detail", listData);
	}
	
	private void getStatisticData() {
		List<SurveyOptionInfo> listResultOption = SurveyService.calculateSurveyResult(survey.getId());
		if (listResultOption == null || listResultOption.isEmpty()) {
			return;
		}
		long resultCount = SurveyService.getCountSurveyResult(survey.getId());
		if (resultCount <= 0) {
			return;
		}
		putJsonData("count", resultCount);
		Collections.sort(listResultOption, new Comparator<SurveyOptionInfo>() {

			@Override
			public int compare(SurveyOptionInfo o1, SurveyOptionInfo o2) {
				return o2.getSelectedCount() - o1.getSelectedCount();
			}
		});
		int total = 0;
		for (SurveyOptionInfo option : listResultOption) {
			total += option.getSelectedCount();
		}
		List<Map<String, String>> listOptionData = new ArrayList<>();
		int rank = 1;
		for (SurveyOptionInfo option : listResultOption) {
			Map<String, String> optionData = new HashMap<>();
			optionData.put("id", String.valueOf(option.getId()));
			optionData.put("rank", String.valueOf(rank++));
			optionData.put("name", option.getName());
			optionData.put("rate", option.getFormatVoteRating(total));
			optionData.put("count", String.valueOf(option.getSelectedCount()));
			listOptionData.add(optionData);
		}
		
		putJsonData("option_list", listOptionData);
	}
}
