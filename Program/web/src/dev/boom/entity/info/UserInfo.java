package dev.boom.entity.info;

import java.util.List;

import dev.boom.dao.core.IDaoValue;

public class UserInfo extends DaoValueInfo implements IDaoValue {
	
	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "user_info";
	private static final String PRIMARY_KEY = "id";
	
	private long id;
	private String username;
	private String password;
	private int flag;

	public UserInfo() {
		this.id = 0;
		this.username = "";
		this.password = "";
		this.flag = 0;
		Sync();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
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
