package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblCannonBlockInfo;

public class CannonBlockService {
	public static final byte NONE_GAME = 0;
	public static final byte GAME_IS_PLAYING = 1;
	public static final byte HUMAN_WIN_GAME = 2;
	public static final byte BOT_WIN_GAME = 3;
	
	public static final int BLUE_CELL = 1;
	public static final int GREEN_CELL = 2;
	public static final int RED_CELL = 3;
	public static final int MIN_TIME_FIRE = 1;
	public static final int MAX_TIME_FIRE = 4;
	public static final char RED_CELL_CHAR = '3';
	public static final int MULTI_DAMAGE_BY_RED_CANNON = 2;
	public static final char TIME_FIRE_NONE = '4';
	public static final int NONE_HP = 0;
	public static final int NUM_ROW = 7;
	public static final int MAX_USER_NAME_LENGTH = 30;
	public static final int MAX_REQUEST_TIME_OUT = 1000;
	
	public CannonBlockService() {

	}

	public static CannonBlockInfo getCannonBlockInfo(long userID, byte status) {
		TblCannonBlockInfo self = new TblCannonBlockInfo();
		self.Set("user_id", userID);
		self.Set("status", status);

		// Select a row from game_board_info by user id
		List<DaoValue> listResult = CommonDaoService.select(self);
		if (listResult == null || listResult.isEmpty()) {
			return null;
		}

		// expect only 1 row per user
		if (listResult.size() != 1) {
			return null;
		}

		return new CannonBlockInfo((TblCannonBlockInfo) listResult.get(0));
	}

	public static boolean createNewGame(UserInfo userInfo, CannonPlayerInfo cannonPlayerInfo, CannonBlockData cannonBlockData) {
		if (userInfo == null) {
			return false;
		}
		TblCannonBlockInfo selfGame = new TblCannonBlockInfo();
		selfGame.Set("user_id", userInfo.getId());
		selfGame.Set("user_board", randomUserBoard(cannonBlockData));
		selfGame.Set("bot_board", randomBotBoard(cannonBlockData));
		selfGame.Set("user_hp", cannonBlockData.getUserHp());
		selfGame.Set("bot_hp", cannonBlockData.getBotHp());
		selfGame.Set("status", GAME_IS_PLAYING);
		if (cannonPlayerInfo == null) {
			cannonPlayerInfo = new CannonPlayerInfo();
			cannonPlayerInfo.setUserId(userInfo.getId());
			cannonPlayerInfo.setUsername(userInfo.getUsername());
		}
		cannonPlayerInfo.setStatus(GAME_IS_PLAYING);

		List<DaoValue> value = new ArrayList<DaoValue>();
		value.add(selfGame);
		value.add(cannonPlayerInfo.getTblCannonPlayerInfo());

		if (CommonDaoService.update(value)) {
			return true;
		}
		return false;
	}

	public static String randomUserBoard(CannonBlockData cannonBlockData) {
		String strUserBoard = "";
		for (int i = 0; i < cannonBlockData.getHeightBoard() * cannonBlockData.getWidthBoard(); i++) {
			strUserBoard += String.valueOf(CommonMethod.randomNumber(BLUE_CELL, GREEN_CELL));
		}
		return strUserBoard;
	}

	public static String randomBotBoard(CannonBlockData cannonBlockData) {
		String strUserBoard = "";
		for (int i = 0; i < cannonBlockData.getHeightBoard(); i++) {
			strUserBoard += String
					.valueOf(CommonMethod.randomNumber(MIN_TIME_FIRE, MAX_TIME_FIRE));
		}
		return strUserBoard;
	}
}
