package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.common.CommonDefine;
import dev.boom.dao.core.IDaoValue;

public class TblMenuInfo extends DaoValueInfo implements IDaoValue {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "menu_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private String name;
	private long shop_id;
	private short sale;
	private String code;
	private long max_discount;
	private long shipping_fee;
	private String description;
	private byte status;
	private int flag;
	private Date created;
	private Date expired;
	private Date updated;

	public TblMenuInfo() {
		this.id = 0;
		this.name = "";
		this.shop_id = 0;
		this.sale = 0;
		this.code = "";
		this.max_discount = 0;
		this.shipping_fee = 0;
		this.description = "";
		this.status = 0;
		this.flag = 0;
		this.created = new Date();
		this.expired = new Date(this.created.getTime() + CommonDefine.MILLION_SECOND_DAY);
		this.updated = this.created;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getShop_id() {
		return shop_id;
	}

	public void setShop_id(long shop_id) {
		this.shop_id = shop_id;
	}

	public short getSale() {
		return sale;
	}

	public void setSale(short sale) {
		this.sale = sale;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getMax_discount() {
		return max_discount;
	}

	public void setMax_discount(long max_discount) {
		this.max_discount = max_discount;
	}

	public long getShipping_fee() {
		return shipping_fee;
	}

	public void setShipping_fee(long shipping_fee) {
		this.shipping_fee = shipping_fee;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
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

	public Date getExpired() {
		return expired;
	}

	public void setExpired(Date expired) {
		this.expired = expired;
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
