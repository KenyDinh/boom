package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblDishInfo;

public class DishService {

	private DishService() {
	}

	public static List<Dish> getDishListAll(String option) {
		TblDishInfo tblInfo = new TblDishInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<Dish> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Dish((TblDishInfo) dao));
		}

		return ret;
	}

	public static List<Dish> getDishListAll() {
		return getDishListAll(null);
	}
}

