package dev.boom.tbl.info;

import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblWorldInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "world_info";
	private static final String PRIMARY_KEY = "id";
	
	private long id;
	private int event_flag;
	private int game_flag;
	
	public TblWorldInfo() {
		this.id = 0;
		this.event_flag = 0;
		this.game_flag = 0;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getEvent_flag() {
		return event_flag;
	}

	public void setEvent_flag(int event_flag) {
		this.event_flag = event_flag;
	}

	public int getGame_flag() {
		return game_flag;
	}

	public void setGame_flag(int game_flag) {
		this.game_flag = game_flag;
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
