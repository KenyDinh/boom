package dev.boom.entity.info;

import java.util.List;

import dev.boom.dao.core.IDaoValue;

public class ShopOptionInfo extends DaoValueInfo implements IDaoValue {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "shop_option_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long shop_id;
	private short type;
	private String name;
	private int price;

	public ShopOptionInfo() {
		this.id = 0;
		this.shop_id = 0;
		this.type = 0;
		this.name = "";
		this.price = 0;
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

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
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
