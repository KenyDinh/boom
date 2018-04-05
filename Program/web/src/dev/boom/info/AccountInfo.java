package dev.boom.info;

import java.util.List;

import dev.boom.core.dao.IDaoValue;

public class AccountInfo extends DaoValueInfo implements IDaoValue {
	
	public static final String TABLE_NAME = "account_info";
	public static final String PRIMARY_KEY = "id";
	
	private int id;
	private String username;
	private String password;

	public AccountInfo() {
		this.id = 0;
		this.username = "";
		this.password = "";
		saveOriginal();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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
