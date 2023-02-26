package dev.boom.pages.manage.milktea;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.Department;
import dev.boom.common.enums.ManageLogType;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaItemOptionType;
import dev.boom.common.milktea.MilkTeaMenuStatus;
import dev.boom.common.milktea.MilkTeaOrderFlag;
import dev.boom.common.milktea.MilkTeaSocketMessage;
import dev.boom.common.milktea.MilkTeaSocketType;
import dev.boom.common.milktea.MilkteaMenuFlag;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.pages.manage.ManagePageBase;
import dev.boom.pages.milktea.MilkTeaMenu;
import dev.boom.services.ManageLogService;
import dev.boom.services.Menu;
import dev.boom.services.MenuService;
import dev.boom.services.MilkteaUser;
import dev.boom.services.MilkteaUserService;
import dev.boom.services.Order;
import dev.boom.services.OrderService;
import dev.boom.services.Shop;
import dev.boom.services.ShopService;
import dev.boom.services.User;
import dev.boom.services.UserService;
import dev.boom.socket.SocketSessionPool;
import dev.boom.socket.endpoint.ManageMilkTeaEndPoint;
import dev.boom.socket.endpoint.MilkTeaEndPoint;

public class MilkTeaManageMenu extends ManagePageBase {

	private static final long serialVersionUID = 1L;

	private static final int MODE_EDIT = 1;

	private int mode = 0;

