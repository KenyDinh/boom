package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblNihongoUserInfo;

public class NihongoUser {
	private TblNihongoUserInfo nihongoUserInfo;

	public NihongoUser() {
		nihongoUserInfo = new TblNihongoUserInfo();
	}

	public NihongoUser(TblNihongoUserInfo nihongoUserInfo) {
		this.nihongoUserInfo = nihongoUserInfo;
	}

	public TblNihongoUserInfo getNihongoUserInfo() {
		return nihongoUserInfo;
	}

	public long getUserId() {
		return (Long) nihongoUserInfo.Get("user_id");
	}

	public void setUserId(long userId) {
		nihongoUserInfo.Set("user_id", userId);
	}

	public String getUsername() {
		return (String) nihongoUserInfo.Get("username");
	}

	public void setUsername(String username) {
		nihongoUserInfo.Set("username", username);
	}

	public int getStar() {
		return (Integer) nihongoUserInfo.Get("star");
	}

	public void setStar(int star) {
		nihongoUserInfo.Set("star", star);
	}

	public String getCreated() {
		return (String) nihongoUserInfo.Get("created");
	}

	public void setCreated(String created) {
		nihongoUserInfo.Set("created", created);
	}

	public Date getCreatedDate() {
		String strCreated = getCreated();
		if (strCreated == null) {
			return null;
		}
		return CommonMethod.getDate(strCreated, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getUpdated() {
		return (String) nihongoUserInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		nihongoUserInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}

}

