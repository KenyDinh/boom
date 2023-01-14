package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblDeviceRegisterInfo;

public class DeviceRegister {
	private TblDeviceRegisterInfo deviceRegisterInfo;

	public DeviceRegister() {
		deviceRegisterInfo = new TblDeviceRegisterInfo();
	}

	public DeviceRegister(TblDeviceRegisterInfo deviceRegisterInfo) {
		this.deviceRegisterInfo = deviceRegisterInfo;
	}

	public TblDeviceRegisterInfo getDeviceRegisterInfo() {
		return deviceRegisterInfo;
	}

	public long getId() {
		return (Long) deviceRegisterInfo.Get("id");
	}

	public void setId(long id) {
		deviceRegisterInfo.Set("id", id);
	}

	public int getDeviceId() {
		return (Integer) deviceRegisterInfo.Get("device_id");
	}

	public void setDeviceId(int deviceId) {
		deviceRegisterInfo.Set("device_id", deviceId);
	}

	public String getDeviceName() {
		return (String) deviceRegisterInfo.Get("device_name");
	}

	public void setDeviceName(String deviceName) {
		deviceRegisterInfo.Set("device_name", deviceName);
	}

	public long getUserId() {
		return (Long) deviceRegisterInfo.Get("user_id");
	}

	public void setUserId(long userId) {
		deviceRegisterInfo.Set("user_id", userId);
	}

	public String getUsername() {
		return (String) deviceRegisterInfo.Get("username");
	}

	public void setUsername(String username) {
		deviceRegisterInfo.Set("username", username);
	}

	public byte getExpired() {
		return (Byte) deviceRegisterInfo.Get("expired");
	}

	public void setExpired(byte expired) {
		deviceRegisterInfo.Set("expired", expired);
	}

	public String getStartDate() {
		return (String) deviceRegisterInfo.Get("start_date");
	}

	public void setStartDate(String startDate) {
		deviceRegisterInfo.Set("start_date", startDate);
	}

	public Date getStartDateDate() {
		String strStartDate = getStartDate();
		if (strStartDate == null) {
			return null;
		}
		return CommonMethod.getDate(strStartDate, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getEndDate() {
		return (String) deviceRegisterInfo.Get("end_date");
	}

	public void setEndDate(String endDate) {
		deviceRegisterInfo.Set("end_date", endDate);
	}

	public Date getEndDateDate() {
		String strEndDate = getEndDate();
		if (strEndDate == null) {
			return null;
		}
		return CommonMethod.getDate(strEndDate, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getUpdated() {
		return (String) deviceRegisterInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		deviceRegisterInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}
	
	public void setExpired() {
		setExpired((byte) 1);
	}
	
	public boolean isExpired() {
		return (getExpired() > 0);
	}

}

