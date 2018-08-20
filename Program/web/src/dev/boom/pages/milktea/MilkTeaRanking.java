package dev.boom.pages.milktea;

import java.util.List;

import org.apache.click.element.JsImport;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaTabEnum;
import dev.boom.services.MilkTeaUserInfo;
import dev.boom.services.MilkTeaUserService;

public class MilkTeaRanking extends MilkTeaMainPage {

	private static final long serialVersionUID = 1L;

	public MilkTeaRanking() {
		setDataTableFormat(true);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new JsImport("/js/milktea/milktea-ranking.js"));
		
		return headElements;
	}

	@Override
	public void onInit() {
		super.onInit();
	}

	@Override
	public void onRender() {
		super.onRender();
		initRankingTable();
	}

	@Override
	protected int getMilkTeaTabIndex() {
		return MilkTeaTabEnum.RANKING.getIndex();
	}
	
	@Override
	protected String initMilkTeaIntro() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"col-lg-12\">");
		sb.append("<div style=\"height:100%;");
			sb.append("background-image:");
				sb.append("url('" + getContextPath() + "/img/milktea/ranking_banner_a.png');");
//			sb.append("background-position:left,500px,1000px,1900px;");
			sb.append("background-repeat:repeat-x;");
		sb.append("\">");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

	private void initRankingTable() {
		List<MilkTeaUserInfo> list = MilkTeaUserService.getAll("ORDER BY total_money DESC");
		StringBuilder sb = new StringBuilder();
		sb.append("<table id=\"milktea-ranking-table\" class=\"table table-striped table-hover nowrap\" style=\"width:100%;\">");
			sb.append("<thead>");
				sb.append("<tr role=\"row\" class=\"text-success\">");
					sb.append("<th>").append("Username").append("</th>");
					sb.append("<th>").append("Total Orders").append("</th>");
					sb.append("<th>").append("Total money (vnd)").append("</th>");
					sb.append("<th>").append("Ice Avg").append("</th>");
					sb.append("<th>").append("Sugar Avg").append("</th>");
					sb.append("<th>").append("Topping count").append("</th>");
				sb.append("</tr>");
			sb.append("</thead>");
			
			sb.append("<tbody>");
			if (list != null && !list.isEmpty()) {
				for (MilkTeaUserInfo info : list) {
					sb.append(String.format("<tr role=\"row\" class=\"%s\">", ((getUserInfo() != null && getUserInfo().getId() == info.getUserId()) ? "bg-success" : "")));
						sb.append("<td>").append(info.getUsername()).append("</td>");
						sb.append("<td>").append(info.getOrderCount()).append("</td>");
						sb.append("<td>").append(CommonMethod.getFormatNumberThousandComma(info.getTotalMoney())).append("</td>");
						sb.append("<td>").append(info.getFormatAverageIce() + "%").append("</td>");
						sb.append("<td>").append(info.getFormatAverageSugar() + "%").append("</td>");
						sb.append("<td>").append(info.getTotalTopping()).append("</td>");
					sb.append("</tr>");
				}
			} else {
				sb.append("<tr role=\"row\">");
				sb.append("<td colspan=\"6\">").append("No records found!").append("</td>");
				sb.append("</tr>");
			}
			sb.append("</tbody>");
			if (list != null && list.size() > 10) {
				sb.append("<tfoot>");
				sb.append("<tr role=\"row\" class=\"text-success\">");
					sb.append("<th>").append("Username").append("</th>");
					sb.append("<th>").append("Total Orders").append("</th>");
					sb.append("<th>").append("Total money (vnd)").append("</th>");
					sb.append("<th>").append("Ice Avg").append("</th>");
					sb.append("<th>").append("Sugar Avg").append("</th>");
					sb.append("<th>").append("Topping count").append("</th>");
				sb.append("</tr>");
				sb.append("</tfoot>");
			}
			
		sb.append("</table>");
		addModel("ranking", sb.toString());
	}
}
