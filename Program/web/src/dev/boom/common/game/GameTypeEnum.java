package dev.boom.common.game;

import java.util.ArrayList;
import java.util.List;

public enum GameTypeEnum {
	NONE(0, 0, "", "", ""), 
	NIHONGO(1, 1, "MSG_GAME_TYPE_NIHONGO", "img/game/nihongo/nihongo_game.jpg", "game/nihongo.htm"),
	CANNONBLOCK(1, 1, "MSG_GAME_TYPE_CANNONBLOCK", "img/game/nihongo/nihongo_game.jpg", "game/cannon_block/cannon_block_menu.htm"),
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
	
	public static List<GameTypeEnum> listValidGame() {
		List<GameTypeEnum> ret = new ArrayList<>();
		for (GameTypeEnum type : GameTypeEnum.values()) {
			if (type == GameTypeEnum.NONE) {
				continue;
			}
			ret.add(type);
		}
		return ret;
	}
}
