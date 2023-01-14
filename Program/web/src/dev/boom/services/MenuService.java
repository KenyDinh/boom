package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import dev.boom.common.milktea.MilkTeaMenuStatus;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.milktea.object.MenuItem;
import dev.boom.tbl.info.TblDishInfo;
import dev.boom.tbl.info.TblMenuInfo;
import net.arnx.jsonic.JSON;

public class MenuService {

	private MenuService() {
	}

	public static Menu getMenuById(long id) {
		TblMenuInfo menuInfo = new TblMenuInfo();
		menuInfo.Set("id", id);
		List<DaoValue> list = CommonDaoFactory.Select(menuInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new Menu((TblMenuInfo) list.get(0));
	}

	public static List<Menu> getMenuListByShopId(long shop_id) {
		TblMenuInfo menuInfo = new TblMenuInfo();
		menuInfo.Set("shop_id", shop_id);
		menuInfo.SetSelectOption("AND status IN (" + MilkTeaMenuStatus.OPENING.getStatus() + 
				"," + MilkTeaMenuStatus.DELIVERING.getStatus() + "," + MilkTeaMenuStatus.COMPLETED.getStatus() + ") ORDER BY created DESC");
		List<DaoValue> list = CommonDaoFactory.Select(menuInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Menu> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Menu((TblMenuInfo) dao));
		}
		return ret;
	}

	public static List<Menu> getMenuListByStatus(byte status) {
		TblMenuInfo menuInfo = new TblMenuInfo();
		menuInfo.Set("status", status);
		List<DaoValue> list = CommonDaoFactory.Select(menuInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Menu> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Menu((TblMenuInfo) dao));
		}
		return ret;
	}
	
	public static List<Menu> getMenuListByStatusList(byte[] status) {
		return getMenuListByStatusList(status, null, -1, -1);
	}
	
	public static long getCountMenu(byte[] status, String options) {
		TblMenuInfo menuInfo = new TblMenuInfo();
		String option = "";
		for (byte stt : status) {
			if (!option.isEmpty()) {
				option += ",";
			}
			option += stt;
		}
		if (!option.isEmpty()) {
			option = "status IN (" + option + ")";
		}
		if (options != null && options.length() > 0) {
			if (!option.isEmpty()) {
				option += " AND ";
			}
			option += options;
		}
		if (!option.isEmpty()) {
			menuInfo.SetSelectOption("WHERE " + option);
		}
		return CommonDaoFactory.Count(menuInfo);
	}
	
	public static List<Menu> getMenuListByStatusList(byte[] status, String options, int limit, int offset) {
		TblMenuInfo menuInfo = new TblMenuInfo();
		String option = "";
		for (byte stt : status) {
			if (!option.isEmpty()) {
				option += ",";
			}
			option += stt;
		}
		if (!option.isEmpty()) {
			option = "status IN (" + option + ")";
		}
		if (options != null && options.length() > 0) {
			if (!option.isEmpty()) {
				option += " AND ";
			}
			option += options;
		}
		if (!option.isEmpty()) {
			menuInfo.SetSelectOption("WHERE " + option + " ORDER BY status ASC, updated DESC");
		}
		if (limit > 0) {
			menuInfo.SetLimit(limit);
			if (offset >= 0) {
				menuInfo.SetOffset(offset);
			}
		}
		List<DaoValue> list = CommonDaoFactory.Select(menuInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Menu> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Menu((TblMenuInfo) dao));
		}
		return ret;
	}

	public static List<MenuItem> getMenuItemListByShopId(long shop_id) {
		TblDishInfo dishInfo = new TblDishInfo();
		dishInfo.Set("shop_id", shop_id);
		List<DaoValue> list = CommonDaoFactory.Select(dishInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<MenuItem> ret = new ArrayList<>();

		for (DaoValue dao : list) {
			try {
				TblDishInfo di = (TblDishInfo) dao;
				MenuItem menuItem = (MenuItem) JSON.decode(StringEscapeUtils.unescapeHtml((String)di.Get("detail")), MenuItem.class);
				menuItem.setId((Long)di.Get("id"));
				menuItem.setShop_id((Long)di.Get("shop_id"));
				ret.add(menuItem);
			} catch (Exception e) {
				e.printStackTrace();
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
		dishInfo.Set("id", menuItemId);
		List<DaoValue> list = CommonDaoFactory.Select(dishInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		try {
			TblDishInfo di = (TblDishInfo) list.get(0);
			MenuItem menuItem = (MenuItem) JSON.decode(StringEscapeUtils.unescapeHtml((String)di.Get("detail")), MenuItem.class);
			menuItem.setShop_id((Long)di.Get("shop_id"));
			menuItem.setId((Long)di.Get("id"));
			return menuItem;
		} catch (Exception e) {
			GameLog.getInstance().error("(getMenuItemById) parse menu item error!");
		}
		return null;
	}
	
}

