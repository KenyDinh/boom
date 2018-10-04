package dev.boom.services;

import java.util.Date;

import dev.boom.tbl.info.TblCannonPlayerInfo;

public class CannonPlayerInfo {
	private TblCannonPlayerInfo tblCannonPlayerInfo;

	public CannonPlayerInfo(TblCannonPlayerInfo cannonPlayerInfo) {
		this.tblCannonPlayerInfo = cannonPlayerInfo;
	}
	
	public CannonPlayerInfo() {
		this.tblCannonPlayerInfo = new TblCannonPlayerInfo();
	}

	// Setter methods
	public void setUserId(long userId) {
		tblCannonPlayerInfo.Set("user_id", userId);
	}

	public void setUsername(String username) {
		tblCannonPlayerInfo.Set("username", username);
	}

	public void setScore(int score) {
		tblCannonPlayerInfo.Set("score", score);
	}

	public void setStatus(byte status) {
		tblCannonPlayerInfo.Set("status", status);
	}

	public void setCreated(Date created) {
		tblCannonPlayerInfo.Set("created", created);
	}

	public void setUpdated(Date updated) {
		tblCannonPlayerInfo.Set("updated", updated);
	}

	public long getUserId() {
		return this.tblCannonPlayerInfo.getUser_id();
	}

	public String getUsername() {
		return this.tblCannonPlayerInfo.getUsername();
	}

	public int getScore() {
		return this.tblCannonPlayerInfo.getScore();
	}

	public byte getStatus() {
		return this.tblCannonPlayerInfo.getStatus();
	}

	public Date getCreated() {
		return this.tblCannonPlayerInfo.getCreated();
	}

	public Date getUpdated() {
		return this.tblCannonPlayerInfo.getUpdated();
	}

	public TblCannonPlayerInfo getTblCannonPlayerInfo() {
		return this.tblCannonPlayerInfo;
	}
}
