package dev.boom.services;

import java.text.DecimalFormat;

import dev.boom.tbl.info.TblMilkTeaUserInfo;

public class MilkTeaUserInfo {

	private TblMilkTeaUserInfo info = null;

	public MilkTeaUserInfo(TblMilkTeaUserInfo info) {
		this.info = info;
	}

	public MilkTeaUserInfo() {
		this.info = new TblMilkTeaUserInfo();
	}
	
	public TblMilkTeaUserInfo getTblInfo() {
		return this.info;
	}
	
	public long getUserId() {
		return this.info.getUser_id();
	}

	public void setUserId(long user_id) {
		this.info.setUser_id(user_id);
	}

	public String getUsername() {
		return this.info.getUsername();
	}

	public void setUsername(String username) {
		this.info.setUsername(username);
	}

	public long getDishCount() {
		return this.info.getDish_count();
	}

	public void setDishCount(long dish_count) {
		this.info.setDish_count(dish_count);
	}

	public long getOrderCount() {
		return this.info.getOrder_count();
	}

	public void setOrderCount(long order_count) {
		this.info.setOrder_count(order_count);
	}

	public long getTotalMoney() {
		return this.info.getTotal_money();
	}

	public void setTotalMoney(long total_money) {
		this.info.setTotal_money(total_money);
	}

	public long getTotalSugar() {
		return this.info.getTotal_sugar();
	}

	public void setTotalSugar(long total_sugar) {
		this.info.setTotal_sugar(total_sugar);
	}

	public long getTotalIce() {
		return this.info.getTotal_ice();
	}

	public void setTotalIce(long total_ice) {
		this.info.setTotal_ice(total_ice);
	}

	public long getTotalTopping() {
		return this.info.getTotal_topping();
	}

	public void setTotalTopping(long total_topping) {
		this.info.setTotal_topping(total_topping);
	}
	
	public byte getFreeTicket() {
		return this.info.getFree_ticket();
	}
	
	public void setFreeTicket(byte free_ticket) {
		if (free_ticket < 0) {
			free_ticket = 0;
		}
		if (free_ticket > Byte.MAX_VALUE) {
			free_ticket = Byte.MAX_VALUE;
		}
		this.info.setFree_ticket(free_ticket);
	}

	public long getLatestOrderId() {
		return this.info.getLatest_order_id();
	}

	public void setLatestOrderId(long latest_order_id) {
		this.info.setLatest_order_id(latest_order_id);
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
