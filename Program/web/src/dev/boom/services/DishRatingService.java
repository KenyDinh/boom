package dev.boom.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblDishRatingInfo;

public class DishRatingService {

	public static List<DishRatingInfo> getDishRatingList(String option) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.setSelectOption("WHERE id > 0");
		if (option != null && option.length() > 0) {
			info.setSelectOption(option);
		}
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() == 0) {
			return null;
		}
		List<DishRatingInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new DishRatingInfo((TblDishRatingInfo) dao));
		}
		
		return ret;
	}
	
	public static DishRatingInfo getDishRatingInfoById(long id) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.setId(id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return new DishRatingInfo((TblDishRatingInfo) list.get(0));
	}
	
	public static List<DishRatingInfo> getDishRatingList(long shop_id) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.setShop_id(shop_id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		List<DishRatingInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new DishRatingInfo((TblDishRatingInfo) dao));
		}
		
		return ret;
	}
	
	public static DishRatingInfo getDishRatingInfo(String name, long shop_id) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.setName(name);
		info.setShop_id(shop_id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return new DishRatingInfo((TblDishRatingInfo) list.get(0));
	}
	
	public static DishRatingInfo getDishRatingInfo(int code, long shop_id) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.setCode(code);
		info.setShop_id(shop_id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			GameLog.getInstance().warn("[getDishRatingInfo] There are more than one dish have the same dish code!");
		}
		
		return new DishRatingInfo((TblDishRatingInfo) list.get(0));
	}
	
	public static Map<Integer, DishRatingInfo> getDishRatingInfoMap(long shop_id) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.setShop_id(shop_id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		Map<Integer, DishRatingInfo> ret = new HashMap<>();
		for (DaoValue dao : list) {
			DishRatingInfo dishRatingInfo = new DishRatingInfo((TblDishRatingInfo) dao);
			ret.put(dishRatingInfo.getCode(), dishRatingInfo);
		}
		
		return ret;
	}
}
