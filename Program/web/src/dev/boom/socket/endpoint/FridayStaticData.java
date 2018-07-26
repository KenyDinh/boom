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
	private static List<MenuOrder> listOrder = null;
	
	
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
	
	public static void setMenuOrderList(List<MenuOrder> orderList) {
		if (!isPlacingOrder && listOrder == null) {
			synchronized (__lockOrder) {
				if (!isPlacingOrder && listOrder == null) {
					isPlacingOrder = true;
					listOrder = orderList;
				}
			}
		}
	}
	
	public static boolean isInPlacingState() {
		return isPlacingOrder;
	}
	
	public static List<MenuOrder> getMenuOrderList() {
		return listOrder;
	}
	
	public static void forceResetmoveState() {
		isPlacingOrder = false;
		listOrder = null;
	}
	
	public static void resetPlacingOrderState() {
		if (isPlacingOrder && listOrder != null) {
			synchronized (__lockOrder) {
				if (isPlacingOrder && listOrder != null) {
					isPlacingOrder = false;
					listOrder = null;
				}
			}
		}
	}
}
