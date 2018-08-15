package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblShopInfo;

public class ShopService {
	
	public static List<ShopInfo> getShopList() {
		TblShopInfo shopInfo = new TblShopInfo();
		List<DaoValue> list = CommonDaoService.select(shopInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<ShopInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new ShopInfo((TblShopInfo) dao));
		}
		return ret;
	}
	
	public static ShopInfo getShopById(long id) {
		TblShopInfo shopInfo = new TblShopInfo();
		shopInfo.setId(id);
		List<DaoValue> list = CommonDaoService.select(shopInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new ShopInfo((TblShopInfo) list.get(0));
	}
	
	public static ShopInfo getShopByUrl(String url) {
		TblShopInfo shopInfo = new TblShopInfo();
		shopInfo.setUrl(url);
		List<DaoValue> list = CommonDaoService.select(shopInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new ShopInfo((TblShopInfo) list.get(0));
	}
	
	public static List<ShopInfo> getShopListByUrl(String name) {
		TblShopInfo shopInfo = new TblShopInfo();
		shopInfo.setName(name);
		List<DaoValue> list = CommonDaoService.select(shopInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<ShopInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new ShopInfo((TblShopInfo) dao));
		}
		return ret;
	}
	
	public static List<ShopInfo> getShopListById(List<Long> ids) {
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
		TblShopInfo shopInfo = new TblShopInfo();
		shopInfo.setSelectOption("WHERE id IN (" + soption + ")");
		List<DaoValue> list = CommonDaoService.select(shopInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<ShopInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new ShopInfo((TblShopInfo) dao));
		}
		return ret;
	}
	
}
