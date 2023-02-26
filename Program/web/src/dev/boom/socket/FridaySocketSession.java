package dev.boom.socket;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.ManageLogType;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.milktea.object.Menu;
import dev.boom.milktea.object.MenuItem;
import dev.boom.services.Dish;
import dev.boom.services.ManageLogService;
import dev.boom.services.Shop;
import dev.boom.services.User;
import dev.boom.socket.endpoint.ManageMilkTeaEndPoint;
import dev.boom.socket.func.FridayStaticData;
import dev.boom.tbl.info.TblShopInfo;
import net.arnx.jsonic.JSON;

public class FridaySocketSession extends SocketSessionBase {

	public FridaySocketSession(Session session, String endPointName, String token) {
		super(session, endPointName, token);
	}

	@Override
	public void process(String message) {
		logInfo(message);
		if (message.startsWith("MENU_OBJECT")) {
			String source = message.replace("MENU_OBJECT", "");
			Menu menu = (Menu) parseMessage(source, Menu.class);
			if (menu != null) {
				FridayStaticData.setMenu(menu);
				Map<String, Object> mapData = new HashMap<>();
				mapData.put("type", "load_menu");
				mapData.put("step", "continue");
				sendMessage(JSON.encode(mapData));
			} else {
				logError("[FridaySocketSession] (process) Menu is null!");
			}
		} else if (message.startsWith("MENU_ITEM")) {
			String source = message.replace("MENU_ITEM", "");
			MenuItem menuItem = (MenuItem) parseMessage(source, MenuItem.class);
			if (menuItem != null) {
				menuItem.setDetail(source);
				FridayStaticData.addMenuItem(menuItem);
				Map<String, Object> mapData = new HashMap<>();
				mapData.put("type", "load_menu");
				mapData.put("step", "continue");
				sendMessage(JSON.encode(mapData));
			} else {
				logError("[FridaySocketSession] (process) MenuItem is null!");
			}
		} else if (message.startsWith("finish_update")) {
			update();
		} else if (message.startsWith("ORDER_DETAIL")) {
			SocketSessionBase manageMilkTeaSS = SocketSessionPool.getStoredSocketSessionByUserId(getUserId(), ManageMilkTeaEndPoint.ENDPOINT_NAME);
			if (manageMilkTeaSS == null) {
				logError("[FridaySocketSession] (process) ManageMilkteaSocket not found!");
				return;
			}
			manageMilkTeaSS.process("PLACING_ORDER");
		} else if (message.startsWith("ORDER_FEED_BACK:")) {
			SocketSessionBase manageMilkTeaSS = SocketSessionPool.getStoredSocketSessionByUserId(getUserId(), ManageMilkTeaEndPoint.ENDPOINT_NAME);
			if (manageMilkTeaSS == null) {
				logError("[FridaySocketSession] (process) ManageMilkteaSocket not found!");
				return;
			}
			manageMilkTeaSS.process(message);
		} else if (message.startsWith("OPEN_FEED_BACK:")) {
			if (getRefSocketId() == null || !CommonMethod.isValidNumeric(getRefSocketId().toString(), 1, Long.MAX_VALUE)) {
				logError("[FridaySocketSession] (process) Not found mmsocket!");
				return;
			}
			SocketSessionBase manageMilkTeaSS = SocketSessionPool.getStoredSocketSessionByUserId(Long.parseLong(getRefSocketId().toString()), ManageMilkTeaEndPoint.ENDPOINT_NAME);
			if (manageMilkTeaSS == null) {
				logError("[FridaySocketSession] (process) Not found mmsocket! id : " + getRefSocketId().toString());
				return;
			}
			String response = message.replace("OPEN_FEED_BACK:", "");
			logInfo("[ManageMilkTeaSocketSession] " + response);
			if (response.equals("complete")) {
				manageMilkTeaSS.sendMessage("{\"message\":\"menu_open\"}");
			} else {
				manageMilkTeaSS.sendMessage(String.format("{\"message\":\"menu_open\",\"error\":\"%s\"}", response));
			}
			setRefSocketId(null);
		} else {
			logError("[FridaySocketSession] (process) invalid message!");
		}
	}

	private void update() {
		Menu menu = FridayStaticData.getMenu();
		if (menu == null) {
			logError("[FridaySocketSession] (update) No Menu found!");
			return;
		}
		List<MenuItem> listItems = FridayStaticData.getMenuItemList();
		if (listItems == null || listItems.isEmpty()) {
			logError("[FridaySocketSession] (update) No MenuItem found!");
			return;
		}
		Shop shopInfo = new Shop();
		shopInfo.setUrl(menu.getUrl());
		List<DaoValue> listShop = CommonDaoFactory.Select(shopInfo.getShopInfo());
		if (listShop == null || listShop.isEmpty()) {
			shopInfo.setAddress(menu.getAddress());
			shopInfo.setName(getShopName(menu.getMenu_name()));
			shopInfo.setImageUrl(menu.getImage_url());
			shopInfo.setPreImageUrl(menu.getPre_image_url());
			long shop_id = CommonDaoFactory.Insert(shopInfo.getShopInfo());
			if (shop_id <= 0) {
				logError("[FridaySocketSession] (update) Insert shop info failed!");
				return;
			}
			shopInfo.setId(shop_id);
		} else {
			shopInfo = new Shop((TblShopInfo) listShop.get(0));
			Dish dishInfo = new Dish();
			dishInfo.setShopId(shopInfo.getId());
			//if (CommonDaoFactory.Count(dishInfo.getDishInfo()) > 0) {
			CommonDaoFactory.executeUpdate(String.format("DELETE FROM %s WHERE shop_id = %d", dishInfo.getDishInfo().getTblName(), dishInfo.getShopId()));
			//}
		}
		dev.boom.services.Menu menuInfo = new dev.boom.services.Menu();
		menuInfo.setName(menu.getMenu_name());
		menuInfo.setShopId(shopInfo.getId());
		menuInfo.setSale((short) menu.getSale());
		menuInfo.setMaxDiscount(menu.getMax_discount());
		menuInfo.setCode(menu.getCode());
		menuInfo.setShippingFee(menu.getShipping_fee());
		Date now = new Date();
		menuInfo.setCreated(CommonMethod.getFormatDateString(now));
		menuInfo.setExpired(CommonMethod.getFormatDateString(new Date(now.getTime() + CommonDefine.MILLION_SECOND_DAY)));
		menuInfo.setUpdated(CommonMethod.getFormatDateString(now));
		boolean success = true;
		if (CommonDaoFactory.Insert(menuInfo.getMenuInfo()) < 0) {
			success = false;
		}
		if (success) {
			for (MenuItem menuItem : listItems) {
				Dish dishInfo = new Dish();
				dishInfo.setShopId(shopInfo.getId());
				dishInfo.setDetail(menuItem.getDetail());
				if (CommonDaoFactory.Insert(dishInfo.getDishInfo()) < 0) {
					success = false;
					break;
				}
			}
		}
		if (!success) {
			logError("[FridaySocketSession] (update) update failed!");
		} else {
			User userInfo = new User();
			userInfo.setId(getUserId());
			userInfo.setUsername(getUsername());
			ManageLogService.createManageLog(userInfo, ManageLogType.ADD_MENU, menu.getMenu_name());
		}
	}

	private String getShopName(String menuName) {
		if (menuName == null || menuName.isEmpty()) {
			return "";
		}
		if (menuName.indexOf(" - ") > 0) {
			return menuName.split(" - ")[0].trim();
		}
		return menuName;
	}
}
