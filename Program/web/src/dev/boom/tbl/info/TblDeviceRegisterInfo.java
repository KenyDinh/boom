package dev.boom.tbl.info;

import java.util.Date;

import dev.boom.dao.core.DaoValueInfo;

public class TblDeviceRegisterInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "device_register_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private int device_id;
	private String device_name;
	private long user_id;
	private String username;
	private byte expired;
	private Date start_date;
	private Date end_date;
	private Date updated;

	public TblDeviceRegisterInfo() {
		this.id = 0;
		this.device_id = 0;
		this.device_name = "";
		this.user_id = 0;
		this.username = "";
		this.expired = 0;
		this.start_date = new Date();
		this.end_date = this.start_date;
		this.updated = this.start_date;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDevice_id() {
		return device_id;
	}

	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
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

	public byte getExpired() {
		return expired;
	}

	public void setExpired(byte expired) {
		this.expired = expired;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
