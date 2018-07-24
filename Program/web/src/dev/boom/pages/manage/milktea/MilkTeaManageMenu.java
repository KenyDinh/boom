package dev.boom.pages.manage.milktea;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaMenuStatus;
import dev.boom.common.milktea.MilkTeaSocketMessage;
import dev.boom.common.milktea.MilkTeaSocketType;
import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.MenuInfo;
import dev.boom.entity.info.MilkTeaUserInfo;
import dev.boom.entity.info.OrderInfo;
import dev.boom.entity.info.ShopInfo;
import dev.boom.entity.info.ShopOptionInfo;
import dev.boom.entity.info.UserInfo;
import dev.boom.pages.manage.ManagePageBase;
import dev.boom.services.CommonDaoService;
import dev.boom.services.MenuService;
import dev.boom.services.MilkTeaUserService;
import dev.boom.services.OrderService;
import dev.boom.services.ShopService;
import dev.boom.services.UserService;
import dev.boom.socket.endpoint.MilkTeaEndPoint;

public class MilkTeaManageMenu extends ManagePageBase {

	private static final long serialVersionUID = 1L;

	private static final int MODE_EDIT = 1;
	private static final int DEFAULT_ICE = 100;
	private static final int DEFAULT_SUGAR = 100;

	private int mode = 0;

	public MilkTeaManageMenu() {
	}

	@Override
	public boolean onSecurityCheck() {
		return super.onSecurityCheck();
	}

	@Override
	public void onInit() {
		super.onInit();
		String strMode = getContext().getRequestParameter("mode");
		if (CommonMethod.isValidNumeric(strMode, 1, 1)) {
			mode = Integer.parseInt(strMode);
		}

	}

