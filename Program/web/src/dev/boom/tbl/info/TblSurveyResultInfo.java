package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblSurveyResultInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "survey_result_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long survey_id;
	private String user_id;
	private String username;
	private String department;
	private String result;
	private byte progress;
	private Date updated;

	public TblSurveyResultInfo() {
		this.id = 0;
		this.survey_id = 0;
		this.user_id = "";
		this.username = "";
		this.department = "";
		this.result = "";
		this.progress = 0;
		this.updated = new Date();
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSurvey_id() {
		return survey_id;
	}

	public void setSurvey_id(long survey_id) {
		this.survey_id = survey_id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public byte getProgress() {
		return progress;
	}

	public void setProgress(byte progress) {
		this.progress = progress;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getCreated() {
		return updated;
	}

	public void setCreated(Date created) {
		this.updated = created;
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
