package dev.boom.services;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.ManageLogType;
import dev.boom.tbl.info.TblManageLogInfo;

public class ManageLog {
	private TblManageLogInfo manageLogInfo;

	public ManageLog() {
		manageLogInfo = new TblManageLogInfo();
	}

	public ManageLog(TblManageLogInfo manageLogInfo) {
		this.manageLogInfo = manageLogInfo;
	}

	public TblManageLogInfo getManageLogInfo() {
		return manageLogInfo;
	}

	public long getId() {
		return (Long) manageLogInfo.Get("id");
	}

	public void setId(long id) {
		manageLogInfo.Set("id", id);
	}

	public long getUserId() {
		return (Long) manageLogInfo.Get("user_id");
	}

	public void setUserId(long userId) {
		manageLogInfo.Set("user_id", userId);
	}

	public String getUsername() {
		return (String) manageLogInfo.Get("username");
	}

	public void setUsername(String username) {
		manageLogInfo.Set("username", username);
	}

	public byte getType() {
		return (Byte) manageLogInfo.Get("type");
	}

	public void setType(byte type) {
		manageLogInfo.Set("type", type);
	}

	public String getParam() {
		return (String) manageLogInfo.Get("param");
	}

	public void setParam(String param) {
		manageLogInfo.Set("param", param);
	}

	public String getCreated() {
		return (String) manageLogInfo.Get("created");
	}

	public void setCreated(String created) {
		manageLogInfo.Set("created", created);
	}

	public Date getCreatedDate() {
		String strCreated = getCreated();
		if (strCreated == null) {
			return null;
		}
		return CommonMethod.getDate(strCreated, CommonDefine.DATE_FORMAT_PATTERN);
	}
	
	@SuppressWarnings("rawtypes")
	public String getContent(Map messages) {
		String label = ManageLogType.valueOf(getType()).getLabel();
		return MessageFormat.format((String) messages.get(label), getParam());
	}

}

