package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblOrderInfo extends DaoValueInfo {
	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "order_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long user_id;
	private String username;
	private long menu_id;
	private long shop_id;
	private String dish_name;
	private String dish_type;
	private long dish_price;
	private long attr_price;
	private long final_price;
	private int dish_code;
	private byte voting_star;
	private long quantity;
	private String size;
	private String ice;
	private String sugar;
	private String option_list;
	private int flag;
	private Date created;
	private Date updated;

	public TblOrderInfo() {
		this.id = 0;
		this.user_id = 0;
		this.username = "";
		this.menu_id = 0;
		this.shop_id = 0;
		this.dish_name = "";
		this.dish_type = "";
		this.dish_price = 0;
		this.attr_price = 0;
		this.final_price = 0;
		this.dish_code = 0;
		this.voting_star = 0;
		this.quantity = 0;
		this.size = "";
		this.ice = "";
		this.sugar = "";
		this.option_list = "";
		this.flag = 0;
		this.created = new Date();
		this.updated = this.created;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(long menu_id) {
		this.menu_id = menu_id;
	}

	public long getShop_id() {
		return shop_id;
	}

	public void setShop_id(long shop_id) {
		this.shop_id = shop_id;
	}

	public String getDish_name() {
		return dish_name;
	}

	public void setDish_name(String dish_name) {
		this.dish_name = dish_name;
	}

	public String getDish_type() {
		return dish_type;
	}

	public void setDish_type(String dish_type) {
		this.dish_type = dish_type;
	}

	public long getDish_price() {
		return dish_price;
	}

	public void setDish_price(long dish_price) {
		this.dish_price = dish_price;
	}

	public long getAttr_price() {
		return attr_price;
	}

	public void setAttr_price(long attr_price) {
		this.attr_price = attr_price;
	}

	public long getFinal_price() {
		return final_price;
	}

	public void setFinal_price(long final_price) {
		this.final_price = final_price;
	}

	public int getDish_code() {
		return dish_code;
	}

	public void setDish_code(int dish_code) {
		this.dish_code = dish_code;
	}

	public byte getVoting_star() {
		return voting_star;
	}

	public void setVoting_star(byte voting_star) {
		this.voting_star = voting_star;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getIce() {
		return ice;
	}

	public void setIce(String ice) {
		this.ice = ice;
	}

	public String getSugar() {
		return sugar;
	}

	public void setSugar(String sugar) {
		this.sugar = sugar;
	}

	public String getOption_list() {
		return option_list;
	}

	public void setOption_list(String option_list) {
		this.option_list = option_list;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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
