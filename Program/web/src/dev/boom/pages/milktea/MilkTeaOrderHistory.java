package dev.boom.pages.milktea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.EventFlagEnum;
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
		headElements.add(new CssImport("/css/milktea/milktea-menu.css"));
		headElements.add(new JsImport("/js/milktea/milktea-history.js"));
		
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
		sb.append("<table id=\"order-history-table\" class=\"table table-striped table-hover nowrap\" style=\"width:100%;\">");
			sb.append("<thead>");
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
			sb.append("</thead>");
			
			sb.append("<tbody>");
			if (list != null && !list.isEmpty()) {
				for (OrderInfo order : list) {
					sb.append("<tr role=\"row\">");
						sb.append("<td>");
							sb.append(order.getDishName());
						sb.append("</td>");
						sb.append("<td>");
							if (shopMap.get(order.getShopId()) != null) {
								sb.append("<div>").append(shopMap.get(order.getShopId()).getName()).append("</div>");
								sb.append("<div class=\"font-italic text-info\" data-toggle=\"tooltip\" data-placement=\"bottom\" style=\"max-width:12.5rem;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;font-size:0.75rem;\" title=\"");
								sb.append(shopMap.get(order.getShopId()).getAddress()).append("\">").append(shopMap.get(order.getShopId()).getAddress()).append("</div>");
							} else {
								sb.append("---");
							}
						sb.append("</td>");
						
						sb.append("<td>").append(order.getTotalOption(MilkTeaItemOptionType.ICE) + "%").append("</td>");
						sb.append("<td>").append(order.getTotalOption(MilkTeaItemOptionType.SUGAR) + "%").append("</td>");
						sb.append("<td>").append(order.getOptionList()).append("</td>");
						sb.append("<td>").append(order.getQuantity()).append("</td>");
						sb.append("<td>").append(CommonMethod.getFormatNumberThousandComma(order.getFinalPrice())).append("</td>");
						sb.append("<td>").append(CommonMethod.getFormatDateString(order.getCreated(), CommonDefine.DATE_FORMAT_PATTERN)).append("</td>");
						if (worldInfo.isActiveEventFlag(EventFlagEnum.ORDER_VOTING)) {
							sb.append("<td>");
							if (order.isVoted() && order.getVotingStar() > 0) {
								sb.append(MilkTeaCommonFunc.getOrderRating(order));
							} else {
								sb.append(String.format("<div id=\"order-rating-%d\">", order.getId()));
									sb.append("<div class=\"text-success\" style=\"cursor:pointer;\">");
									sb.append(String.format("<span data-toggle=\"modal\" data-target=\"#voting-up-order-%d\">Upvote</span>", order.getId()));
									sb.append("</div>");
								sb.append("</div>");
								sbModal.append(MilkTeaCommonFunc.getOrderVotingModal(order, getMessages()));
							}
							sb.append("</td>");
						}
					sb.append("</tr>");
				}
			} else {
				sb.append("<tr role=\"row\">");
				sb.append("<td colspan=\"7\">").append("No records found!").append("</td>");
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
