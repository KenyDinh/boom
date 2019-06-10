package dev.boom.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaItemOptionType;
import dev.boom.common.milktea.MilkTeaOrderFlag;
import dev.boom.dao.core.DaoValue;
import dev.boom.milktea.object.MenuItem;
import dev.boom.milktea.object.MenuItemOption;
import dev.boom.milktea.object.MenuOrder;
import dev.boom.milktea.object.MenuOrderItem;
import dev.boom.milktea.object.MenuOrderOption;
import dev.boom.milktea.object.MenuOrderOptionItem;
import dev.boom.services.CommonDaoService;
import dev.boom.services.MenuInfo;
import dev.boom.services.MenuService;
import dev.boom.services.OrderInfo;
import dev.boom.services.OrderService;
import dev.boom.services.ShopInfo;
import dev.boom.services.ShopService;
import dev.boom.socket.endpoint.FridayEndpoint;
import dev.boom.socket.func.FridayStaticData;
import net.arnx.jsonic.JSON;

public class ManageMilkTeaSocketSession extends SocketSessionBase {

	
	public ManageMilkTeaSocketSession(Session session, String endPointName, String token) {
		super(session, endPointName, token);
	}

	@Override
	public void process(String message) {
		logInfo(message);
		if (message.startsWith("PREPARING_ORDER:")) {
			if (FridayStaticData.isInPlacingState()) {
				logError("[ManageMilkTeaSocketSession] (process) still be in placing state!");
				return;
			}
			SocketSessionBase fridaySocket = SocketSessionPool.getStoredSocketSessionByUserId(getUserId(), FridayEndpoint.ENDPOINT_NAME);
			if (fridaySocket == null) {
				logError("[ManageMilkTeaSocketSession] fridaySocket not found!");
				return;
			}
			String datas = message.replace("PREPARING_ORDER:", "");
			if (!datas.matches("[0-9]+(,[0-9]+)*")) {
				logError("[ManageMilkTeaSocketSession] (process) Order list is invalid: " + datas);
				return;
			}
			String[] strIds = datas.split(",");
			List<Long> ids = new ArrayList<>();
			for (String strOrderId : strIds) {
				if (!CommonMethod.isValidNumeric(strOrderId, 1, Long.MAX_VALUE)) {
					logError("[ManageMilkTeaSocketSession] (process) Order's id is invalid: " + strOrderId);
					continue;
				}
				ids.add(Long.parseLong(strOrderId));
			}
			List<OrderInfo> orderList = OrderService.getOrderList(ids);
			if (orderList == null || orderList.isEmpty()) {
				logError("[ManageMilkTeaSocketSession] (process) No order found: " + strIds);
				return;
			}
			if (orderList.size() != ids.size()) {
				logWarning("[ManageMilkTeaSocketSession] (process) order_list'size: " + orderList.size() + " is different from id_list's size: " + ids.size());
			}
			long menuId = orderList.get(0).getMenuId();
			long shopId = orderList.get(0).getShopId();
			MenuInfo menuInfo = MenuService.getMenuById(menuId);
			if (menuInfo == null) {
				logError("[ManageMilkTeaSocketSession] menu not found, menu_id:" + menuId);
				return;
			}
			ShopInfo shopInfo = ShopService.getShopById(shopId);
			if (shopInfo == null) {
				logError("[ManageMilkTeaSocketSession] shop not found, shop_id:" + shopId);
				return;
			}
			List<MenuItem> menuItemList = MenuService.getMenuItemListByShopId(shopId);
			if (menuItemList == null || menuItemList.isEmpty()) {
				logError("[ManageMilkTeaSocketSession] (process) No menu item found, shop_id:" + shopId);
				return;
			}
			List<MenuOrderItem> menuOrderItemList = new ArrayList<>();
			List<OrderInfo> uniqueOrderList = new ArrayList<>();
			List<Long> _ids = new ArrayList<>();
			for (OrderInfo orderInfo : orderList) {
				if (orderInfo.getMenuId() != menuId) {
					logError("[ManageMilkTeaSocketSession] menu id is invalid, menu_id:" + menuId + ", order_id:" + orderInfo.getId());
					return;
				}
				if (orderInfo.getShopId() != shopId) {
					logError("[ManageMilkTeaSocketSession] menu id is invalid, menu_id:" + menuId + ", order_id:" + orderInfo.getId());
					return;
				}
				_ids.add(orderInfo.getId());
				boolean exist = false;
				for (OrderInfo checkedOrder : uniqueOrderList) {
					if (orderInfo.isRoughtlySimilar(checkedOrder)) {
						checkedOrder.setQuantity(checkedOrder.getQuantity() + orderInfo.getQuantity());
						exist = true;
						break;
					}
				}
				if (exist) {
					continue;
				}
				uniqueOrderList.add(orderInfo);
				
			}
			for (OrderInfo orderInfo : uniqueOrderList) {
				MenuOrderItem menuOrderItem = toMenuOrderItem(menuItemList, orderInfo);
				if (menuOrderItem == null) {
					logError("[ManageMilkTeaSocketSession] invalid order, menu_id:" + menuId + ", order_id:" + orderInfo.getId());
					return;
				}
				menuOrderItemList.add(menuOrderItem);
			}
			MenuOrder menuOrder = new MenuOrder();
			menuOrder.setDishes(menuOrderItemList.toArray(new MenuOrderItem[0]));
			System.out.println(JSON.encode(menuOrder));
			
			FridayStaticData.setMenuOrder(menuOrder);
			FridayStaticData.setIds(_ids);
			Map<String, Object> mapData = new HashMap<>();
			mapData.put("type", "milktea_order");
			mapData.put("step", "prepare");
			mapData.put("url", shopInfo.getUrl());
			mapData.put("menu_order", menuOrder);
			fridaySocket.sendMessage(JSON.encode(mapData));
		} else if (message.startsWith("ORDER_FEED_BACK:")) {
			if (!FridayStaticData.isInPlacingState()) {
				logError("[ManageMilkTeaSocketSession] (process) still be in placing state!");
				return;
			}
			String result = message.replace("ORDER_FEED_BACK:", "");
			if (result == null || !result.equals("success")) {
				logError("[ManageMilkTeaSocketSession] (process) Place order failed!");
				return;
			}
			List<Long> ids = FridayStaticData.getIds();
			if (ids != null && !ids.isEmpty()) {
				List<OrderInfo> orderList = OrderService.getOrderList(ids);
				if (orderList != null && !orderList.isEmpty()) {
					List<DaoValue> updates = new ArrayList<>();
					for (OrderInfo order : orderList) {
						order.setFlag(order.getFlag() | MilkTeaOrderFlag.PLACED.getBitMask());
						updates.add(order.getTblInfo());
					}
					if (!CommonDaoService.update(updates)) {
						logError("[ManageMilkTeaSocketSession] (process) update order flag failed!");
					}
				}
			} else {
				logWarning("[ManageMilkTeaSocketSession] No order_id found!");
			}
			FridayStaticData.resetOrderState();
			sendMessage("order_done");
		} else if (message.startsWith("FORCE_RESET_STATE")) {
			FridayStaticData.forceResetmoveState();
		}
	}
	
