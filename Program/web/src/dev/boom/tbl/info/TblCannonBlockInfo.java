package dev.boom.tbl.info;

import dev.boom.dao.core.DaoValueInfo;

public class TblCannonBlockInfo extends DaoValueInfo {
	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "cannon_block_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long user_id;
	private String user_board;
	private String bot_board;
	private int user_hp;
	private int bot_hp;
	private byte status;

	public TblCannonBlockInfo() {
		this.id = 0;
		this.user_id = 0;
		this.user_board = "";
		this.bot_board = "";
		this.user_hp = 0;
		this.bot_hp = 0;
		this.status = 0;
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

	public String getUser_board() {
		return user_board;
	}

	public void setUser_board(String user_board) {
		this.user_board = user_board;
	}

	public String getBot_board() {
		return bot_board;
	}

	public void setBot_board(String bot_board) {
		this.bot_board = bot_board;
	}

	public int getUser_hp() {
		return user_hp;
	}

	public void setUser_hp(int user_hp) {
		this.user_hp = user_hp;
	}

	public int getBot_hp() {
		return bot_hp;
	}

	public void setBot_hp(int bot_hp) {
		this.bot_hp = bot_hp;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
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
