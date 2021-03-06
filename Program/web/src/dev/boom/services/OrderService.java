package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblOrderInfo;

public class OrderService {
	
	public static OrderInfo getOrderInfoById(long id) {
		TblOrderInfo orderInfo = new TblOrderInfo();
		orderInfo.setId(id);
		List<DaoValue> list = CommonDaoService.select(orderInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return new OrderInfo((TblOrderInfo) list.get(0));
	}
	
	public static List<OrderInfo> getOrderList(List<Long> ids) {
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
		TblOrderInfo orderInfo = new TblOrderInfo();
		orderInfo.setSelectOption("WHERE id IN (" + soption + ")");
		List<DaoValue> list = CommonDaoService.select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<OrderInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new OrderInfo((TblOrderInfo) dao));
		}
		return ret;
	}

	public static List<OrderInfo> getOrderInfoListByMenuId(long menu_id) {
		return getOrderInfoListByMenuId(menu_id, null);
	}
	
	public static List<OrderInfo> getOrderInfoListByMenuId(long menu_id, String option) {
		TblOrderInfo orderInfo = new TblOrderInfo();
		orderInfo.setMenu_id(menu_id);
		if (option != null) {
			orderInfo.setSelectOption(option);
		} else {
			orderInfo.setSelectOption("ORDER BY created DESC");
		}
		List<DaoValue> list = CommonDaoService.select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<OrderInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new OrderInfo((TblOrderInfo) dao));
		}

		return ret;
	}

	public static List<TblOrderInfo> getOrderInfoListByShopId(long shop_id) {
		TblOrderInfo orderInfo = new TblOrderInfo();
		orderInfo.setShop_id(shop_id);
		List<DaoValue> list = CommonDaoService.select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<TblOrderInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblOrderInfo) dao);
		}

		return ret;
	}
	
	public static List<OrderInfo> getCompletedOrderListByUserId(long user_id) {
		TblOrderInfo orderInfo = new TblOrderInfo();
		orderInfo.setUser_id(user_id);
		orderInfo.setSelectOption("AND final_price > 0");
		List<DaoValue> list = CommonDaoService.select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<OrderInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new OrderInfo((TblOrderInfo) dao));
		}
		return ret;
	}
	
}
