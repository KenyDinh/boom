package dev.boom.tbl.data;

import dev.boom.dao.core.DaoValueData;

public class TblCannonBlockData extends DaoValueData {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "cannon_block_data";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private int user_hp;
	private int bot_hp;
	private int damage;
	private int width_board;
	private int height_board;
	private int first_cannon_color;

	
	public TblCannonBlockData() {
		this.id = 0;
		this.user_hp = 0;
		this.bot_hp = 0;
		this.damage = 0;
		this.width_board = 0;
		this.height_board = 0;
		this.first_cannon_color = 0;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getWidth_board() {
		return width_board;
	}

	public void setWidth_board(int width_board) {
		this.width_board = width_board;
	}

	public int getHeight_board() {
		return height_board;
	}

	public void setHeight_board(int height_board) {
		this.height_board = height_board;
	}

	public int getFirst_cannon_color() {
		return first_cannon_color;
	}

	public void setFirst_cannon_color(int first_cannon_color) {
		this.first_cannon_color = first_cannon_color;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

}
