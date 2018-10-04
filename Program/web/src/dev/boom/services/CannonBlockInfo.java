package dev.boom.services;

import dev.boom.tbl.info.TblCannonBlockInfo;

public class CannonBlockInfo {
	private TblCannonBlockInfo tblCannonBlockInfo;
	
	public CannonBlockInfo(TblCannonBlockInfo cannonBlockInfo) {
		this.tblCannonBlockInfo = cannonBlockInfo;
	}

	// Setter methods
	public void setID(int id) {
		tblCannonBlockInfo.Set("id", id);
	}

	public void setUserID(int userId) {
		tblCannonBlockInfo.Set("user_id", userId);
	}

	public void setUserBoard(String userBoard) {
		tblCannonBlockInfo.Set("user_board", userBoard);
	}

	public void setBotBoard(String aiBoard) {
		tblCannonBlockInfo.Set("bot_board", aiBoard);
	}

	public void setUserHP(int userHp) {
		tblCannonBlockInfo.Set("user_hp", userHp);
	}

	public void setBotHP(int aiHp) {
		tblCannonBlockInfo.Set("bot_hp", aiHp);
	}

	public void setStatus(byte status) {
		tblCannonBlockInfo.Set("status", status);
	}

	// Getter methods
	public TblCannonBlockInfo getGameInfo() {
		return tblCannonBlockInfo;
	}

	public long getGameID() {
		return tblCannonBlockInfo.getId();
	}

	public long getUserID() {
		return tblCannonBlockInfo.getUser_id();
	}

	public String getUserBoard() {
		return tblCannonBlockInfo.getUser_board();
	}

	public String getBotBoard() {
		return tblCannonBlockInfo.getBot_board();
	}

	public int getUserHP() {
		return tblCannonBlockInfo.getUser_hp();
	}

	public int getBotHP() {
		return tblCannonBlockInfo.getBot_hp();
	}

	public byte getStatus() {
		return tblCannonBlockInfo.getStatus();
	}
	
	public boolean isEnded() {
		return (getStatus() == CannonBlockService.BOT_WIN_GAME || getStatus() == CannonBlockService.HUMAN_WIN_GAME);
	}
}
