package dev.boom.pages.milktea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.click.element.JsScript;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.EventFlagEnum;
import dev.boom.common.enums.FridayThemes;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaItemOptionType;
import dev.boom.common.milktea.MilkTeaTabEnum;
import dev.boom.pages.Home;
import dev.boom.services.OrderInfo;
import dev.boom.services.OrderService;
import dev.boom.services.ShopInfo;
import dev.boom.services.ShopService;

public class MilkTeaOrderHistory extends MilkTeaMainPage {

	private static final long serialVersionUID = 1L;

	public MilkTeaOrderHistory() {
		setDataTableFormat(true);
		initTheme(FridayThemes.STAR);
	}
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (getUserInfo() == null) {
			setRedirect(Home.class);
			return false;
		}
		return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(importCss("/css/milktea/milktea-menu.css"));
		headElements.add(importJs("/js/milktea/milktea-history.js"));
		headElements.add(new JsScript("$j('#canvas').css('z-index','1');"));
		
		return headElements;
	}

	@Override
	public void onInit() {
		super.onInit();
	}

	@Override
	public void onRender() {
		super.onRender();
		initTable();
	}
	
	@Override
	protected String initMilkTeaIntro() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"col-lg-12\">");
		sb.append("<div style=\"height:100%;");
			sb.append("background-image:");
				sb.append("url('" + getContextPath() + "/img/milktea/ranking_banner_a.png');");
			sb.append("background-repeat:repeat-x;");
		sb.append("\">");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

	@Override
	protected int getMilkTeaTabIndex() {
		return MilkTeaTabEnum.ORDER_HISTORY.getIndex();
	}

	private void initTable() {
		List<OrderInfo> list = OrderService.getCompletedOrderListByUserId(getUserInfo().getId());
		Map<Long, ShopInfo> shopMap = new HashMap<>();
		List<Long> ids = new ArrayList<>();
		if (list != null && !list.isEmpty()) {
			for (OrderInfo order : list) {
				if (!ids.contains(order.getShopId())) {
					ids.add(order.getShopId());
				}
			}
		}
		if (ids.size() > 0) {
			List<ShopInfo> shopList = ShopService.getShopListById(ids);
			if (shopList != null && shopList.size() > 0) {
				for (ShopInfo shopInfo : shopList) {
					shopMap.put(shopInfo.getId(), shopInfo);
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		StringBuilder sbModal = new StringBuilder();
		sb.append("<table id=\"order-history-table\" class=\"table table-hover nowrap\" style=\"width:100%;\">");
			sb.append("<thead>");
				sb.append("<tr role=\"row\" class=\"text-success\">");
					sb.append("<th><span class=\"overlay\">").append(getMessage("MSG_MILK_TEA_ORDER_COLUMN_ORDERNAME")).append("</span></th>");
					sb.append("<th><span class=\"overlay\">").append(getMessage("MSG_MILK_TEA_TAB_SHOP")).append("</span></th>");
					sb.append("<th><span class=\"overlay\">").append(getMessage("MSG_MILK_TEA_OPTION_ICE")).append("</span></th>");
					sb.append("<th><span class=\"overlay\">").append(getMessage("MSG_MILK_TEA_OPTION_SUGAR")).append("</span></th>");
					sb.append("<th><span class=\"overlay\">").append(getMessage("MSG_MILK_TEA_ORDER_COLUMN_OPTION")).append("</span></th>");
					sb.append("<th><span class=\"overlay\">").append(getMessage("MSG_MILK_TEA_ORDER_COLUMN_QUANTITY")).append("</span></th>");
					sb.append("<th><span class=\"overlay\">").append(getMessage("MSG_MILK_TEA_ORDER_COLUMN_PRICE")).append("</span></th>");
					sb.append("<th><span class=\"overlay\">").append(getMessage("MSG_MILK_TEA_ORDER_ORDERTIME")).append("</span></th>");
					if (worldInfo.isActiveEventFlag(EventFlagEnum.ORDER_VOTING)) {
						sb.append("<th><span class=\"overlay\">").append(getMessage("MSG_MILK_TEA_ORDER_RATING")).append("</span></th>");
					}
				sb.append("</tr>");
			sb.append("</thead>");
			
			sb.append("<tbody>");
			if (list != null && !list.isEmpty()) {
				for (OrderInfo order : list) {
					sb.append("<tr role=\"row\">");
						sb.append("<td class=\"overlay\">");
							sb.append(order.getDishName());
						sb.append("</td>");
						sb.append("<td><span class=\"overlay\">");
							if (shopMap.get(order.getShopId()) != null) {
								sb.append("<div>");
								if (order.getMenuId() > 0) {
									sb.append("<a href=\"" + getHostURL() + getContextPath() + "/milktea/milk_tea_menu.htm?id=" + order.getMenuId() + "\" style=\"color:#FFFFFF\" >");
									sb.append(shopMap.get(order.getShopId()).getName());
									sb.append("</a>");
								} else {
									sb.append(shopMap.get(order.getShopId()).getName());
								}
								sb.append("</div>");
								sb.append("<div class=\"font-italic text-info\" data-toggle=\"tooltip\" data-placement=\"bottom\" style=\"max-width:12.5rem;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;font-size:0.75rem;\" title=\"");
								sb.append(shopMap.get(order.getShopId()).getAddress()).append("\">").append(shopMap.get(order.getShopId()).getAddress()).append("</div>");
							} else {
								sb.append("---");
							}
						sb.append("</span></td>");
						
						sb.append("<td><span class=\"overlay\">").append(order.getTotalOption(MilkTeaItemOptionType.ICE) + "%").append("</span></td>");
						sb.append("<td><span class=\"overlay\">").append(order.getTotalOption(MilkTeaItemOptionType.SUGAR) + "%").append("</span></td>");
						sb.append("<td><span class=\"overlay\">").append(order.getOptionList()).append("</span></td>");
						sb.append("<td><span class=\"overlay\">").append(order.getQuantity()).append("</span></td>");
						sb.append("<td><span class=\"overlay\">").append(CommonMethod.getFormatNumberThousandComma(order.getFinalPrice())).append("</span></td>");
						sb.append("<td><span class=\"overlay\">").append(CommonMethod.getFormatDateString(order.getCreated(), CommonDefine.DATE_FORMAT_PATTERN)).append("</span></td>");
						if (worldInfo.isActiveEventFlag(EventFlagEnum.ORDER_VOTING)) {
							sb.append("<td><span class=\"overlay\">");
							sb.append(MilkTeaCommonFunc.getOrderRatingWithComment(order));
							sb.append("</span></td>");
						}
					sb.append("</tr>");
				}
			} else {
				sb.append("<tr role=\"row\">");
				sb.append("<td colspan=\"" + (worldInfo.isActiveEventFlag(EventFlagEnum.ORDER_VOTING) ? "9" : "8") + "\">").append("No records found!").append("</td>");
				sb.append("</tr>");
			}
			sb.append("</tbody>");
			if (list != null && list.size() > 10) {
				sb.append("<tfoot>");
					sb.append("<tr role=\"row\" class=\"text-success\">");
						sb.append("<th>").append("Order Name").append("</th>");
						sb.append("<th>").append("Shop").append("</th>");
						sb.append("<th>").append("Ice").append("</th>");
						sb.append("<th>").append("Sugar").append("</th>");
						sb.append("<th>").append("Option").append("</th>");
						sb.append("<th>").append("Quantity").append("</th>");
						sb.append("<th>").append("Price").append("</th>");
						sb.append("<th>").append("Ordering time").append("</th>");
						if (worldInfo.isActiveEventFlag(EventFlagEnum.ORDER_VOTING)) {
							sb.append("<th>").append("Rating").append("</th>");
						}
					sb.append("</tr>");
				sb.append("</tfoot>");
			}
			
		sb.append("</table>");
		sb.append(sbModal.toString());
		addModel("history", sb.toString());
	}
}
