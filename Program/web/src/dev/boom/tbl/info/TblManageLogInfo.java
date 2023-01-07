package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblManageLogInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "manage_log_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long user_id;
	private String username;
	private byte type;
	private String param;
	private Date created;

	public TblManageLogInfo() {
		this.id = 0;
		this.user_id = 0;
		this.username = "";
		this.type = 0;
		this.param = "";
		this.created = new Date();
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
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
