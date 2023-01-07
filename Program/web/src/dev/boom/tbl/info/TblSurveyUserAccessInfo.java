package dev.boom.tbl.info;

import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblSurveyUserAccessInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "survey_user_access_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long survey_id;
	private String user_code;
	private int flag;

	public TblSurveyUserAccessInfo() {
		this.id = 0;
		this.survey_id = 0;
		this.user_code = "";
		this.flag = 0;
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

	public String getUser_code() {
		return user_code;
	}

	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
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
