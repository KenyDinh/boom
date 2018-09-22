package dev.boom.tbl.info;

import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblDishInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "dish_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long shop_id;
	private String detail;

	public TblDishInfo() {
		this.id = 0;
		this.shop_id = 0;
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
