package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.entity.info.OrderInfo;

public class OrderService {
	
	public static OrderInfo getOrderInfoById(long id) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setId(id);
		List<DaoValue> list = CommonDaoService.select(orderInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return (OrderInfo) list.get(0);
	}

	public static List<OrderInfo> getOrderInfoListByMenuId(long menu_id) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setMenu_id(menu_id);
		orderInfo.setSelectOption("ORDER BY created DESC");
		List<DaoValue> list = CommonDaoService.select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<OrderInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((OrderInfo) dao);
		}

		return ret;
	}

	public static List<OrderInfo> getOrderInfoListByShopId(long shop_id) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setShop_id(shop_id);
		List<DaoValue> list = CommonDaoService.select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<OrderInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((OrderInfo) dao);
		}

		return ret;
	}
	
}
