package dev.boom.common.milktea;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonHtmlFunc;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.BootStrapColorEnum;
import dev.boom.common.enums.UserFlagEnum;
import dev.boom.core.GameLog;
import dev.boom.entity.info.MenuInfo;
import dev.boom.entity.info.OrderInfo;
import dev.boom.entity.info.ShopInfo;
import dev.boom.entity.info.ShopOptionInfo;
import dev.boom.entity.info.UserInfo;
import dev.boom.milktea.object.MenuItem;
import dev.boom.milktea.object.MenuItemOption;
import dev.boom.services.MenuService;
import dev.boom.services.ShopService;

public class MilkTeaCommonFunc {
	
	public static final int DEFAULT_PERCENTAGE_OPTION = 100;
	
	private static final String DISH_BORDER_COLOR = BootStrapColorEnum.PRIMARY.getColorCode();
	private static final String ORDER_BORDER_COLOR = BootStrapColorEnum.WARNING.getColorCode();
	private static final int MAX_OPENING_MENU_PER_ROW = 3;
	private static final int MAX_DELIVERING_MENU_PER_ROW = 4;
	private static final int MAX_COMPLETED_MENU_PER_ROW = 5;
	
	// ========================================== //
	@SuppressWarnings("rawtypes")
	public static String getHtmlListMenuItem(MenuInfo menuInfo, List<MenuItem> listMenuItem, List<ShopOptionInfo> listShopOption, String contextPath, UserInfo userInfo, Map messages) {
		if (listMenuItem == null || listMenuItem.isEmpty()) {
			return "";
		}
		sortListMenuItemByType(listMenuItem);
		StringBuilder sb = new StringBuilder();
		String type = null;
		int groupType = 0;
		for (MenuItem item : listMenuItem) {
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
				sb.append("<div class=\"col-sm-2\">");
					String imgSrc = item.getImage_url();
					if (imgSrc == null || !imgSrc.startsWith("http")) {
						imgSrc = contextPath + "/img/milktea/no_image.png?@no_image.png@";
					}
					sb.append(String.format("<img src=\"%s\" class=\"dish-image\" title=\"\" alt=\"\" style=\"height:3.125rem;padding-left:0;margin-top:0.3125rem;\"/>", imgSrc));
				sb.append("</div>");
				
				sb.append("<div class=\"col-sm-7\">");
					sb.append("<div class=\"row\" style=\"position:relative;\">");
						sb.append("<span style=\"\">").append(item.getName()).append("</span>");
					sb.append("</div>");
				sb.append("</div>");
				
				sb.append("<div class=\"col-sm-3\">");
					sb.append("<div class=\"row\"><div class=\"col-sm-12\" style=\"text-align:right;\">");
						sb.append(getShowPriceWithUnit(item.getPrice(), "height:100%;", messages));
					sb.append("</div></div>");
					sb.append("<div class=\"row\"><div class=\"col-sm-12\" style=\"position:relative;height:1.6875rem;\">");
					if (menuInfo.getStatus() == MilkTeaMenuStatus.OPENING.ordinal()) {
						if (userInfo != null) {
							if (!UserFlagEnum.ACTIVE.isValid(userInfo.getFlag())) {
								sb.append("<div data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Can not order!\" style=\"position:absolute;cursor:pointer;right:1.25rem;bottom:0;\" >");
								sb.append(String.format("<i style=\"color:%s;font-size:1.25rem;\" class=\"fas fa-plus-circle\" data-toggle=\"modal\" data-target=\"#account-not-active-modal\" ></i>", BootStrapColorEnum.WARNING.getColorCode(), item.getId()));
								sb.append("</div>");
							} else if (UserFlagEnum.MILKTEA_BANNED.isValid(userInfo.getFlag())) {
								sb.append("<div data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"Banned!\" style=\"position:absolute;cursor:pointer;right:1.25rem;bottom:0;\" >");
								sb.append(String.format("<i style=\"color:%s;font-size:1.25rem;\" class=\"fas fa-plus-circle\" data-toggle=\"modal\" data-target=\"#account-benned-modal\" ></i>", BootStrapColorEnum.DANGER.getColorCode(), item.getId()));
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
				sb.append("</div>");
			sb.append("</div>");
		}
		if (type != null) {
			sb.append("</div>");
		}
		// modal
		if (menuInfo.getStatus() == MilkTeaMenuStatus.OPENING.ordinal()) {
			if (userInfo != null) {
				if (!UserFlagEnum.ACTIVE.isValid(userInfo.getFlag())) {
					sb.append(CommonHtmlFunc.getModalAlertWithMessage("account-not-active-modal", "warning", (String)messages.get("MSG_ACCOUNT_INACTIVE")));
				} else if (UserFlagEnum.MILKTEA_BANNED.isValid(userInfo.getFlag())) {
					sb.append(CommonHtmlFunc.getModalAlertWithMessage("account-benned-modal", "danger", (String)messages.get("MSG_ACCOUNT_MILKTEA_BANNED")));
				} else {
					sb.append("<div>");
					for (MenuItem item : listMenuItem) {
						sb.append(getPlaceOrderModal(menuInfo, item, listShopOption, messages, contextPath));
					}
					sb.append("</div>");
				}
			}
		}
		
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private static String getPlaceOrderModal(MenuInfo menuInfo, MenuItem item, List<ShopOptionInfo> listShopOption, Map messages, String contextPath) {
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
								sb.append("<div class=\"col-sm-4\" >");
								String imgSrc = item.getImage_url();
									if (imgSrc == null || !imgSrc.startsWith("http")) {
										imgSrc = contextPath + "/img/milktea/no_image.png?@no_image.png@";
									}
									sb.append(String.format("<img src=\"%s\" title=\"\" alt=\"\" style=\"height:3.125rem;\"/>", imgSrc));
								sb.append("</div>");
								sb.append("<div class=\"col-sm-8\">");
									sb.append("<label class=\"font-weight-bold text-info\" style=\"font-size:1.25rem;\">").append(item.getName()).append(" + ").append(getShowPriceWithUnit(item.getPrice(), "", messages)).append("</label>");
								sb.append("</div>");
							sb.append("</div>");
							sb.append("</div>");
							sb.append("<div style=\"max-height:19.375rem;min-height:1.875rem;;overflow:auto;overflow-x:hidden;\">");
							// -------------------- //
							sb.append(getGroupInputFormItemOption(item, ShopService.ITEM_OPTION_TYPE_SIZE, item.getList_size(), listShopOption, messages));
							// -------------------- //
							sb.append(getGroupInputFormItemOption(item, ShopService.ITEM_OPTION_TYPE_ICE, item.getList_ice(), listShopOption, messages));
							// -------------------- //
							sb.append(getGroupInputFormItemOption(item, ShopService.ITEM_OPTION_TYPE_SUGAR, item.getList_sugar(), listShopOption, messages));
							// -------------------- //
							sb.append(getGroupInputFormItemOption(item, ShopService.ITEM_OPTION_TYPE_TOPPING, item.getList_topping(), listShopOption, messages));
							// -------------------- //
							sb.append("</div>");
							sb.append("<div class=\"form-group\">");
							sb.append(String.format("<label for=\"quantity-item-%d\" class=\"font-weight-bold\" style=\"font-size:1.125rem;\">", item.getId()));
							sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_QUANTITY")).append("</label>");
							sb.append(String.format("<input type=\"number\" id=\"quantity-item-%d\" class=\"form-control\" style=\"width:22%%;\" min=\"1\" max=\"15\" value=\"1\"/>", item.getId()));
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
	
	private static ShopOptionInfo getOptionByNameFromList(MenuItemOption option, List<ShopOptionInfo> listShopOptions) {
		if (listShopOptions == null || listShopOptions.isEmpty()) {
			return null;
		}
		for (ShopOptionInfo info : listShopOptions) {
			if (option.getType() == info.getType() && option.getName().equals(info.getName())) {
				return info;
			}
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private static String getGroupInputFormItemOption(MenuItem item, short optionType, MenuItemOption[] listItemOption, List<ShopOptionInfo> listShopOption, Map messages) {
		if (listItemOption == null || listItemOption.length == 0) {
			return "";
		}
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
		switch (optionType) {
		case ShopService.ITEM_OPTION_TYPE_SIZE:
			inputName = "size";
			label = "MSG_MILK_TEA_OPTION_SIZE";
			break;
		case ShopService.ITEM_OPTION_TYPE_ICE:
			inputName = "ice";
			label = "MSG_MILK_TEA_OPTION_ICE";
			break;
		case ShopService.ITEM_OPTION_TYPE_SUGAR:
			inputName = "sugar";
			label = "MSG_MILK_TEA_OPTION_SUGAR";
			break;
		case ShopService.ITEM_OPTION_TYPE_TOPPING:
			inputName = "topping";
			label = "MSG_MILK_TEA_OPTION_TOPPING";
			inputType = "checkbox";
			break;
		default:
			return "";
		}
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"row\"><div class=\"col-sm-12\">");
		sb.append("<label class=\"font-weight-bold\" style=\"font-size:1.125rem;\">").append(messages.get(label)).append("</label>");
		sb.append("</div></div>");
		for (int i = 0; i < rowCount; i++) {
			sb.append("<div class=\"row\">");
			for (int j = 0; j < 2; j++) {
				sb.append("<div class=\"col-sm-6\">");
				if (2 * i + j < listItemOption.length) {
					MenuItemOption mto = listItemOption[2 * i + j];
					mto.setType(optionType);
					ShopOptionInfo optionInfo = getOptionByNameFromList(mto, listShopOption);
					if (optionInfo == null) {
						GameLog.getInstance().error("[MilkTeaCommonFunc](getPlaceOrderModal) No valid option found for the item: " + item.getName() +", option name: " + mto.getName());
					} else {
						sb.append(String.format("<div class=\"custom-control custom-%s\">", inputType));
						sb.append(String.format("<input type=\"%s\" class=\"custom-control-input\" id=\"item-option-%d-%d\" name=\"item-option-%s\" %s/>", inputType, item.getId(), optionInfo.getId(), inputName, (i + j == 0 && !inputName.equals("topping")) ? "checked" : ""));
						sb.append(String.format("<label class=\"custom-control-label text-success\" for=\"item-option-%d-%d\">", item.getId(), optionInfo.getId()));
						sb.append(optionInfo.getName());
						if (optionInfo.getPrice() != 0) {
							sb.append(" + ").append(getShowPriceWithUnit(optionInfo.getPrice(), "", messages));
						}
						sb.append("</label>");
						sb.append("</div>");
					}
				}
				sb.append("</div>");
			}
			sb.append("</div>");
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
	private static  Map<Short, String> getMapOptionNameByOrder(OrderInfo order, List<ShopOptionInfo> listShopOptions) {
		Map<Short, String> map = null;
		if (order != null && listShopOptions != null && !listShopOptions.isEmpty()) {
			String options = order.getOption_list();
			if (options != null && options.length() > 0) {
				String[] ids = options.split(",");
				for (String strId : ids) {
					if (!CommonMethod.isValidNumeric(strId, 1, Long.MAX_VALUE)) {
						continue;
					}
					for (ShopOptionInfo info : listShopOptions) {
						if (info.getId() == Long.parseLong(strId)) {
							if (map == null) {
								map = new HashMap<>();
							}
							String name = info.getName();
							if (map.containsKey(info.getType())) {
								name += "," + map.get(info.getType());
							}
							map.put(info.getType(), name);
							break;
						}
					}
				}
			}
		}
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	public static String getHtmlListOrder(List<OrderInfo> orderList, MenuInfo menuInfo, UserInfo userInfo, List<ShopOptionInfo> listShopOption, String contextPath, Map messages) {
		long userId = (userInfo != null) ? userInfo.getId() : 0;
		boolean isMenuOpening = (menuInfo.getStatus() == MilkTeaMenuStatus.OPENING.ordinal());
		boolean hasOrder = (orderList != null && !orderList.isEmpty());
		StringBuilder sb = new StringBuilder();
		StringBuilder sbModal = new StringBuilder();
		sb.append("<div id=\"order-list\">");
		sb.append("<div style=\"text-align:center;\" class=\"text-success\">");
		sb.append(messages.get("MSG_MILK_TEA_ORDER_LIST"));
		sb.append("</div>");
		sb.append(String.format("<table class=\"table table-responsive %s\">", (isMenuOpening ? "table-striped table-hover" : (hasOrder ? "table-striped" : ""))));
			sb.append("<thead>");
				sb.append("<tr>");
					String thStyle = String.format("border-top:0.0625rem solid %s;border-bottom:0.125rem solid %s;", ORDER_BORDER_COLOR, ORDER_BORDER_COLOR);
					sb.append("<th scope=\"col\" style=\"width:24%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_ORDERNAME")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:15%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_ORDERER")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:8%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_OPTION_SUGAR")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:8%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_OPTION_ICE")).append("</th>");
					sb.append("<th scope=\"col\" style=\"width:8%;" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_QUANTITY")).append("</th>");
					sb.append("<th scope=\"col\" style=\"" + thStyle + "\">");
						sb.append(messages.get("MSG_MILK_TEA_ORDER_COLUMN_OPTION")).append("</th>");
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
				Map<Short, String> mapOptions = getMapOptionNameByOrder(order, listShopOption);
				sb.append(String.format("<tr %s>", (userId == order.getUser_id() ? "class=\"bg-primary text-success\"" : "style=\"color:#C5C5C5;\"")));
					sb.append(tdStyle);
						sb.append(order.getDish_name());
						if (mapOptions != null && mapOptions.containsKey(ShopService.ITEM_OPTION_TYPE_SIZE)) {
							sb.append(String.format(" (%s)", mapOptions.get(ShopService.ITEM_OPTION_TYPE_SIZE)));
						}
						sb.append("</td>");
					sb.append(tdStyle);
						sb.append(order.getUsername()).append("</td>");
					sb.append(tdStyle);
						if (mapOptions != null && mapOptions.containsKey(ShopService.ITEM_OPTION_TYPE_SUGAR)) {
							String strSugar = mapOptions.get(ShopService.ITEM_OPTION_TYPE_SUGAR);
							strSugar = getStringOptionAmount(strSugar) + "%";
							sb.append(strSugar);
						}
					sb.append("</td>");
					sb.append(tdStyle);
						if (mapOptions != null && mapOptions.containsKey(ShopService.ITEM_OPTION_TYPE_ICE)) {
							String strIce = mapOptions.get(ShopService.ITEM_OPTION_TYPE_ICE);
							strIce = getStringOptionAmount(strIce) + "%";
							sb.append(strIce);
						}
					sb.append("</td>");
					sb.append(tdStyle);
						sb.append(order.getQuantity()).append("</td>");
					sb.append(tdStyle);
						if (mapOptions != null && mapOptions.containsKey(ShopService.ITEM_OPTION_TYPE_TOPPING)) {
							sb.append(mapOptions.get(ShopService.ITEM_OPTION_TYPE_TOPPING));
						}
					sb.append("</td>");
					sb.append(tdStyle);
						sb.append("<div class=\"row\" style=\"margin-left:0;\">");
						long orderCost = getFinalCost(totalMoney, orderList.size(), menuInfo, order);
						totalRealCost += orderCost;
						sb.append(getShowPriceWithUnit(orderCost, "", messages));
						sb.append("</div>");
					sb.append("</td>");
					if (isMenuOpening && userId == order.getUser_id()) { 
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
									sbModal.append("<label class=\"text-info\" style=\"font-size:1.125rem;\">").append(order.getDish_name()).append(" : ").append(getShowPriceWithUnit(getFinalCost(totalMoney, orderList.size(), menuInfo, order), "", messages)).append("</label>");
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
				sb.append("<tr>");
				sb.append(String.format("<td style=\"border-top:0.0625rem solid %s;\" colspan=\"6\">", ORDER_BORDER_COLOR));
					sb.append("<div class=\"text-center text-danger font-weight-bold\">");
						sb.append("Total: " + dishcount + " dishs / " + orderList.size() + " orders");
					sb.append("</div>");
				sb.append("</td>");
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
	public static String getHtmlListMenu(List<MenuInfo> menuList, List<ShopInfo> listShopInfo, Map messages, String contextPath) {
		if (menuList == null || menuList.isEmpty()) {
			return "";
		}
		byte status = menuList.get(0).getStatus();
		int maxMenuPerRow = 0;
		if (status == MilkTeaMenuStatus.OPENING.ordinal()) {
			maxMenuPerRow = MAX_OPENING_MENU_PER_ROW;
		} else if (status == MilkTeaMenuStatus.DELIVERING.ordinal()) {
			maxMenuPerRow = MAX_DELIVERING_MENU_PER_ROW;
		} else if (status == MilkTeaMenuStatus.COMPLETED.ordinal()) {
			maxMenuPerRow = MAX_COMPLETED_MENU_PER_ROW;
		}
		if (maxMenuPerRow == 0) {
			return "";
		}
		int size = menuList.size();
		int rowNum = (size - 1) / maxMenuPerRow + 1;
		int left = 0;
		int rest = 0;
		int col;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rowNum; i++) {
			left = size - i * maxMenuPerRow;
			if (left < maxMenuPerRow) {
				rest = (12 - left * 2) / 2;
				col = left;
			} else {
				rest = (12 - maxMenuPerRow * 2) / 2;
				col = maxMenuPerRow;
			}
			sb.append("<div class=\"row\">");
			if (rest > 0) {
				sb.append(String.format("<div class=\"col-sm-%d\"></div>", rest));
			}
			for (int j = 0; j < col; j++) {
				MenuInfo menuInfo = menuList.get(i * maxMenuPerRow + j);
				ShopInfo shopInfo = null;
				if (listShopInfo == null) {
					listShopInfo = new ArrayList<>();
				}
				for (ShopInfo shop : listShopInfo) {
					if (shop.getId() == menuInfo.getShop_id()) {
						shopInfo = shop;
						break;
					}
				}
				if (shopInfo == null) {
					shopInfo = ShopService.getShopById(menuInfo.getShop_id());
					if (shopInfo != null) {
						listShopInfo.add(shopInfo);
					}
				}
				if (shopInfo == null) {
					sb.append("<div class=\"col-sm-2\" style=\"\">");
					sb.append("</div>");
				} else {
					sb.append("<div class=\"col-sm-2\" style=\"position:relative;padding:0.5rem;\">");
					sb.append("<div style=\"background-color:white;\" class=\"border\">");
					sb.append(String.format("<a href=\"%s\">", contextPath + "/milktea/milk_tea_menu.htm?id=" + menuInfo.getId()));
					String preUrlImage = shopInfo.getPre_image_url();
					if (preUrlImage == null || preUrlImage.isEmpty() || preUrlImage.indexOf("http") < 0) {
						sb.append(String.format("<img src=\"%s\" style=\"object-fit:cover;width:100%%;\" class=\"menu-pre-image\"/>", contextPath + getMenuCommonImage()));
					} else {
						sb.append(String.format("<img src=\"%s\" style=\"object-fit:cover;width:100%%;\" class=\"menu-pre-image\"/>", preUrlImage));
					}
					sb.append("</a>");
					sb.append("<div class=\"text-secondary font-weight-bold\" data-toggle=\"tooltip\" data-placement=\"bottom\" style=\"text-overflow:ellipsis;overflow:hidden;white-space:nowrap;\" title=\"").append(menuInfo.getName()).append("\">").append(menuInfo.getName()).append("</div>");
					sb.append("</div>");
					sb.append("</div>");
				}
			}
			if ((rest + col) * 2 < 12) {
				for (int k = 0; k < 12 - 2 * (rest + col); k++) {
					sb.append("<div class=\"col-sm-1\"></div>");
				}
			}
			if (rest > 0) {
				sb.append(String.format("<div class=\"col-sm-%d\"></div>", rest));
			}
			sb.append("</div>");
		}
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getHtmlListMenu(String contextPath, Map messages) {
		StringBuilder sb = new StringBuilder();
		List<ShopInfo> listShopInfo = null;
		sb.append("<div id=\"menu-list\">");
		sb.append("<div>");
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-sm-12 bg-success text-center rounded\" style=\"line-height:2;margin-top:1rem;margin-bottom:0.5rem;\">");
		sb.append(messages.get("MSG_MILK_TEA_MENU_OPENING"));
		sb.append("</div>");
		sb.append("</div>");
		List<MenuInfo> listOpeningMenu = MenuService.getMenuListByStatus((byte)MilkTeaMenuStatus.OPENING.ordinal());
		if (listOpeningMenu == null) {
			sb.append("<div class=\"col-sm-12 text-center\">");
			sb.append(messages.get("MSG_MILK_TEA_NO_MENU_OPENING"));
			sb.append("</div>");
		} else {
			sb.append(getHtmlListMenu(listOpeningMenu, listShopInfo, messages, contextPath));
		}
		sb.append("</div>");
		sb.append("<div>");
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-sm-12 bg-success text-center rounded\" style=\"line-height:2;margin-top:1rem;margin-bottom:0.5rem;\">");
		sb.append(messages.get("MSG_MILK_TEA_MENU_DELIVERING"));
		sb.append("</div>");
		sb.append("</div>");
		List<MenuInfo> listDeliveringMenu = MenuService.getMenuListByStatus((byte)MilkTeaMenuStatus.DELIVERING.ordinal());
		if (listDeliveringMenu == null) {
			sb.append("<div class=\"col-sm-12 text-center\">");
			sb.append(messages.get("MSG_MILK_TEA_NO_MENU_DELIVERING"));
			sb.append("</div>");
		} else {
			sb.append(getHtmlListMenu(listDeliveringMenu, listShopInfo, messages, contextPath));
		}
		sb.append("</div>");
		sb.append("<div>");
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-sm-12 bg-success text-center rounded\" style=\"line-height:2;margin-top:1rem;margin-bottom:0.5rem;\">");
		sb.append(messages.get("MSG_MILK_TEA_MENU_COMPLETED"));
		sb.append("</div>");
		sb.append("</div>");
		List<MenuInfo> listCompletedMenu = MenuService.getMenuListByStatus((byte)MilkTeaMenuStatus.COMPLETED.ordinal());
		if (listCompletedMenu == null) {
			sb.append("<div class=\"col-sm-12 text-center\">");
			sb.append(messages.get("MSG_MILK_TEA_NO_MENU_COMPLETED"));
			sb.append("</div>");
		} else {
			sb.append(getHtmlListMenu(listCompletedMenu, listShopInfo, messages, contextPath));
		}
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getHtmlMenuDetail(MenuInfo menuInfo, UserInfo userInfo, String contextPath, Map messages) {
		StringBuilder sb = new StringBuilder();
		ShopInfo shopInfo = ShopService.getShopById(menuInfo.getShop_id());
		if (shopInfo != null) {
			sb.append("<div id=\"menu-detail\" class=\"col-12\"><div class=\"row\">");
			sb.append("<div class=\"col-lg-3 col-md-6\" style=\"\">");
			String url = shopInfo.getImage_url();
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
					sb.append(menuInfo.getName()).append("</a></h5>");
				} else {
					sb.append(menuInfo.getName()).append("</h5>");
				}
				sb.append("<div class=\"font-italic\" style=\"font-size:0.875rem;margin-bottom:0.5rem;\">").append(shopInfo.getAddress()).append("</div>");
				sb.append("<div class=\"rating\">");
					double rating = 0.0;
					if (shopInfo.getVoting_count() > 0) {
						rating = (double)shopInfo.getStar_count() / shopInfo.getVoting_count();
						rating = ((double)Math.round(rating * 10) / 10);
					}
					sb.append("<div class=\"stars\">");
						for (int i = 0; i < CommonDefine.MAX_MILKTEA_VOTING_STAR; i++) {
							double gap = rating - i;
							if (gap >= 0.3) {
								if (gap <= 0.8 || (gap < 1.0 && i == CommonDefine.MAX_MILKTEA_VOTING_STAR - 1)) {
									sb.append("<span class=\"half\"></span>");
								} else {
									sb.append("<span class=\"full\"></span>");
								}
							} else {
								sb.append("<span class=\"empty\"></span>");
							}
						}
					sb.append("</div>");
					sb.append("<span class=\"text-info font-italic\" style=\"margin-left:0.4rem;\">").append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_SHOP_RATING_S"), shopInfo.getStar_count(), shopInfo.getVoting_count())).append("</span>");
					if (shopInfo.getOpening_count() > 0) {
						sb.append("<div class=\"text-success\" style=\"margin-bottom:0.5rem;font-size:0.875rem;\">").append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_SHOP_INFO_STATISTIC"), shopInfo.getOpening_count(), shopInfo.getOrdered_dish_count())).append("</div>");
					} else {
						sb.append("<div class=\"text-success\" style=\"margin-bottom:0.5rem;font-size:0.875rem;\">").append((String)messages.get("MSG_MILK_TEA_SHOP_INFO_FIRST_TIME_OPEN")).append("</div>");
					}
				sb.append("</div>");
				if (menuInfo.getMax_discount() > 0) {
					sb.append("<div style=\"margin-bottom:0.25rem;\">");
				} else {
					sb.append("<div style=\"margin-bottom:0.5rem;\">");
				}
					sb.append("<span>");
						sb.append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_MENU_INFO_SALE_RATE"), menuInfo.getSale()));
					sb.append("</span>");
					if (menuInfo.getMax_discount() > 0) {
						sb.append("<span style=\"margin-left:1rem;\">");
						sb.append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_MENU_INFO_SALE_MAX_DISCOUNT"), getShowPriceWithUnit(menuInfo.getMax_discount(), "", messages)));
						sb.append("</span>");
						sb.append("</div>");
						sb.append("<div style=\"margin-bottom:0.5rem;\">");
						sb.append("<span>");
					} else {
						sb.append("<span style=\"margin-left:1rem;\">");
					}
						sb.append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_MENU_INFO_SHIPPING_FEE"), getShowPriceWithUnit(menuInfo.getShipping_fee(), "", messages)));
					sb.append("</span>");
				sb.append("</div>");
				sb.append("<div style=\"margin-bottom:0.5rem;\">");
					MilkTeaMenuStatus status = MilkTeaMenuStatus.valueOf(menuInfo.getStatus());
					sb.append("<span>");
					sb.append(MessageFormat.format((String)messages.get("MSG_MILK_TEA_MENU_INFO_STATUS"), status.name().toLowerCase()));
					sb.append("</span>");
				sb.append("</div>");
			sb.append("</div>");
			sb.append("</div></div>");
		}
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
	public static String getShowPriceWithUnit(long price, String option, Map messages) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<span style=\"position:relative;text-align:right;padding-right:0.625rem;%s\">",option));
		sb.append("<span style=\"\">").append(CommonMethod.getFormatNumberThousandComma(price)).append("</span>");
		sb.append("<span style=\"position:absolute;top:0;right:0;font-size:0.75rem;\">").append(messages.get("MSG_MILK_TEA_PRICE_UNIT")).append("</span>");
		sb.append("</span>");
		return sb.toString();
	}
	
	public static long getTotalMoney(List<OrderInfo> orderList) {
		if (orderList == null || orderList.isEmpty()) {
			return 0;
		}
		long total = 0;
		for (OrderInfo order : orderList) {
			total += order.getDish_price() * Math.max(order.getQuantity(), 1);
		}
		return total;
	}
	
	public static long getFinalCost(long totalMoney, int totalOrder, MenuInfo menuInfo, OrderInfo order) {
		int sale = 100;
		if (menuInfo.getSale() > 0 && menuInfo.getSale() < 100) {
			sale = 100 - menuInfo.getSale();
		}
		long price = order.getDish_price() * Math.max(order.getQuantity(), 1);
		if (menuInfo.getMax_discount() > 0 && ((totalMoney * sale) / 100) > menuInfo.getMax_discount()) {
			double rate = ((double) price) / totalMoney;
			price = price - (long) Math.floor(menuInfo.getMax_discount() * rate);
		} else {
			price = (price * sale) / 100;
		}
		long shippingFee = (long) Math.ceil(((double) menuInfo.getShipping_fee()) / totalOrder);
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
		if (option.matches("[^0-9]*([0-9]+).*")) {
			return option.replaceAll("[^0-9]*([0-9]+).*", "$1");
		}
		return option;
	}
	
	public static int calcOptionAmount(String option) {
		String strAmount = getStringOptionAmount(option);
		if (CommonMethod.isValidNumeric(strAmount, 0, Integer.MAX_VALUE)) {
			return Integer.parseInt(strAmount);
		}
		option = option.toLowerCase().replaceAll("\\s+", "-");
		if (option.indexOf("không") >= 0 || option.indexOf("hot") >= 0 || option.indexOf("warm") >= 0
				 || option.indexOf("ấm") >= 0 || option.indexOf("nóng") >= 0 || option.indexOf("ko-đá") >= 0) {
			return 0;
		} else if (option.indexOf("ít") >= 0) {
			return 50;
		} else if (option.indexOf("rất-ít") >= 0) {
			return 30;
		} else if (option.indexOf("vừa") >= 0) {
			return 70;
		} else if (option.indexOf("bình-thường") >= 0 || option.indexOf("normal") >= 0) {
			return 100;
		} else if (option.indexOf("rất-nhiều") >= 0) {
			return 130;
		} else if (option.indexOf("nhiều") >= 0) {
			return 120;
		}
		return 0;
	}
	
}
