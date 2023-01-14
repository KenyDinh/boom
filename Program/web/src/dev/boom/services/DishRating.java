package dev.boom.services;

import java.text.DecimalFormat;
import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblDishRatingInfo;

public class DishRating {
	private TblDishRatingInfo dishRatingInfo;

	public DishRating() {
		dishRatingInfo = new TblDishRatingInfo();
	}

	public DishRating(TblDishRatingInfo dishRatingInfo) {
		this.dishRatingInfo = dishRatingInfo;
	}

	public TblDishRatingInfo getDishRatingInfo() {
		return dishRatingInfo;
	}

	public long getId() {
		return (Long) dishRatingInfo.Get("id");
	}

	public void setId(long id) {
		dishRatingInfo.Set("id", id);
	}

	public long getShopId() {
		return (Long) dishRatingInfo.Get("shop_id");
	}

	public void setShopId(long shopId) {
		dishRatingInfo.Set("shop_id", shopId);
	}

	public String getName() {
		return (String) dishRatingInfo.Get("name");
	}

	public void setName(String name) {
		dishRatingInfo.Set("name", name);
	}

	public int getCode() {
		return (Integer) dishRatingInfo.Get("code");
	}

	public void setCode(int code) {
		dishRatingInfo.Set("code", code);
	}

	public long getOrderCount() {
		return (Long) dishRatingInfo.Get("order_count");
	}

	public void setOrderCount(long orderCount) {
		dishRatingInfo.Set("order_count", orderCount);
	}

	public long getStarCount() {
		return (Long) dishRatingInfo.Get("star_count");
	}

	public void setStarCount(long starCount) {
		dishRatingInfo.Set("star_count", starCount);
	}

	public String getUpdated() {
		return (String) dishRatingInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		dishRatingInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
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

}

