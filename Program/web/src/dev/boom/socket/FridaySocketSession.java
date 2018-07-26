package dev.boom.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import dev.boom.common.CommonMethod;
import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.DishInfo;
import dev.boom.entity.info.MenuInfo;
import dev.boom.entity.info.ShopInfo;
import dev.boom.entity.info.ShopOptionInfo;
import dev.boom.milktea.object.Menu;
import dev.boom.milktea.object.MenuItem;
import dev.boom.milktea.object.MenuItemOption;
import dev.boom.services.CommonDaoService;
import dev.boom.socket.endpoint.FridayStaticData;
import dev.boom.socket.endpoint.ManageMilkTeaEndPoint;
import net.arnx.jsonic.JSON;

public class FridaySocketSession extends SocketSessionBase {

	// ---------- DB update flag ---------- //
	public static final int FLAG_CREATE_NEW_SHOP = 0x001;
	public static final int FLAG_CREATE_NEW_MENU = 0x002;
	public static final int FLAG_UPDATE_SHOP_DETAIL = 0x004;

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
			String strFlag = message.replace("finish_update", "");
			if (CommonMethod.isValidNumeric(strFlag, 1, 15)) {
				update(Integer.parseInt(strFlag));
			}
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

	private void update(int flag) {
		if ((flag & 7) == 0) {
			return;
		}
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
		ShopInfo shopInfo = new ShopInfo();
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
			shopInfo = (ShopInfo) listShop.get(0);
			if (checkUpdateFlag(flag, FLAG_UPDATE_SHOP_DETAIL)) {
				// delete away the old data.
				List<DaoValue> updates = new ArrayList<>();
				DishInfo ddishInfo = new DishInfo();
				ddishInfo.setShop_id(shopInfo.getId());
				if (CommonDaoService.count(ddishInfo) > 0) {
					ddishInfo.setDelete();
					updates.add(ddishInfo);
				}

				ShopOptionInfo dshopOptionInfo = new ShopOptionInfo();
				dshopOptionInfo.setShop_id(shopInfo.getId());
				if (CommonDaoService.count(dshopOptionInfo) > 0) {
					dshopOptionInfo.setDelete();
					updates.add(dshopOptionInfo);
				}

				shopInfo.setAddress(menu.getAddress());
				shopInfo.setName(getShopName(menu.getMenu_name()));
				shopInfo.setImage_url(menu.getImage_url());
				updates.add(shopInfo);

				if (!CommonDaoService.update(updates)) {
					logError("[FridaySocketSession] (update) update/delete failed");
					return;
				}
				shopInfo.Sync();
			}
		}
		List<DaoValue> updateList = new ArrayList<>();
		//
		if (checkUpdateFlag(flag, FLAG_CREATE_NEW_MENU)) {
			MenuInfo menuInfo = new MenuInfo();
			menuInfo.setName(menu.getMenu_name());
			menuInfo.setShop_id(shopInfo.getId());
			menuInfo.setSale((short) menu.getSale());
			menuInfo.setCode(menu.getCode());
			menuInfo.setShipping_fee(menu.getShipping_fee());
			updateList.add(menuInfo);
		}
		//
		boolean insert = false;
		if (checkUpdateFlag(flag, FLAG_UPDATE_SHOP_DETAIL)) {
			insert = true;
		} else if (checkUpdateFlag(flag, FLAG_CREATE_NEW_SHOP)) {
			DishInfo ddishInfo = new DishInfo();
			ddishInfo.setShop_id(shopInfo.getId());
			long c_dish = CommonDaoService.count(ddishInfo);
			ShopOptionInfo dshopOptionInfo = new ShopOptionInfo();
			dshopOptionInfo.setShop_id(shopInfo.getId());
			long c_opt = CommonDaoService.count(dshopOptionInfo);
			if (c_dish > 0 && c_opt > 0) {
			} else if (c_dish > 0 || c_opt > 0) { // data is wrong.
				List<DaoValue> deleteAway = new ArrayList<>();
				if (c_dish > 0) {
					ddishInfo.setDelete();
					deleteAway.add(ddishInfo);
				} else {
					dshopOptionInfo.setDelete();
					deleteAway.add(dshopOptionInfo);
				}
				if (!CommonDaoService.update(deleteAway)) {
					logError("[FridaySocketSession] (update) update/delete failed");
					return;
				}
				insert = true;
			}
		}
		if (insert) {
			Map<Integer, MenuItemOption> mapOptions = new HashMap<>();
			for (MenuItem menuItem : listItems) {
				DishInfo dishInfo = new DishInfo();
				dishInfo.setDetail(menuItem.getDetail());
				dishInfo.setShop_id(shopInfo.getId());
				
				updateList.add(dishInfo);
				//
				for (MenuItemOption option : menuItem.getListOptions()) {
					if (!mapOptions.containsKey(option.hashCode())) {
						mapOptions.put(option.hashCode(), option);
					}
				}
			}
			for (Integer key : mapOptions.keySet()) {
				MenuItemOption option = mapOptions.get(key);
				ShopOptionInfo shopOptionInfo = new ShopOptionInfo();
				shopOptionInfo.setShop_id(shopInfo.getId());
				shopOptionInfo.setType(option.getType());
				shopOptionInfo.setName(option.getName());
				shopOptionInfo.setPrice(option.getPrice());
				
				updateList.add(shopOptionInfo);
			}
			if (!updateList.isEmpty() && !CommonDaoService.update(updateList)) {
				logError("[FridaySocketSession] (update) update failed!");
			}
		}
	}

	private String getShopName(String menuName) {
		try {
			return menuName.split("-")[0].trim();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return menuName;
	}

	private boolean checkUpdateFlag(int flag, int type) {
		return (flag & type) != 0;
	}
}
