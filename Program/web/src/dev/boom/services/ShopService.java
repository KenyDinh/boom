package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblShopInfo;

public class ShopService {

	private ShopService() {
	}

	public static List<Shop> getShopList(String option, int limit, int offset) {
		TblShopInfo shopInfo = new TblShopInfo();
		if (option != null && option.length() > 0) {
			shopInfo.SetSelectOption(option);
		}
		if (limit > 0) {
			shopInfo.SetLimit(limit);
			shopInfo.SetOffset(offset);
		}
		List<DaoValue> list = CommonDaoFactory.Select(shopInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Shop> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Shop((TblShopInfo) dao));
		}
		return ret;
	}
	
	public static Shop getShopById(long id) {
		TblShopInfo shopInfo = new TblShopInfo();
		shopInfo.Set("id", id);
		List<DaoValue> list = CommonDaoFactory.Select(shopInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new Shop((TblShopInfo) list.get(0));
	}
	
	public static Shop getShopByUrl(String url) {
		TblShopInfo shopInfo = new TblShopInfo();
		shopInfo.Set("url", url);
		List<DaoValue> list = CommonDaoFactory.Select(shopInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new Shop((TblShopInfo) list.get(0));
	}
	
	public static List<Shop> getShopListByUrl(String name) {
		TblShopInfo shopInfo = new TblShopInfo();
		shopInfo.Set("name", name);
		List<DaoValue> list = CommonDaoFactory.Select(shopInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Shop> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Shop((TblShopInfo) dao));
		}
		return ret;
	}
	
	public static List<Shop> getShopListById(List<Long> ids) {
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
		shopInfo.SetSelectOption("WHERE id IN (" + soption + ")");
		List<DaoValue> list = CommonDaoFactory.Select(shopInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Shop> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Shop((TblShopInfo) dao));
		}
		return ret;
	}
	
}

