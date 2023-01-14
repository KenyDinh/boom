package dev.boom.pages.milktea;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.EventFlagEnum;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaItemOptionType;
import dev.boom.common.milktea.MilkTeaOrderFlag;
import dev.boom.common.milktea.MilkTeaSocketMessage;
import dev.boom.common.milktea.MilkTeaSocketType;
import dev.boom.common.milktea.MilkteaMenuFlag;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.milktea.object.MenuItem;
import dev.boom.milktea.object.MenuItemOption;
import dev.boom.milktea.object.MenuItemSelectionLimit;
import dev.boom.services.DishRating;
import dev.boom.services.DishRatingService;
import dev.boom.services.Menu;
import dev.boom.services.MenuService;
import dev.boom.services.MilkteaUser;
import dev.boom.services.MilkteaUserService;
import dev.boom.services.Order;
import dev.boom.services.OrderService;
import dev.boom.services.Shop;
import dev.boom.services.ShopService;
import dev.boom.socket.endpoint.MilkTeaEndPoint;

public class MilkTeaManageOrder extends MilkTeaAjaxPageBase {

	private static final long serialVersionUID = 1L;

	private static final int MODE_INSERT = 1;
	private static final int MODE_DELETE = 2;
	private static final int MODE_RATING = 3;
	
	private static final int MODE_MIN = MODE_INSERT;
	private static final int MODE_MAX = MODE_RATING;

	private int mode = 0;
	private boolean error = false;
	private boolean isAdminAccess = false;
	private Menu menuInfo = null;
	private MilkteaUser milkteaUser = null;

