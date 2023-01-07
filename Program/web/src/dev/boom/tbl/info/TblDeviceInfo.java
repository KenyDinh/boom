package dev.boom.tbl.info;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.dao.core.DaoValueInfo;

public class TblDeviceInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "device_info";
	private static final String PRIMARY_KEY = "id";

	private int id;
	private String name;
	private String serial;
	private String image;
	private byte dept;
	private byte type;
	private byte status;
	private Date buy_date;
	private String note;
	private Date hold_date;
	private Date release_date;
	private Date extend_date;
	private long user_id;
	private String username;
	private byte available;
	private int flag;
	private int regist_count;
	private Date updated;

	public TblDeviceInfo() {
		this.id = 0;
		this.name = "";
		this.serial = "";
		this.image = "";
		this.dept = 0;
		this.type = 0;
		this.status = 0;
		this.buy_date = CommonMethod.getDate(CommonDefine.DEFAULT_DATE_TIME);
		this.note = "";
		this.hold_date = CommonMethod.getDate(CommonDefine.DEFAULT_DATE_TIME);
		this.release_date = this.hold_date;
		this.extend_date = this.hold_date;
		this.user_id = 0;
		this.username = "";
		this.available = 0;
		this.flag = 0;
		this.regist_count = 0;
		this.updated = new Date();
		Sync();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public byte getDept() {
		return dept;
	}

	public void setDept(byte dept) {
		this.dept = dept;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Date getBuy_date() {
		return buy_date;
	}

	public void setBuy_date(Date buy_date) {
		this.buy_date = buy_date;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getHold_date() {
		return hold_date;
	}

	public void setHold_date(Date hold_date) {
		this.hold_date = hold_date;
	}

	public Date getRelease_date() {
		return release_date;
	}

	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}

	public Date getExtend_date() {
		return extend_date;
	}

	public void setExtend_date(Date extend_date) {
		this.extend_date = extend_date;
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

	public byte getAvailable() {
		return available;
	}

	public void setAvailable(byte available) {
		this.available = available;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getRegist_count() {
		return regist_count;
	}

	public void setRegist_count(int regist_count) {
		this.regist_count = regist_count;
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
