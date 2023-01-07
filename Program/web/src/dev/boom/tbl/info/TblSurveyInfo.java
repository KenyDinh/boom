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
	private String pathname;
	private byte status;
	private byte type;
	private int flag;
	private String description;
	private Date created;
	private Date expired;
	private Date updated;

	public TblSurveyInfo() {
		this.id = 0;
		this.name = "";
		this.pathname = "";
		this.status = 0;
		this.type = 0;
		this.flag = 0;
		this.description = "";
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

	public String getPathname() {
		return pathname;
	}

	public void setPathname(String pathname) {
		this.pathname = pathname;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
