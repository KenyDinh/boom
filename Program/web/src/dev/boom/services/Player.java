package dev.boom.services;

import dev.boom.tbl.info.TblChessPlayerInfo;

public class Player {

	private TblChessPlayerInfo chessPlayerInfo;

	public Player() {
		chessPlayerInfo = new TblChessPlayerInfo();
	}

	public Player(TblChessPlayerInfo chessPlayerInfo) {
		this.chessPlayerInfo = chessPlayerInfo;
	}

	public long getId() {
		return (Long) chessPlayerInfo.Get("id");
	}

	public String getName() {
		return (String) chessPlayerInfo.Get("name");
	}

	public TblChessPlayerInfo getChessPlayer() {
		return chessPlayerInfo;
	}
}
