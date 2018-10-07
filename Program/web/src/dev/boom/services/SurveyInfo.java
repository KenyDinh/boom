package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblSurveyInfo;

public class SurveyInfo {

	private TblSurveyInfo info = null;

	public SurveyInfo() {
		this.info = new TblSurveyInfo();
	}

	public SurveyInfo(TblSurveyInfo info) {
		this.info = info;
	}

	public TblSurveyInfo getInfo() {
		return info;
	}

	public long getId() {
		return this.info.getId();
	}

	public void setId(long id) {
		this.info.setId(id);
	}

	public String getName() {
		return this.info.getName();
	}

	public void setName(String name) {
		this.info.setName(name);
	}

	public byte getStatus() {
		return this.info.getStatus();
	}

	public void setStatus(byte status) {
		this.info.setStatus(status);
	}

	public boolean isActive() {
		return (getStatus() > 0);
	}

	public String getDescription() {
		return this.info.getDescription();
	}

	public void setDescription(String description) {
		this.info.setDescription(description);
	}

	public byte getMaxChoice() {
		return this.info.getMax_choice();
	}

	public void setMaxChoice(byte max_choice) {
		this.info.setMax_choice(max_choice);
	}

	public byte getMaxRetry() {
		return this.info.getMax_retry();
	}

	public void setMaxRetry(byte max_retry) {
		this.info.setMax_retry(max_retry);
	}

	public Date getCreated() {
		return this.info.getCreated();
	}

	public void setCreated(Date created) {
		this.info.setCreated(created);
	}

	public Date getExpired() {
		return this.info.getExpired();
	}

	public void setExpired(Date expired) {
		this.info.setExpired(expired);
	}
	
	public String getFormatStringExpireDate() {
		return CommonMethod.getFormatDateString(getExpired(), CommonDefine.DATE_FORMAT_PATTERN);
	}

	public Date getUpdated() {
		return this.info.getUpdated();
	}

	public void setUpdated(Date updated) {
		this.info.setUpdated(updated);
	}

}
