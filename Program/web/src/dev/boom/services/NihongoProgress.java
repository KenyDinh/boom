package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblNihongoProgressInfo;

public class NihongoProgress {
	private TblNihongoProgressInfo nihongoProgressInfo;

	public NihongoProgress() {
		nihongoProgressInfo = new TblNihongoProgressInfo();
	}

	public NihongoProgress(TblNihongoProgressInfo nihongoProgressInfo) {
		this.nihongoProgressInfo = nihongoProgressInfo;
	}

	public TblNihongoProgressInfo getNihongoProgressInfo() {
		return nihongoProgressInfo;
	}

	public long getId() {
		return (Long) nihongoProgressInfo.Get("id");
	}

	public void setId(long id) {
		nihongoProgressInfo.Set("id", id);
	}

	public long getUserId() {
		return (Long) nihongoProgressInfo.Get("user_id");
	}

	public void setUserId(long userId) {
		nihongoProgressInfo.Set("user_id", userId);
	}

	public int getTestId() {
		return (Integer) nihongoProgressInfo.Get("test_id");
	}

	public void setTestId(int testId) {
		nihongoProgressInfo.Set("test_id", testId);
	}

	public int getProgress() {
		return (Integer) nihongoProgressInfo.Get("progress");
	}

	public void setProgress(int progress) {
		nihongoProgressInfo.Set("progress", progress);
	}

	public String getCreated() {
		return (String) nihongoProgressInfo.Get("created");
	}

	public void setCreated(String created) {
		nihongoProgressInfo.Set("created", created);
	}

	public Date getCreatedDate() {
		String strCreated = getCreated();
		if (strCreated == null) {
			return null;
		}
		return CommonMethod.getDate(strCreated, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getUpdated() {
		return (String) nihongoProgressInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		nihongoProgressInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}

}

