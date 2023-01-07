package dev.boom.tbl.info;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.dao.core.DaoValueInfo;

public class TblDemoSessionInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "demo_session_info";
	private static final String PRIMARY_KEY = "id";

	private int id;
	private Date demo_time;
	private String demo_location;
	private String content;

	public TblDemoSessionInfo() {
		this.id = 0;
		this.demo_time = CommonMethod.getDate(CommonDefine.DEFAULT_DATE_TIME);
		this.demo_location = "";
		this.content = "";
		Sync();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDemo_time() {
		return demo_time;
	}

	public void setDemo_time(Date demo_time) {
		this.demo_time = demo_time;
	}

	public String getDemo_location() {
		return demo_location;
	}

	public void setDemo_location(String demo_location) {
		this.demo_location = demo_location;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
