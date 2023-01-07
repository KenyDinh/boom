package dev.boom.pages.milktea;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.FridayThemes;
import dev.boom.common.milktea.MilkTeaItemOptionType;
import dev.boom.common.milktea.MilkTeaOrderFlag;
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
		initTheme(FridayThemes.PARALLAX);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(importJs("/js/milktea/milktea-ranking.js"));
		
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
		sb.append("<table id=\"milktea-ranking-table\" class=\"table table-hover nowrap\" style=\"width:100%;\">");
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
					String highlight = "";
					if (getUserInfo() != null && getUserInfo().getUsername().equals(map.get("username"))) {
						highlight = "class=\"bg-success\"";
					} else if (map.get("king_of_chasua") != null) {
						highlight = "class=\"font-weight-bold data-highlight\" style=\"background-color:#903b76;\"";
					}
					sb.append(String.format("<tr role=\"row\" %s>", highlight));
						sb.append("<td>").append(map.get("username")).append("</td>");
						sb.append("<td>").append(map.get("order_count")).append("</td>");
						sb.append("<td>").append(CommonMethod.getFormatNumberThousandComma(Long.parseLong(map.get("total_money")))).append("</td>");
						sb.append("<td>").append(map.get("ice") + "%").append("</td>");
						sb.append("<td>").append(map.get("sugar") + "%").append("</td>");
						sb.append("<td>").append(map.get("topping")).append("</td>");
					sb.append("</tr>");
				}
			} else {
				sb.append("<tr role=\"row\">");
				sb.append("<td colspan=\"6\" id=\"no-data\">");
				switch (mode) {
				case MODE_KING_OF_CHASUA:
					sb.append("No one is eligible for \"King Of Chasua\"");
					break;
				default:
					sb.append("No records found!");
					break;
				}
				sb.append("</td>");
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
		case MODE_KING_OF_CHASUA:
			Calendar cal = Calendar.getInstance();
			Date now = new Date();
			cal.setTime(now);
			if (mode == MODE_LAST_MONTH || mode == MODE_KING_OF_CHASUA) {
				cal.add(Calendar.MONTH, -1);
			}
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			start = CommonMethod.getFormatDateString(cal.getTime());
			String query;
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			end = CommonMethod.getFormatDateString(cal.getTime());
			if (mode == MODE_KING_OF_CHASUA) {
				query = "SELECT temp.username, COUNT(temp.username), SUM(temp.max_price), GROUP_CONCAT(temp.ice SEPARATOR ',') AS total_ice, GROUP_CONCAT(temp.sugar SEPARATOR ',') AS total_sugar, GROUP_CONCAT(temp.option_list SEPARATOR ',') AS total_topping "
						+ "FROM (SELECT username, menu_id, MAX(final_price) AS max_price, ice, sugar, option_list FROM order_info WHERE final_price > 0 AND (flag & " + MilkTeaOrderFlag.KOC_VALID.getBitMask() + ") AND created >= '" + start + "' AND created <= '" + end + "' GROUP BY username, menu_id, ice, sugar, option_list) AS temp "
						+ "GROUP BY username ORDER BY (CASE WHEN COUNT(temp.username) >= " + CommonDefine.KING_OF_CHASUA_MIN_ORDER_NUM + " THEN SUM(temp.max_price) ELSE COUNT(temp.username) END) DESC LIMIT 10";
			} else {
				query = "SELECT username, COUNT(user_id), SUM(final_price), GROUP_CONCAT(ice SEPARATOR ',') AS total_ice, GROUP_CONCAT(sugar SEPARATOR ',') AS total_sugar, GROUP_CONCAT(option_list SEPARATOR ',') AS total_topping "
						+ "FROM order_info WHERE final_price > 0 AND created >= '" + start + "' AND created <= '" + end + "' GROUP BY username;";
			}
			List<Object> listObject = CommonDaoService.executeNativeSQLQuery(query);
			if (listObject != null && !listObject.isEmpty()) {
				try {
					for (Object obj : listObject) {
						if (obj instanceof Object[]) {
							Object[] arrObj = (Object[]) obj;
							Map<String, String> map = new HashMap<>();
							map.put("username", arrObj[0].toString());
							map.put("order_count", arrObj[1].toString());
							map.put("total_money", arrObj[2].toString());
							OrderInfo orderInfo = new OrderInfo();
							orderInfo.setLimitSplitOption(Integer.parseInt(map.get("order_count")));
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
				if (mode == MODE_KING_OF_CHASUA) {
					sortKingOfChasua(listData);
				}
			}
			break;
		default:
			List<MilkTeaUserInfo> list = MilkTeaUserService.getMilkteaUserInfo("WHERE order_count >= 10 ORDER BY total_money DESC");
			if (list != null && !list.isEmpty()) {
				for (MilkTeaUserInfo mku : list) {
					Map<String, String> map = new HashMap<>();
					map.put("username", mku.getUsername());
					map.put("order_count", String.valueOf(mku.getOrderCount()));
					map.put("total_money", String.valueOf(mku.getTotalMoney()));
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
	
	private void sortKingOfChasua(List<Map<String, String>> listData) {
		if (listData == null || listData.isEmpty()) {
			return;
		}
		List<Map<String, String>> listEligible = new ArrayList<>();
		List<Map<String, String>> listInsufficient = new ArrayList<>();
		for (Map<String, String> mapData : listData) {
			if (Integer.parseInt(mapData.get("order_count")) < CommonDefine.KING_OF_CHASUA_MIN_ORDER_NUM) {
				listInsufficient.add(mapData);
			} else {
				listEligible.add(mapData);
			}
		}
		if (!listEligible.isEmpty()) {
			Collections.sort(listEligible, new RankingDataSorting());
			listEligible.get(0).put("king_of_chasua", "1");
		}
		if (!listInsufficient.isEmpty()) {
			Collections.sort(listInsufficient, new RankingDataSorting());
		}
		listData.clear();
		listData.addAll(listEligible);
		listData.addAll(listInsufficient);
	}
	
	class RankingDataSorting implements Comparator<Map<String, String>> {

		@Override
		public int compare(Map<String, String> o1, Map<String, String> o2) {
			long m1 = Long.parseLong(o1.get("total_money"));
			long m2 = Long.parseLong(o2.get("total_money"));
			int c1 = Integer.parseInt(o1.get("order_count"));
			int c2 = Integer.parseInt(o2.get("order_count"));
			if (m1 == m2) {
				return c2 - c1;
			}
			return (int)(m2 - m1);
		}
		
	}
	
}