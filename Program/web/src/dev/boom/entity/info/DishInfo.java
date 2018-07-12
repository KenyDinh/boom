package dev.boom.entity.info;

import java.util.List;

import dev.boom.dao.core.IDaoValue;

public class DishInfo extends DaoValueInfo implements IDaoValue {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "dish_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long shop_id;
	private String name;
	private String detail;

	public DishInfo() {
		this.id = 0;
		this.shop_id = 0;
		this.name = "";
		this.detail = "";
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getShop_id() {
		return shop_id;
	}

	public void setShop_id(long shop_id) {
		this.shop_id = shop_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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
