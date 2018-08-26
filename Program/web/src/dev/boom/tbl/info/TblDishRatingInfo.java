package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.dao.core.IDaoValue;

public class TblDishRatingInfo extends DaoValueInfo implements IDaoValue {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "dish_rating_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long shop_id;
	private String name;
	private int code;
	private long order_count;
	private long star_count;
	private Date updated;

	public TblDishRatingInfo() {
		this.id = 0;
		this.shop_id = 0;
		this.name = "";
		this.code = 0;
		this.order_count = 0;
		this.star_count = 0;
		this.updated = new Date();
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getOrder_count() {
		return order_count;
	}

	public void setOrder_count(long order_count) {
		this.order_count = order_count;
	}

	public long getStar_count() {
		return star_count;
	}

	public void setStar_count(long star_count) {
		this.star_count = star_count;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
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
