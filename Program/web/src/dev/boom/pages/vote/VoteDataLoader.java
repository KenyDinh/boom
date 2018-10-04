package dev.boom.pages.vote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.pages.JsonPageBase;
import dev.boom.services.SurveyInfo;
import dev.boom.services.SurveyOptionInfo;
import dev.boom.services.SurveyService;

public class VoteDataLoader extends JsonPageBase {

	private static final long serialVersionUID = 1L;

	private SurveyInfo activeSurvey = null;
	
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
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
		activeSurvey = SurveyService.getActiveSurveyInfo();
	}

	@Override
	public void onPost() {
		super.onPost();
		if (activeSurvey == null) {
			return;
		}
		List<SurveyOptionInfo> listResultOption = SurveyService.calculateSurveyResult(activeSurvey.getId());
		if (listResultOption == null || listResultOption.isEmpty()) {
			return;
		}
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

	@Override
	public void onRender() {
		super.onRender();
	}
	
}
