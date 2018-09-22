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
	private String user;
	private String result;
	private byte retry_remain;
	private Date created;

	public TblSurveyResultInfo() {
		this.id = 0;
		this.survey_id = 0;
		this.user = "";
		this.result = "";
		this.retry_remain = 0;
		this.created = new Date();
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public byte getRetry_remain() {
		return retry_remain;
	}

	public void setRetry_remain(byte retry_remain) {
		this.retry_remain = retry_remain;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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
