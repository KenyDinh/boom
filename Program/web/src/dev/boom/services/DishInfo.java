package dev.boom.services;

import dev.boom.tbl.info.TblDishInfo;

public class DishInfo {

	private TblDishInfo info = null;

	public DishInfo(TblDishInfo info) {
		this.info = info;
	}

	public DishInfo() {
		this.info = new TblDishInfo();
	}
	
	public TblDishInfo getTblInfo() {
		return this.info;
	}
	
	public long getId() {
		return this.info.getId();
	}

	public void setId(long id) {
		this.info.setId(id);
	}

	public long getShopId() {
		return this.info.getShop_id();
	}

	public void setShopId(long shop_id) {
		this.info.setShop_id(shop_id);
	}

	public String getDetail() {
		return this.info.getDetail();
	}

	public void setDetail(String detail) {
		this.info.setDetail(detail);
	}
	
}
