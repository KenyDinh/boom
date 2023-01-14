package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblSurveyInfo;

public class Survey {
	private TblSurveyInfo surveyInfo;

	public Survey() {
		surveyInfo = new TblSurveyInfo();
	}

	public Survey(TblSurveyInfo surveyInfo) {
		this.surveyInfo = surveyInfo;
	}

	public TblSurveyInfo getSurveyInfo() {
		return surveyInfo;
	}

	public long getId() {
		return (Long) surveyInfo.Get("id");
	}

	public void setId(long id) {
		surveyInfo.Set("id", id);
	}

	public String getName() {
		return (String) surveyInfo.Get("name");
	}

	public void setName(String name) {
		surveyInfo.Set("name", name);
	}

	public String getPathname() {
		return (String) surveyInfo.Get("pathname");
	}

	public void setPathname(String pathname) {
		surveyInfo.Set("pathname", pathname);
	}

	public byte getStatus() {
		return (Byte) surveyInfo.Get("status");
	}

	public void setStatus(byte status) {
		surveyInfo.Set("status", status);
	}

	public byte getType() {
		return (Byte) surveyInfo.Get("type");
	}

	public void setType(byte type) {
		surveyInfo.Set("type", type);
	}

	public int getFlag() {
		return (Integer) surveyInfo.Get("flag");
	}

	public void setFlag(int flag) {
		surveyInfo.Set("flag", flag);
	}

	public String getDescription() {
		return (String) surveyInfo.Get("description");
	}

	public void setDescription(String description) {
		surveyInfo.Set("description", description);
	}

	public String getCreated() {
		return (String) surveyInfo.Get("created");
	}

	public void setCreated(String created) {
		surveyInfo.Set("created", created);
	}

	public Date getCreatedDate() {
		String strCreated = getCreated();
		if (strCreated == null) {
			return null;
		}
		return CommonMethod.getDate(strCreated, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getExpired() {
		return (String) surveyInfo.Get("expired");
	}

	public void setExpired(String expired) {
		surveyInfo.Set("expired", expired);
	}

	public Date getExpiredDate() {
		String strExpired = getExpired();
		if (strExpired == null) {
			return null;
		}
		return CommonMethod.getDate(strExpired, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getUpdated() {
		return (String) surveyInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		surveyInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}

}

