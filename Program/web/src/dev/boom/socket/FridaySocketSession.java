package dev.boom.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import dev.boom.dao.core.DaoValue;
import dev.boom.milktea.object.Menu;
import dev.boom.milktea.object.MenuItem;
import dev.boom.services.CommonDaoService;
import dev.boom.socket.endpoint.FridayStaticData;
import dev.boom.socket.endpoint.ManageMilkTeaEndPoint;
import dev.boom.tbl.info.TblDishInfo;
import dev.boom.tbl.info.TblMenuInfo;
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
		}
		else {
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
		TblShopInfo shopInfo = new TblShopInfo();
		shopInfo.setUrl(menu.getUrl());
		List<DaoValue> listShop = CommonDaoService.select(shopInfo);
		if (listShop == null || listShop.isEmpty()) {
			shopInfo.setAddress(menu.getAddress());
			shopInfo.setName(getShopName(menu.getMenu_name()));
			shopInfo.setImage_url(menu.getImage_url());
			shopInfo.setPre_image_url(menu.getPre_image_url());
			long shop_id = (Long) CommonDaoService.insert(shopInfo);
			if (shop_id <= 0) {
				logError("[FridaySocketSession] (update) Insert shop info failed!");
				return;
			}
			shopInfo.setId(shop_id);
		} else {
			shopInfo = (TblShopInfo) listShop.get(0);
			TblDishInfo dishInfo = new TblDishInfo();
			dishInfo.setShop_id(shopInfo.getId());
			if (CommonDaoService.count(dishInfo) > 0) {
				dishInfo.setDelete();
				if (!CommonDaoService.delete(dishInfo)) {
					logError("[FridaySocketSession] (update) delete old dishs failed!");
					return;
				}
			}
		}
		List<DaoValue> updateList = new ArrayList<>();
		TblMenuInfo menuInfo = new TblMenuInfo();
		menuInfo.setName(menu.getMenu_name());
		menuInfo.setShop_id(shopInfo.getId());
		menuInfo.setSale((short) menu.getSale());
		menuInfo.setMax_discount(menu.getMax_discount());
		menuInfo.setCode(menu.getCode());
		menuInfo.setShipping_fee(menu.getShipping_fee());
		updateList.add(menuInfo);
		for (MenuItem menuItem : listItems) {
			TblDishInfo dishInfo = new TblDishInfo();
			dishInfo.setShop_id(shopInfo.getId());
			dishInfo.setDetail(menuItem.getDetail());
			updateList.add(dishInfo);
		}
		if (!CommonDaoService.update(updateList)) {
			logError("[FridaySocketSession] (update) update failed!");
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
