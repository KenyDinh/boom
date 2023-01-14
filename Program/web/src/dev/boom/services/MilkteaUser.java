package dev.boom.services;

import java.text.DecimalFormat;

import dev.boom.tbl.info.TblMilkteaUserInfo;

public class MilkteaUser {
	private TblMilkteaUserInfo milkteaUserInfo;

	public MilkteaUser() {
		milkteaUserInfo = new TblMilkteaUserInfo();
	}

	public MilkteaUser(TblMilkteaUserInfo milkteaUserInfo) {
		this.milkteaUserInfo = milkteaUserInfo;
	}

	public TblMilkteaUserInfo getMilkteaUserInfo() {
		return milkteaUserInfo;
	}

	public long getUserId() {
		return (Long) milkteaUserInfo.Get("user_id");
	}

	public void setUserId(long userId) {
		milkteaUserInfo.Set("user_id", userId);
	}

	public String getUsername() {
		return (String) milkteaUserInfo.Get("username");
	}

	public void setUsername(String username) {
		milkteaUserInfo.Set("username", username);
	}

	public long getDishCount() {
		return (Long) milkteaUserInfo.Get("dish_count");
	}

	public void setDishCount(long dishCount) {
		milkteaUserInfo.Set("dish_count", dishCount);
	}

	public long getOrderCount() {
		return (Long) milkteaUserInfo.Get("order_count");
	}

	public void setOrderCount(long orderCount) {
		milkteaUserInfo.Set("order_count", orderCount);
	}

	public long getTotalMoney() {
		return (Long) milkteaUserInfo.Get("total_money");
	}

	public void setTotalMoney(long totalMoney) {
		milkteaUserInfo.Set("total_money", totalMoney);
	}

	public long getTotalSugar() {
		return (Long) milkteaUserInfo.Get("total_sugar");
	}

	public void setTotalSugar(long totalSugar) {
		milkteaUserInfo.Set("total_sugar", totalSugar);
	}

	public long getTotalIce() {
		return (Long) milkteaUserInfo.Get("total_ice");
	}

	public void setTotalIce(long totalIce) {
		milkteaUserInfo.Set("total_ice", totalIce);
	}

	public long getTotalTopping() {
		return (Long) milkteaUserInfo.Get("total_topping");
	}

	public void setTotalTopping(long totalTopping) {
		milkteaUserInfo.Set("total_topping", totalTopping);
	}

	public byte getFreeTicket() {
		return (Byte) milkteaUserInfo.Get("free_ticket");
	}

	public void setFreeTicket(byte freeTicket) {
		if (freeTicket < 0) {
			freeTicket = 0;
		}
		if (freeTicket > Byte.MAX_VALUE) {
			freeTicket = Byte.MAX_VALUE;
		}
		milkteaUserInfo.Set("free_ticket", freeTicket);
	}

	public long getLatestOrderId() {
		return (Long) milkteaUserInfo.Get("latest_order_id");
	}

	public void setLatestOrderId(long latestOrderId) {
		milkteaUserInfo.Set("latest_order_id", latestOrderId);
	}
	
	public String getFormatAverageSugar() {
		if (getOrderCount() <= 0) {
			return "0.00";
		}
		DecimalFormat df = new DecimalFormat("#.##");
		double avg = (double)getTotalSugar() / getOrderCount();
		return df.format(avg);
	}
	
	public String getFormatAverageIce() {
		if (getOrderCount() <= 0) {
			return "0.00";
		}
		DecimalFormat df = new DecimalFormat("#.##");
		double avg = (double)getTotalIce() / getOrderCount();
		return df.format(avg);
	}

}

