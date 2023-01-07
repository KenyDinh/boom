package dev.boom.game.card;

public enum CardType {
	NONE((byte) -1, ""),
	SPADES((byte) 0, ""),
	CLUBS((byte) 1, ""),
	DIAMONDS((byte) 2, ""),
	HEARTS((byte) 3, ""),
	;
	
	public static final CardType BLACK_JOKER = CLUBS;
	public static final CardType RED_JOKER = HEARTS;
	
	private byte id;
	private String sprite;
	
	private CardType(byte id, String sprite) {
		this.id = id;
		this.sprite = sprite;
	}

	public byte getId() {
		return id;
	}

	public String getSprite() {
		return sprite;
	}
	
	public boolean isValid() {
		return !this.equals(CardType.NONE);
	}
	
	public static CardType valueOf(byte id) {
		for (CardType type : CardType.values()) {
			if (type.getId() == id) {
				return type;
			}
		}
		return CardType.NONE;
	}
	
}
