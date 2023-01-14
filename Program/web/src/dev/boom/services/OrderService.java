package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblOrderInfo;

public class OrderService {

	private OrderService() {
	}

	public static List<Order> getOrderList(String option) {
		TblOrderInfo orderInfo = new TblOrderInfo();
		orderInfo.SetSelectOption("WHERE id > 0");
		if (option != null && option.length() > 0) {
			orderInfo.SetSelectOption(option);
		}
		List<DaoValue> list = CommonDaoFactory.Select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Order> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Order((TblOrderInfo) dao));
		}
		return ret;
	}
	
	public static Order getOrderInfoById(long id) {
		TblOrderInfo orderInfo = new TblOrderInfo();
		orderInfo.Set("id", id);
		List<DaoValue> list = CommonDaoFactory.Select(orderInfo);
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return new Order((TblOrderInfo) list.get(0));
	}
	
	public static List<Order> getOrderList(List<Long> ids) {
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
		orderInfo.SetSelectOption("WHERE id IN (" + soption + ")");
		List<DaoValue> list = CommonDaoFactory.Select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Order> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Order((TblOrderInfo) dao));
		}
		return ret;
	}

	public static List<Order> getOrderInfoListByMenuId(long menu_id) {
		return getOrderInfoListByMenuId(menu_id, null);
	}
	
	public static List<Order> getOrderInfoListByMenuId(long menu_id, String option) {
		TblOrderInfo orderInfo = new TblOrderInfo();
		orderInfo.Set("menu_id", menu_id);
		if (option != null) {
			orderInfo.SetSelectOption(option);
		} else {
			orderInfo.SetSelectOption("ORDER BY created DESC");
		}
		List<DaoValue> list = CommonDaoFactory.Select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Order> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Order((TblOrderInfo) dao));
		}

		return ret;
	}

	public static List<TblOrderInfo> getOrderInfoListByShopId(long shop_id) {
		TblOrderInfo orderInfo = new TblOrderInfo();
		orderInfo.Set("shop_id", shop_id);
		List<DaoValue> list = CommonDaoFactory.Select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<TblOrderInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add((TblOrderInfo) dao);
		}

		return ret;
	}
	
	public static List<Order> getCompletedOrderListByUserId(long user_id) {
		TblOrderInfo orderInfo = new TblOrderInfo();
		orderInfo.Set("user_id", user_id);
		orderInfo.SetSelectOption("AND final_price > 0");
		List<DaoValue> list = CommonDaoFactory.Select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Order> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Order((TblOrderInfo) dao));
		}
		return ret;
	}
	
	public static List<Order> getOrderCommentList(long shopId, int dishCode) {
		TblOrderInfo orderInfo = new TblOrderInfo();
		orderInfo.Set("shop_id", shopId);
		orderInfo.Set("dish_code", dishCode);
		orderInfo.SetSelectOption("AND voting_star > 0 ORDER BY id DESC");
		List<DaoValue> list = CommonDaoFactory.Select(orderInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Order> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Order((TblOrderInfo) dao));
		}
		return ret;
	}
	
}

