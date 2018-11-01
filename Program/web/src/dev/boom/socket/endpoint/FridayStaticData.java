package dev.boom.socket.endpoint;

import java.util.ArrayList;
import java.util.List;

import dev.boom.core.GameLog;
import dev.boom.milktea.object.Menu;
import dev.boom.milktea.object.MenuItem;
import dev.boom.milktea.object.MenuOrder;

public class FridayStaticData {
	
	private static boolean isPlacingOrder = false;
	private static Object __lockOrder = new Object();
	
	// ---------- Data ---------- //
	private static Menu menu = null;
	private static List<MenuItem> listItems = null;
	private static MenuOrder menuOrder = null;
	private static List<Long> ids = null;
	
	
	public static void setMenu(Menu menu) {
		FridayStaticData.menu = menu;
		listItems = null;
	}
	
	public static void addMenuItem(MenuItem menuItem) {
		if (menu == null) {
			GameLog.getInstance().error("[FridayStaticData] Menu is null, can not add any MenuItem");
			return;
		}
		if (listItems == null) {
			listItems = new ArrayList<>();
		}
		listItems.add(menuItem);
	}
	
	public static Menu getMenu() {
		return menu;
	}
	
	public static List<MenuItem> getMenuItemList() {
		return listItems;
	}
	
	public static void setMenuOrder(MenuOrder _menuOrder) {
		if (!isPlacingOrder && menuOrder == null) {
			synchronized (__lockOrder) {
				if (!isPlacingOrder && menuOrder == null) {
					isPlacingOrder = true;
					menuOrder = _menuOrder;
				}
			}
		}
	}
	
	public static List<Long> getIds() {
		return ids;
	}

	public static void setIds(List<Long> _ids) {
		ids = _ids;
	}

	public static boolean isInPlacingState() {
		return isPlacingOrder;
	}
	
	public static MenuOrder getMenuOrder() {
		return menuOrder;
	}
	
	public static void forceResetmoveState() {
		isPlacingOrder = false;
		menuOrder = null;
	}
	
	public static void resetOrderState() {
		if (isPlacingOrder && menuOrder != null) {
			synchronized (__lockOrder) {
				if (isPlacingOrder && menuOrder != null) {
					isPlacingOrder = false;
					menuOrder = null;
					ids = null;
				}
			}
		}
	}
}
