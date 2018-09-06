package dev.boom.services;

import dev.boom.common.enums.UserFlagEnum;
import dev.boom.common.milktea.MilkteaMenuFlag;
import dev.boom.tbl.info.TblUserInfo;

public class UserInfo {
	
	private TblUserInfo info;

	public UserInfo(TblUserInfo info) {
		this.info = info;
	}

	public UserInfo() {
		this.info = new TblUserInfo();
	}

	public TblUserInfo getInfo() {
		return info;
	}
	
	public long getId() {
		return this.info.getId();
	}

	public void setId(long id) {
		this.info.setId(id);
	}

	public String getUsername() {
		return this.info.getUsername();
	}

	public void setUsername(String username) {
		this.info.setUsername(username);
	}

	public String getPassword() {
		return this.info.getPassword();
	}

	public void setPassword(String password) {
		this.info.setPassword(password);
	}

	public int getFlag() {
		return this.info.getFlag();
	}

	public void setFlag(int flag) {
		this.info.setFlag(flag);
	}
	
	public boolean isMenuAvailable(MenuInfo menuInfo) {
		if (UserFlagEnum.ADMINISTRATOR.isValid(getFlag())) {
			return true;
		}
		for (MilkteaMenuFlag mmf : MilkteaMenuFlag.values()) {
			if (mmf == MilkteaMenuFlag.INVALID) {
				continue;
			}
			if (mmf.isValid(menuInfo.getShowFlag()) && mmf.isValidUserFlag(getFlag())) {
				return true;
			}
		}
		return false;
	}

}
