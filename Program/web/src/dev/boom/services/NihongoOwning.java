package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblNihongoOwningInfo;

public class NihongoOwning {
	private TblNihongoOwningInfo nihongoOwningInfo;

	public NihongoOwning() {
		nihongoOwningInfo = new TblNihongoOwningInfo();
	}

	public NihongoOwning(TblNihongoOwningInfo nihongoOwningInfo) {
		this.nihongoOwningInfo = nihongoOwningInfo;
	}

	public TblNihongoOwningInfo getNihongoOwningInfo() {
		return nihongoOwningInfo;
	}

	public long getId() {
		return (Long) nihongoOwningInfo.Get("id");
	}

	public void setId(long id) {
		nihongoOwningInfo.Set("id", id);
	}

	public long getUserId() {
		return (Long) nihongoOwningInfo.Get("user_id");
	}

	public void setUserId(long userId) {
		nihongoOwningInfo.Set("user_id", userId);
	}

	public long getPetId() {
		return (Long) nihongoOwningInfo.Get("pet_id");
	}

	public void setPetId(long petId) {
		nihongoOwningInfo.Set("pet_id", petId);
	}

	public int getCurrentLevel() {
		return (Integer) nihongoOwningInfo.Get("current_level");
	}

	public void setCurrentLevel(int currentLevel) {
		nihongoOwningInfo.Set("current_level", currentLevel);
	}

	public String getCreated() {
		return (String) nihongoOwningInfo.Get("created");
	}

	public void setCreated(String created) {
		nihongoOwningInfo.Set("created", created);
	}

	public Date getCreatedDate() {
		String strCreated = getCreated();
		if (strCreated == null) {
			return null;
		}
		return CommonMethod.getDate(strCreated, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getUpdated() {
		return (String) nihongoOwningInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		nihongoOwningInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}

}

