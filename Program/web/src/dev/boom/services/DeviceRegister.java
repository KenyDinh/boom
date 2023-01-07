package dev.boom.services;

import java.util.Date;

import dev.boom.tbl.info.TblDeviceRegisterInfo;

public class DeviceRegister {

	private TblDeviceRegisterInfo deviceRegisterInfo;

	public DeviceRegister(TblDeviceRegisterInfo deviceLogInfo) {
		this.deviceRegisterInfo = deviceLogInfo;
	}

	public DeviceRegister() {
		this.deviceRegisterInfo = new TblDeviceRegisterInfo();
	}

	public TblDeviceRegisterInfo getDeviceRegisterInfo() {
		return deviceRegisterInfo;
	}

	public long getId() {
		return this.deviceRegisterInfo.getId();
	}

	public void setId(long id) {
		this.deviceRegisterInfo.setId(id);
	}

	public int getDevice_id() {
		return this.deviceRegisterInfo.getDevice_id();
	}

	public void setDevice_id(int device_id) {
		this.deviceRegisterInfo.setDevice_id(device_id);
	}

	public long getUser_id() {
		return this.deviceRegisterInfo.getUser_id();
	}

	public void setUser_id(long user_id) {
		this.deviceRegisterInfo.setUser_id(user_id);
	}

	public String getUsername() {
		return this.deviceRegisterInfo.getUsername();
	}

	public void setUsername(String username) {
		this.deviceRegisterInfo.setUsername(username);
	}

	public Date getStartDate() {
		return this.deviceRegisterInfo.getStart_date();
	}
	
	public Date getEndDate() {
		return this.deviceRegisterInfo.getEnd_date();
	}
	
	public Date getUpdated() {
		return this.deviceRegisterInfo.getUpdated();
	}

	public void setUpdated(Date updated) {
		this.deviceRegisterInfo.setUpdated(updated);
	}
	
	public void setExpired() {
		this.deviceRegisterInfo.setExpired((byte) 1);
	}
	
	public boolean isExpired() {
		return (this.deviceRegisterInfo.getExpired() > 0);
	}
}
