package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblNihongoPetInfo;

public class NihongoPet {
	private TblNihongoPetInfo nihongoPetInfo;

	public NihongoPet() {
		nihongoPetInfo = new TblNihongoPetInfo();
	}

	public NihongoPet(TblNihongoPetInfo nihongoPetInfo) {
		this.nihongoPetInfo = nihongoPetInfo;
	}

	public TblNihongoPetInfo getNihongoPetInfo() {
		return nihongoPetInfo;
	}

	public long getId() {
		return (Long) nihongoPetInfo.Get("id");
	}

	public void setId(long id) {
		nihongoPetInfo.Set("id", id);
	}

	public String getPetName() {
		return (String) nihongoPetInfo.Get("pet_name");
	}

	public void setPetName(String petName) {
		nihongoPetInfo.Set("pet_name", petName);
	}

	public int getMaxLevel() {
		return (Integer) nihongoPetInfo.Get("max_level");
	}

	public void setMaxLevel(int maxLevel) {
		nihongoPetInfo.Set("max_level", maxLevel);
	}

	public String getCreated() {
		return (String) nihongoPetInfo.Get("created");
	}

	public void setCreated(String created) {
		nihongoPetInfo.Set("created", created);
	}

	public Date getCreatedDate() {
		String strCreated = getCreated();
		if (strCreated == null) {
			return null;
		}
		return CommonMethod.getDate(strCreated, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getUpdated() {
		return (String) nihongoPetInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		nihongoPetInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}

}

