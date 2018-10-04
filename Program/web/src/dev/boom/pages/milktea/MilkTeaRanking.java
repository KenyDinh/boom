package dev.boom.pages.milktea;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.click.element.JsImport;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaItemOptionType;
import dev.boom.common.milktea.MilkTeaTabEnum;
import dev.boom.services.CommonDaoService;
import dev.boom.services.MilkTeaUserInfo;
import dev.boom.services.MilkTeaUserService;
import dev.boom.services.OrderInfo;

public class MilkTeaRanking extends MilkTeaMainPage {

	private static final long serialVersionUID = 1L;
	
	private static final int MODE_ALL_TIME = 0;
	private static final int MODE_LAST_MONTH = 1;
	private static final int MODE_THIS_MONTH = 2;
	private static final int MODE_KING_OF_CHASUA = 3;
	
	private static final int MODE_MIN = MODE_ALL_TIME;
	private static final int MODE_MAX = MODE_KING_OF_CHASUA;
	
	private int mode = 0;
	
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
		String strMode = getContext().getRequestParameter("mode");
		if (CommonMethod.isValidNumeric(strMode, MODE_MIN, MODE_MAX)) {
			mode = Integer.parseInt(strMode);
		}
		addModel("style_" + mode, "active bg-info");
		for (int i = MODE_MIN; i <= MODE_MAX; i++) {
			if (i == mode) {
				addModel("url_" + i, "href=\"javascript:void(0);return false;\"");
				continue;
			}
			addModel("url_" + i, "href=\"" + getContextPath() + getContext().getPagePath(this.getClass()) + "?mode=" + i + "\""); 
		}
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
		List<Map<String, String>> listData = getRankingList();
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
			if (listData != null && !listData.isEmpty()) {
				for (Map<String, String> map : listData) {
					sb.append(String.format("<tr role=\"row\" class=\"%s\">", ((getUserInfo() != null && getUserInfo().getUsername().equals(map.get("username"))) ? "bg-success" : "")));
						sb.append("<td>").append(map.get("username")).append("</td>");
						sb.append("<td>").append(map.get("order_count")).append("</td>");
						sb.append("<td>").append(map.get("total_money")).append("</td>");
						sb.append("<td>").append(map.get("ice") + "%").append("</td>");
						sb.append("<td>").append(map.get("sugar") + "%").append("</td>");
						sb.append("<td>").append(map.get("topping")).append("</td>");
					sb.append("</tr>");
				}
			} else {
				sb.append("<tr role=\"row\">");
				sb.append("<td colspan=\"6\">").append("No records found!").append("</td>");
				sb.append("</tr>");
			}
			sb.append("</tbody>");
			if (listData != null && listData.size() > 10) {
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
	
	private List<Map<String, String>> getRankingList() {
		List<Map<String, String>> listData = new ArrayList<>();
		String start, end;
		switch (mode) {
		case MODE_LAST_MONTH:
		case MODE_THIS_MONTH:
			Calendar cal = Calendar.getInstance();
			Date now = new Date();
			cal.setTime(now);
			if (mode == MODE_LAST_MONTH) {
				cal.add(Calendar.MONTH, -1);
			}
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			start = CommonMethod.getFormatDateString(cal.getTime());
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			end = CommonMethod.getFormatDateString(cal.getTime());
			String query = "SELECT username, COUNT(user_id), SUM(final_price), GROUP_CONCAT(ice SEPARATOR ',') AS total_ice, GROUP_CONCAT(sugar SEPARATOR ',') AS total_sugar, GROUP_CONCAT(option_list SEPARATOR ',') AS total_topping "
					+ "FROM order_info WHERE final_price > 0 AND created >= '" + start + "' AND created <= '" + end + "' GROUP BY user_id;";
			List<Object> listObject = CommonDaoService.executeNativeSQLQuery(query);
			if (listObject != null && !listObject.isEmpty()) {
				try {
					for (Object obj : listObject) {
						if (obj instanceof Object[]) {
							Object[] arrObj = (Object[]) obj;
							Map<String, String> map = new HashMap<>();
							map.put("username", arrObj[0].toString());
							map.put("order_count", arrObj[1].toString());
							map.put("total_money", CommonMethod.getFormatNumberThousandComma(Long.parseLong(arrObj[2].toString())));
							OrderInfo orderInfo = new OrderInfo();
							orderInfo.setIce(arrObj[3].toString());
							orderInfo.setSugar(arrObj[4].toString());
							orderInfo.setOptionList(arrObj[5].toString());
							MilkTeaUserInfo dumUser = new MilkTeaUserInfo();
							dumUser.setOrderCount(Long.parseLong(map.get("order_count")));
							dumUser.setTotalIce(orderInfo.getTotalOption(MilkTeaItemOptionType.ICE));
							dumUser.setTotalSugar(orderInfo.getTotalOption(MilkTeaItemOptionType.SUGAR));
							map.put("ice", dumUser.getFormatAverageIce());
							map.put("sugar", dumUser.getFormatAverageSugar());
							map.put("topping", String.valueOf(orderInfo.getTotalOption(MilkTeaItemOptionType.TOPPING)));
							listData.add(map);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case MODE_KING_OF_CHASUA:
			
			break;
		default:
			List<MilkTeaUserInfo> list = MilkTeaUserService.getMilkteaUserInfo("ORDER BY total_money DESC");
			if (list != null && !list.isEmpty()) {
				for (MilkTeaUserInfo mku : list) {
					Map<String, String> map = new HashMap<>();
					map.put("username", mku.getUsername());
					map.put("order_count", String.valueOf(mku.getOrderCount()));
					map.put("total_money", CommonMethod.getFormatNumberThousandComma(mku.getTotalMoney()));
					map.put("ice", mku.getFormatAverageIce());
					map.put("sugar", mku.getFormatAverageSugar());
					map.put("topping", String.valueOf(mku.getTotalTopping()));
					listData.add(map);
				}
			}
			break;
		}
		return listData;
	}
	
}
