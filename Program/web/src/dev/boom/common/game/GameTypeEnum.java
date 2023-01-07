package dev.boom.common.game;

import java.util.ArrayList;
import java.util.List;

import dev.boom.services.WorldInfo;

public enum GameTypeEnum {
	NONE(0, 0, "", "", ""), 
	NIHONGO(1, 1, "MSG_GAME_TYPE_NIHONGO", "img/game/nihongo/nihongo_game.jpg", "game/nihongo.htm"),
	SUDOKU(2, 1, "MSG_GAME_TYPE_SUDOKU", "img/game/sudoku/sudoku_game.png", "game/sudoku.htm"),
	BOOM(3, 1, "MSG_GAME_TYPE_BOOM", "img/game/boom/boom_game.jpg", "game/boom.htm"),
	POKER(4, 1, "MSG_GAME_TYPE_POKER", "img/game/poker/poker_game.png", "game/poker.htm"),
	;

	private int index;
	private int category;
	private String label;
	private String image;
	private String page;

	private GameTypeEnum(int index, int category, String label, String image, String page) {
		this.index = index;
		this.category = category;
		this.label = label;
		this.image = image;
		this.page = page;
	}

	public int getIndex() {
		return index;
	}

	public int getCategory() {
		return category;
	}

	public String getLabel() {
		return label;
	}

	public String getImage() {
		return image;
	}

	public String getPage() {
		return page;
	}

	public static GameTypeEnum valueOf(int index) {
		for (GameTypeEnum type : GameTypeEnum.values()) {
			if (type.getIndex() == index) {
				return type;
			}
		}
		return GameTypeEnum.NONE;
	}
	
	public static List<GameTypeEnum> listValidGame(WorldInfo worldInfo) {
		List<GameTypeEnum> ret = new ArrayList<>();
		for (GameTypeEnum type : GameTypeEnum.values()) {
			if (type == GameTypeEnum.NONE || !worldInfo.isActiveGameFlag(type)) {
				continue;
			}
			ret.add(type);
		}
		return ret;
	}
}
