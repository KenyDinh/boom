package dev.boom.pages.game.json.cannon;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonMethod;
import dev.boom.dao.core.DaoValue;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.CannonBlockData;
import dev.boom.services.CannonBlockDataService;
import dev.boom.services.CannonBlockInfo;
import dev.boom.services.CannonBlockService;
import dev.boom.services.CannonPlayerInfo;
import dev.boom.services.CannonPlayerService;
import dev.boom.services.CommonDaoService;

public class CannonSwapJson extends JsonPageBase {
	private static final long serialVersionUID = 1L;
	private CannonPlayerInfo cannonPlayerInfo;
	private boolean initError = false;
	private CannonBlockInfo cannonBlockInfo;
	private CannonBlockData cannonBlockData;
	private int boardWidth;
	private static final int DISTANCE_CELL = 1;

	public CannonSwapJson() {
	}

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!initUserInfo()) {
			return false;
		}
		
		return true;
	}

	public void onInit() {
//		String token = getContext().getRequestParameter("token");
		String firstPos = getContext().getRequestParameter("firstPos");
		String secondPos = getContext().getRequestParameter("secondPos");
		super.onInit();
		cannonBlockData = CannonBlockDataService.getCannonBlockDataById((short) 1);
//		if (token != null) {
//			long tokenID = Long.parseLong(token.substring(0,token.indexOf(" ")));
//			cannonPlayerInfo = UserService.getUser(tokenID);
//			if(!CommonMethod.checkTimeOutToken(cannonPlayerInfo.getUpdated())) {
//				putJsonData("time_out_token", CannonBlockService.NONE_GAME);
//				return;
//			}
//			putJsonData("time_out_token", CannonBlockService.GAME_IS_PLAYING);
//
//			if (cannonPlayerInfo != null) {
//				String token1 = cannonPlayerInfo.getUser();
//				putJsonData("token", token1);
//			}
//		} else {
		cannonPlayerInfo = CannonPlayerService.getCannonPlayerInfo(getUserInfo().getId()); 
//		}

		if (cannonPlayerInfo == null) {
			initError = true;
			putJsonData("status", -1);
			return;
		}
		cannonBlockInfo = CannonBlockService.getCannonBlockInfo(cannonPlayerInfo.getUserId(), CannonBlockService.GAME_IS_PLAYING);
		if (cannonBlockInfo == null) {
			initError = true;
			putJsonData("status", -1);
			return;
		}

		if (cannonBlockInfo != null) {
			if (firstPos == null && secondPos == null) {
				putJsonData("userBoard", cannonBlockInfo.getUserBoard());
				putJsonData("aiBoard", cannonBlockInfo.getBotBoard());
				putJsonData("userHP", cannonBlockInfo.getUserHP());
				putJsonData("aiHP", cannonBlockInfo.getBotHP());
				putJsonData("gameStatus", cannonBlockInfo.getStatus());
				putJsonData("firstColor", cannonBlockData.getCannonColor());
			}

		}

		boardWidth = cannonBlockData.getWidthBoard();

	}

	@Override
	public void onGet() {
		if (initError) {
			return;
		}
		// Get the game board data from DB
		if (cannonBlockInfo != null) {
			putJsonData("userBoard", cannonBlockInfo.getUserBoard());
			putJsonData("aiBoard", cannonBlockInfo.getBotBoard());
			putJsonData("userHP", cannonBlockInfo.getUserHP());
			putJsonData("aiHP", cannonBlockInfo.getBotHP());
			putJsonData("gameStatus", cannonBlockInfo.getStatus());
			putJsonData("firstColor", cannonBlockData.getCannonColor());
		}
	}

	@Override
	public void onPost() {
		if (initError) {
			return;
		}
		super.onPost();

		// Read the data posted from ajax call in game_play.js
		// data: "firstPos" + "secondPos"
		String firstPos = getContext().getRequestParameter("firstPos");
		String secondPos = getContext().getRequestParameter("secondPos");
		boolean updateDB = false;

		if (cannonBlockInfo == null || cannonBlockInfo.isEnded()) {
			// failed to read data
			putJsonData("status", -1);
			return;
		}
		if (!StringUtils.isEmpty(firstPos) && StringUtils.isNumeric(firstPos) && !StringUtils.isEmpty(secondPos) && StringUtils.isNumeric(secondPos)) {
			try {
				byte pos1 = Byte.parseByte(firstPos);
				pos1--;
				byte pos2 = Byte.parseByte(secondPos);
				pos2--;
				if (pos1 == pos2) {
					putJsonData("status", -1);
					return;
				}

				if (checkCellIsBlocked(pos1) || checkCellIsBlocked(pos2)) {
					putJsonData("status", -1);
					return;
				}

				if ((!checkSwap(pos1, pos2))) {
					putJsonData("status", -1);
					return;
				}

				String strUserBoard = cannonBlockInfo.getUserBoard();
				String strBotBoard = cannonBlockInfo.getBotBoard();
				char[] c = strUserBoard.toCharArray();

				if (c[pos1] == CannonBlockService.RED_CELL_CHAR) {
					if (checkRedCellIsBlocked(pos1)) {
						putJsonData("status", -1);
						return;
					}
				}

				if (c[pos2] == CannonBlockService.RED_CELL_CHAR) {
					if (checkRedCellIsBlocked(pos2)) {
						putJsonData("status", -1);
						return;
					}
				}

				if (c[pos1] == c[pos2]) {
					putJsonData("status", -1);
					return;
				}
				char temp = c[pos1];
				c[pos1] = c[pos2];
				c[pos2] = temp;
				strUserBoard = new String(c);
				// calculation Red Cell on user Board
				int numberOfRedCell = 0;
				for (int i = 0; i < c.length; i++) {
					if (c[i] == CannonBlockService.RED_CELL_CHAR) {
						numberOfRedCell++;
					}
				}
				// calculation ai board
				String strResult = canculateBotBoard(strBotBoard);
				strBotBoard = strResult.substring(0,CannonBlockService.NUM_ROW);
				String strFireByBot = strResult.substring(CannonBlockService.NUM_ROW, CannonBlockService.NUM_ROW*2);

				// Get New UserBoard because appears random RedCell afet Bot fire
				BotFireResult aiFireResult = fireByBot(strUserBoard, strBotBoard);
				strUserBoard = aiFireResult.getFirst();

				int userHP = cannonBlockInfo.getUserHP() - aiFireResult.getSecond() - numberOfRedCell;
				int aiHP = cannonBlockInfo.getBotHP();
				byte gameStatus = CannonBlockService.GAME_IS_PLAYING;
//				cannonPlayerInfo.setLastLogin(CommonMethod.getFormatString(new Date()));
				if (aiHP <= 0 && userHP > 0) {
					gameStatus = CannonBlockService.HUMAN_WIN_GAME;
					cannonPlayerInfo.setStatus(CannonBlockService.NONE_GAME);
				} else if (aiHP > 0 && userHP <= 0) {
					gameStatus = CannonBlockService.BOT_WIN_GAME;
					cannonPlayerInfo.setStatus(CannonBlockService.NONE_GAME);
				}

				putJsonData("fireBot", strFireByBot);
				putJsonData("userBoard", strUserBoard);
				putJsonData("aiBoard", strBotBoard);
				if (userHP >= 0) {
					putJsonData("userHP", userHP);
				} else {
					putJsonData("userHP", CannonBlockService.NONE_HP);
				}
				putJsonData("aiHP", cannonBlockInfo.getBotHP());
				putJsonData("gameStatus", gameStatus);

				if (cannonBlockInfo.getStatus() == CannonBlockService.GAME_IS_PLAYING) {
					cannonBlockInfo.setUserBoard(strUserBoard);
					cannonBlockInfo.setBotBoard(strBotBoard);
					if (userHP >= 0) {
						cannonBlockInfo.setUserHP(userHP);
					} else {
						cannonBlockInfo.setUserHP(CannonBlockService.NONE_HP);
					}
					cannonBlockInfo.setStatus(gameStatus);
					updateDB = true;
				}

			} catch (NumberFormatException e) {
				putJsonData("status", -1);
				return;
			}
		}
		// Update the DB
		if (updateDB) {
			List<DaoValue> values = new ArrayList<DaoValue>();
			values.add(cannonBlockInfo.getGameInfo());
			values.add(cannonPlayerInfo.getTblCannonPlayerInfo());

			if (CommonDaoService.update(values)) {
				// update DB successfully
				putJsonData("status", 0);
				return;
			}
		}
	}

	public boolean checkSwap(byte pos1, byte pos2) {
		byte[] arr1 = convertPosition(pos1);
		byte[] arr2 = convertPosition(pos2);
		byte x1, y1, x2, y2;
		x1 = arr1[0];
		y1 = arr1[1];
		x2 = arr2[0];
		y2 = arr2[1];

		if (x1 == x2) {
			if (y2 - y1 == DISTANCE_CELL || y1 - y2 == DISTANCE_CELL) {
				return true;
			}
		}

		if (y1 == y2) {
			if (x2 - x1 == DISTANCE_CELL || x1 - x2 == DISTANCE_CELL) {
				return true;
			}
		}

		return false;
	}

	public byte[] convertPosition(byte position) {
		byte[] arr = new byte[2];
		byte x, y;

		x = (byte) ((position) / boardWidth);
		y = (byte) ((position) % boardWidth);
		arr[0] = x;
		arr[1] = y;
		return arr;
	}

	public String canculateBotBoard(String aIBoard) {
		String result = "";
		String strFire = "";
		for (int i = 0; i < aIBoard.length(); i++) {
			int temp = Integer.parseInt(aIBoard.substring(i, i + 1));
			if (temp > CannonBlockService.MIN_TIME_FIRE) {
				temp = temp - 1;
				strFire += "0";
			} else {
				strFire += "1";
				temp = boardWidth;
			}
			result += String.valueOf(temp);
		}
		return result+strFire;
	}

	public boolean checkCellIsBlocked(byte position) {
		int[] arrCannon = getCannonsStatus(cannonBlockData);

		byte[] arrCoo = convertPosition(position);
		byte x = arrCoo[0];
		byte y = arrCoo[1];
		byte distance = (byte) (boardWidth - y);

		int valuePosition = Integer.parseInt(cannonBlockInfo.getUserBoard().substring(position, position + 1));

		if (distance == DISTANCE_CELL) {
			if (valuePosition == arrCannon[(int) x]) {
				return true;
			}
		} else {
			boolean flag = false;
			for (byte i = 1; i < distance; i++) {
				if (cannonBlockInfo.getUserBoard().charAt(position) != cannonBlockInfo.getUserBoard().charAt(position + i)) {
					flag = false;
					return false;
				} else {
					flag = true;
				}
			}
			if (flag) {
				if (valuePosition == arrCannon[(int) x]) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean checkRedCellIsBlocked(byte position) {
		byte[] arrCoo = convertPosition(position);
		byte y = arrCoo[1];
		byte distance = (byte) (boardWidth - y);

		if (distance == DISTANCE_CELL) {
			return false;
		} else {
			if (checkCellIsBlocked((byte) (position + 1))) {
				return true;
			}
		}
		return false;
	}

	public BotFireResult fireByBot(String userBoard, String aiBoard) {
		int[] iBotBoard = new int[aiBoard.length()];
		int[] countRedCell = new int[aiBoard.length()];
		int damage = 0;
		for (int i = 0; i < aiBoard.length(); i++) {
			int randomPosition;
			countRedCell[i] = 0;
			iBotBoard[i] = Integer.parseInt(aiBoard.substring(i, i + 1));
			if (iBotBoard[i] == boardWidth) {
				for (int j = i * boardWidth; j < (i * boardWidth + boardWidth); j++) {
					if (userBoard.charAt(j) == CannonBlockService.RED_CELL_CHAR) {
						countRedCell[i]++;
					}
				}
				do {
					randomPosition = CommonMethod.randomNumber(i * boardWidth, i * boardWidth + boardWidth - 1);
				} while (userBoard.charAt(randomPosition) == CannonBlockService.RED_CELL_CHAR && countRedCell[i] < boardWidth);
				userBoard = userBoard.substring(0, randomPosition) + CannonBlockService.RED_CELL_CHAR + userBoard.substring(randomPosition + 1);
				damage++;
			}
		}
		BotFireResult result = new BotFireResult(userBoard, damage);
		return result;
	}

	public static int[] getCannonsStatus(CannonBlockData cannonBlockData) {
		int[] arrCannon = new int[cannonBlockData.getHeightBoard()];

		if (cannonBlockData.getCannonColor() == CannonBlockService.BLUE_CELL) {
			for (int j = 0; j < arrCannon.length; j++) {
				if (j % 2 == 0) {
					arrCannon[j] = CannonBlockService.BLUE_CELL;
				} else {
					arrCannon[j] = CannonBlockService.GREEN_CELL;
				}
			}
		} else {
			for (int j = 0; j < arrCannon.length; j++) {
				if (j % 2 == 0) {
					arrCannon[j] = CannonBlockService.GREEN_CELL;
				} else {
					arrCannon[j] = CannonBlockService.BLUE_CELL;
				}
			}
		}

		return arrCannon;
	}

	class BotFireResult {
		private final String first;
		private final int second;

		public BotFireResult(String first, int second) {
			this.first = first;
			this.second = second;
		}

		public String getFirst() {
			return first;
		}

		public int getSecond() {
			return second;
		}
	}
}
