package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.ShopInfo;
import dev.boom.entity.info.ShopOptionInfo;

public class ShopService {
	
	// ---------- MenuItemOption Type ---------- //
	public static final short ITEM_OPTION_TYPE_NONE 	= 0;
	public static final short ITEM_OPTION_TYPE_ICE 		= 1;
	public static final short ITEM_OPTION_TYPE_SIZE 	= 2;
	public static final short ITEM_OPTION_TYPE_SUGAR 	= 3;
	public static final short ITEM_OPTION_TYPE_TOPPING 	= 4;
	public static final short ITEM_OPTION_TYPE_ADDITION = 5;
	
	public static List<ShopInfo> getShopList() {
		ShopInfo shopInfo = new ShopInfo();
		List<DaoValue> list = CommonDaoService.select(shopInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<ShopInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((ShopInfo) dao);
		}
		return ret;
	}
	
	public static ShopInfo getShopById(long id) {
		ShopInfo shopInfo = new ShopInfo();
		shopInfo.setId(id);
		List<DaoValue> list = CommonDaoService.select(shopInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		return (ShopInfo) list.get(0);
	}
	
	public static ShopInfo getShopByUrl(String url) {
		ShopInfo shopInfo = new ShopInfo();
		shopInfo.setUrl(url);
		List<DaoValue> list = CommonDaoService.select(shopInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		return (ShopInfo) list.get(0);
	}
	
	public static List<ShopInfo> getShopListByUrl(String name) {
		ShopInfo shopInfo = new ShopInfo();
		shopInfo.setName(name);
		List<DaoValue> list = CommonDaoService.select(shopInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<ShopInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((ShopInfo) dao);
		}
		return ret;
	}
	
	public static List<ShopOptionInfo> getShopOptionListByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		String soption = "";
		for (Long id : ids) {
			if (soption.length() > 0) {
				soption += ",";
			}
			soption += id;
		}
		ShopOptionInfo info = new ShopOptionInfo();
		info.setSelectOption("WHERE id IN (" + soption + ")");
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<ShopOptionInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((ShopOptionInfo) dao);
		}
		
		return ret;
	}
	
	public static List<ShopOptionInfo> getShopOptionListByShopId(long shop_id) {
		ShopOptionInfo info = new ShopOptionInfo();
		info.setShop_id(shop_id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<ShopOptionInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((ShopOptionInfo) dao);
		}
		
		return ret;
	}
}
