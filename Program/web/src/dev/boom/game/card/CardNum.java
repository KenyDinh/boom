package dev.boom.game.card;

public enum CardNum {
	NONE((byte) -1, ""),
	JOKER((byte) 0, ""),
	ACE((byte) 1, ""),
	TWO((byte) 2, ""),
	THREE((byte) 3, ""),
	FOUR((byte) 4, ""),
	FIVE((byte) 5, ""),
	SIX((byte) 6, ""),
	SEVEN((byte) 7, ""),
	EIGHT((byte) 8, ""),
	NINE((byte) 9, ""),
	TEN((byte) 10, ""),
	JACK((byte) 11, ""),
	QUEEN((byte) 12, ""),
	KING((byte) 13, ""),
	;
	
	private byte id;
	private String sprite;
	
	private CardNum(byte id, String sprite) {
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
		return !this.equals(CardNum.NONE);
	}
	
	public static CardNum valueOf(byte id) {
		for (CardNum num : CardNum.values()) {
			if (num.getId() == id) {
				return num;
			}
		}
		return CardNum.NONE;
	}
}
