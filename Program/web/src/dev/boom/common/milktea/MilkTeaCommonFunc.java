package dev.boom.common.milktea;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonHtmlFunc;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.BootStrapColorEnum;
import dev.boom.common.enums.UserFlagEnum;
import dev.boom.core.GameLog;
import dev.boom.milktea.object.MenuItem;
import dev.boom.milktea.object.MenuItemOption;
import dev.boom.milktea.object.MenuItemSelectionLimit;
import dev.boom.services.DishRatingInfo;
import dev.boom.services.DishRatingService;
import dev.boom.services.MenuInfo;
import dev.boom.services.MenuService;
import dev.boom.services.MilkTeaUserInfo;
import dev.boom.services.MilkTeaUserService;
import dev.boom.services.OrderInfo;
import dev.boom.services.ShopInfo;
import dev.boom.services.ShopService;
import dev.boom.services.UserInfo;

public class MilkTeaCommonFunc {
	
	public static final int DEFAULT_PERCENTAGE_OPTION = 100;
	
	private static final String DISH_BORDER_COLOR = BootStrapColorEnum.PRIMARY.getColorCode();
	private static final String ORDER_BORDER_COLOR = BootStrapColorEnum.WARNING.getColorCode();
	
	// ========================================== //
	@SuppressWarnings("rawtypes")
	public static String getHtmlListMenuItem(MenuInfo menuInfo, List<MenuItem> listMenuItem, String contextPath, UserInfo userInfo, Map messages) {
		if (listMenuItem == null || listMenuItem.isEmpty()) {
			return "";
		}
		sortListMenuItemByType(listMenuItem);
		Map<Integer, DishRatingInfo> dishRatingMap = null;
		if (menuInfo != null) {
			dishRatingMap = DishRatingService.getDishRatingInfoMap(menuInfo.getShopId());
		}
		boolean isAdminAccess = (userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag()));
		StringBuilder sb = new StringBuilder();
		String type = null;
		int groupType = 0;
		for (MenuItem item : listMenuItem) {
			int itemCode = item.getName().hashCode();
			String strRating = null;
			if (dishRatingMap != null && dishRatingMap.containsKey(itemCode)) {
				String rating = dishRatingMap.get(itemCode).getFormatRating();
				if (rating != null) {
					strRating = MessageFormat.format((String)messages.get("MSG_MILK_TEA_DISH_RATING"), rating, CommonDefine.MAX_MILKTEA_VOTING_STAR, dishRatingMap.get(itemCode).getOrderCount());
				}
			}
			if (type == null || !type.equals(item.getType())) {
				if (type != null) {
					sb.append("</div>");
				}
				groupType++;
				type = item.getType();
				sb.append("<div class=\"\" data=\"" + type + "\">");
					sb.append(String.format("<div class=\"row\" id=\"menu-item-group-%d\" >", groupType));
						sb.append("<span class=\"text-success\">").append(type).append("</span>");
					sb.append("</div>");
			}
			sb.append(String.format("<div class=\"row menu-item bg-light text-info\" style=\"min-height:3.75rem;border-bottom:0.0625rem solid %s;\" >", DISH_BORDER_COLOR));
				String itemPriceBlock = getItemPriceBlock(item, menuInfo, userInfo, messages);
				sb.append("<div class=\"col-lg-2 col-md-3 col-8\">");
					String imgSrc = item.getImage_url();
					if (imgSrc == null || !imgSrc.startsWith("http") || imgSrc.contains("no-image")) {
						imgSrc = contextPath + "/img/milktea/no_image.png?@no_image.png@";
					}
					sb.append(String.format("<img src=\"%s\" class=\"dish-image\" title=\"\" alt=\"\" style=\"height:3.125rem;padding-left:0;margin-top:0.3125rem;\"/>", imgSrc));
					if (strRating != null) {
						sb.append("<span class=\"text-warning d-sm-none\" style=\"font-size:0.8125rem;margin-left:0.25rem;\">").append(strRating).append("</span>");
					}
				sb.append("</div>");
				
				sb.append("<div class=\"col-4 d-lg-none d-md-none\">");
					sb.append(itemPriceBlock);
				sb.append("</div>");
				
				sb.append("<div class=\"col-lg-7 col-md-6 col-sm-12\">");
					sb.append("<div class=\"row\" style=\"position:relative;\"><div class=\"col-lg-12\" style=\"padding:0;margin-left:0.125rem;margin-right:0.125rem;\">");
						sb.append("<span class=\"item-findable\" style=\"\">").append(item.getName()).append("</span>");
					sb.append("</div></div>");
					if (item.getDesc() != null && item.getDesc().length() > 0) {
						sb.append("<div class=\"row font-italic\" style=\"font-size:0.8rem;color:lightgray;\">");
						sb.append("<div class=\"col-lg-12\" style=\"padding:0;margin-left:0.125rem;margin-right:0.125rem;line-height:1.15;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;\">");
						sb.append("<span data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"" + item.getDesc() + "\">").append(item.getDesc()).append("</span>");
						sb.append("</div>");
						sb.append("</div>");
					}
					sb.append("<div class=\"row\" style=\"position:relative;\"><div class=\"col-lg-12\" style=\"font-size:0.8125rem;padding:0;margin-left:0.125rem;margin-right:0.125rem;margin-top:0.25rem;\">");
					if (strRating != null) {
						sb.append("<span class=\"text-warning d-none d-sm-block\" style=\"font-size:0.8125rem;\">").append(strRating).append("</span>");
					}
					sb.append("</div></div>");
				sb.append("</div>");
				
				sb.append("<div class=\"col-lg-3 col-md-3 d-none d-lg-block d-md-block\">");
					sb.append(itemPriceBlock);
				sb.append("</div>");
			sb.append("</div>");
		}
		if (type != null) {
			sb.append("</div>");
		}
		// modal
		if (menuInfo != null && (menuInfo.isOpening() || (isAdminAccess && !menuInfo.isCompleted()))) {
			if (userInfo != null) {
				MilkTeaUserInfo milkteaUser = MilkTeaUserService.getMilkTeaUserInfoById(userInfo.getId());
				if (!UserFlagEnum.ACTIVE.isValid(userInfo.getFlag())) {
					sb.append(CommonHtmlFunc.getModalAlertWithMessage("account-not-active-modal", "warning", (String)messages.get("MSG_ACCOUNT_INACTIVE")));
				} else if (UserFlagEnum.MILKTEA_BANNED.isValid(userInfo.getFlag())) {
					sb.append(CommonHtmlFunc.getModalAlertWithMessage("account-banned-modal", "danger", (String)messages.get("MSG_ACCOUNT_MILKTEA_BANNED")));
				} else {
					sb.append("<div id=\"menu-item-place-modal\">");
					for (MenuItem item : listMenuItem) {
						sb.append(getPlaceOrderModal(milkteaUser, menuInfo, item, messages, contextPath));
					}
					sb.append("</div>");
				}
			}
		}
		
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private static String getItemPriceBlock(MenuItem item, MenuInfo menuInfo, UserInfo userInfo, Map messages) {
		boolean isAdminAccess = (userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag()));
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"row\"><div class=\"col-sm-12\" style=\"text-align:right;\">");
		sb.append(getShowPriceWithUnit(item.getPrice(), "height:100%;", "class=\"d-sm-none d-lg-block\"", messages));
		sb.append("</div></div>");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\" style=\"position:relative;height:1.6875rem;\">");
		if (menuInfo != null && (menuInfo.isOpening() || (isAdminAccess && !menuInfo.isCompleted()))) {
			if (userInfo != null) {
				if (!UserFlagEnum.ACTIVE.isValid(userInfo.getFlag())) {
					sb.append("<div data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Can not order!\" style=\"position:absolute;cursor:pointer;right:1.25rem;bottom:0;\" >");
					sb.append(String.format("<i style=\"color:%s;font-size:1.25rem;\" class=\"fas fa-plus-circle\" data-toggle=\"modal\" data-target=\"#account-not-active-modal\" ></i>", BootStrapColorEnum.WARNING.getColorCode(), item.getId()));
					sb.append("</div>");
				} else if (UserFlagEnum.MILKTEA_BANNED.isValid(userInfo.getFlag())) {
					sb.append("<div data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Banned!\" style=\"position:absolute;cursor:pointer;right:1.25rem;bottom:0;\" >");
					sb.append(String.format("<i style=\"color:%s;font-size:1.25rem;\" class=\"fas fa-plus-circle\" data-toggle=\"modal\" data-target=\"#account-banned-modal\" ></i>", BootStrapColorEnum.DANGER.getColorCode(), item.getId()));
					sb.append("</div>");
				} else {
					sb.append("<div data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Place the order\" style=\"position:absolute;cursor:pointer;right:1.25rem;bottom:0;\" >");
					sb.append(String.format("<i style=\"color:%s;font-size:1.25rem;\" class=\"fas fa-plus-circle\" data-toggle=\"modal\" data-target=\"#place-order-modal-%d\" ></i>", BootStrapColorEnum.INFO.getColorCode(), item.getId()));
					sb.append("</div>");
				}
			} else {
				sb.append("<div data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Login\" style=\"position:absolute;cursor:pointer;right:1.25rem;bottom:0;\" >");
				sb.append(String.format("<i style=\"color:%s;font-size:1.25rem;\" class=\"fas fa-plus-circle\" data-toggle=\"modal\" data-target=\"#login-form-modal\" ></i>", BootStrapColorEnum.INFO.getColorCode()));
				sb.append("</div>");
			}
		}
		sb.append("</div></div>");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getPlaceOrderModal(MilkTeaUserInfo milkteaUser, MenuInfo menuInfo, MenuItem item, Map messages, String contextPath) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<div class=\"modal fade\" id=\"place-order-modal-%d\">", item.getId()));
			sb.append("<div class=\"modal-dialog modal-dialog-centered modal-lg\" >");
				sb.append("<div class=\"modal-content\" >");
					//header
					sb.append("<div class=\"modal-header bg-primary\">");
						sb.append("<h4 class=\"modal-title\">").append(messages.get("MSG_MILK_TEA_PLACE_ORDER_HEADING")).append("</h4>");
						sb.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>");
					sb.append("</div>");
					//body
					sb.append("<div class=\"modal-body\">");
						sb.append(String.format("<form id=\"place-order-form-%d\">", item.getId()));
							sb.append("<div class=\"form-group\">");
							sb.append("<div class=\"row\">");
								sb.append("<div class=\"col-4 col-sm-4\" >");
								String imgSrc = item.getImage_url();
									if (imgSrc == null || !imgSrc.startsWith("http")) {
										imgSrc = contextPath + "/img/milktea/no_image.png?@no_image.png@";
									}
									sb.append(String.format("<img src=\"%s\" title=\"\" alt=\"\" style=\"height:3.125rem;\"/>", imgSrc));
								sb.append("</div>");
								sb.append("<div class=\"col-8 col-sm-8\">");
									sb.append("<label class=\"font-weight-bold text-info\" style=\"font-size:1.25rem;\">").append(item.getName()).append(" + ").append(getShowPriceWithUnit(item.getPrice(), messages)).append("</label>");
								sb.append("</div>");
							sb.append("</div>");
							if (item.getDesc() != null && item.getDesc().length() > 0) {
								sb.append("<div class=\"row\">");
								sb.append("<div class=\"col-sm-12 text-center font-italic\" style=\"font-size:0.9375rem;color:lightgray;margin-top:0.5rem;padding:0 10%;\">");
								sb.append(item.getDesc());
								sb.append("</div>");
								sb.append("</div>");
							}
							sb.append("</div>");
							sb.append("<div style=\"max-height:19.375rem;min-height:1.875rem;;overflow:auto;overflow-x:hidden;\">");
							// -------------------- //
							sb.append(getGroupInputFormItemOption(menuInfo, item, MilkTeaItemOptionType.SIZE, item.getList_size(), messages));
							// -------------------- //
							sb.append(getGroupInputFormItemOption(menuInfo, item, MilkTeaItemOptionType.ICE, item.getList_ice(), messages));
							// -------------------- //
							sb.append(getGroupInputFormItemOption(menuInfo, item, MilkTeaItemOptionType.SUGAR, item.getList_sugar(), messages));
							// -------------------- //
							sb.append(getGroupInputFormItemOption(menuInfo, item, MilkTeaItemOptionType.TOPPING, item.getList_topping(), messages));
							// -------------------- //
							sb.append(getGroupInputFormItemOption(menuInfo, item, MilkTeaItemOptionType.ADDITION, item.getList_addition(), messages));
							// -------------------- //
							sb.append("</div>");
							sb.append("<div class=\"form-group\">");
							sb.append("<div class=\"row\">");
								sb.append("<div class=\"col-md-6\">");
								sb.append(String.format("<label for=\"quantity-item-%d\" class=\"font-weight-bold\" style=\"font-size:1.125rem;\">", item.getId()));
								sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_QUANTITY")).append("</label>");
								sb.append(String.format("<input type=\"number\" id=\"quantity-item-%d\" class=\"form-control\" style=\"width:50%%;\" min=\"1\" max=\"15\" value=\"1\"/>", item.getId()));
								sb.append("</div>");
								
								sb.append("<div class=\"col-md-6\">");
								if (milkteaUser != null && milkteaUser.getFreeTicket() > 0) {
									sb.append(String.format("<label class=\"font-weight-bold\" style=\"font-size:1.125rem;\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"%s\">", messages.get("MSG_MILK_TEA_FREE_TICKET_EXPLANE")));
									sb.append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_FREE_TICKET_REMAIN"), milkteaUser.getFreeTicket())).append("</label>");
									sb.append("<div class=\"custom-control custom-checkbox\">");
									sb.append(String.format("<input type=\"checkbox\" class=\"custom-control-input\" id=\"free-ticket-%d\"/>", item.getId()));
									sb.append(String.format("<label class=\"custom-control-label\" for=\"free-ticket-%d\">%s</label>", item.getId(), messages.get("MSG_MILK_TEA_FREE_TICKET_USE_LABEL")));
									sb.append("</div>");
								}
								sb.append("</div>");
							sb.append("</div>");
							sb.append("</div>");
						sb.append(String.format("<button type=\"button\" class=\"btn btn-primary\" onclick=\"placeTheOrder(%d,%d);this.blur();return false;\">", menuInfo.getId(), item.getId())).append(messages.get("MSG_GENERAL_SUBMIT")).append("</button>");
					sb.append("</form>");
					sb.append("</div>");
					//footer
					sb.append("<div class=\"modal-footer\">");
						sb.append("<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">").append(messages.get("MSG_GENERAL_CLOSE")).append("</button>");
					sb.append("</div>");
				sb.append("</div>");
			sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private static String getGroupInputFormItemOption(MenuInfo menuInfo, MenuItem item, MilkTeaItemOptionType optionType, MenuItemOption[] listItemOption, Map messages) {
		if (listItemOption == null || listItemOption.length == 0) {
			return "";
		}
		MenuItemSelectionLimit limit = item.getLimit_select();
		if (limit == null) {
			limit = new MenuItemSelectionLimit();
			item.setLimit_select(limit);
		}
		String attention = "";
		String inputName = null;
		String label = null;
		String inputType = null;
		int rowCount = (listItemOption.length - 1) / 2 + 1;
		StringBuilder sb = new StringBuilder();
		if (listItemOption.length == 1) {
			inputType = "checkbox";
		} else {
			inputType = "radio";
		}
		int minSelect = -1;
		int maxSelect = -1;
		switch (optionType) {
		case SIZE:
			inputName = "size";
			label = "MSG_MILK_TEA_OPTION_SIZE";
			minSelect = limit.getSize_min();
			maxSelect = limit.getSize_max();
			break;
		case ICE:
			inputName = "ice";
			label = "MSG_MILK_TEA_OPTION_ICE";
			minSelect = limit.getIce_min();
			maxSelect = limit.getIce_max();
			break;
		case SUGAR:
			inputName = "sugar";
			label = "MSG_MILK_TEA_OPTION_SUGAR";
			minSelect = limit.getSugar_min();
			maxSelect = limit.getSugar_max();
			break;
		case TOPPING:
			inputName = "topping";
			label = "MSG_MILK_TEA_OPTION_TOPPING";
			inputType = "checkbox";
			if (!menuInfo.isActiveFlag(MilkteaMenuFlag.IGNORE_VALIDATION)) {
				minSelect = limit.getTopping_min();
				maxSelect = limit.getTopping_max();
			}
			break;
		case ADDITION:
			inputName = "addition";
			label = "MSG_MILK_TEA_OPTION_ADDITION";
			inputType = "checkbox";
			if (!menuInfo.isActiveFlag(MilkteaMenuFlag.IGNORE_VALIDATION)) {
				minSelect = limit.getAddition_min();
				maxSelect = limit.getAddition_max();
			}
			break;
		default:
			return "";
		}
		if (minSelect == maxSelect && minSelect == 1) {
			inputType = "radio";
		}
		if (minSelect > 0) {
			attention += "Minimum: " + minSelect;
		}
		if (maxSelect > 0 && maxSelect >= minSelect) {
			if (attention.length() > 0) {
				attention += ", ";
			}
			attention += "Maximum: " + maxSelect;
		}
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		sb.append("<label class=\"font-weight-bold\" style=\"font-size:1.125rem;\">").append(messages.get(label)).append("</label>");
		if (attention.length() > 0) {
			sb.append("<label class=\"font-italic none-select\" style=\"margin-left:1rem;font-size:0.75rem;color:lightgray;\">").append(attention).append("</label>");
		} else {
			sb.append("<label class=\"font-italic none-select\" style=\"margin-left:1rem;font-size:1rem;color:lightgray;\">Not Available</label>");
		}
		sb.append("</div></div>");
		if (attention.length() > 0) {
			for (int i = 0; i < rowCount; i++) {
				sb.append("<div class=\"row\">");
				for (int j = 0; j < 2; j++) {
					sb.append("<div class=\"col-sm-6\">");
					if (2 * i + j < listItemOption.length) {
						MenuItemOption itemOption = listItemOption[2 * i + j];
						sb.append(String.format("<div class=\"custom-control custom-%s\">", inputType));
						sb.append(String.format("<input type=\"%s\" class=\"custom-control-input\" id=\"item-option-%s\" name=\"item-option-%s\" value=\"%s\" %s/>", inputType, (item.getId() + "-" + inputName + "-" + (2 * i + j)), inputName, itemOption.getName(), (i + j == 0 && !inputName.equals("topping")) ? "checked" : ""));
						sb.append(String.format("<label class=\"custom-control-label text-success\" for=\"item-option-%s\">", (item.getId() + "-" + inputName + "-" + (2 * i + j))));
						sb.append(itemOption.getName());
						if (itemOption.getPrice() != 0) {
							sb.append(" + ").append(getShowPriceWithUnit(itemOption.getPrice(), messages));
						}
						sb.append("</label>");
						sb.append("</div>");
					}
					sb.append("</div>");
				}
				sb.append("</div>");
			}
		}
		sb.append("</div>");
		return sb.toString();
	}
	
	// ========================================== //
	public static String getHtmlListMenuItemType(List<MenuItem> listMenuItem, String contextPath) {
		if (listMenuItem == null || listMenuItem.isEmpty()) {
			return "";
		}
		sortListMenuItemByType(listMenuItem);
		List<Integer> listCount = new ArrayList<>();
		List<String> listType = new ArrayList<>();
		String type = null;
		int count = 0;
		for (MenuItem item : listMenuItem) {
			if (type == null || !type.equals(item.getType())) {
				if (type != null) {
					listCount.add(count);
					count = 0;
				}
				listType.add(item.getType());
				type = item.getType();
			}
			count ++;
		}
		if (type != null) {
			listCount.add(count);
			count = 0;
		}
		if (listCount.size() != listType.size()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"on-scroll-dish-type\">");
			sb.append("<ul class=\"list-group\">");
			for (int i = 0; i < listType.size(); i++) {
				sb.append("<li class=\"list-group-item d-flex justify-content-between align-items-center\">");
					sb.append(String.format("<a href=\"javascript:void(0);\" onclick=\"viewMenuItemGroup(%d)\">%s</a>", (i + 1), listType.get(i)));
					sb.append("<span class=\"badge badge-primary badge-pill\" >").append(listCount.get(i)).append("</span>");
				sb.append("</li>");
			}
			sb.append("</ul>");
		sb.append("</div>");
		return sb.toString();
	}
	
	// ========================================== //
	@SuppressWarnings("rawtypes")
	public static String getHtmlListOrder(List<OrderInfo> orderList, MenuInfo menuInfo, UserInfo userInfo, String contextPath, Map messages) {
		return getHtmlListOrder(orderList, menuInfo, userInfo, contextPath, messages, false);
	}
	
	@SuppressWarnings("rawtypes")
	public static String getHtmlListOrder(List<OrderInfo> orderList, MenuInfo menuInfo, UserInfo userInfo, String contextPath, Map messages, boolean isManagement) {
		long userId = (userInfo != null) ? userInfo.getId() : 0;
		boolean isAdminAccess = (userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag()));
		boolean isMenuOpening = menuInfo.isOpening();
		boolean hasOrder = (orderList != null && !orderList.isEmpty());
		StringBuilder sb = new StringBuilder();
		StringBuilder sbModal = new StringBuilder();
		sb.append("<div id=\"order-list\">");
		sb.append("<div style=\"text-align:center;\" class=\"text-success\">");
		sb.append(messages.get("MSG_MILK_TEA_ORDER_LIST"));
		sb.append("</div>");
		sb.append(String.format("<table class=\"table %s %s\">",(isManagement ? "" : "table-responsive"), ((isMenuOpening || (isAdminAccess && !menuInfo.isCompleted())) ? "table-striped table-hover" : (hasOrder ? "table-striped" : ""))));
			sb.append("<thead>");
				sb.append("<tr>");
				String thStyle = String.format("border-top:0.0625rem solid %s;border-bottom:0.125rem solid %s;", ORDER_BORDER_COLOR, ORDER_BORDER_COLOR);
				if (isManagement && userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag()) && menuInfo.getStatus() == MilkTeaMenuStatus.DELIVERING.ordinal()) {
					sb.append("<th scope=\"col\" style=\"width:5%;" + thStyle + "\">");
						sb.append("<input type=\"checkbox\" id=\"check-all-order\" />").append("</th>");
					sb.append("<th scope=\"col\" style=\"width:20%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_ORDERNAME")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:12%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_ORDERER")).append("</th>");
				} else {
					sb.append("<th scope=\"col\" style=\"width:24%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_ORDERNAME")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:15%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_ORDERER")).append("</th>");
				}
				if (isManagement && userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag())) {
					sb.append("<th scope=\"col\" style=\"width:7%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_OPTION_SUGAR")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:7%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_OPTION_ICE")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:6%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_QUANTITY")).append("</th>");
					sb.append("<th scope=\"col\" style=\"" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_OPTION")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:10%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_ORIGINAL_PRICE")).append("</th>");
				} else {
					sb.append("<th scope=\"col\" style=\"width:8%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_OPTION_SUGAR")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:8%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_OPTION_ICE")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:8%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_QUANTITY")).append("</th>");
					sb.append("<th scope=\"col\" style=\"" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_OPTION")).append("</th>");
				}
					sb.append("<th scope=\"col\" style=\"width:10%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_PRICE")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:5%;" + thStyle + "\">");
						sb.append("</th>");
				sb.append("</tr>");
			sb.append("</thead>");
			sb.append("<tbody>");
			String tdStyle = String.format("<td style=\"border-top:0.0625rem solid %s;\">", ORDER_BORDER_COLOR);
		if (hasOrder) {
			long totalMoney = getTotalMoney(orderList);
			long totalRealCost = 0;
			long dishcount = 0;
			for (OrderInfo order : orderList) {
				String trHighlight = "style=\"color:#C5C5C5;\"";
				if (userId == order.getUserId() && !isManagement) {
					trHighlight = "class=\"bg-primary text-success\"";
				} else if (MilkTeaOrderFlag.KOC_TICKET.isValidFlag(order.getFlag())) {
					trHighlight = "class=\"text-warning data-highlight\"";
				}
				sb.append(String.format("<tr %s>", trHighlight));
				if (isManagement && userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag()) && menuInfo.getStatus() == MilkTeaMenuStatus.DELIVERING.ordinal()) {
					sb.append(tdStyle);
					if (MilkTeaOrderFlag.PLACED.isValidFlag(order.getFlag())) {
						sb.append("<i class=\"fas fa-check-square text-success\"></i>");
					} else {
						sb.append(String.format("<input type=\"checkbox\" class=\"checkbox-order\" value=\"%d\"/>", order.getId()));
					}
					sb.append("</td>");
				}
					sb.append(String.format("<td style=\"border-top:0.0625rem solid %s;%s\">", ORDER_BORDER_COLOR, (MilkTeaOrderFlag.CANCELED.isValidFlag(order.getFlag()) ? "text-decoration:line-through;" : "")));
						sb.append("<span class=\"follow-item\">").append(order.getDishName()).append("</span>");
						if (order.getSize() != null && order.getSize().length() > 0) {
							String size = order.getSize().toUpperCase();
							if (size.matches(".*SIZE ([A-Z]).*")) {
								size = size.replaceAll(".*SIZE ([A-Z]{1,2}).*", "$1");
								sb.append(String.format(" (%s)", size));
							} else {
								sb.append(String.format(" (%s)", order.getSize()));
							}
						}
						sb.append("</td>");
					sb.append(tdStyle);
						sb.append(order.getUsername()).append("</td>");
					sb.append(tdStyle);
					if (order.getSugar() != null && order.getSugar().length() > 0) {
						sb.append(getStringOptionAmount(order.getSugar()));
					}
					sb.append("</td>");
					sb.append(tdStyle);
					if (order.getIce() != null && order.getIce().length() > 0) {
						sb.append(getStringOptionAmount(order.getIce()));
					}
					sb.append("</td>");
					sb.append(tdStyle);
						sb.append(order.getQuantity()).append("</td>");
					sb.append(tdStyle);
						sb.append(order.getOptionList());
					sb.append("</td>");
					if (isManagement && userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag())) {
						sb.append(tdStyle);
							sb.append("<div class=\"row\" style=\"margin-left:0;\">");
							long orderOriginalCost = (order.getDishPrice() + order.getAttrPrice()) * Math.max(1, order.getQuantity());
							sb.append(getShowPriceWithUnit(orderOriginalCost, messages));
							sb.append("</div>");
						sb.append("</td>");
					}
					sb.append(tdStyle);
						sb.append("<div class=\"row\" style=\"margin-left:0;\">");
						long orderCost = getFinalCost(totalMoney, orderList.size(), menuInfo, order);
						totalRealCost += orderCost;
						sb.append(getShowPriceWithUnit(orderCost, messages));
						sb.append("</div>");
					sb.append("</td>");
					if (isManagement && userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag())) {
						if (menuInfo.getStatus() < MilkTeaMenuStatus.COMPLETED.ordinal()) {
							sb.append(tdStyle);
							sb.append(String.format("<a href=\"%s\">%s</a>", contextPath + "/manage/milktea/milk_tea_manage_order.htm?order_id=" + order.getId(), messages.get("MSG_GENERAL_EDIT")));
							sb.append("</td>");
						}
					} else if ((isMenuOpening && userId == order.getUserId()) || (isAdminAccess && !menuInfo.isCompleted())) { 
						sb.append(String.format("<td style=\"border-top:0.0625rem solid %s;\">", ORDER_BORDER_COLOR));
							sb.append(String.format("<div id=\"delete-order-%d\" class=\"text-secondary\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Delete This Order\" style=\"cursor:pointer;\" >", order.getId()));
								sb.append(String.format("<i class=\"fas fa-trash\" data-toggle=\"modal\" data-target=\"#confirm-delete-order-%d\"></i>", order.getId()));
							sb.append("</div>");
						sb.append("</td>");
						sbModal.append(String.format("<div class=\"modal fade\" id=\"confirm-delete-order-%d\">", order.getId()));
							sbModal.append("<div class=\"modal-dialog modal-dialog-centered modal-md\">");
							sbModal.append("<div class=\"modal-content\">");
								sbModal.append("<div class=\"modal-header bg-primary\">");
									sbModal.append("<h4 class=\"modal-title\">").append(messages.get("MSG_MILK_TEA_DELETE_ORDER_HEADING")).append("</h4>");
									sbModal.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>");
								sbModal.append("</div>");
									
								sbModal.append("<div class=\"modal-body\">");
									sbModal.append(String.format("<form id=\"delete-order-form-%d\">", order.getId()));
									sbModal.append("<div class=\"form-group\">");
									sbModal.append("<label class=\"text-info\" style=\"font-size:1.125rem;\">").append(order.getDishName()).append(" : ").append(getShowPriceWithUnit(getFinalCost(totalMoney, orderList.size(), menuInfo, order), "", messages)).append("</label>");
									sbModal.append("</div>");
									sbModal.append(String.format("<button type=\"button\" class=\"btn btn-danger\" onclick=\"deleteTheOrder(%d,%d);this.blur();return false;\">", menuInfo.getId(), order.getId())).append(messages.get("MSG_GENERAL_DELETE")).append("</button>");
									sbModal.append("</form>");
								sbModal.append("</div>");
								
								sbModal.append("<div class=\"modal-footer\">");
									sbModal.append("<button type=\"button\" class=\"btn btn-dark text-danger\" data-dismiss=\"modal\">").append(messages.get("MSG_GENERAL_CLOSE")).append("</button>");
								sbModal.append("</div>");
							sbModal.append("</div>");
							sbModal.append("</div>");
						sbModal.append("</div>");
					} else {
						sb.append(tdStyle).append("</td>");
					}
					
				sb.append("</tr>");
				dishcount += Math.max(1, order.getQuantity());
			}
			if (userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag())) {
				int d = 6;
				if (isManagement && userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag())) {
					if (menuInfo.getStatus() == MilkTeaMenuStatus.DELIVERING.ordinal()) {
						d = 7;
					}
				}
				sb.append("<tr>");
				sb.append(String.format("<td style=\"border-top:0.0625rem solid %s;\" colspan=\"%d\">", ORDER_BORDER_COLOR, d));
					sb.append("<div class=\"text-center text-danger font-weight-bold\">");
						sb.append("Total: " + dishcount + " dishs / " + orderList.size() + " orders");
					sb.append("</div>");
				sb.append("</td>");
				if (isManagement && userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag())) {
					sb.append(String.format("<td style=\"border-top:0.0625rem solid %s;\">", ORDER_BORDER_COLOR));
						sb.append("<div class=\"text-danger font-weight-bold\">");
							sb.append(getShowPriceWithUnit(totalMoney, "", messages));
						sb.append("</div>");
					sb.append("</td>");
				}
				sb.append(String.format("<td style=\"border-top:0.0625rem solid %s;\" colspan=\"2\">", ORDER_BORDER_COLOR));
					sb.append("<div class=\"text-danger font-weight-bold\">");
						sb.append(getShowPriceWithUnit(totalRealCost, "", messages));
					sb.append("</div>");
				sb.append("</td>");
				sb.append("</tr>");
			}
		} else {
			sb.append("<tr>");
			sb.append(String.format("<td style=\"border-top:0.0625rem solid %s;\" colspan=\"8\">", ORDER_BORDER_COLOR));
			if (isMenuOpening) {
				sb.append("<div class=\"text-success\" style=\"width:100%;text-align:center;\">");
				sb.append(messages.get("MSG_MILK_TEA_NO_ORDER_INFO"));
				sb.append("</div>");
			} else {
				sb.append("<div class=\"text-secondary\" style=\"width:100%;text-align:center;\">");
				sb.append(messages.get("MSG_MILK_TEA_NO_ORDER_FOUND"));
				sb.append("</div>");
			}
			sb.append("</td>");
			sb.append("</tr>");
		}
			sb.append("</tbody>");
		sb.append("</table>");
		sb.append(sbModal);
		sb.append("</div>");
		return sb.toString();
	}
	
	// ========================================== //
	@SuppressWarnings("rawtypes")
	private static String getHtmlMenuPreview(MenuInfo menuInfo, ShopInfo shopInfo, String contextPath, Map messages) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style=\"background-color:white;\" class=\"border\">");
		sb.append(String.format("<a href=\"%s\">", contextPath + "/milktea/milk_tea_menu.htm?id=" + menuInfo.getId()));
		String preUrlImage = shopInfo.getPreImageUrl();
		if (preUrlImage == null || preUrlImage.isEmpty() || preUrlImage.indexOf("http") < 0) {
			sb.append(String.format("<img src=\"%s\" style=\"object-fit:cover;width:100%%;\" class=\"menu-pre-image\"/>", contextPath + getMenuCommonImage()));
		} else {
			sb.append(String.format("<img src=\"%s\" style=\"object-fit:cover;width:100%%;\" class=\"menu-pre-image\"/>", preUrlImage));
		}
		sb.append("</a>");
		sb.append("<div class=\"text-secondary font-weight-bold\" data-toggle=\"tooltip\" data-placement=\"bottom\" style=\"text-overflow:ellipsis;overflow:hidden;white-space:nowrap;\" title=\"").append(menuInfo.getName()).append("\">").append(menuInfo.getName()).append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private static String getHtmlShopPreview(ShopInfo shopInfo, String contextPath, Map messages) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style=\"background-color:white;\" class=\"border\">");
		sb.append(String.format("<a href=\"%s\">", contextPath + "/milktea/milk_tea_list_shop.htm?id=" + shopInfo.getId()));
		String preUrlImage = shopInfo.getPreImageUrl();
		if (preUrlImage == null || preUrlImage.isEmpty() || preUrlImage.indexOf("http") < 0) {
			sb.append(String.format("<img src=\"%s\" style=\"object-fit:cover;width:100%%;\" class=\"menu-pre-image\"/>", contextPath + getMenuCommonImage()));
		} else {
			sb.append(String.format("<img src=\"%s\" style=\"object-fit:cover;width:100%%;\" class=\"menu-pre-image\"/>", preUrlImage));
		}
		sb.append("</a>");
		sb.append("<div class=\"text-secondary font-weight-bold\" style=\"text-overflow:ellipsis;overflow:hidden;white-space:nowrap;\" title=\"").append(shopInfo.getName()).append("\">").append(shopInfo.getName()).append("</div>");
		sb.append("<div class=\"text-secondary font-italic\" data-toggle=\"tooltip\" data-placement=\"bottom\" style=\"text-overflow:ellipsis;overflow:hidden;white-space:nowrap;font-size:0.75rem;\" title=\"").append(shopInfo.getAddress()).append("\">").append(shopInfo.getAddress()).append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getHtmlListMenu(UserInfo userInfo, String contextPath, Map messages) {
		byte[] statusList = new byte[] {MilkTeaMenuStatus.OPENING.getStatus(), MilkTeaMenuStatus.DELIVERING.getStatus(), MilkTeaMenuStatus.COMPLETED.getStatus()};
		String options = null;
		int validFalg = 0;
		if (userInfo == null) {
			validFalg = MilkteaMenuFlag.combineShowFlag();
			if (validFalg > 0) {
				options = "flag & " + validFalg + " = " + validFalg;
			}
		} else if (!UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag())){
			for (MilkteaMenuFlag mmf : MilkteaMenuFlag.values()) {
				if (mmf == MilkteaMenuFlag.INVALID) {
					continue;
				}
				if (mmf.getUserFlag() == UserFlagEnum.INVALID) {
					continue;
				}
				if (mmf.isValidUserFlag(userInfo.getFlag())) {
					validFalg |= mmf.getFlag();
				}
			}
			if (validFalg > 0) {
				options = "flag & " + validFalg + " > 0 ";
			}
		}
		List<MenuInfo> menuList = MenuService.getMenuListByStatusList(statusList, options);
		Map<Byte,List<String>> menuMaps = new HashMap<>();
		List<Long> shopids = new ArrayList<>();
		if (menuList != null && menuList.size() > 0) {
			Iterator<MenuInfo> it = menuList.iterator();
			while(it.hasNext()) {
				MenuInfo menuInfo =  it.next();
				if (!menuInfo.isAvailableForUser(userInfo)) {
					it.remove();
					continue;
				}
				if (shopids.contains(menuInfo.getShopId())) {
					continue;
				}
				shopids.add(menuInfo.getShopId());
			}
		}
		List<ShopInfo> shopList = ShopService.getShopListById(shopids);
		if (shopList != null && shopList.size() > 0) {
			for (MenuInfo menuInfo : menuList) {
				if (!menuMaps.containsKey(menuInfo.getStatus())) {
					menuMaps.put(menuInfo.getStatus(), new ArrayList<>());
				}
				for (ShopInfo shopInfo : shopList) {
					if (shopInfo.getId() == menuInfo.getShopId()) {
						menuMaps.get(menuInfo.getStatus()).add(getHtmlMenuPreview(menuInfo, shopInfo, contextPath, messages));
						break;
					}
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"menu-list\">");
		for (byte stt : statusList) {
			MilkTeaMenuStatus menuStatus = MilkTeaMenuStatus.valueOf(stt);
			sb.append("<div>");
			sb.append("<div class=\"row\">");
			sb.append("<div class=\"col-sm-12 bg-success text-center rounded\" style=\"line-height:2;margin-top:1rem;margin-bottom:0.5rem;\">");
			sb.append(messages.get(menuStatus.getLabel()));
			sb.append("</div>");
			sb.append("</div>");
			List<String> elements = menuMaps.get(stt);
			if (elements == null || elements.isEmpty()) {
				sb.append("<div class=\"col-sm-12 text-center\">");
				sb.append(messages.get(menuStatus.getNoMenuLabel()));
				sb.append("</div>");
			} else {
				sb.append(CommonHtmlFunc.getGridLayoutElement(elements, menuStatus.getMaxGridElemPerRow()));
			}
			sb.append("</div>");
		}
		sb.append("</div>");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getHtmlMenuDetail(MenuInfo menuInfo, UserInfo userInfo, String contextPath, Map messages) {
		StringBuilder sb = new StringBuilder();
		ShopInfo shopInfo = ShopService.getShopById(menuInfo.getShopId());
		if (shopInfo != null) {
			boolean menuOpening = menuInfo.isOpening();
			boolean isAdminAccess = (userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag()));
			sb.append("<div id=\"menu-detail\" class=\"col-12\"><div class=\"row\">");
			sb.append("<div class=\"col-lg-3 col-md-6\" style=\"\">");
			String url = shopInfo.getImageUrl();
			if (url != null && url.indexOf("youtube") >= 0) {
				sb.append(String.format("<iframe style=\"%s\" src=\"%s\"></iframe>", "width:100%;height:15.625rem;", url));
			} else if (url != null && url.startsWith("http")) {
				sb.append(String.format("<img src=\"%s\" style=\"%s\"/>", url, "width:100%;max-height:15.625rem;"));
			} else {
				sb.append(String.format("<img src=\"%s\" style=\"%s\" />", contextPath + getMenuCommonImage(), "width:100%;max-height:15.625rem;"));
			}
			sb.append("</div>");
			sb.append(String.format("<div class=\"col-lg-%d col-md-6\">", ((menuOpening || (isAdminAccess && !menuInfo.isCompleted())) ? 4 : 5)));
				sb.append("<h5 class=\"font-weight-bol text-info\" style=\"margin-top:1rem;\">");
				sb.append(String.format("<a class=\"text-info\" href=\"%s\" target=\"_blank\">", shopInfo.getUrl()));
				sb.append(menuInfo.getName()).append("</a></h5>");
				sb.append("<div class=\"font-italic\" style=\"font-size:0.875rem;margin-bottom:0.5rem;\">").append(shopInfo.getAddress()).append("</div>");
				sb.append("<div class=\"rating\">");
					double rating = 0.0;
					if (shopInfo.getVotingCount() > 0) {
						rating = (double)shopInfo.getStarCount() / shopInfo.getVotingCount();
						rating = ((double)Math.round(rating * 10) / 10);
					}
					sb.append(CommonHtmlFunc.getHtmlStarRating(rating, CommonDefine.MAX_MILKTEA_VOTING_STAR));
					sb.append("<span class=\"text-info font-italic\" style=\"margin-left:0.4rem;\">").append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_SHOP_RATING_S"), shopInfo.getStarCount(), shopInfo.getVotingCount())).append("</span>");
					if (shopInfo.getOpeningCount() > 0) {
						sb.append("<div class=\"text-success\" style=\"margin-bottom:0.5rem;font-size:0.875rem;\">").append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_SHOP_INFO_STATISTIC"), shopInfo.getOpeningCount(), shopInfo.getOrderedDishCount())).append("</div>");
					} else {
						sb.append("<div class=\"text-success\" style=\"margin-bottom:0.5rem;font-size:0.875rem;\">").append((String)messages.get("MSG_MILK_TEA_SHOP_INFO_FIRST_TIME_OPEN")).append("</div>");
					}
				sb.append("</div>");
				if (menuInfo.getMaxDiscount() > 0) {
					sb.append("<div style=\"margin-bottom:0.25rem;\">");
				} else {
					sb.append("<div style=\"margin-bottom:0.5rem;\">");
				}
					sb.append("<span>");
						sb.append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_MENU_INFO_SALE_RATE"), menuInfo.getSale()));
					sb.append("</span>");
					if (menuInfo.getMaxDiscount() > 0) {
						sb.append("<span style=\"margin-left:1rem;\">");
						sb.append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_MENU_INFO_SALE_MAX_DISCOUNT"), getShowPriceWithUnit(menuInfo.getMaxDiscount(), messages)));
						sb.append("</span>");
						sb.append("</div>");
						sb.append("<div style=\"margin-bottom:0.5rem;\">");
						sb.append("<span>");
					} else {
						sb.append("<span style=\"margin-left:1rem;\">");
					}
						sb.append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_MENU_INFO_SHIPPING_FEE"), getShowPriceWithUnit(menuInfo.getShippingFee(), messages)));
					sb.append("</span>");
				sb.append("</div>");
				sb.append("<div style=\"margin-bottom:0.5rem;\">");
					MilkTeaMenuStatus status = MilkTeaMenuStatus.valueOf(menuInfo.getStatus());
					sb.append("<span>");
					sb.append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_MENU_INFO_STATUS"), status.name().toLowerCase()));
					sb.append("</span>");
				sb.append("</div>");
				sb.append("<div style=\"margin-bottom:0.5rem;\">");
				sb.append("<span class=\"text-info\">").append(menuInfo.getDescription()).append("</span>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("<div class=\"col-lg-4 col-md-12\">");
			if (menuOpening) {
				long left = menuInfo.getExpired().getTime() - Calendar.getInstance().getTimeInMillis();
				if (left <= 5 * CommonDefine.MILLION_SECOND_HOUR) {
					sb.append("<div id=\"menu-countdown\" class=\"text-center\" >");
					sb.append("<div class=\"text-warning\" style=\"font-size:1.25rem;\">");
						sb.append(messages.get("MSG_MILK_TEA_MENU_ATTENTION_CLOSE"));
					sb.append("</div>");
					sb.append("<div class=\"\" style=\"\">");
					sb.append(String.format("<span id=\"menu-countdown-time\" class=\"font-weight-bold text-warning countdown-timer\" style=\"font-size:2.5rem;\" data-reload=\"1\" data-parent=\"#menu-countdown\">%s</span>", CommonMethod.getStringTimeLeft(left)));
					sb.append("</div>");
					sb.append("</div>");
				}
			}
			sb.append("</div>");
			sb.append("</div></div>");
		}
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getHtmlListShop(List<ShopInfo> listShop, String contextPath, Map messages) {	
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"shop-list\">");
		sb.append("<div>");
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-sm-12 bg-success text-center rounded\" style=\"line-height:2;margin-top:1rem;margin-bottom:0.5rem;\">");
		sb.append(messages.get("MSG_MILK_TEA_LIST_SHOP"));
		sb.append("</div>");
		sb.append("</div>");
		if (listShop == null || listShop.isEmpty()) {
			sb.append("<div class=\"col-sm-12 text-center\">");
			sb.append(messages.get("MSG_MILK_TEA_LIST_SHOP_NO_SHOP"));
			sb.append("</div>");
		} else {
			List<String> elements = new ArrayList<>();
			for (ShopInfo shop : listShop) {
				elements.add(getHtmlShopPreview(shop, contextPath, messages));
			}
			sb.append(CommonHtmlFunc.getGridLayoutElement(elements, 6));
		}
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getHtmlShopDetail(ShopInfo shopInfo, UserInfo userInfo, String contextPath, Map messages) {
		StringBuilder sb = new StringBuilder();
		if (shopInfo != null) {
			sb.append("<div id=\"shop-detail\" class=\"col-12\"><div class=\"row\">");
			sb.append("<div class=\"col-lg-3 col-md-6\" style=\"\">");
			String url = shopInfo.getImageUrl();
			if (url != null && url.indexOf("youtube") >= 0) {
				sb.append(String.format("<iframe style=\"%s\" src=\"%s\"></iframe>", "width:100%;height:15.625rem;", url));
			} else if (url != null && url.startsWith("http")) {
				sb.append(String.format("<img src=\"%s\" style=\"%s\"/>", url, "width:100%;max-height:15.625rem;"));
			} else {
				sb.append(String.format("<img src=\"%s\" style=\"%s\" />", contextPath + getMenuCommonImage(), "width:100%;max-height:15.625rem;"));
			}
			sb.append("</div>");
			sb.append("<div class=\"col-lg-9 col-md-6\">");
				sb.append("<h5 class=\"font-weight-bol text-info\" style=\"margin-top:1rem;\">");
				if (userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag())) {
					sb.append(String.format("<a class=\"text-info\" href=\"%s\" target=\"_blank\">", shopInfo.getUrl()));
					sb.append(shopInfo.getName()).append("</a></h5>");
				} else {
					sb.append(shopInfo.getName()).append("</h5>");
				}
				sb.append("<div class=\"font-italic\" style=\"font-size:0.875rem;margin-bottom:0.5rem;\">").append(shopInfo.getAddress()).append("</div>");
				sb.append("<div class=\"rating\">");
					double rating = 0.0;
					if (shopInfo.getVotingCount() > 0) {
						rating = (double)shopInfo.getStarCount() / shopInfo.getVotingCount();
						rating = ((double)Math.round(rating * 10) / 10);
					}
					sb.append(CommonHtmlFunc.getHtmlStarRating(rating, CommonDefine.MAX_MILKTEA_VOTING_STAR));
					sb.append("<span class=\"text-info font-italic\" style=\"margin-left:0.4rem;\">").append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_SHOP_RATING_S"), shopInfo.getStarCount(), shopInfo.getVotingCount())).append("</span>");
					if (shopInfo.getOpeningCount() > 0) {
						sb.append("<div class=\"text-success\" style=\"margin-bottom:0.5rem;font-size:0.875rem;\">").append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_SHOP_INFO_STATISTIC"), shopInfo.getOpeningCount(), shopInfo.getOrderedDishCount())).append("</div>");
					} else {
						sb.append("<div class=\"text-success\" style=\"margin-bottom:0.5rem;font-size:0.875rem;\">").append((String)messages.get("MSG_MILK_TEA_SHOP_INFO_ZERO_TIME_OPEN")).append("</div>");
					}
				sb.append("</div>");
			sb.append("</div>");
			sb.append("</div></div>");
		}
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getHtmlListMenuOnShop(List<MenuInfo> menuList, String contextPath, Map messages) {
		boolean hasMenu = (menuList != null && !menuList.isEmpty());
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"order-list\">");
		sb.append("<div style=\"text-align:center;\" class=\"text-success\">");
		sb.append(messages.get("MSG_MILK_TEA_LIST_SHOP_MENU_LIST"));
		sb.append("</div>");
		sb.append(String.format("<table class=\"table table-responsive %s\">", (hasMenu ? "table-striped table-hover" : "")));
			sb.append("<thead>");
				sb.append("<tr>");
				String thStyle = String.format("border-top:0.0625rem solid %s;border-bottom:0.125rem solid %s;", ORDER_BORDER_COLOR, ORDER_BORDER_COLOR);
					sb.append("<th scope=\"col\" style=\"" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_LIST_SHOP_COLUMN_MENU_NAME")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:10%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_LIST_SHOP_COLUMN_MENU_STATUS")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:10%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_LIST_SHOP_COLUMN_MENU_SALE")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:18%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_LIST_SHOP_COLUMN_MENU_SHIPPING_FEE")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:22%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_LIST_SHOP_COLUMN_MENU_OPENING_TIME")).append("</th>");
				sb.append("</tr>");
			sb.append("</thead>");
			sb.append("<tbody>");
			String tdStyle = String.format("<td style=\"border-top:0.0625rem solid %s;\">", ORDER_BORDER_COLOR);
		if (hasMenu) {
			for (MenuInfo menuInfo : menuList) {
				sb.append("<tr>");
					sb.append(String.format("<td style=\"border-top:0.0625rem solid %s;\">", ORDER_BORDER_COLOR));
						sb.append("<div style=\"max-width:250px;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;\">");
						sb.append("<a href=\"" + contextPath + "/milktea/milk_tea_menu.htm?id=" + menuInfo.getId() + "\">");
							sb.append(menuInfo.getName());
						sb.append("</a>");
						sb.append("</div>");
					sb.append("</td>");
					sb.append(tdStyle);
							sb.append(MilkTeaMenuStatus.valueOf(menuInfo.getStatus()).name());
					sb.append("</td>");
					sb.append(tdStyle);
					if (menuInfo.getSale() > 0) {
						sb.append(menuInfo.getSale() + "%");
					} else {
						sb.append("---");
					}
					sb.append("</td>");
					sb.append(tdStyle);
						sb.append(getShowPriceWithUnit(menuInfo.getShippingFee(), messages));
					sb.append("</td>");
					sb.append(tdStyle);
						sb.append(CommonMethod.getFormatDateString(menuInfo.getCreated(), CommonDefine.DATE_FORMAT_PATTERN));
					sb.append("</td>");
				sb.append("</tr>");
			}
		} else {
			sb.append("<tr>");
			sb.append(String.format("<td style=\"border-top:0.0625rem solid %s;\" colspan=\"8\">", ORDER_BORDER_COLOR));
			sb.append("<div class=\"text-success\" style=\"width:100%;text-align:center;\">");
			sb.append(messages.get("MSG_MILK_TEA_LIST_SHOP_NO_MENU"));
			sb.append("</div>");
			sb.append("</td>");
			sb.append("</tr>");
		}
			sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getOrderVotingModal(OrderInfo order, Map messages) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<div class=\"modal fade\" id=\"voting-up-order-%d\">", order.getId()));
			sb.append("<div class=\"modal-dialog modal-dialog-centered modal-md\">");
				sb.append("<div class=\"modal-content\">");
					sb.append("<div>");
					sb.append("<span class=\"text-info font-weight-bold\" style=\"font-size:1rem;padding-left:1rem;\">").append(order.getDishName()).append("</span>");
					sb.append("<button type=\"button\" class=\"close\" style=\"padding-right:0.5rem;\" data-dismiss=\"modal\">&times;</button>");
					sb.append("</div>");
					sb.append("<div class=\"modal-body text-center\">");
						sb.append(String.format("<form id=\"voting-up-order-form-%d\">", order.getId()));
						sb.append("<div class=\"form-group\">");
							for (int i = 0; i < CommonDefine.MAX_MILKTEA_VOTING_STAR; i++) {
								sb.append(String.format("<span id=\"order-star-%d-%d\" class=\"order-star\" style=\"cursor:pointer;font-size:3rem;\"><i class=\"fas fa-star\"></i></span>", order.getId(), (i+1)));
							}
						sb.append("</div>");
						sb.append("<button type=\"button\" class=\"btn btn-success\" style=\"width:100%;\" onclick=\"upvoteTheOrder(" + order.getId() + ");this.blur();return false;\">");
							sb.append(messages.get("MSG_GENERAL_SUBMIT"));
						sb.append("</button>");
						sb.append("</form>");
					sb.append("</div>");
		
				sb.append("</div>");
			sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	public static String getOrderRating(OrderInfo order) {
		StringBuilder sb = new StringBuilder();
			sb.append(String.format("<div id=\"order-rating-%d\" class=\"stars\">", order.getId()));
			sb.append("<span class=\"\" style=\"\">").append(order.getVotingStar()).append("</span>");
			sb.append("<span class=\"star full\"></span>");
			sb.append("</div>");
		return sb.toString();
	}
	
	// ========================================== //
	public static void sortListMenuItemByType(List<MenuItem> listMenuItem) {
		Collections.sort(listMenuItem, new Comparator<MenuItem>() {

			@Override
			public int compare(MenuItem o1, MenuItem o2) {
				return o1.getType().compareTo(o2.getType());
			}
			
		});
	}
	
	@SuppressWarnings("rawtypes")
	public static String getShowPriceWithUnit(long price, Map messages) {
		return getShowPriceWithUnit(price, "", messages);
	}
	
	@SuppressWarnings("rawtypes")
	public static String getShowPriceWithUnit(long price, String optionStyle, Map messages) {
		return getShowPriceWithUnit(price, optionStyle, "", messages);
	}
	
	@SuppressWarnings("rawtypes")
	public static String getShowPriceWithUnit(long price, String optionStyle, String unitOption, Map messages) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<span style=\"position:relative;text-align:right;padding-right:0.625rem;%s\">",optionStyle));
		sb.append("<span style=\"\">").append(CommonMethod.getFormatNumberThousandComma(price)).append("</span>");
		sb.append("<span style=\"position:absolute;top:0;right:0;font-size:0.75rem;\" " + unitOption + ">").append(messages.get("MSG_MILK_TEA_PRICE_UNIT")).append("</span>");
		sb.append("</span>");
		return sb.toString();
	}
	
	public static long getTotalMoney(List<OrderInfo> orderList) {
		if (orderList == null || orderList.isEmpty()) {
			return 0;
		}
		long total = 0;
		for (OrderInfo order : orderList) {
			total += (order.getDishPrice() + order.getAttrPrice()) * Math.max(order.getQuantity(), 1);
		}
		return total;
	}
	
	public static long getFinalCost(long totalMoney, int totalOrder, MenuInfo menuInfo, OrderInfo order) {
		if (MilkTeaOrderFlag.KOC_TICKET.isValidFlag(order.getFlag())) {
			return 0;
		}
		int sale = 100;
		if (menuInfo.getSale() > 0 && menuInfo.getSale() < 100) {
			sale = 100 - menuInfo.getSale();
		}
		long price = (order.getDishPrice() + order.getAttrPrice()) * Math.max(order.getQuantity(), 1);
		if (menuInfo.getMaxDiscount() > 0 && ((totalMoney * menuInfo.getSale()) / 100) > menuInfo.getMaxDiscount()) {
			double rate = ((double) price) / totalMoney;
			price = price - (long) Math.floor(menuInfo.getMaxDiscount() * rate);
		} else {
			price = (price * sale) / 100;
		}
		long shippingFee = (long) Math.ceil(((double) menuInfo.getShippingFee()) / totalOrder);
		price += shippingFee;
		if (price % 1000 > 300) {
			price = price - (price % 1000) + 1000;
		} else {
			price = price - (price % 1000);
		}
		return price;
	}
	
	public static String getMenuCommonImage() {
		return "/img/milktea/menu_common_1.jpg?@menu_common_1.jpg@";
	}
	
	public static String getStringOptionAmount(String option) {
		if (option.matches(".*[0-9]+/[0-9]+.*")) {
			return option;
		}
		if (option.matches("[^0-9]*([0-9]+).*")) {
			return option.replaceAll("[^0-9]*([0-9]+).*", "$1") + "%";
		}
		return option;
	}
	
	public static int calcOptionAmount(String option) {
		String strAmount = getStringOptionAmount(option);
		if (strAmount.length() > 0 && strAmount.charAt(strAmount.length() - 1) == '%') {
			strAmount = strAmount.substring(0, strAmount.length() - 1);
		}
		if (CommonMethod.isValidNumeric(strAmount, 0, Integer.MAX_VALUE)) {
			return Integer.parseInt(strAmount);
		}
		option = option.toLowerCase().replaceAll("\\s+", "-");
		if (option.indexOf("không") >= 0 || option.indexOf("hot") >= 0 || option.indexOf("warm") >= 0
				 || option.indexOf("ấm") >= 0 || option.indexOf("nóng") >= 0 || option.indexOf("ko-đá") >= 0) {
			return 0;
		} else if (option.indexOf("rất-ít") >= 0 || option.indexOf("1/3") >= 0) {
			return 30;
		} else if (option.indexOf("ít") >= 0 || option.indexOf("1/2") >= 0) {
			return 50;
		} else if (option.indexOf("vừa") >= 0 || option.indexOf("2/3") >= 0) {
			return 70;
		} else if (option.indexOf("bình-thường") >= 0 || option.indexOf("normal") >= 0 || option.indexOf("full") >= 0) {
			return 100;
		} else if (option.indexOf("rất-nhiều") >= 0) {
			return 130;
		} else if (option.indexOf("nhiều") >= 0) {
			return 120;
		}
		GameLog.getInstance().warn("[calcOptionAmount] Can not determine option's value: " + option);
		return 0;
	}
	
}