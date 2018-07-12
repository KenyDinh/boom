package dev.boom.pages.milktea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaMenuStatus;
import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.MilkTeaUserInfo;
import dev.boom.entity.info.MenuInfo;
import dev.boom.entity.info.OrderInfo;
import dev.boom.entity.info.ShopOptionInfo;
import dev.boom.milktea.object.MenuItem;
import dev.boom.services.CommonDaoService;
import dev.boom.services.MenuService;
import dev.boom.services.OrderService;
import dev.boom.services.ShopService;

public class MilkTeaManageOrder extends MilkTeaAjaxPageBase {

	private static final long serialVersionUID = 1L;
	private static final int MAX_TOPPING_COUNT = 3;

	private static final int MODE_INSERT = 1;
	private static final int MODE_DELETE = 2;

	private int mode = 0;
	private boolean error = false;
	private MenuInfo menuInfo;

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
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
		String strMode = getContext().getRequestParameter("mode");
		if (strMode != null && CommonMethod.isValidNumeric(strMode, MODE_INSERT, MODE_DELETE)) {
			mode = Integer.parseInt(strMode);
		}
		if (mode == 0) {
			GameLog.getInstance().error("[MilkTeaManageOrder] No mode id found!");
			error = true;
			return;
		}
		long menuId = 0;
		String strMenuId = getContext().getRequestParameter("menu_id");
		if (strMenuId != null && CommonMethod.isValidNumeric(strMenuId, 1, Long.MAX_VALUE)) {
			menuId = Long.parseLong(strMenuId);
		}
		if (menuId == 0) {
			GameLog.getInstance().error("[MilkTeaManageOrder] No menu id found!");
			error = true;
			return;
		}
		menuInfo = MenuService.getMenuById(menuId);
		if (menuInfo == null) {
			GameLog.getInstance().error("[MilkTeaManageOrder] No menu found!");
			error = true;
			return;
		}
		if ((int) menuInfo.getStatus() != MilkTeaMenuStatus.OPENING.ordinal()) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Menu is no longer open!, id:" + menuId);
			error = true;
			return;
		}

	}

	@Override
	public void onPost() {
		super.onPost();
		if (error) {
			return;
		}
		switch (mode) {
		case MODE_INSERT:
			doInsertOrder();
			break;
		case MODE_DELETE:
			doDeleteOrder();
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
		List<Long> listOptionId = null;
		int quantity = 1;
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
			} else if (key.equals("menu_item_option")) {
				if (values != null && values.length > 0) {
					for (String optId : values) {
						if (CommonMethod.isValidNumeric(optId, 1, Long.MAX_VALUE)) {
							if (listOptionId == null) {
								listOptionId = new ArrayList<>();
							}
							listOptionId.add(Long.parseLong(optId));
						}
					}
				}
			} else if (key.equals("quantity")) {
				if (values != null && values.length > 0) {
					if (CommonMethod.isValidNumeric(values[0], 1, Integer.MAX_VALUE)) {
						quantity = Integer.parseInt(values[0]);
					}
				}
			}
		}
		MenuItem menuItem = MenuService.getMenuItemById(menuItemId);
		if (menuItem == null) {
			GameLog.getInstance().error("[MilkTeaManageOrder] MenuItem is null!");
			error = true;
			return;
		}
		if (menuItem.getShop_id() != menuInfo.getShop_id()) {
			GameLog.getInstance().error("[MilkTeaManageOrder] MenuItem is invalid, shop id is different from the menu!");
			error = true;
			return;
		}
		String optionList = "";
		long plusPrice = 0;
		Map<Short, Integer> mapCountOption = new HashMap<>();
		if (listOptionId != null && !listOptionId.isEmpty()) {
			List<ShopOptionInfo> listOptions = ShopService.getShopOptionListByIds(listOptionId);
			if (listOptions == null || listOptions.isEmpty()) {
				GameLog.getInstance().error("[MilkTeaManageOrder] Option list is null!");
				error = true;
				return;
			}
			for (ShopOptionInfo option : listOptions) {
				int c = 1;
				if (mapCountOption.containsKey(option.getType())) {
					c += mapCountOption.get(option.getType());
				}
				mapCountOption.put(option.getType(), c);
				plusPrice += option.getPrice();
				if (optionList.length() > 0) {
					optionList += ",";
				}
				optionList += option.getId();
			}
		}
