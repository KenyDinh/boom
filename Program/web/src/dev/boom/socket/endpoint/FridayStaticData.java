package dev.boom.socket.endpoint;

import java.util.ArrayList;
import java.util.List;

import dev.boom.core.GameLog;
import dev.boom.milktea.object.Menu;
import dev.boom.milktea.object.MenuItem;

public class FridayStaticData {
	
	// ---------- Data ---------- //
	private static Menu menu = null;
	private static List<MenuItem> listItems = null;
	
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
}
