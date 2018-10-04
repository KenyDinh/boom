package dev.boom.pages.vote;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.click.element.JsImport;

import dev.boom.core.GameLog;
import dev.boom.pages.PageBase;
import dev.boom.services.SurveyInfo;
import dev.boom.services.SurveyOptionInfo;
import dev.boom.services.SurveyService;

public class ManageVote extends PageBase {

	private static final long serialVersionUID = 1L;
	
	private SurveyInfo activeSurvey = null;

	public ManageVote() {
	}

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		
		return super.onSecurityCheck();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		
		headElements.add(new JsImport("/js/lib/chart-2.7.2.js"));
		headElements.add(new JsImport("/js/lib/random-color.min.js"));
		headElements.add(new JsImport("/js/vote/manage_vote.js"));
		
		return headElements;
	}

	@Override
	public void onInit() {
		super.onInit();
		activeSurvey = SurveyService.getActiveSurveyInfo();
		if (activeSurvey != null) {
			addModel("activeSurvey", activeSurvey);
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		if (activeSurvey == null) {
			return;
		}
	}

	@Override
	public void onRender() {
		super.onRender();
		if (activeSurvey == null) {
			GameLog.getInstance().error("[ManageVote] no survey active now!");
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
		showResult(listResultOption);
	}
	
	private void showResult(List<SurveyOptionInfo> listResultOption) {
		if (activeSurvey == null || listResultOption == null || listResultOption.isEmpty()) {
			return;
		}
		long resultCount = SurveyService.getCountSurveyResult(activeSurvey.getId());
		if (resultCount <= 0) {
			GameLog.getInstance().error("[ManageVote] no result record found!");
			return;
		}
		addModel("resultCount", resultCount);
		int total = 0;
		for (SurveyOptionInfo option : listResultOption) {
			total += option.getSelectedCount();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"\" class=\"\">");
		sb.append("<table class=\"table\">");
		sb.append("<thead>");
			sb.append("<tr>");
				sb.append("<th>");
					sb.append("Rank");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Performances");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Rating");
				sb.append("</th>");
				sb.append("<th>");
					sb.append("Vote");
				sb.append("</th>");
			sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		int rank = 1;
		for (SurveyOptionInfo option : listResultOption) {
			sb.append("<tr id=\"option-rank-" + rank + "\">");
				sb.append("<td class=\"rank\">");
					sb.append(rank++);
				sb.append("</td>");
				sb.append("<td class=\"name\">");
					sb.append(option.getName());
				sb.append("</td>");
				sb.append("<td class=\"rate\">");
					sb.append(option.getFormatVoteRating(total));
				sb.append("</td>");
				sb.append("<td class=\"count\">");
					sb.append(option.getSelectedCount());
				sb.append("</td>");
			sb.append("</tr>");
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		
		addModel("table", sb.toString());
	}
	
}