	public MilkTeaManageMenu() {
	}

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!userInfo.isMilkteaAdmin()) {
			return false;
		}
		return true;
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
				Menu menuInfo = MenuService.getMenuById(Long.parseLong(strMenuId));
				if (menuInfo == null) {
					addModel("error", "menu is null");
					return;
				}
				if (!menuInfo.isEditable()) {
					addModel("error", "can not modify the menu!");
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
					if (maxDC != menuInfo.getMaxDiscount()) {
						menuInfo.setMaxDiscount(maxDC);
						update = true;
					}
				}
				String strShippingFee = getContext().getRequestParameter("shipping_fee");
				if (CommonMethod.isValidNumeric(strShippingFee, 0, Long.MAX_VALUE)) {
					long fee = Long.parseLong(strShippingFee);
					if (fee != menuInfo.getShippingFee()) {
						menuInfo.setShippingFee(fee);
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
				String strExpiration = getContext().getRequestParameter("expired");
				if (CommonMethod.isValidDateTimeFormat(strExpiration)) {
					Date expired = CommonMethod.getDate(strExpiration, CommonDefine.DATE_FORMAT_PATTERN);
					if (expired != null && expired.getTime() != menuInfo.getExpiredDate().getTime()) {
						menuInfo.setExpired(CommonMethod.getFormatDateString(expired));
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
				menuInfo.setFlag(0);
				update = true;
				String[] menuFlags = getContext().getRequestParameterValues("menu_flag");
				if (menuFlags != null && menuFlags.length > 0) {
					for (String strFlag : menuFlags) {
						if (CommonMethod.isValidNumeric(strFlag, 1, Integer.MAX_VALUE)) {
							MilkteaMenuFlag mmf = MilkteaMenuFlag.valueOf(Integer.parseInt(strFlag));
							if (mmf == MilkteaMenuFlag.INVALID) {
								continue;
							}
							menuInfo.addFlag(mmf);
						}
					}
				}
				menuInfo.setDept(0);
				String[] showFlags = getContext().getRequestParameterValues("show_flag");
				if (showFlags != null && showFlags.length > 0) {
					for (String strFlag : showFlags) {
						if (CommonMethod.isValidNumeric(strFlag, 1, Integer.MAX_VALUE)) {
							Department dept = Department.valueOf(Integer.parseInt(strFlag));
							if (dept == Department.NONE) {
								continue;
							}
							menuInfo.addDeptFlag(dept);
						}
					}
				}
				if (update) {
					doUpdateMenu(menuInfo);
					MilkTeaEndPoint.sendSocketUpdate(menuInfo.getId(), MilkTeaSocketType.MENU_LIST, MilkTeaSocketMessage.UPDATE_MENU_LIST);
					MilkTeaEndPoint.sendSocketUpdate(menuInfo.getId(), MilkTeaSocketType.MENU_DETAIL, MilkTeaSocketMessage.UPDATE_MENU_DETAIL);
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
				Menu menuInfo = MenuService.getMenuById(Long.parseLong(strMenuId));
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
				sb.setLength(0);
				for (MilkteaMenuFlag mmf : MilkteaMenuFlag.values()) {
					if (mmf == MilkteaMenuFlag.INVALID) {
						continue;
					}
					sb.append("<div class=\"custom-control custom-checkbox\">");
					sb.append("<input type=\"checkbox\" class=\"custom-control-input\" name=\"menu_flag\" id=\"menu-flag-" + mmf.ordinal() + "\" value=\"" + mmf.getFlag() + "\" " + (menuInfo.isActiveFlag(mmf) ? "checked" : "") + "/>");
					sb.append("<label class=\"custom-control-label\" for=\"menu-flag-" + mmf.ordinal() + "\">").append(getMessage(mmf.getLabel())).append("</label>");
					sb.append("</div>");
				}
				addModel("flags", sb.toString());
				sb.setLength(0);
				for (Department dept : Department.values()) {
					if (dept == Department.NONE) {
						continue;
					}
					sb.append("<div class=\"custom-control custom-checkbox\">");
					sb.append("<input type=\"checkbox\" class=\"custom-control-input\" name=\"show_flag\" id=\"show-flag-" + dept.ordinal() + "\" value=\"" + dept.getFlag() + "\" " + (menuInfo.isActiveDeptFlag(dept.getFlag()) ? "checked" : "") + "/>");
					sb.append("<label class=\"custom-control-label\" for=\"show-flag-" + dept.ordinal() + "\">").append(getMessage(dept.getLabel())).append("</label>");
					sb.append("</div>");
				}
				addModel("display", sb.toString());
			}
			break;
		default:
			byte[] status = new byte[] {MilkTeaMenuStatus.INIT.getStatus(), MilkTeaMenuStatus.OPENING.getStatus(), MilkTeaMenuStatus.DELIVERING.getStatus()};
			List<Menu> menuList = MenuService.getMenuListByStatusList(status);
			initTableMenuList(menuList);
			String token = SocketSessionPool.generateValidToken(ManageMilkTeaEndPoint.ENDPOINT_NAME, userInfo);
			String params = "?" + ManageMilkTeaEndPoint.VALIDATION_KEY + "=" + token;
			addModel("socket_url", getSocketUrl(ManageMilkTeaEndPoint.SOCKET_PATH, params));
			break;
		}
	}

	private void initTableMenuList(List<Menu> menuList) {
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
		table.append("<th scope=\"col\">").append("Display").append("</th>");
		table.append("<th scope=\"col\">").append("Created").append("</th>");
		table.append("<th scope=\"col\">").append("Expired").append("</th>");
		table.append("<th scope=\"col\">").append("Edit").append("</th>");
		table.append("</thead>");
		table.append("<tbody>");
		if (menuList == null || menuList.isEmpty()) {
			table.append("<tr><td colspan=\"12\">No menu found!</td></tr>");
		} else {
			for (Menu menu : menuList) {
				table.append("<tr>");
				table.append("<td>").append(String.format("<a href=\"%s\">%d</a>", getHostURL() + getContextPath() + "/manage/milktea/milk_tea_manage_order.htm?menu_id=" + menu.getId(), menu.getId())).append("</td>");
				table.append("<td>").append(menu.getName()).append("</td>");
				table.append("<td>").append(menu.getSale() > 0 ? (menu.getSale() + "%") : "0").append("</td>");
				table.append("<td>").append(menu.getCode()).append("</td>");
				if (menu.getMaxDiscount() > 0) {
					table.append("<td>").append(MilkTeaCommonFunc.getShowPriceWithUnit(menu.getMaxDiscount(), getMessages())).append("</td>");
				} else {
					table.append("<td>-</td>");
				}
				table.append("<td>").append(MilkTeaCommonFunc.getShowPriceWithUnit(menu.getShippingFee(), getMessages())).append("</td>");
				String desc = menu.getDescription();
				table.append("<td><div data-toggle=\"tooltip\" data-placement=\"bottom\" style=\"width:200px;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;\" title=\"").append(desc).append("\">").append(desc).append("</div></td>");
				table.append("<td>").append(MilkTeaMenuStatus.valueOf(menu.getStatus()).name()).append("</td>");
				table.append("<td>");
					String show = "";
					for (Department dept : Department.values()) {
						if (dept == Department.NONE) {
							continue;
						}
						if (!menu.isActiveDeptFlag(dept.getFlag())) {
							continue;
						}
						if (show.length() > 0) {
							show += ",";
						}
						show += dept.name();
					}
					show = (show.length() > 0) ? show : "---";
					table.append(show);
				table.append("</td>");
				table.append("<td>").append((menu.getCreated())).append("</td>");
				table.append("<td>").append((menu.getExpired())).append("</td>");
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
		addBackLink(MilkTeaMenu.class, "MSG_MAIN_NAV_BAR_MILKTEA");
	}

	private void doUpdateMenu(Menu menuInfo) {
		menuInfo.setUpdated(CommonMethod.getFormatStringNow());
		List<DaoValue> updates = new ArrayList<>();
		updates.add(menuInfo.getMenuInfo());
		if (menuInfo.getStatus() == MilkTeaMenuStatus.COMPLETED.ordinal() || menuInfo.getStatus() == MilkTeaMenuStatus.CANCELED.ordinal()) {
			Shop shopInfo = ShopService.getShopById(menuInfo.getShopId());
			List<Order> orderList = OrderService.getOrderInfoListByMenuId(menuInfo.getId());
			if (orderList != null && !orderList.isEmpty()) {
				if (menuInfo.getStatus() == MilkTeaMenuStatus.COMPLETED.ordinal()) {
					long totalMoney = MilkTeaCommonFunc.getTotalMoney(orderList);
					Map<Long, MilkteaUser> listUser = new HashMap<>();
					for (Order order : orderList) {
						long userId = order.getUserId();
						MilkteaUser mtUser = null;
						if (listUser.containsKey(userId)) {
							mtUser = listUser.get(userId);
						} else {
							mtUser = MilkteaUserService.getMilkTeaUserInfoById(userId);
						}
						if (mtUser == null) {
							User userInfo = UserService.getUserById(userId);
							if (userInfo == null) {
								GameLog.getInstance().error("user is null, user_id: " + userId + " in order_id: " + order.getId());
								return;
							}
							mtUser = new MilkteaUser();
							mtUser.setUserId(userInfo.getId());
							mtUser.setUsername(userInfo.getUsername());
						}
						long finalCost = MilkTeaCommonFunc.getFinalCost(totalMoney, orderList.size(), menuInfo, order);
						mtUser.setTotalMoney(mtUser.getTotalMoney() + finalCost);
						mtUser.setDishCount(mtUser.getDishCount() + Math.max(1, order.getQuantity()));
						mtUser.setOrderCount(mtUser.getOrderCount() + 1);
						mtUser.setLatestOrderId(Math.max(order.getId(), mtUser.getLatestOrderId()));
						mtUser.setTotalIce(mtUser.getTotalIce() + order.getTotalOption(MilkTeaItemOptionType.ICE));
						mtUser.setTotalSugar(mtUser.getTotalSugar() + order.getTotalOption(MilkTeaItemOptionType.SUGAR));
						mtUser.setTotalTopping(mtUser.getTotalTopping() + order.getTotalOption(MilkTeaItemOptionType.TOPPING) * order.getQuantity());
						listUser.put(userId, mtUser);
						order.setFinalPrice(finalCost);
						order.setUpdated(CommonMethod.getFormatStringNow());
						updates.add(order.getOrderInfo());
					}
					for (Long userId : listUser.keySet()) {
						updates.add(listUser.get(userId).getMilkteaUserInfo());
					}
				} else {
					for (Order order : orderList) {
						order.setFlag(MilkTeaOrderFlag.CANCELED.getValidFlag(order.getFlag()));
						updates.add(order.getOrderInfo());
					}
				}
			}
			if (shopInfo != null) {
				if (menuInfo.getStatus() == MilkTeaMenuStatus.COMPLETED.ordinal()) {
					shopInfo.setOpeningCount(shopInfo.getOpeningCount() + 1);
					if (orderList != null && !orderList.isEmpty()) {
						long c = 0;
						for (Order order : orderList) {
							c += Math.max(1, order.getQuantity());
						}
						shopInfo.setOrderedDishCount(shopInfo.getOrderedDishCount() + c);
					}
					updates.add(shopInfo.getShopInfo());
				}
			} else {
				GameLog.getInstance().error("shop is null, shop_id:" + menuInfo.getShopId());
			}
		}
		if (CommonDaoFactory.Update(updates) < 0) {
			GameLog.getInstance().error("[MilkteaManageMenu] update menu failed!");
		} else {
			ManageLogService.createManageLog(userInfo, ManageLogType.UPDATE_MENU, menuInfo.getName());
		}
	}

	@Override
	protected int getTabIndex() {
		// TODO Auto-generated method stub
		return 1;
	}
	
}
