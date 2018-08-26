package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.milktea.object.MenuItem;
import dev.boom.tbl.info.TblDishInfo;
import dev.boom.tbl.info.TblMenuInfo;
import net.arnx.jsonic.JSON;

public class MenuService {

	public static MenuInfo getMenuById(long id) {
		TblMenuInfo menuInfo = new TblMenuInfo();
		menuInfo.setId(id);
		List<DaoValue> list = CommonDaoService.select(menuInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new MenuInfo((TblMenuInfo) list.get(0));
	}

	public static List<MenuInfo> getMenuListByShopId(long shop_id) {
		TblMenuInfo menuInfo = new TblMenuInfo();
		menuInfo.setShop_id(shop_id);
		menuInfo.setSelectOption("ORDER BY created DESC");
		List<DaoValue> list = CommonDaoService.select(menuInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<MenuInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new MenuInfo((TblMenuInfo) dao));
		}
		return ret;
	}

	public static List<MenuInfo> getMenuListByStatus(byte status) {
		TblMenuInfo menuInfo = new TblMenuInfo();
		menuInfo.setStatus(status);
		List<DaoValue> list = CommonDaoService.select(menuInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<MenuInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new MenuInfo((TblMenuInfo) dao));
		}
		return ret;
	}
	
	public static List<MenuInfo> getMenuListByStatusList(byte[] status) {
		TblMenuInfo menuInfo = new TblMenuInfo();
		String option = "";
		for (byte stt : status) {
			if (!option.isEmpty()) {
				option += ",";
			}
			option += stt;
		}
		if (!option.isEmpty()) {
			menuInfo.setSelectOption("WHERE status IN (" + option + ") ORDER BY status ASC");
		}
		List<DaoValue> list = CommonDaoService.select(menuInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<MenuInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new MenuInfo((TblMenuInfo) dao));
		}
		return ret;
	}

	public static List<MenuItem> getMenuItemListByShopId(long shop_id) {
		TblDishInfo dishInfo = new TblDishInfo();
		dishInfo.setShop_id(shop_id);
		List<DaoValue> list = CommonDaoService.select(dishInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<MenuItem> ret = new ArrayList<>();

		for (DaoValue dao : list) {
			try {
				TblDishInfo di = (TblDishInfo) dao;
				MenuItem menuItem = (MenuItem) JSON.decode(di.getDetail(), MenuItem.class);
				menuItem.setId(di.getId());
				menuItem.setShop_id(di.getShop_id());
				ret.add(menuItem);
			} catch (Exception e) {
				GameLog.getInstance().error("(getMenuItemListByShopId) parse menu item error!");
			}
		}

		if (ret.isEmpty()) {
			return null;
		}
		return ret;
	}

	public static MenuItem getMenuItemById(long menuItemId) {
		TblDishInfo dishInfo = new TblDishInfo();
		dishInfo.setId(menuItemId);
		List<DaoValue> list = CommonDaoService.select(dishInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		try {
			TblDishInfo di = (TblDishInfo) list.get(0);
			MenuItem menuItem = (MenuItem) JSON.decode(di.getDetail(), MenuItem.class);
			menuItem.setShop_id(di.getShop_id());
			menuItem.setId(di.getId());
			return menuItem;
		} catch (Exception e) {
			GameLog.getInstance().error("(getMenuItemById) parse menu item error!");
		}
		return null;
	}
}
