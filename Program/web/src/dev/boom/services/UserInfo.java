package dev.boom.services;

import dev.boom.common.enums.Department;
import dev.boom.common.enums.UserFlagEnum;
import dev.boom.common.enums.UserRole;
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
	
	public String getEmpid() {
		return this.info.getEmpid();
	}
	
	public void setEmpid(String empid) {
		this.info.setEmpid(empid);
	}
	
	public String getName() {
		return this.info.getName();
	}
	
	public void setName(String name) {
		this.info.setName(name);
	}
	
	public int getRole() {
		return this.info.getRole();
	}
	
	public void setRole(int role) {
		this.info.setRole(role);
	}
	
	public int getDept() {
		return this.info.getDept();
	}
	
	public void setDept(int dept) {
		this.info.setDept(dept);
	}

	public int getFlag() {
		return this.info.getFlag();
	}

	public void setFlag(int flag) {
		this.info.setFlag(flag);
	}
	
	public TblUserInfo getTblUserInfo() {
		return info;
	}
	
	public boolean isActive() {
		return UserFlagEnum.ACTIVE.isValid(getFlag());
	}
	
	public boolean isMilkteaBanned() {
		return UserFlagEnum.MILKTEA_BANNED.isValid(getFlag());
	}
	
	public boolean isDeviceBanned() {
		return UserFlagEnum.DEVICE_BANNED.isValid(getFlag());
	}
	
	public boolean isCommentBanned() {
		return UserFlagEnum.COMMENT_BANNED.isValid(getFlag());
	}
	
	public String getDepartment() {
		String strDept = "";
		for (Department dept : Department.values()) {
			if (dept.isValid(getDept())) {
				if (strDept.length() > 0) {
					strDept += ",";
				}
				strDept += dept.name();
			}
		}
		return strDept;
	}

	public boolean isAdministrator() {
		return UserRole.ADMINISTRATOR.getRole() == getRole();
	}
	
	public boolean isMilkteaAdmin() {
		return (UserRole.ADMINISTRATOR.getRole() == getRole() || UserRole.MILKTEA_ADMIN.getRole() == getRole());
	}
	
	public boolean isDeviceAdmin() {
		return (UserRole.ADMINISTRATOR.getRole() == getRole() || UserRole.DEVICE_ADMIN.getRole() == getRole());
	}
	
	public boolean isVoteAdmin() {
		return (UserRole.ADMINISTRATOR.getRole() == getRole() || UserRole.VOTE_ADMIN.getRole() == getRole());
	}
	
	public boolean hasAdminRole() {
		return (isAdministrator() || isMilkteaAdmin() || isDeviceAdmin());
	}
}
