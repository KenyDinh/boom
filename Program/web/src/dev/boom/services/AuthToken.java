package dev.boom.services;

import java.util.Date;

import dev.boom.tbl.info.TblAuthTokenInfo;

public class AuthToken {

	private TblAuthTokenInfo authTokenInfo;

	public AuthToken() {
		super();
	}

	public AuthToken(TblAuthTokenInfo authTokenInfo) {
		super();
		this.authTokenInfo = authTokenInfo;
	}

	public TblAuthTokenInfo getAuthTokenInfo() {
		return authTokenInfo;
	}

	public long getId() {
		return this.authTokenInfo.getId();
	}

	public void setId(long id) {
		this.authTokenInfo.setId(id);
	}

	public String getToken() {
		return this.authTokenInfo.getToken();
	}

	public void setToken(String token) {
		this.authTokenInfo.setToken(token);
	}

	public String getValidator() {
		return this.authTokenInfo.getValidator();
	}

	public void setValidator(String validator) {
		this.authTokenInfo.setValidator(validator);
	}

	public long getUserId() {
		return this.authTokenInfo.getUser_id();
	}

	public void setUserId(long userId) {
		this.authTokenInfo.setUser_id(userId);
	}

	public Date getExpired() {
		return this.authTokenInfo.getExpired();
	}

	public void setExpired(Date expired) {
		this.authTokenInfo.setExpired(expired);
	}

	public boolean isExpired() {
		return getExpired().before(new Date());
	}
	
}
