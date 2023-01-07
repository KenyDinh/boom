package dev.boom.tbl.info;

import java.util.Date;

import dev.boom.dao.core.DaoValueInfo;

public class TblAuthTokenInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "auth_token_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private String token;
	private String validator;
	private long user_id;
	private Date expired;

	public TblAuthTokenInfo() {
		this.id = 0;
		this.token = "";
		this.validator = "";
		this.user_id = 0;
		this.expired = new Date();
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public Date getExpired() {
		return expired;
	}

	public void setExpired(Date expired) {
		this.expired = expired;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
