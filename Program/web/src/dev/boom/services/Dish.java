package dev.boom.services;

import dev.boom.tbl.info.TblDishInfo;

public class Dish {
	private TblDishInfo dishInfo;

	public Dish() {
		dishInfo = new TblDishInfo();
	}

	public Dish(TblDishInfo dishInfo) {
		this.dishInfo = dishInfo;
	}

	public TblDishInfo getDishInfo() {
		return dishInfo;
	}

	public long getId() {
		return (Long) dishInfo.Get("id");
	}

	public void setId(long id) {
		dishInfo.Set("id", id);
	}

	public long getShopId() {
		return (Long) dishInfo.Get("shop_id");
	}

	public void setShopId(long shopId) {
		dishInfo.Set("shop_id", shopId);
	}

	public String getDetail() {
		return (String) dishInfo.Get("detail");
	}

	public void setDetail(String detail) {
		dishInfo.Set("detail", detail);
	}

}

