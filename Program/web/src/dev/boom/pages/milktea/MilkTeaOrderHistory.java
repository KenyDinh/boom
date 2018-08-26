package dev.boom.pages.milktea;

import java.util.List;

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
		StringBuilder sb = new StringBuilder();
		StringBuilder sbModal = new StringBuilder();
		sb.append("<table id=\"order-history-table\" class=\"table table-striped table-hover nowrap\" style=\"width:100%;\">");
			sb.append("<thead>");
				sb.append("<tr role=\"row\" class=\"text-success\">");
					sb.append("<th>").append("Order Name").append("</th>");
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
						sb.append("<td>").append(order.getDishName()).append("</td>");
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
						sb.append("<th>").append("Ice").append("</th>");
						sb.append("<th>").append("Sugar").append("</th>");
						sb.append("<th>").append("Option").append("</th>");
						sb.append("<th>").append("Quantity").append("</th>");
						sb.append("<th>").append("Price").append("</th>");
						sb.append("<th>").append("Ordering time").append("</th>");
						//sb.append("<th>").append("Vote").append("</th>");
					sb.append("</tr>");
				sb.append("</tfoot>");
			}
			
		sb.append("</table>");
		sb.append(sbModal.toString());
		addModel("history", sb.toString());
	}
}
