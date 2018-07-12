package dev.boom.entity.info;

import java.util.Date;
import java.util.List;

import dev.boom.dao.core.IDaoValue;

public class NihongoUserInfo extends DaoValueInfo implements IDaoValue {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "nihongo_user_info";
	private static final String PRIMARY_KEY = "user_id";
	
	private long user_id;
	private String username;
	private int star;
	private Date created;
	private Date updated;
	
	public NihongoUserInfo() {
		this.user_id = 0;
		this.username = "";
		this.star = 0;
		this.created = new Date();
		this.updated = this.created;
		Sync();
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

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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
