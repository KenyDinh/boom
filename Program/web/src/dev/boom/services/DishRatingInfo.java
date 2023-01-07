package dev.boom.services;

import java.text.DecimalFormat;
import java.util.Date;

import dev.boom.tbl.info.TblDishRatingInfo;

public class DishRatingInfo {
	
	private TblDishRatingInfo info;

	public DishRatingInfo(TblDishRatingInfo info) {
		this.info = info;
	}

	public DishRatingInfo() {
		this.info = new TblDishRatingInfo();
	}
	
	public TblDishRatingInfo getTblInfo() {
		return this.info;
	}
	
	public long getId() {
		return this.info.getId();
	}

	public void setId(long id) {
		this.info.setId(id);;
	}

	public long getShopId() {
		return this.info.getShop_id();
	}

	public void setShopId(long shop_id) {
		this.info.setShop_id(shop_id);
	}

	public String getName() {
		return this.info.getName();
	}

	public void setName(String name) {
		this.info.setName(name);;
	}

	public int getCode() {
		return this.info.getCode();
	}

	public void setCode(int code) {
		this.info.setCode(code);
	}
	
	public long getOrderCount() {
		return this.info.getOrder_count();
	}

	public void setOrderCount(long order_count) {
		this.info.setOrder_count(order_count);
	}

	public long getStarCount() {
		return this.info.getStar_count();
	}

	public void setStarCount(long star_count) {
		this.info.setStar_count(star_count);;
	}
	
	public double getRating() {
		if (getOrderCount() <= 0 || getStarCount() <= 0) {
			return 0;
		}
		double rate = (double) getStarCount() / getOrderCount();
		return rate;
	}
	
	public String getFormatRating() {
		double rate = getRating();
		if (rate == 0) {
			return null;
		}
		DecimalFormat df= new DecimalFormat("#.#");
		return df.format(rate);
	}

	public Date getUpdated() {
		return this.info.getUpdated();
	}

	public void setUpdated(Date updated) {
		this.info.setUpdated(updated);
	}

}
