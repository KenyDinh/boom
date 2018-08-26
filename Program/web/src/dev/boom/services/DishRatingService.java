package dev.boom.services;

import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblDishRatingInfo;

public class DishRatingService {

	
	public static DishRatingInfo getDishRatingInfoById(long id) {
		TblDishRatingInfo info = new TblDishRatingInfo();
		info.setId(id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return new DishRatingInfo((TblDishRatingInfo) list.get(0));
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
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return new DishRatingInfo((TblDishRatingInfo) list.get(0));
	}
}
