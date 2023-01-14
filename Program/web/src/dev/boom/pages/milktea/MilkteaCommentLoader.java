package dev.boom.pages.milktea;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.core.BoomSession;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.FunctionTransaction;
import dev.boom.services.DishRating;
import dev.boom.services.DishRatingService;
import dev.boom.services.Order;
import dev.boom.services.OrderService;
import dev.boom.services.User;
import dev.boom.services.UserService;

public class MilkteaCommentLoader extends MilkTeaAjaxPageBase {

	private static final long serialVersionUID = 1L;

	@Override
	public String getContentType() {
		String charset = getContext().getRequest().getCharacterEncoding();

		if (charset == null) {
			return "text/html";
		} else {
			return "text/html; charset=" + charset;
		}
	}
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!getContext().isGet()) {
			return false;
		}
		BoomSession boomSession = getBoomSession();
		if (boomSession != null) {
			userInfo = UserService.getUserById(boomSession.getId());
		}
		return true;
	}
	
	@Override
	public void onPost() {
		if (!initUserInfo()) {
			GameLog.getInstance().error("[Update order code] not login yet!");
			return;
		}
		if (userInfo == null || !userInfo.isMilkteaAdmin()) {
			GameLog.getInstance().error("[Update order code] permission denied!");
			return;
		}
		String strAction = getContext().getRequestParameter("action");
		if (StringUtils.isNotBlank(strAction)) {
			if (strAction.equals("update_code")) {
				List<Order> orderInfoList = OrderService.getOrderList("");
				List<DishRating> dishRatingList = DishRatingService.getDishRatingList("");
				
				FunctionTransaction ft = (conn) -> {
					if (orderInfoList != null && orderInfoList.size() > 0) {
						for (Order orderInfo : orderInfoList) {
							int code = MilkTeaCommonFunc.getItemCodeName(orderInfo.getDishName());
							if (code != orderInfo.getDishCode()) {
								orderInfo.setDishCode(code);
								if (CommonDaoFactory.Update(conn, orderInfo.getOrderInfo()) < 0) {
									GameLog.getInstance().error("[Update order code] update dish code failed!");
									return false;
								}
							}
						}
					}
					if (dishRatingList != null && dishRatingList.size() > 0) {
						for (DishRating dri : dishRatingList) {
							int code = MilkTeaCommonFunc.getItemCodeName(dri.getName());
							if (code != dri.getCode()) {
								dri.setCode(code);
								if (CommonDaoFactory.Update(conn, dri.getDishRatingInfo()) < 0) {
									GameLog.getInstance().error("[Update rating code] update dish rating code failed!");
									return false;
								}
							}
						}
					}
					return true;
				};
				CommonDaoFactory.functionTransaction(ft);
			}
		}
	}
	
	@Override
	public void onRender() {
		String strShopId = getContext().getRequestParameter("shop_id");
		String strDishCode = getContext().getRequestParameter("dish_code");
		if (StringUtils.isBlank(strShopId) || StringUtils.isBlank(strDishCode) || !CommonMethod.isValidNumeric(strShopId, 1, Long.MAX_VALUE) || !CommonMethod.isValidNumeric(strDishCode, Integer.MIN_VALUE, Integer.MAX_VALUE)) {
			return;
		}
		
		List<Order> orderList = OrderService.getOrderCommentList(Long.parseLong(strShopId), Integer.parseInt(strDishCode));
		if (orderList == null || orderList.isEmpty()) {
			return;
		}
		List<Long> ids = new ArrayList<>();
		for (Order order : orderList) {
			if (ids.contains(order.getUserId())) {
				continue;
			}
			ids.add(order.getUserId());
		}
		Map<Long, User> mapUser = UserService.loadMapUsers(ids);
		if (mapUser == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		sb.append("<div class=\"item-comment item-code-").append(strDishCode).append("\">");
		for (Order order : orderList) {
			if (!mapUser.containsKey(order.getUserId())) {
				continue;
			}
			if (mapUser.get(order.getUserId()).isCommentBanned()) {
				continue;
			}
			if (index > 0) {
				sb.append("<hr style=\"background-color:#bdbcbc;\"/>");
			}
			sb.append("<div class=\"block-comment\">");
			sb.append("<div style=\"display:flex;justify-content:space-between;padding:0 0.5rem 0 0.25rem;color:#e2e0e0e6;\">");
			sb.append(String.format("<label class=\"%s\">", ((userInfo != null && userInfo.getId() == order.getUserId()) ? "text-success" : "")));
			sb.append(order.getUsername()).append("&nbsp;");
			sb.append(MilkTeaCommonFunc.getOrderRating(order));
			sb.append("</label>");
			sb.append("<label class=\"font-italic\">");
			sb.append(order.getCreated());
			sb.append("</label>");
			sb.append("</div>");
			
			sb.append("<div class=\"para-content\">");
			sb.append("<p style=\"line-break:anywhere;\">");
			sb.append(order.getFormatComment());
			sb.append("</p>");
			sb.append("</div>");
			sb.append("</div>");
			index++;
		}
		if (index == 0) {
			sb.append("<div><p>No comment found!</p></div>");
		}
		sb.append("</div>");
		addModel("comment_list", sb.toString());
	}
}
