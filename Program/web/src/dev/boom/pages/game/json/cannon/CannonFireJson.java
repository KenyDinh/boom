package dev.boom.pages.game.json.cannon;

import java.util.ArrayList;
import java.util.List;

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

public class CannonFireJson extends JsonPageBase{
	private static final long serialVersionUID = 1L;
	private CannonPlayerInfo cannonPlayerInfo;
	private CannonBlockInfo cannonBlockInfo;
	private boolean initError = false;
	private int boardWidth;
	private int boardHeight;
	private ArrayList<Cannon> arrCannons;

	public CannonBlockData cannonBlockData;

	public CannonFireJson() {
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
		super.onInit();
		cannonBlockData = CannonBlockDataService.getCannonBlockDataById((short) 1);
		boardWidth = cannonBlockData.getWidthBoard();
		boardHeight = cannonBlockData.getHeightBoard();
		arrCannons = new ArrayList<Cannon>();
//		String token = getContext().getRequestParameter("token");
//		if (token != null) {
//			long tokenID = Long.parseLong(token.substring(0,token.indexOf(" ")));
//			cannonPlayerInfo = UserService.getUser(tokenID);
//
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
//
//		} else {
		cannonPlayerInfo = CannonPlayerService.getCannonPlayerInfo(getUserInfo().getId());
//		}

		if (cannonPlayerInfo == null) {
			initError = true;
			putJsonData("status", -1);
			return;
		}

		cannonBlockInfo = CannonBlockService.getCannonBlockInfo(cannonPlayerInfo.getUserId(), CannonBlockService.GAME_IS_PLAYING);
	}

	@Override
	public void onPost() {
		if (initError) {
			return;
		}

		super.onPost();
		boolean updateDB = false;

		if (cannonBlockInfo == null) {
			// Failed to read game_info from DB.
			putJsonData("status", -1);
			return;
		}

		String userBoard = cannonBlockInfo.getUserBoard();
		int damage = damageByPlayer(userBoard);
		if (damage == 0) {
			putJsonData("status", -1);
			return;
		}
		String newUserBoard = randomNewBoard(userBoard);
		String aiBoard = cannonBlockInfo.getBotBoard();

		String strResult = canculateBotBoard(drawAiBoard(aiBoard));
		String newAiBoard = strResult.substring(0,CannonBlockService.NUM_ROW);
		String strFireByBot = strResult.substring(CannonBlockService.NUM_ROW, CannonBlockService.NUM_ROW*2);

		BotFireResult aiFireResult = fireByBot(newUserBoard, newAiBoard);
		newUserBoard = aiFireResult.getFirst();
		int numberOfRedCell = 0;
		for (int i = 0; i < newUserBoard.length(); i++) {
			if (newUserBoard.charAt(i) == CannonBlockService.RED_CELL_CHAR) {
				numberOfRedCell++;
			}
		}
		int aiHP = cannonBlockInfo.getBotHP() - damage;
		int userHP = cannonBlockInfo.getUserHP() - numberOfRedCell;

		byte status = CannonBlockService.GAME_IS_PLAYING;
		List<DaoValue> values = new ArrayList<DaoValue>();
//		cannonPlayerInfo.setLastLogin(CommonMethod.getFormatString(new Date()));
		if (aiHP <= 0) {
			status = CannonBlockService.HUMAN_WIN_GAME;
			cannonPlayerInfo.setStatus(CannonBlockService.NONE_GAME);
//			ScoreBoardInfo scoreBoardInfo = CommonMethod.updateScore(cannonPlayerInfo, CannonBlockService.HUMAN_WIN_GAME);
//			values.add(scoreBoardInfo.getScoreBoardInfo());
		} else if (aiHP > 0 && userHP <= 0) {
			status = CannonBlockService.BOT_WIN_GAME;
			cannonPlayerInfo.setStatus(CannonBlockService.NONE_GAME);
		}
		putJsonData("fireBot", strFireByBot);
		putJsonData("userBoard", newUserBoard);
		putJsonData("aiBoard", newAiBoard);

		if (userHP >= 0) {
			putJsonData("userHP", userHP);
		} else {
			putJsonData("userHP", CannonBlockService.NONE_HP);
		}
		if (aiHP >= 0) {
			putJsonData("aiHP", aiHP);
		} else {
			putJsonData("aiHP", CannonBlockService.NONE_HP);
		}
		putJsonData("gameStatus", status);

		String strCannonFire = "";
		for (int i = 0; i < arrCannons.size(); i++) {
			strCannonFire += String.valueOf(arrCannons.get(i).getDamage());
		}
		putJsonData("cannonFire", strCannonFire);

		if (cannonBlockInfo.getStatus() == CannonBlockService.GAME_IS_PLAYING) {
			cannonBlockInfo.setBotBoard(newAiBoard);
			cannonBlockInfo.setUserBoard(newUserBoard);
			if (aiHP >= 0) {
				cannonBlockInfo.setBotHP(aiHP);
			} else {
				cannonBlockInfo.setBotHP(CannonBlockService.NONE_HP);
			}
			if (userHP >= 0) {
				cannonBlockInfo.setUserHP(userHP);
			} else {
				cannonBlockInfo.setUserHP(CannonBlockService.NONE_HP);
			}
			cannonBlockInfo.setStatus(status);
			updateDB = true;
		}

		// Update the DB
		if (updateDB) {
			values.add(cannonBlockInfo.getGameInfo());
			values.add(cannonPlayerInfo.getTblCannonPlayerInfo());

			if (CommonDaoService.update(values)) {
				// update DB successfully
				putJsonData("status", 0);
				return;
			}
		}

		// generic error occurred.
		putJsonData("status", -1);
	}

