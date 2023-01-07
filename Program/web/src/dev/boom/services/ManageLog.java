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

	public ManageLog(TblManageLogInfo manageLogInfo) {
		this.manageLogInfo = manageLogInfo;
	}

	public ManageLog() {
		this.manageLogInfo = new TblManageLogInfo();
	}

	public long getId() {
		return manageLogInfo.getId();
	}
	
	public long getUserId() {
		return manageLogInfo.getUser_id();
	}
	
	public String getUsername() {
		return manageLogInfo.getUsername();
	}
	
	public byte getType() {
		return manageLogInfo.getType();
	}
	
	public String getParam() {
		return manageLogInfo.getParam();
	}
	
	public Date getCreated() {
		return manageLogInfo.getCreated();
	}
	
	public String getStrCreated() {
		return CommonMethod.getFormatDateString(getCreated(), CommonDefine.DATE_FORMAT_PATTERN);
	}
	
	@SuppressWarnings("rawtypes")
	public String getContent(Map messages) {
		String label = ManageLogType.valueOf(getType()).getLabel();
		return MessageFormat.format((String) messages.get(label), getParam());
	}
	
}
