package dev.boom.tbl.info;

import dev.boom.dao.core.DaoValueInfo;

public class TblDemoSignupInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "demo_signup_info";
	private static final String PRIMARY_KEY = "id";

	private int id;
	private String game_name;
	private String speaker_name;
	private String description;
	private int flag;

	public TblDemoSignupInfo() {
		this.id = 0;
		this.game_name = "";
		this.speaker_name = "";
		this.flag = 0;
		Sync();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGame_name() {
		return game_name;
	}

	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}

	public String getSpeaker_name() {
		return speaker_name;
	}

	public void setSpeaker_name(String speaker_name) {
		this.speaker_name = speaker_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
