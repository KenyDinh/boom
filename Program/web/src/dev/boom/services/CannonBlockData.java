package dev.boom.services;

import dev.boom.tbl.data.TblCannonBlockData;

public class CannonBlockData {

	private TblCannonBlockData tblCannonBlockData;
	
	public CannonBlockData(TblCannonBlockData tblCannonBlockData) {
		this.tblCannonBlockData = tblCannonBlockData;
	}

	public short getId() {
		return (Short) tblCannonBlockData.Get("id");
	}

	public int getUserHp() {
		return (Integer) tblCannonBlockData.Get("user_hp");
	}

	public int getBotHp() {
		return (Integer) tblCannonBlockData.Get("bot_hp");
	}

	public int getDamage() {
		return (Integer) tblCannonBlockData.Get("damage");
	}

	public int getWidthBoard() {
		return (Integer) tblCannonBlockData.Get("width_board");
	}

	public int getHeightBoard() {
		return (Integer) tblCannonBlockData.Get("height_board");
	}

	public int getCannonColor() {
		return (Integer) tblCannonBlockData.Get("first_cannon_color");
	}
}