	public int damageByPlayer(String userBoard) {
		int result = 0;
		int cannonPosition = 0;
		for (int i = 0; i < userBoard.length(); i = i + boardWidth) {
			Cannon cannon = getEachCannon(userBoard.substring(i, i + boardWidth), cannonPosition);
			cannon.setPosition(cannonPosition);
			arrCannons.add(cannon);
			cannonPosition++;
		}

		for (int i = 0; i < arrCannons.size(); i++) {
			result += arrCannons.get(i).getDamage();
		}
		return result;
	}

	public Cannon getEachCannon(String cannonStatus, int cannonPosition) {
		int damage = 0;
		int[] arrCannon = CannonSwapJson.getCannonsStatus(cannonBlockData);
		int cannonColor = arrCannon[cannonPosition];

		boolean flag = false;
		int length = boardWidth;

		if (Integer.parseInt(cannonStatus.substring(boardWidth - 1, boardWidth)) != cannonColor) {
			damage = 0;
		} else {
			flag = true;
			damage++;
			length--;
		}

		if (flag) {
			for (int i = cannonStatus.length() - 1; i > 0; i--) {
				if (Integer.parseInt(cannonStatus.substring(i - 1, i)) == cannonColor) {
					flag = true;
					damage++;
					length--;
				} else if (Integer.parseInt(cannonStatus.substring(i - 1, i)) == CannonBlockService.RED_CELL) {
					damage *= CannonBlockService.MULTI_DAMAGE_BY_RED_CANNON;
					length--;
					break;
				} else {
					break;
				}
			}
		}

		Cannon cannon = new Cannon(cannonStatus.substring(0, length), length, damage);
		return cannon;
	}

	public String randomNewBoard(String userBoard) {
		String str = "";

		for (int i = 0; i < boardHeight; i++) {
			String randomString = randomCellInCannon(arrCannons.get(i).getStrCannon(), arrCannons.get(i).getLength());
			str += randomString;
		}
		return str;
	}

	public String randomCellInCannon(String strRemainCell, int cannonLength) {
		String str = "";
		for (int i = 0; i < boardWidth - cannonLength; i++) {
			str += String.valueOf(CommonMethod.randomNumber(CannonBlockService.BLUE_CELL, CannonBlockService.GREEN_CELL));
		}
		return str + strRemainCell;
	}

	public String drawAiBoard(String aiBoard) {
		String str = "";
		char arrChar[] = aiBoard.toCharArray();

		for (int i = 0; i < arrCannons.size(); i++) {
			if (arrCannons.get(i).getDamage() != 0) {
				arrChar[i] = CannonBlockService.TIME_FIRE_NONE + 1;
			}
		}
		str = new String(arrChar);
		return str;
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

	public BotFireResult fireByBot(String userBoard, String aiBoard) {
		int[] iBotBoard = new int[aiBoard.length()];
		int[] countRedCell = new int[aiBoard.length()];
		int damage = 0;
		for (int i = 0; i < aiBoard.length(); i++) {
			int randomPosition;
			countRedCell[i] = 0;
			iBotBoard[i] = Integer.parseInt(aiBoard.substring(i, i + 1));
			if (iBotBoard[i] == boardWidth && arrCannons.get(i).getDamage() == 0) {
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

	class Cannon {
		private String strCannon;
		private int length;
		private int damage;
		private int position;

		public Cannon(String strCannon, int length, int damage) {
			this.strCannon = strCannon;
			this.length = length;
			this.damage = damage;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public String getStrCannon() {
			return strCannon;
		}

		public void setStrCannon(String strCannon) {
			this.strCannon = strCannon;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public int getDamage() {
			return damage;
		}

		public void setDamage(int damge) {
			this.damage = damge;
		}

	}
}
