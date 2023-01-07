package dev.boom.pages.milktea;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.connect.HibernateSessionFactory;
import dev.boom.core.BoomSession;
import dev.boom.core.GameLog;
import dev.boom.services.CommonDaoService;
import dev.boom.services.DishRatingInfo;
import dev.boom.services.DishRatingService;
import dev.boom.services.OrderInfo;
import dev.boom.services.OrderService;
import dev.boom.services.UserInfo;
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
				List<OrderInfo> orderInfoList = OrderService.getOrderList("");
				List<DishRatingInfo> dishRatingList = DishRatingService.getDishRatingList("");
				
				Session session = HibernateSessionFactory.openSession();
				Transaction tx = null;
				try {
					GameLog.getInstance().info("Transaction Begin!");
					tx = session.beginTransaction();
					if (orderInfoList != null && orderInfoList.size() > 0) {
						for (OrderInfo orderInfo : orderInfoList) {
							int code = MilkTeaCommonFunc.getItemCodeName(orderInfo.getDishName());
							if (code != orderInfo.getDishCode()) {
								orderInfo.setDishCode(code);
								if (!CommonDaoService.update(session, orderInfo.getTblInfo())) {
									GameLog.getInstance().error("[Update order code] update dish code failed!");
									tx.rollback();
									return;
								}
							}
						}
					}
					if (dishRatingList != null && dishRatingList.size() > 0) {
						for (DishRatingInfo dri : dishRatingList) {
							int code = MilkTeaCommonFunc.getItemCodeName(dri.getName());
							if (code != dri.getCode()) {
								dri.setCode(code);
								if (!CommonDaoService.update(session, dri.getTblInfo())) {
									GameLog.getInstance().error("[Update rating code] update dish rating code failed!");
									tx.rollback();
									return;
								}
							}
						}
					}
					tx.commit();
					GameLog.getInstance().info("Transaction Commit!");
				} catch (Exception e) {
					e.printStackTrace();
					if (tx != null) {
						tx.rollback();
					}
				} finally {
					HibernateSessionFactory.closeSession(session);
				}
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
		
		List<OrderInfo> orderList = OrderService.getOrderCommentList(Long.parseLong(strShopId), Integer.parseInt(strDishCode));
		if (orderList == null || orderList.isEmpty()) {
			return;
		}
		List<Long> ids = new ArrayList<>();
		for (OrderInfo order : orderList) {
			if (ids.contains(order.getUserId())) {
				continue;
			}
			ids.add(order.getUserId());
		}
		Map<Long, UserInfo> mapUser = UserService.loadMapUsers(ids);
		if (mapUser == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		sb.append("<div class=\"item-comment item-code-").append(strDishCode).append("\">");
		for (OrderInfo order : orderList) {
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
			sb.append(CommonMethod.getFormatDateString(order.getCreated(), CommonDefine.DATE_FORMAT_PATTERN));
			sb.append("</label>");
			sb.append("</div>");
			
			sb.append("<div class=\"para-content\">");
			sb.append("<p style=\"line-break:anywhere;\">");
//			sb.append(StringEscapeUtils.escapeHtml(order.getComment()));
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
