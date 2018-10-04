package dev.boom.tbl.info;

import java.util.Date;

import dev.boom.dao.core.DaoValueInfo;


public class TblCannonPlayerInfo extends DaoValueInfo{
	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "cannon_player_info";
	private static final String PRIMARY_KEY = "user_id";

	private long user_id;
	private String username;
	private int score;
	private byte status;
	private Date created;
	private Date updated;
	
	public TblCannonPlayerInfo() {
		this.user_id = 0;
		this.username = "";
		this.score = 0;
		this.status = 0;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}
	
	
}
