package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblAuthTokenInfo;

public class AuthToken {
	private TblAuthTokenInfo authTokenInfo;

	public AuthToken() {
		authTokenInfo = new TblAuthTokenInfo();
	}

	public AuthToken(TblAuthTokenInfo authTokenInfo) {
		this.authTokenInfo = authTokenInfo;
	}

	public TblAuthTokenInfo getAuthTokenInfo() {
		return authTokenInfo;
	}

	public long getId() {
		return (Long) authTokenInfo.Get("id");
	}

	public void setId(long id) {
		authTokenInfo.Set("id", id);
	}

	public String getToken() {
		return (String) authTokenInfo.Get("token");
	}

	public void setToken(String token) {
		authTokenInfo.Set("token", token);
	}

	public String getValidator() {
		return (String) authTokenInfo.Get("validator");
	}

	public void setValidator(String validator) {
		authTokenInfo.Set("validator", validator);
	}

	public long getUserId() {
		return (Long) authTokenInfo.Get("user_id");
	}

	public void setUserId(long userId) {
		authTokenInfo.Set("user_id", userId);
	}

	public String getExpired() {
		return (String) authTokenInfo.Get("expired");
	}

	public void setExpired(String expired) {
		authTokenInfo.Set("expired", expired);
	}

	public Date getExpiredDate() {
		String strExpired = getExpired();
		if (strExpired == null) {
			return null;
		}
		return CommonMethod.getDate(strExpired, CommonDefine.DATE_FORMAT_PATTERN);
	}

}

