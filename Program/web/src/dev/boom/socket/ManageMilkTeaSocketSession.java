package dev.boom.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaOrderFlag;
import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.MenuInfo;
import dev.boom.entity.info.OrderInfo;
import dev.boom.entity.info.ShopInfo;
import dev.boom.entity.info.ShopOptionInfo;
import dev.boom.milktea.object.MenuOrder;
import dev.boom.milktea.object.MenuOrderOption;
import dev.boom.services.CommonDaoService;
import dev.boom.services.MenuService;
import dev.boom.services.OrderService;
import dev.boom.services.ShopService;
import dev.boom.socket.endpoint.FridayEndpoint;
import dev.boom.socket.endpoint.FridayStaticData;
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
			long menuId = orderList.get(0).getMenu_id();
			long shopId = orderList.get(0).getShop_id();
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
			Map<Long, ShopOptionInfo> mapOptions = new HashMap<>();
			for (OrderInfo orderInfo : orderList) {
				initListOrderOption(orderInfo, mapOptions);
			}
			List<MenuOrder> menuOrderList = new ArrayList<>();
			for (OrderInfo orderInfo : orderList) {
				if (orderInfo.getMenu_id() != menuId) {
					logError("[ManageMilkTeaSocketSession] menu id is invalid, menu_id:" + menuId + ", order_id:" + orderInfo.getId());
					return;
				}
				if (orderInfo.getShop_id() != shopId) {
					logError("[ManageMilkTeaSocketSession] menu id is invalid, menu_id:" + menuId + ", order_id:" + orderInfo.getId());
					return;
				}
				MenuOrder menuOrder = fromOrderInfoToMenuOrder(orderInfo, mapOptions);
				if (menuOrder == null) {
					logError("[ManageMilkTeaSocketSession] menuOrder is null!");
					return;
				}
				for (int i = 0; i < orderInfo.getQuantity(); i++) {
					menuOrderList.add(menuOrder);
				}
			}
			//send message
			SocketSessionBase fridaySocket = SocketSessionPool.getStoredSocketSessionByUserId(getUserId(), FridayEndpoint.ENDPOINT_NAME);
			if (fridaySocket == null) {
				logError("[ManageMilkTeaSocketSession] fridaySocket not found!");
				return;
			}
			FridayStaticData.setMenuOrderList(menuOrderList);
			Map<String, Object> mapData = new HashMap<>();
			mapData.put("type", "milktea_order");
			mapData.put("step", "prepare");
			mapData.put("url", shopInfo.getUrl());
			fridaySocket.sendMessage(JSON.encode(mapData));
		} else if (message.startsWith("PLACING_ORDER")) {
			if (!FridayStaticData.isInPlacingState()) {
				logError("[ManageMilkTeaSocketSession] (process) not in placing state!");
				return;
			}
			SocketSessionBase fridaySocket = SocketSessionPool.getStoredSocketSessionByUserId(getUserId(), FridayEndpoint.ENDPOINT_NAME);
			if (fridaySocket == null) {
				FridayStaticData.resetPlacingOrderState();
				logError("[ManageMilkTeaSocketSession] fridaySocket not found!");
				return;
			}
			List<MenuOrder> menuOrderList = FridayStaticData.getMenuOrderList();
			if (menuOrderList == null || menuOrderList.isEmpty()) {
				FridayStaticData.resetPlacingOrderState();
				logError("[ManageMilkTeaSocketSession] menuOrderList is null!");
				return;
			}
			MenuOrder nextOrder = null;
			for (MenuOrder menuOrder : menuOrderList) {
				if (!menuOrder.isSent()) {
					nextOrder = menuOrder;
					break;
				}
			}
			Map<String, Object> mapData = new HashMap<>();
			mapData.put("type", "milktea_order");
			fridaySocket.sendMessage(JSON.encode(mapData));
			if (nextOrder == null) {
				mapData.put("step", "order");
				fridaySocket.sendMessage(JSON.encode(mapData));
			} else {
				mapData.put("step", "detail");
				mapData.put("order", nextOrder);
				fridaySocket.sendMessage(JSON.encode(mapData));
				nextOrder.setSent(true);
			}
		} else if (message.startsWith("ORDER_FEED_BACK:")) {
			if (!FridayStaticData.isInPlacingState()) {
				logError("[ManageMilkTeaSocketSession] (process) still be in placing state!");
				return;
			}
			FridayStaticData.resetPlacingOrderState();
			String datas = message.replace("ORDER_FEED_BACK:", "");
			if (!datas.matches("[0-9]+(,[0-9]+)*")) {
				logError("[ManageMilkTeaSocketSession] (process) Order feed back id list is invalid: " + datas);
				//return;
			}
			List<Long> ids = new ArrayList<>();
			for (String strId : datas.split(",")) {
				if (CommonMethod.isValidNumeric(strId, 1, Long.MAX_VALUE)) {
					ids.add(Long.parseLong(strId));
				}
			}
			if (!ids.isEmpty()) {
				List<OrderInfo> orderList = OrderService.getOrderList(ids);
				if (orderList != null && !orderList.isEmpty()) {
					List<DaoValue> updates = new ArrayList<>();
					for (OrderInfo order : orderList) {
						order.setFlag(order.getFlag() | MilkTeaOrderFlag.PLACED.getBitMask());
						updates.add(order);
					}
					if (!CommonDaoService.update(updates)) {
						logError("[ManageMilkTeaSocketSession] (process) update order flag failed!");
					}
				}
			}
			sendMessage("order_done");
		} else if (message.startsWith("FORCE_RESET_STATE")) {
			FridayStaticData.forceResetmoveState();
		}
	}
	
	private void initListOrderOption(OrderInfo orderInfo, Map<Long, ShopOptionInfo> mapOptions) {
		String optionList = orderInfo.getOption_list();
		if (optionList == null || optionList.isEmpty() || !optionList.matches("[0-9]+(,[0-9]+)*")) {
			return;
		}
		List<Long> shopOptionIds = new ArrayList<>();
		for (String strId : optionList.split(",")) {
			if (CommonMethod.isValidNumeric(strId, 1, Long.MAX_VALUE)) {
				Long id = Long.parseLong(strId);
				if (mapOptions.containsKey(id)) {
					continue;
				}
				shopOptionIds.add(id);
			} else {
				logError("[initListOrderOption] invalid id:" + strId);
			}
		}
		if (shopOptionIds.isEmpty()) {
			return;
		}
		List<ShopOptionInfo> shopOptionInfo = ShopService.getShopOptionListByIds(shopOptionIds);
		if (shopOptionInfo != null && !shopOptionInfo.isEmpty()) {
			for (ShopOptionInfo info : shopOptionInfo) {
				mapOptions.put(info.getId(), info);
			}
		}
	}

	private MenuOrder fromOrderInfoToMenuOrder(OrderInfo orderInfo, Map<Long, ShopOptionInfo> mapOptions) {
		if (orderInfo == null) {
			throw new NullPointerException("(fromOrderInfoToMenuOrder) order null!");
		}
		MenuOrder menuOrder = new MenuOrder();
		menuOrder.setId(orderInfo.getId());
		menuOrder.setName(orderInfo.getDish_name());
		menuOrder.setPrice(orderInfo.getDish_price());
		menuOrder.setSent(false);
		String optionList = orderInfo.getOption_list();
		if (optionList == null || optionList.isEmpty() || !optionList.matches("[0-9]+(,[0-9]+)*")) {
			menuOrder.setOptions(new MenuOrderOption[0]);
		} else {
			String[] idList = optionList.split(",");
			MenuOrderOption[] options = new MenuOrderOption[idList.length];
			int index = 0;
			for (String strId : idList) {
				if (!CommonMethod.isValidNumeric(strId, 1, Long.MAX_VALUE)) {
					logError("[fromOrderInfoToMenuOrder] invalid id:" + strId);
					return null;
				}
				Long id = Long.parseLong(strId);
				if (!mapOptions.containsKey(id)) {
					logError("[fromOrderInfoToMenuOrder] option not found, id:" + id);
					return null;
				}
				ShopOptionInfo shopOption = mapOptions.get(id);
				MenuOrderOption menuOrderOption = new MenuOrderOption();
				menuOrderOption.setName(shopOption.getName());
				menuOrderOption.setPrice(shopOption.getPrice());
				options[index++] = menuOrderOption;
			}
			menuOrder.setOptions(options);
		}
		
		return menuOrder;
	}
}
