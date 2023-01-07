package dev.boom.game.card.poker;

public enum PokerPlayerRole {
	NONE((byte) 0, "", ""),
	DEALER((byte) 1, "", ""),
	SMALL_BLIND((byte) 2, "", ""),
	BIG_BLIND((byte) 3, "", ""),
	UNDER_THE_GUN((byte) 4, "", ""),
	;
	
	private byte id;
	private String name;
	private String sprite;

	private PokerPlayerRole(byte id, String name, String sprite) {
		this.id = id;
		this.name = name;
		this.sprite = sprite;
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

	public int getBitMask() {
		if (getId() <= 0) {
			return 0;
		}
		return (1 << getId());
	}
}
