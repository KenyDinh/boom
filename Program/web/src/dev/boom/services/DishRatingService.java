package dev.boom.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblDishRatingInfo;

public class DishRatingService {

	private DishRatingService() {
	}

	public static List<DishRating> getDishRatingList(String option) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.SetSelectOption("WHERE id > 0");
		if (option != null && option.length() > 0) {
			info.SetSelectOption(option);
		}
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() == 0) {
			return null;
		}
		List<DishRating> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new DishRating((TblDishRatingInfo) dao));
		}
		
		return ret;
	}
	
	public static DishRating getDishRatingInfoById(long id) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.Set("id", id);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return new DishRating((TblDishRatingInfo) list.get(0));
	}
	
	public static List<DishRating> getDishRatingList(long shop_id) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.Set("shop_id", shop_id);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		List<DishRating> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new DishRating((TblDishRatingInfo) dao));
		}
		
		return ret;
	}
	
	public static DishRating getDishRatingInfo(String name, long shop_id) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.Set("name", name);
		info.Set("shop_id", shop_id);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return new DishRating((TblDishRatingInfo) list.get(0));
	}
	
	public static DishRating getDishRatingInfo(int code, long shop_id) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.Set("code", code);
		info.Set("shop_id", shop_id);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			GameLog.getInstance().warn("[getDishRatingInfo] There are more than one dish have the same dish code!");
		}
		
		return new DishRating((TblDishRatingInfo) list.get(0));
	}
	
	public static Map<Integer, DishRating> getDishRatingInfoMap(long shop_id) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.Set("shop_id", shop_id);
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		Map<Integer, DishRating> ret = new HashMap<>();
		for (DaoValue dao : list) {
			DishRating dishRatingInfo = new DishRating((TblDishRatingInfo) dao);
			ret.put(dishRatingInfo.getCode(), dishRatingInfo);
		}
		
		return ret;
	}
	
}

