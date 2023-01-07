package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.DeviceDept;
import dev.boom.common.enums.DeviceFlag;
import dev.boom.common.enums.DeviceStatus;
import dev.boom.tbl.info.TblDeviceInfo;

public class Device {

	private TblDeviceInfo deviceInfo;

	public Device(TblDeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public Device() {
		this.deviceInfo = new TblDeviceInfo();
	}

	public TblDeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public int getId() {
		return this.deviceInfo.getId();
	}

	public void setId(int id) {
		this.deviceInfo.setId(id);
	}

	public String getName() {
		return this.deviceInfo.getName();
	}

	public void setName(String name) {
		this.deviceInfo.setName(name);
	}

	public String getSerial() {
		return this.deviceInfo.getSerial();
	}

	public void setSerial(String serial) {
		this.deviceInfo.setSerial(serial);
	}
	
	public String getImage() {
		return this.deviceInfo.getImage();
	}

	public void setImage(String image) {
		this.deviceInfo.setImage(image);
	}

	public byte getDept() {
		return this.deviceInfo.getDept();
	}

	public void setDept(byte dept) {
		this.deviceInfo.setDept(dept);
	}

	public byte getType() {
		return this.deviceInfo.getType();
	}

	public void setType(byte type) {
		this.deviceInfo.setType(type);
	}

	public byte getStatus() {
		return this.deviceInfo.getStatus();
	}

	public void setStatus(byte status) {
		this.deviceInfo.setStatus(status);
	}

	public Date getBuyDate() {
		return this.deviceInfo.getBuy_date();
	}
	
	public void setBuyDate(Date buyDate) {
		this.deviceInfo.setBuy_date(buyDate);
	}
	
	public String getNote() {
		return this.deviceInfo.getNote();
	}
	
	public String getFormatNote() {
		String note = this.deviceInfo.getNote();
		if (note == null || note.length() == 0) {
			return "";
		}
		note = note.replace("<", "&lt;").replace(">", "&gt;");
//		note = note.replaceAll("\r\n", "<br/>").replaceAll("[\r\n]", "<br/>");
		return note;
	}
	
	public void setNote(String note) {
		this.deviceInfo.setNote(note);
	}
	
	public Date getHoldDate() {
		return this.deviceInfo.getHold_date();
	}

	public void setHoldDate(Date holdDate) {
		this.deviceInfo.setHold_date(holdDate);
	}

	public Date getReleaseDate() {
		return this.deviceInfo.getRelease_date();
	}

	public void setReleaseDate(Date releaseDate) {
		this.deviceInfo.setRelease_date(releaseDate);
	}
	
	public Date getExtendDate() {
		return this.deviceInfo.getExtend_date();
	}

	public void setExtendDate(Date extendDate) {
		this.deviceInfo.setExtend_date(extendDate);
	}

	public long getUserId() {
		return this.deviceInfo.getUser_id();
	}

	public void setUserId(long userId) {
		this.deviceInfo.setUser_id(userId);
	}

	public String getUsername() {
		return this.deviceInfo.getUsername();
	}

	public void setUsername(String username) {
		this.deviceInfo.setUsername(username);
	}
	
	public boolean isAvailable() {
		return (this.deviceInfo.getAvailable() != 0);
	}
	
	public int getFlag() {
		return this.deviceInfo.getFlag();
	}
	
	public void setFlag(int flag) {
		this.deviceInfo.setFlag(flag);
	}
	
	public int getRegistCount() {
		return this.deviceInfo.getRegist_count();
	}
	
	public void setRegistCount(int count) {
		this.deviceInfo.setRegist_count(Math.max(0, count));
	}
	
	public void incRegistCount() {
		setRegistCount(getRegistCount() + 1);
	}
	
	public void descRegistCount() {
		setRegistCount(getRegistCount() - 1);
	}
	
	public void setAvailable(boolean available) {
		if (available) {
			this.deviceInfo.setAvailable((byte) 1);
		} else {
			this.deviceInfo.setAvailable((byte) 0);
		}
	}

	public Date getUpdated() {
		return this.deviceInfo.getUpdated();
	}

	public void setUpdated(Date updated) {
		this.deviceInfo.setUpdated(updated);
	}

	public boolean isEditable(UserInfo user) {
		if (user == null) {
			return false;
		}
		int flag = getFlag();
		DeviceDept dept = DeviceDept.valueOf(getDept());
		if (flag == 0) {
			return true;
		}
		if (user.isDeviceAdmin()) {
			return true;
		}
		if (DeviceFlag.ALL.isValid(flag)) {
			return true;
		}
		if (DeviceFlag.DEPT.isValid(flag)) {
			if (dept == DeviceDept.NONE) {
				return true;
			}
			if (dept.isValid(user.getDept())) {
				return true;
			}
		}
		
		return false;
	}

	public void resetStatus() {
		setStatus(DeviceStatus.AVAILABLE.getStatus());
		setUserId(0);
		setUsername("");
		setHoldDate(CommonMethod.getDate(CommonDefine.DEFAULT_DATE_TIME));
		setReleaseDate(CommonMethod.getDate(CommonDefine.DEFAULT_DATE_TIME));
	}

	public boolean isShowDate() {
		switch (DeviceStatus.valueOf(getStatus())) {
		case UNAVAILABLE:
		case CHANGE_PENDING:
			return true;
		default:
			return false;
		}
	}
}
