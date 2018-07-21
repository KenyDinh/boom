package dev.boom.entity.info;

import java.util.List;

import dev.boom.dao.core.IDaoValue;

public class MilkTeaUserInfo extends DaoValueInfo implements IDaoValue {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "milktea_user_info";
	private static final String PRIMARY_KEY = "user_id";

	private long user_id;
	private String username;
	private long dish_count;
	private long order_count;
	private long total_money;
	private long total_sugar;
	private long total_ice;
	private long total_topping;
	private long latest_order_id;

	public MilkTeaUserInfo() {
		this.user_id = 0;
		this.username = "";
		this.dish_count = 0;
		this.order_count = 0;
		this.total_money = 0;
		this.total_sugar = 0;
		this.total_ice = 0;
		this.total_topping = 0;
		this.latest_order_id = 0;
		Sync();
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getDish_count() {
		return dish_count;
	}

	public void setDish_count(long dish_count) {
		this.dish_count = dish_count;
	}

	public long getOrder_count() {
		return order_count;
	}

	public void setOrder_count(long order_count) {
		this.order_count = order_count;
	}

	public long getTotal_money() {
		return total_money;
	}

	public void setTotal_money(long total_money) {
		this.total_money = total_money;
	}

	public long getTotal_sugar() {
		return total_sugar;
	}

	public void setTotal_sugar(long total_sugar) {
		this.total_sugar = total_sugar;
	}

	public long getTotal_ice() {
		return total_ice;
	}

	public void setTotal_ice(long total_ice) {
		this.total_ice = total_ice;
	}

	public long getTotal_topping() {
		return total_topping;
	}

	public void setTotal_topping(long total_topping) {
		this.total_topping = total_topping;
	}

	public long getLatest_order_id() {
		return latest_order_id;
	}

	public void setLatest_order_id(long latest_order_id) {
		this.latest_order_id = latest_order_id;
	}

	public List<String> getSubKey() {
		return null;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