	@Override
	public void onPost() {
		super.onPost();
		switch (mode) {
		case MODE_EDIT:
			String strMenuId = getContext().getRequestParameter("menu_id");
			if (CommonMethod.isValidNumeric(strMenuId, 1, Long.MAX_VALUE)) {
				MenuInfo menuInfo = MenuService.getMenuById(Long.parseLong(strMenuId));
				if (menuInfo == null) {
					addModel("error", "menu is null");
					return;
				}
				if (menuInfo.getStatus() == MilkTeaMenuStatus.COMPLETED.ordinal()) {
					addModel("error", "can not modify completed menu!");
					return;
				}
				boolean update = false;
				String strSale = getContext().getRequestParameter("sale");
				if (CommonMethod.isValidNumeric(strSale, 0, 100)) {
					int sale = Integer.parseInt(strSale);
					if (sale != menuInfo.getSale()) {
						menuInfo.setSale((short) sale);
						update = true;
						if (sale == 0) {
							menuInfo.setCode("");
						}
					}
				}
				String strCode = getContext().getRequestParameter("code");
				if (strCode != null) {
					if (!menuInfo.getCode().isEmpty() && !strCode.equals(menuInfo.getCode())) {
						menuInfo.setCode(strCode);
						update = true;
					}
				}
				String strMaxDiscount = getContext().getRequestParameter("max_discount");
				if (CommonMethod.isValidNumeric(strMaxDiscount, 0, Long.MAX_VALUE)) {
					long maxDC = Long.parseLong(strMaxDiscount);
					if (maxDC != menuInfo.getMax_discount()) {
						menuInfo.setMax_discount(maxDC);
						update = true;
					}
				}
				String strShippingFee = getContext().getRequestParameter("shipping_fee");
				if (CommonMethod.isValidNumeric(strShippingFee, 0, Long.MAX_VALUE)) {
					long fee = Long.parseLong(strShippingFee);
					if (fee != menuInfo.getShipping_fee()) {
						menuInfo.setShipping_fee(fee);
						update = true;
					}
				}
				String strDescription = getContext().getRequestParameter("description");
				if (strDescription != null) {
					if (menuInfo.getDescription() == null || !strDescription.equals(menuInfo.getDescription())) {
						menuInfo.setDescription(strDescription);
						update = true;
					}
				}
				String strStatus = getContext().getRequestParameter("status");
				if (CommonMethod.isValidNumeric(strStatus, 0, Integer.MAX_VALUE)) {
					MilkTeaMenuStatus stt = MilkTeaMenuStatus.valueOf(Integer.parseInt(strStatus));
					if (menuInfo.getStatus() != stt.ordinal()) {
						menuInfo.setStatus((byte) stt.ordinal());
						update = true;
					}
				}
				if (update) {
					doUpdateMenu(menuInfo);
					MilkTeaEndPoint.sendSocketUpdate(MilkTeaSocketType.MENU_LIST, MilkTeaSocketMessage.UPDATE_MENU_LIST);
					MilkTeaEndPoint.sendSocketUpdate(MilkTeaSocketType.MENU_DETAIL, MilkTeaSocketMessage.UPDATE_MENU_DETAIL);
					mode = 0;
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onRender() {
		super.onRender();
		if (getModel().get("error") != null) {
			return;
		}
		switch (mode) {
		case MODE_EDIT:
			String strMenuId = getContext().getRequestParameter("menu_id");
			if (CommonMethod.isValidNumeric(strMenuId, 1, Long.MAX_VALUE)) {
				MenuInfo menuInfo = MenuService.getMenuById(Long.parseLong(strMenuId));
				if (menuInfo == null) {
					addModel("error", "menu is null");
					return;
				}
				if (menuInfo.getStatus() == MilkTeaMenuStatus.COMPLETED.ordinal()) {
					addModel("error", "can not modify completed menu!");
					return;
				}
				addModel("menuInfo", menuInfo);
				StringBuilder sb = new StringBuilder();
				sb.append("<select id=\"status\" class=\"form-control\" name=\"status\">");
				for (MilkTeaMenuStatus status : MilkTeaMenuStatus.values()) {
					sb.append("<option value=\"").append(status.ordinal()).append("\" ");
					if (status.ordinal() == menuInfo.getStatus()) {
						sb.append("selected");
					}
					sb.append(">").append(status.name()).append("</option>");
				}
				sb.append("</select>");
				addModel("selectStatus", sb.toString());
			}
			break;
		default:
			byte[] status = new byte[] { (byte) MilkTeaMenuStatus.INIT.ordinal(), (byte) MilkTeaMenuStatus.OPENING.ordinal(), (byte) MilkTeaMenuStatus.DELIVERING.ordinal() };
			List<MenuInfo> menuList = MenuService.getMenuListByStatusList(status);
			initTableMenuList(menuList);
			break;
		}
	}

	private void initTableMenuList(List<MenuInfo> menuList) {
		StringBuilder table = new StringBuilder();
		table.append("<table class=\"table table-hover\">");
		table.append("<thead>");
		table.append("<th scope=\"col\">").append("Id").append("</th>");
		table.append("<th scope=\"col\">").append("Name").append("</th>");
		table.append("<th scope=\"col\">").append("Sale").append("</th>");
		table.append("<th scope=\"col\">").append("Code").append("</th>");
		table.append("<th scope=\"col\">").append("Max Discount").append("</th>");
		table.append("<th scope=\"col\">").append("Shipping Fee").append("</th>");
		table.append("<th scope=\"col\">").append("Description").append("</th>");
		table.append("<th scope=\"col\">").append("Status").append("</th>");
		table.append("<th scope=\"col\">").append("Created").append("</th>");
		table.append("<th scope=\"col\">").append("Updated").append("</th>");
		table.append("<th scope=\"col\">").append("Edit").append("</th>");
		table.append("</thead>");
		table.append("<tbody>");
		if (menuList == null || menuList.isEmpty()) {
			table.append("<tr><td colspan=\"9\">No menu found!</td></tr>");
		} else {
			for (MenuInfo menu : menuList) {
				table.append("<tr>");
				table.append("<td>").append(String.format("<a href=\"%s\">%d</a>", getHostURL() + getContextPath() + "/manage/milktea/milk_tea_manage_order.htm?menu_id=" + menu.getId(), menu.getId())).append("</td>");
				table.append("<td>").append(menu.getName()).append("</td>");
				table.append("<td>").append(menu.getSale() > 0 ? (menu.getSale() + "%") : "0").append("</td>");
				table.append("<td>").append(menu.getCode()).append("</td>");
				if (menu.getMax_discount() > 0) {
					table.append("<td>").append(MilkTeaCommonFunc.getShowPriceWithUnit(menu.getMax_discount(), "", getMessages())).append("</td>");
				} else {
					table.append("<td>-</td>");
				}
				table.append("<td>").append(MilkTeaCommonFunc.getShowPriceWithUnit(menu.getShipping_fee(), "", getMessages())).append("</td>");
				String desc = StringEscapeUtils.escapeHtml(menu.getDescription());
				table.append("<td><div data-toggle=\"tooltip\" data-placement=\"bottom\" style=\"width:200px;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;\" title=\"").append(desc).append("\">").append(desc).append("</div></td>");
				table.append("<td>").append(MilkTeaMenuStatus.valueOf(menu.getStatus()).name()).append("</td>");
				table.append("<td>").append(CommonMethod.getFormatDateString(menu.getCreated())).append("</td>");
				table.append("<td>").append(CommonMethod.getFormatDateString(menu.getUpdated())).append("</td>");
				table.append("<td>");
				if (menu.getStatus() < MilkTeaMenuStatus.COMPLETED.ordinal()) {
					table.append(String.format("<a href=\"%s\">Edit</a>", getPagePath(this.getClass()) + "?mode=" + MODE_EDIT + "&menu_id=" + menu.getId()));
				}
				table.append("</td>");
				table.append("</tr>");
			}
		}
		table.append("</tbody>");
		table.append("</table>");
		addModel("table", table.toString());
		addHomeBackLink();
	}

	private void doUpdateMenu(MenuInfo menuInfo) {
		menuInfo.setUpdated(new Date());
		List<DaoValue> updates = new ArrayList<>();
		updates.add(menuInfo);
		if (menuInfo.getStatus() == MilkTeaMenuStatus.COMPLETED.ordinal() || menuInfo.getStatus() == MilkTeaMenuStatus.CANCELED.ordinal()) {
			Date now = new Date();
			ShopInfo shopInfo = ShopService.getShopById(menuInfo.getShop_id());
			List<OrderInfo> orderList = OrderService.getOrderInfoListByMenuId(menuInfo.getId());
			if (orderList != null && !orderList.isEmpty()) {
				if (menuInfo.getStatus() == MilkTeaMenuStatus.COMPLETED.ordinal()) {
					long totalMoney = MilkTeaCommonFunc.getTotalMoney(orderList);
					Map<Long, MilkTeaUserInfo> listUser = new HashMap<>();
					// Map<Long, DishInfo> listDish = new HashMap<>();
					for (OrderInfo order : orderList) {
						long userId = order.getUser_id();
						MilkTeaUserInfo mtUser = null;
						if (listUser.containsKey(userId)) {
							mtUser = listUser.get(userId);
						} else {
							mtUser = MilkTeaUserService.getFridayUserInfoByUserId(userId);
						}
						if (mtUser == null) {
							UserInfo userInfo = UserService.getUserById(userId);
							if (userInfo == null) {
								GameLog.getInstance().error("user is null, user_id: " + userId + " in order_id: " + order.getId());
								return;
							}
							mtUser = new MilkTeaUserInfo();
							mtUser.setUser_id(userInfo.getId());
							mtUser.setUsername(userInfo.getUsername());
						}
						long finalCost = MilkTeaCommonFunc.getFinalCost(totalMoney, orderList.size(), menuInfo, order);
						mtUser.setTotal_money(mtUser.getTotal_money() + finalCost);
						mtUser.setDish_count(mtUser.getDish_count() + Math.max(1, order.getQuantity()));
						mtUser.setOrder_count(mtUser.getOrder_count() + 1);
						mtUser.setLatest_order_id(Math.max(order.getId(), mtUser.getLatest_order_id()));
						List<Long> optionIds = new ArrayList<>();
						String optionList = order.getOption_list();
						if (optionList != null && !optionList.isEmpty()) {
							String[] optIds = optionList.split(",");
							for (String optId : optIds) {
								if (StringUtils.isNotBlank(optId) && StringUtils.isNumeric(optId)) {
									long oId = Long.parseLong(optId);
									if (optionIds.contains(oId)) {
										GameLog.getInstance().warn("this order:" + order.getId() + " has the same option id: " + oId);
										continue;
									}
									optionIds.add(oId);
								}
							}
						}
						long countIce = 0;
						long countSugar = 0;
						if (!optionIds.isEmpty()) {
							List<ShopOptionInfo> shopOptionList = ShopService.getShopOptionListByIds(optionIds);
							if (shopOptionList != null && !shopOptionList.isEmpty()) {
								for (ShopOptionInfo optionInfo : shopOptionList) {
									if (optionInfo.getType() == ShopService.ITEM_OPTION_TYPE_TOPPING || optionInfo.getType() == ShopService.ITEM_OPTION_TYPE_ADDITION) {
										mtUser.setTotal_topping(mtUser.getTotal_topping() + 1);
									} else if (optionInfo.getType() == ShopService.ITEM_OPTION_TYPE_ICE) {
										countIce++;
										mtUser.setTotal_ice(mtUser.getTotal_ice() + MilkTeaCommonFunc.calcOptionAmount(optionInfo.getName()));
									} else if (optionInfo.getType() == ShopService.ITEM_OPTION_TYPE_SUGAR) {
										countSugar++;
										mtUser.setTotal_sugar(mtUser.getTotal_sugar() + MilkTeaCommonFunc.calcOptionAmount(optionInfo.getName()));
									} 
								}
							}
						}
						if (countIce == 0) {
							mtUser.setTotal_ice(mtUser.getTotal_ice() + DEFAULT_ICE);
						}
						if (countSugar == 0) {
							mtUser.setTotal_sugar(mtUser.getTotal_sugar() + DEFAULT_SUGAR);
						}
						listUser.put(userId, mtUser);
						order.setFinal_price(finalCost);
						order.setUpdated(now);
						updates.add(order);
					}
					for (Long userId : listUser.keySet()) {
						updates.add(listUser.get(userId));
					}
				} else {
					for (OrderInfo order : orderList) {
						order.setDelete();
						updates.add(order);
					}
				}
			}
			if (shopInfo != null) {
				if (menuInfo.getStatus() == MilkTeaMenuStatus.COMPLETED.ordinal()) {
					shopInfo.setOpening_count(shopInfo.getOpening_count() + 1);
					if (orderList != null && !orderList.isEmpty()) {
						long c = 0;
						for (OrderInfo order : orderList) {
							c += Math.max(1, order.getQuantity());
						}
						shopInfo.setOrdered_dish_count(shopInfo.getOrdered_dish_count() + c);
					}
					updates.add(shopInfo);
				}
			} else {
				GameLog.getInstance().error("shop is null, shop_id:" + menuInfo.getShop_id());
			}
		}
		if (!CommonDaoService.update(updates)) {
			GameLog.getInstance().error("update failed!");
		}
	}
}
