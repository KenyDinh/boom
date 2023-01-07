package dev.boom.game.card.poker;

public enum PokerTurn {
	PRE_FLOP((byte) 1, "", "", PokerDef.MAX_PLAYER_HOLD_CARD_NUM, 0),
	THE_FLOP((byte) 2, "", "", 0, PokerDef.MAX_CARD_NUM_IN_THE_FLOP),
	THE_TURN((byte) 3, "", "", 0, 1),
	THE_RIVER((byte) 4, "", "", 0, 1),
	SHOW_HAND((byte) 5, "", "", 0, 0),
	;
	
	private byte id;
	private String name;
	private String sprite;
	private int playerCard;
	private int sharedCard;

	private PokerTurn(byte id, String name, String sprite, int playerCard, int sharedCard) {
		this.id = id;
		this.name = name;
		this.sprite = sprite;
		this.playerCard = playerCard;
		this.sharedCard = sharedCard;
	}

	public byte getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getSprite() {
		return sprite;
	}
	public int getPlayerCard() {
		return playerCard;
	}
	public int getSharedCard() {
		return sharedCard;
	}
	
	public static PokerTurn valueOf(byte id) {
		for (PokerTurn pt : PokerTurn.values()) {
			if (pt.getId() == id) {
				return pt;
			}
		}
		return null;
	}
	
}