	public MilkTeaManageOrder() {
	}

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!getContext().isPost()) {
			return false;
		}
		if (!initUserInfo()) {
			return false;
		}
		if (!userInfo.isActive()) {
			GameLog.getInstance().error("[MilkTeaManageOrder] user is not active yet!");
			return false;
		}
		if (userInfo.isMilkteaBanned()) {
			GameLog.getInstance().error("[MilkTeaManageOrder] user is banned!");
			return false;
		}
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
		String strMode = getContext().getRequestParameter("mode");
		if (strMode != null && CommonMethod.isValidNumeric(strMode, MODE_MIN, MODE_MAX)) {
			mode = Integer.parseInt(strMode);
		}
		if (mode == 0) {
			GameLog.getInstance().error("[MilkTeaManageOrder] No mode found!");
			error = true;
			return;
		}
		String strMenuId = getContext().getRequestParameter("menu_id");
		if (strMenuId != null && CommonMethod.isValidNumeric(strMenuId, 1, Long.MAX_VALUE)) {
			menuInfo = MenuService.getMenuById(Long.parseLong(strMenuId));
		}
		milkteaUser = MilkteaUserService.getMilkTeaUserInfoById(getUserInfo().getId());
		isAdminAccess = (userInfo!= null && userInfo.isMilkteaAdmin());
	}

	@Override
	public void onPost() {
		super.onPost();
		if (error) {
			return;
		}
		switch (mode) {
		case MODE_INSERT:
		case MODE_DELETE:
			if (menuInfo == null) {
				GameLog.getInstance().error("[MilkTeaManageOrder] No menu found!");
				error = true;
				return;
			}
			if (!menuInfo.isAvailableForUser(getUserInfo())) {
				GameLog.getInstance().error("[MilkTeaManageOrder] Menu is not available for user!");
				error = true;
				return;
			}
			if (menuInfo.isCompleted()) {
				GameLog.getInstance().error("[MilkTeaManageOrder] Menu is completed, id:" + menuInfo.getId());
				error = true;
				return;
			}
			if (!menuInfo.isOpening() && !isAdminAccess) {
				GameLog.getInstance().error("[MilkTeaManageOrder] Menu is no longer open!, id:" + menuInfo.getId());
				error = true;
				return;
			}
			if (mode == MODE_INSERT) {
				doInsertOrder();
			} else {
				doDeleteOrder();
			}
			returnDetail();
			break;
		case MODE_RATING:
			doRatingOrder();
			break;
		default:
			break;
		}
	}

	private void doInsertOrder() {
		Map<String, String[]> mapParams = getContext().getRequest().getParameterMap();
		if (mapParams == null) {
			error = true;
			return;
		}
		long menuItemId = 0;
		List<MenuItemOption> listItemOptions = null;
		int quantity = 1;
		boolean isTicket = false;
		for (String key : mapParams.keySet()) {
			String[] values = mapParams.get(key);
			if (key.equals("menu_item_id")) {
				if (values != null && values.length > 0) {
					if (CommonMethod.isValidNumeric(values[0], 1, Long.MAX_VALUE)) {
						menuItemId = Long.parseLong(values[0]);
					}
				}
				if (menuItemId == 0) {
					GameLog.getInstance().error("[MilkTeaManageOrder] MenuItem id is invalid!");
					error = true;
					return;
				}
			} else if (key.startsWith("menu_item_option_")) {
				if (values != null && values.length > 0) {
					for (String strName : values) {
						String strType = key.replace("menu_item_option_", "");
						if (!CommonMethod.isValidNumeric(strType, 1, MilkTeaItemOptionType.MAX_TYPE)) {
							continue;
						}
						MilkTeaItemOptionType type = MilkTeaItemOptionType.valueOf(Short.parseShort(strType));
						if (type == MilkTeaItemOptionType.NONE) {
							continue;
						}
						MenuItemOption itemOption = new MenuItemOption();
						itemOption.setName(strName);
						itemOption.setType(type.getType());
						if (listItemOptions == null) {
							listItemOptions = new ArrayList<>();
						}
						listItemOptions.add(itemOption);
					}
				}
			} else if (key.equals("quantity")) {
				if (values != null && values.length > 0) {
					if (CommonMethod.isValidNumeric(values[0], 1, 15)) {
						quantity = Integer.parseInt(values[0]);
					}
				}
			} else if (key.equals("ticket")) {
				if (values != null && values.length > 0) {
					if (values[0].equals("true")) {
						isTicket = true;
					}
				}
			}
		}
		if (isTicket) {
			byte ticket = (milkteaUser == null ? 0 : milkteaUser.getFreeTicket());
			if (ticket < quantity) {
				GameLog.getInstance().error("[MilkTeaManageOrder] Not enough free ticket, ticket:" + ticket + ",quantity:" + quantity);
				error = true;
				return;
			}
			GameLog.getInstance().info("[MilkTeaManageOrder] Order with free ticket!");
		}
		MenuItem menuItem = MenuService.getMenuItemById(menuItemId);
		if (menuItem == null) {
			GameLog.getInstance().error("[MilkTeaManageOrder] MenuItem is null!");
			error = true;
			return;
		}
		if (menuItem.getShop_id() != menuInfo.getShopId()) {
			GameLog.getInstance().error("[MilkTeaManageOrder] MenuItem is invalid, shop id is different from the menu!");
			error = true;
			return;
		}
		long plusPrice = 0;
		Map<Short, List<String>> mapOption = new HashMap<>();
		if (listItemOptions != null && !listItemOptions.isEmpty()) {
			List<MenuItemOption> listOriginalItemOption = menuItem.getListOptions();
			for (MenuItemOption itemOption : listItemOptions) {
				for (MenuItemOption originalOption : listOriginalItemOption) {
					if (itemOption.getType() == originalOption.getType() && 
						itemOption.getName().equals(originalOption.getName())) {
						if (!mapOption.containsKey(itemOption.getType())) {
							mapOption.put(itemOption.getType(), new ArrayList<>());
						}
						mapOption.get(itemOption.getType()).add(originalOption.getName());
						plusPrice += originalOption.getPrice();
						listOriginalItemOption.remove(originalOption);
						break;
					}
				}
			}
		}
		List<DaoValue> updates = new ArrayList<>();
		
		MenuItemSelectionLimit limitSelectOption = menuItem.getLimit_select();
		if (limitSelectOption == null) {
			GameLog.getInstance().warn("[MilkTeaManageOrder] No limit selection, use the default one!");
			limitSelectOption = new MenuItemSelectionLimit();
		}
		Order orderInfo = new Order();
		for (MilkTeaItemOptionType optionType : MilkTeaItemOptionType.values()) {
			if (optionType == MilkTeaItemOptionType.NONE) {
				continue;
			}
			if (optionType == MilkTeaItemOptionType.TOPPING || optionType == MilkTeaItemOptionType.ADDITION) {
				if (menuInfo.isActiveFlag(MilkteaMenuFlag.IGNORE_VALIDATION)) {
					continue;
				}
			}
			int minselect = limitSelectOption.getMinSelect(optionType);
			int maxselect = limitSelectOption.getMaxSelect(optionType);
			if (maxselect <= 0) {
				GameLog.getInstance().warn("[MilkTeaManageOrder] Option " + getMessage(optionType.getLabel()) + " is not able to select");
				continue;
			}
			if (minselect > 0) {
				if (!mapOption.containsKey(optionType.getType()) || mapOption.get(optionType.getType()).size() < minselect) {
					GameLog.getInstance().error("[MilkTeaManageOrder] Option " + getMessage(optionType.getLabel()) + " is required:" + minselect);
					error = true;
					return;
				}
			}
			if (maxselect > 0 && mapOption.containsKey(optionType.getType())) {
				if (mapOption.get(optionType.getType()).size() > maxselect) {
					GameLog.getInstance().error("[MilkTeaManageOrder] Option " + getMessage(optionType.getLabel()) + " is excees max:" + mapOption.get(optionType.getType()).size() + "/" + maxselect);
					error = true;
					return;
				}
			}
			if (!mapOption.containsKey(optionType.getType())) {
				continue;
			}
			String strOption = "";
			for (String name : mapOption.get(optionType.getType())) {
				if (strOption.length() > 0) {
					strOption += ",";
				}
				strOption += name.replace(",", "&#44;");
			}
			switch (optionType) {
			case ICE:
				orderInfo.setIce(strOption);
				break;
			case SIZE:
				orderInfo.setSize(strOption);
				break;
			case SUGAR:
				orderInfo.setSugar(strOption);
				break;
			case TOPPING:
			case ADDITION:
				orderInfo.setOptionList(strOption);
				break;
			default:
				break;
			}
		}
		orderInfo.setUserId(userInfo.getId());
		orderInfo.setUsername(userInfo.getUsername());
		orderInfo.setMenuId(menuInfo.getId());
		orderInfo.setShopId(menuInfo.getShopId());
		orderInfo.setDishName(menuItem.getName());
		orderInfo.setDishType(menuItem.getType());
		orderInfo.setDishPrice(menuItem.getPrice());
		orderInfo.setAttrPrice(plusPrice);
		orderInfo.setDishCode(MilkTeaCommonFunc.getItemCodeName(menuItem.getName()));
		orderInfo.setQuantity(quantity);
		orderInfo.setFlag(MilkTeaOrderFlag.KOC_VALID.getValidFlag(orderInfo.getFlag()));
		if (isTicket) {
			orderInfo.setFlag(MilkTeaOrderFlag.KOC_TICKET.getValidFlag(orderInfo.getFlag()));
			milkteaUser.setFreeTicket((byte)(milkteaUser.getFreeTicket() - quantity));
			updates.add(milkteaUser.getMilkteaUserInfo());
		}
		updates.add(orderInfo.getOrderInfo());
		if (CommonDaoFactory.Update(updates) < 0) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Create order failed!");
			error = true;
			return;
		}
		
	}

	private void doDeleteOrder() {
		String strOrderId = getContext().getRequestParameter("order_id");
		if (strOrderId == null || !CommonMethod.isValidNumeric(strOrderId, 1, Long.MAX_VALUE)) {
			GameLog.getInstance().error("[MilkTeaManageOrder] No order id to delete!");
			error = true;
			return;
		}
		Order orderInfo = OrderService.getOrderInfoById(Long.parseLong(strOrderId));
		if (orderInfo == null) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Order is null!");
			error = true;
			return;
		}
		if (orderInfo.getMenuId() != menuInfo.getId() || (orderInfo.getUserId() != userInfo.getId() && !isAdminAccess)) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Order is invalid!");
			error = true;
			return;
		}
		orderInfo.getOrderInfo().setDelete();
		List<DaoValue> updates = new ArrayList<>();
		updates.add(orderInfo.getOrderInfo());
		if (MilkTeaOrderFlag.KOC_TICKET.isValidFlag(orderInfo.getFlag())) {
			if (milkteaUser == null) {
				GameLog.getInstance().error("[MilkTeaManageOrder] Delete free order without MilkteUserInfo!!!");
			} else {
				milkteaUser.setFreeTicket((byte)(milkteaUser.getFreeTicket() + orderInfo.getQuantity()));
				updates.add(milkteaUser.getMilkteaUserInfo());
			}
		}
		if (CommonDaoFactory.Update(updates) < 0) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Delete order failed!");
			error = true;
			return;
		}
	}
	
	private void doRatingOrder() {
		if (!worldInfo.isActiveEventFlag(EventFlagEnum.ORDER_VOTING)) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Voting system is disabled!");
			error = true;
			return;
		}
		String strOrderId = getContext().getRequestParameter("order_id");
		if (strOrderId == null || !CommonMethod.isValidNumeric(strOrderId, 1, Long.MAX_VALUE)) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Order's id is invalid!");
			error = true;
			return;
		}
		String strStar = getContext().getRequestParameter("star");
		if (!CommonMethod.isValidNumeric(strStar, 1, CommonDefine.MAX_MILKTEA_VOTING_STAR)) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Invalid star num:" + strStar);
			error = true;
			return;
		}
		String strComment = getContext().getRequestParameter("comment");
		Byte star = Byte.parseByte(strStar);
		Date now = new Date();
		Order order = OrderService.getOrderInfoById(Long.parseLong(strOrderId));
		if (order == null) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Order is null:" + strOrderId);
			error = true;
			return;
		}
		if (order.isVoted() && order.getVotingStar() > 0) {
			if (order.isCommented()) {
				GameLog.getInstance().error("[MilkTeaManageOrder] Order is voted up!");
				error = true;
				return;
			} else if (StringUtils.isBlank(strComment)) {
				GameLog.getInstance().warn("[MilkTeaManageOrder] Comment is empty!");
				error = true;
				return;
			}
		}
		Menu menuInfo = MenuService.getMenuById(order.getMenuId());
		if (menuInfo == null) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Order's menu is null");
			error = true;
			return;
		}
		if (!menuInfo.isCompleted()) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Order's menu haven't completed yet!");
			error = true;
			return;
		}
		Shop shopInfo = ShopService.getShopById(order.getShopId());
		if (shopInfo == null) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Order's shop is null!");
			error = true;
			return;
		}
		List<DaoValue> updates = new ArrayList<>();
		updates.add(order.getOrderInfo());
		order.setUpdated(CommonMethod.getFormatDateString(now));
		if (!order.isCommented() && StringUtils.isNotBlank(strComment)) {
			order.setComment(strComment.trim());
			order.setFlag(MilkTeaOrderFlag.COMMENT.getValidFlag(order.getFlag()));
		}
		if (!order.isVoted() || order.getVotingStar() <= 0) {
			order.setVotingStar(star);
			if (!worldInfo.isActiveEventFlag(EventFlagEnum.RATING_CRONJOB)) {
				order.setFlag(MilkTeaOrderFlag.VOTE.getValidFlag(order.getFlag()));
				DishRating dishRatingInfo = DishRatingService.getDishRatingInfo(order.getDishCode(), order.getShopId());
				if (dishRatingInfo == null) {
					dishRatingInfo = new DishRating();
					dishRatingInfo.setShopId(shopInfo.getId());
					dishRatingInfo.setName(order.getDishName());
					dishRatingInfo.setCode(order.getDishCode());
				} else {
					dishRatingInfo.setUpdated(CommonMethod.getFormatDateString(now));
				}
				dishRatingInfo.setOrderCount(dishRatingInfo.getOrderCount() + order.getQuantity());
				dishRatingInfo.setStarCount(dishRatingInfo.getStarCount() + order.getQuantity() * star);
				updates.add(dishRatingInfo.getDishRatingInfo());
				
				shopInfo.setVotingCount(shopInfo.getVotingCount() + 1);
				shopInfo.setStarCount(shopInfo.getStarCount() + star);
				updates.add(shopInfo.getShopInfo());
			} else {
				GameLog.getInstance().info("[MilkTeaManageOrder] Rating update by cronjob!");
				order.setFlag(MilkTeaOrderFlag.VOTE_CRON.getValidFlag(order.getFlag()));
			}
		}
		if (CommonDaoFactory.Update(updates) < 0) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Update failed!");
			error = true;
			return;
		}
		putJsonData("data", MilkTeaCommonFunc.getOrderRatingWithComment(order));
	}

	@Override
	public void onRender() {
		if (error) {
			return;
		}
		if (mode == MODE_RATING) { // data has been sent out in the function itself.
			super.onRender();
			return;
		}
		MilkTeaEndPoint.sendSocketUpdate(menuInfo.getId(), MilkTeaSocketType.MENU_DETAIL, MilkTeaSocketMessage.UPDATE_ORDER_LIST, getUserInfo().getId());
		super.onRender();
	}

	private void returnDetail() {
		if (error) {
			return;
		}
		if (menuInfo == null || getUserInfo() == null) {
			return;
		}
		List<MenuItem> listMenuItem = MenuService.getMenuItemListByShopId(menuInfo.getShopId());
		if (listMenuItem == null || listMenuItem.isEmpty()) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (MenuItem item : listMenuItem) {
			sb.append(MilkTeaCommonFunc.getPlaceOrderModal(milkteaUser, menuInfo, item, getMessages(), getHostURL() + getContextPath()));
		}
		putJsonData("model_list", sb.toString());
		List<Order> listOrder = OrderService.getOrderInfoListByMenuId(menuInfo.getId());
		putJsonData("order_list", MilkTeaCommonFunc.getHtmlListOrder(listOrder, menuInfo, userInfo, getHostURL() + getContextPath(), getMessages(), getWorldInfo()));
	}
	
}