//		if (menuItem.getList_size() != null && !mapCountOption.containsKey(ShopService.ITEM_OPTION_TYPE_SIZE)) {
//			GameLog.getInstance().error("[MilkTeaManageOrder] No type of size was chosen!");
//			error = true;
//			return;
//		}
		if (mapCountOption.containsKey(ShopService.ITEM_OPTION_TYPE_SIZE) && mapCountOption.get(ShopService.ITEM_OPTION_TYPE_SIZE) > 1) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Only one type of size can be chosen!");
			error = true;
			return;
		}
//		if (menuItem.getList_ice() != null && !mapCountOption.containsKey(ShopService.ITEM_OPTION_TYPE_ICE)) {
//			GameLog.getInstance().error("[MilkTeaManageOrder] No type of ice was chosen!");
//			error = true;
//			return;
//		}
		if (mapCountOption.containsKey(ShopService.ITEM_OPTION_TYPE_ICE) && mapCountOption.get(ShopService.ITEM_OPTION_TYPE_ICE) > 1) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Only one type of ice can be chosen!");
			error = true;
			return;
		}
//		if (menuItem.getList_sugar() != null && !mapCountOption.containsKey(ShopService.ITEM_OPTION_TYPE_SUGAR)) {
//			GameLog.getInstance().error("[MilkTeaManageOrder] No type of sugar was chosen!");
//			error = true;
//			return;
//		}
		if (mapCountOption.containsKey(ShopService.ITEM_OPTION_TYPE_SUGAR) && mapCountOption.get(ShopService.ITEM_OPTION_TYPE_SUGAR) > 1) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Only one type of ice can be chosen!");
			error = true;
			return;
		}
		if (mapCountOption.containsKey(ShopService.ITEM_OPTION_TYPE_TOPPING) && mapCountOption.get(ShopService.ITEM_OPTION_TYPE_TOPPING) > MAX_TOPPING_COUNT) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Number of topping is exceed max : " + mapCountOption.get(ShopService.ITEM_OPTION_TYPE_TOPPING) + " > " + MAX_TOPPING_COUNT);
			error = true;
			return;
		}
		List<DaoValue> updates = new ArrayList<>();
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setUser_id(userInfo.getId());
		orderInfo.setUsername(userInfo.getUsername());
		orderInfo.setMenu_id(menuInfo.getId());
		orderInfo.setShop_id(menuInfo.getShop_id());
		orderInfo.setDish_name(menuItem.getName());
		orderInfo.setDish_type(menuItem.getType());
		orderInfo.setDish_price(menuItem.getPrice() + plusPrice);
		orderInfo.setDish_code(menuItem.getName().hashCode());
		orderInfo.setQuantity(quantity);
		orderInfo.setOption_list(optionList);
		updates.add(orderInfo);
		if (milkteaUserInfo == null) {
			milkteaUserInfo = new MilkTeaUserInfo();
			milkteaUserInfo.setUser_id(userInfo.getId());
			milkteaUserInfo.setUsername(userInfo.getUsername());
			updates.add(milkteaUserInfo);
		}
		if (!CommonDaoService.update(updates)) {
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
		OrderInfo orderInfo = OrderService.getOrderInfoById(Long.parseLong(strOrderId));
		if (orderInfo == null) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Order is null!");
			error = true;
			return;
		}
		if (orderInfo.getMenu_id() != menuInfo.getId() || orderInfo.getUser_id() != userInfo.getId()) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Order is invalid!");
			error = true;
			return;
		}
		if (!CommonDaoService.delete(orderInfo)) {
			GameLog.getInstance().error("[MilkTeaManageOrder] Deleteo order failed!");
			error = true;
			return;
		}
	}

	@Override
	public void onRender() {
		super.onRender();
		if (error) {
			return;
		}
		List<ShopOptionInfo> listShopOption = ShopService.getShopOptionListByShopId(menuInfo.getShop_id());
		List<OrderInfo> listOrder = OrderService.getOrderInfoListByMenuId(menuInfo.getId());
		addModel("result", MilkTeaCommonFunc.getHtmlListOrder(listOrder, menuInfo, userInfo, listShopOption, getHostURL() + getContextPath(), getMessages()));
	}

}
