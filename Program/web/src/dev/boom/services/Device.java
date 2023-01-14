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

	public Device() {
		deviceInfo = new TblDeviceInfo();
	}

	public Device(TblDeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public TblDeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public int getId() {
		return (Integer) deviceInfo.Get("id");
	}

	public void setId(int id) {
		deviceInfo.Set("id", id);
	}

	public String getName() {
		return (String) deviceInfo.Get("name");
	}

	public void setName(String name) {
		deviceInfo.Set("name", name);
	}

	public String getSerial() {
		return (String) deviceInfo.Get("serial");
	}

	public void setSerial(String serial) {
		deviceInfo.Set("serial", serial);
	}

	public String getImage() {
		return (String) deviceInfo.Get("image");
	}

	public void setImage(String image) {
		deviceInfo.Set("image", image);
	}

	public byte getDept() {
		return (Byte) deviceInfo.Get("dept");
	}

	public void setDept(byte dept) {
		deviceInfo.Set("dept", dept);
	}

	public byte getType() {
		return (Byte) deviceInfo.Get("type");
	}

	public void setType(byte type) {
		deviceInfo.Set("type", type);
	}

	public byte getStatus() {
		return (Byte) deviceInfo.Get("status");
	}

	public void setStatus(byte status) {
		deviceInfo.Set("status", status);
	}

	public String getBuyDate() {
		return (String) deviceInfo.Get("buy_date");
	}

	public void setBuyDate(String buyDate) {
		deviceInfo.Set("buy_date", buyDate);
	}

	public Date getBuyDateDate() {
		String strBuyDate = getBuyDate();
		if (strBuyDate == null) {
			return null;
		}
		return CommonMethod.getDate(strBuyDate, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getNote() {
		return (String) deviceInfo.Get("note");
	}

	public void setNote(String note) {
		deviceInfo.Set("note", note);
	}

	public String getHoldDate() {
		return (String) deviceInfo.Get("hold_date");
	}

	public void setHoldDate(String holdDate) {
		deviceInfo.Set("hold_date", holdDate);
	}

	public Date getHoldDateDate() {
		String strHoldDate = getHoldDate();
		if (strHoldDate == null) {
			return null;
		}
		return CommonMethod.getDate(strHoldDate, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getReleaseDate() {
		return (String) deviceInfo.Get("release_date");
	}

	public void setReleaseDate(String releaseDate) {
		deviceInfo.Set("release_date", releaseDate);
	}

	public Date getReleaseDateDate() {
		String strReleaseDate = getReleaseDate();
		if (strReleaseDate == null) {
			return null;
		}
		return CommonMethod.getDate(strReleaseDate, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getExtendDate() {
		return (String) deviceInfo.Get("extend_date");
	}

	public void setExtendDate(String extendDate) {
		deviceInfo.Set("extend_date", extendDate);
	}

	public Date getExtendDateDate() {
		String strExtendDate = getExtendDate();
		if (strExtendDate == null) {
			return null;
		}
		return CommonMethod.getDate(strExtendDate, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public long getUserId() {
		return (Long) deviceInfo.Get("user_id");
	}

	public void setUserId(long userId) {
		deviceInfo.Set("user_id", userId);
	}

	public String getUsername() {
		return (String) deviceInfo.Get("username");
	}

	public void setUsername(String username) {
		deviceInfo.Set("username", username);
	}

	public byte getAvailable() {
		return (Byte) deviceInfo.Get("available");
	}

	public void setAvailable(byte available) {
		deviceInfo.Set("available", available);
	}

	public int getFlag() {
		return (Integer) deviceInfo.Get("flag");
	}

	public void setFlag(int flag) {
		deviceInfo.Set("flag", flag);
	}

	public int getRegistCount() {
		return (Integer) deviceInfo.Get("regist_count");
	}

	public void setRegistCount(int registCount) {
		deviceInfo.Set("regist_count", registCount);
	}

	public String getUpdated() {
		return (String) deviceInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		deviceInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}
	
	public String getFormatNote() {
		String note = getNote();
		if (note == null || note.length() == 0) {
			return "";
		}
		note = note.replace("<", "&lt;").replace(">", "&gt;");
//		note = note.replaceAll("\r\n", "<br/>").replaceAll("[\r\n]", "<br/>");
		return note;
	}
	
	public boolean isAvailable() {
		return (getAvailable() != 0);
	}
	
	public void incRegistCount() {
		setRegistCount(getRegistCount() + 1);
	}
	
	public void descRegistCount() {
		setRegistCount(getRegistCount() - 1);
	}
	
	public void setAvailable(boolean available) {
		if (available) {
			setAvailable((byte) 1);
		} else {
			setAvailable((byte) 0);
		}
	}
	
	public boolean isEditable(User user) {
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
		setHoldDate(CommonDefine.DEFAULT_DATE_TIME);
		setReleaseDate(CommonDefine.DEFAULT_DATE_TIME);
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

