package dev.boom.services;

import dev.boom.common.enums.Department;
import dev.boom.common.enums.UserFlagEnum;
import dev.boom.common.enums.UserRole;
import dev.boom.tbl.info.TblUserInfo;

public class User {
	private TblUserInfo userInfo;

	public User() {
		userInfo = new TblUserInfo();
	}

	public User(TblUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public TblUserInfo getUserInfo() {
		return userInfo;
	}

	public long getId() {
		return (Long) userInfo.Get("id");
	}

	public void setId(long id) {
		userInfo.Set("id", id);
	}

	public String getUsername() {
		return (String) userInfo.Get("username");
	}

	public void setUsername(String username) {
		userInfo.Set("username", username);
	}

	public String getPassword() {
		return (String) userInfo.Get("password");
	}

	public void setPassword(String password) {
		userInfo.Set("password", password);
	}

	public String getEmpid() {
		return (String) userInfo.Get("empid");
	}

	public void setEmpid(String empid) {
		userInfo.Set("empid", empid);
	}

	public String getName() {
		return (String) userInfo.Get("name");
	}

	public void setName(String name) {
		userInfo.Set("name", name);
	}

	public int getRole() {
		return (Integer) userInfo.Get("role");
	}

	public void setRole(int role) {
		userInfo.Set("role", role);
	}

	public int getDept() {
		return (Integer) userInfo.Get("dept");
	}

	public void setDept(int dept) {
		userInfo.Set("dept", dept);
	}

	public int getFlag() {
		return (Integer) userInfo.Get("flag");
	}

	public void setFlag(int flag) {
		userInfo.Set("flag", flag);
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
	
	public boolean isGameAdmin() {
		return (UserRole.ADMINISTRATOR.getRole() == getRole() || UserRole.GAME_ADMIN.getRole() == getRole());
	}
	
	public boolean hasAdminRole() {
		// skip check for vote admin
		return (isAdministrator() || isMilkteaAdmin() || isDeviceAdmin() || isGameAdmin());
	}

}