	private MenuOrderItem toMenuOrderItem(List<MenuItem> menuItemList, OrderInfo orderInfo) {
		MenuItem itemOrder = null;
		for (MenuItem menuItem : menuItemList) {
			if (menuItem.getName().equals(orderInfo.getDishName()) && menuItem.getType().equals(orderInfo.getDishType())) {
				itemOrder = menuItem;
				break;
			}
		}
		if (itemOrder == null) {
			logError("[ManageMilkTeaSocketSession] No matched menu item, order_id:" + orderInfo.getId() + ", order_name:" + orderInfo.getDishName());
			return null;
		}
		MenuOrderItem menuOrderItem = new MenuOrderItem();
		menuOrderItem.setDish_id(itemOrder.getItem_id());
		menuOrderItem.setQuantity((int)orderInfo.getQuantity());
		//
		List<MenuOrderOption> menuOrderOptionList = new ArrayList<>();
		//
		List<MenuItemOption> listItemOption = itemOrder.getListOptions();
		List<String> optionNameList = orderInfo.getAllOptionList();
		for (String name : optionNameList) {
			MenuItemOption matchedOption = null;
			for (MenuItemOption menuItemOption : listItemOption) {
				if (name.equals(menuItemOption.getName())) {
					matchedOption = menuItemOption;
					break;
				}
			}
			if (matchedOption == null) {
				logWarning("[ManageMilkTeaSocketSession] (proceNo matched option:" + name);
				continue;
			}
			long optionTypeId = itemOrder.getLimit_select().getOptionId(MilkTeaItemOptionType.valueOf(matchedOption.getType()));
			MenuOrderOption moo = null;
			for (MenuOrderOption _menuOrderOption : menuOrderOptionList) {
				if (_menuOrderOption.getId() == optionTypeId) {
					moo = _menuOrderOption;
					break;
				}
			}
			if (moo == null) {
				moo = new MenuOrderOption();
				moo.setId(optionTypeId);
				menuOrderOptionList.add(moo);
			}
			MenuOrderOptionItem mooi = new MenuOrderOptionItem();
			mooi.setId(matchedOption.getId());
			mooi.setQuantity(1);
			moo.addOption_items(mooi);
		}
		menuOrderItem.setOptions(menuOrderOptionList.toArray(new MenuOrderOption[0]));
		
		return menuOrderItem;
	}
}
