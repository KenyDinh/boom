package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.common.CommonDefine;
import dev.boom.dao.core.DaoValueInfo;

public class TblSurveyInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "survey_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private String name;
	private byte status;
	private String description;
	private byte max_choice;
	private byte max_retry;
	private Date created;
	private Date expired;
	private Date updated;

	public TblSurveyInfo() {
		this.id = 0;
		this.name = "";
		this.status = 0;
		this.description = "";
		this.max_choice = 1;
		this.max_retry = 0;
		this.created = new Date();
		this.expired = new Date(this.created.getTime() + CommonDefine.MILLION_SECOND_DAY);
		this.updated = this.created;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte getMax_choice() {
		return max_choice;
	}

	public void setMax_choice(byte max_choice) {
		this.max_choice = max_choice;
	}

	public byte getMax_retry() {
		return max_retry;
	}

	public void setMax_retry(byte max_retry) {
		this.max_retry = max_retry;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getExpired() {
		return expired;
	}

	public void setExpired(Date expired) {
		this.expired = expired;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public List<String> getSubKey() {
		return null;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

}
