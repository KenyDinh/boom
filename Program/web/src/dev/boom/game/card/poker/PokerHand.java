package dev.boom.game.card.poker;

public enum PokerHand {
	NONE((byte) 0, 0, "", ""),
	FIVE_OF_A_KIND((byte) 1, 9999, "", ""),
	ROYAL_STRAINGHT_FLUSH((byte) 2, 8888, "", ""),
	STRAINGHT_FLUSH((byte) 3, 1000, "", ""),
	FOUR_OF_A_KIND((byte) 4, 800, "", ""),
	FULL_HOUSE((byte) 5, 700, "", ""),
	FLUSH((byte) 6, 600, "", ""),
	STRAIGHT((byte) 7, 500, "", ""),
	THREE_OF_A_KIND((byte) 8, 400, "", ""),
	TWO_PAIR((byte) 9, 300, "", ""),
	ONE_PAIR((byte) 10, 200, "", ""),
	HIGH_CARD((byte) 11, 100, "", ""),
	;
	
	private byte id;
	private int rank;
	private String label;
	private String sprite;

	private PokerHand(byte id, int rank, String label, String sprite) {
		this.id = id;
		this.rank = rank;
		this.label = label;
		this.sprite = sprite;
	}

	public byte getId() {
		return id;
	}

	public int getRank() {
		return rank;
	}

	public String getLabel() {
		return label;
	}

	public String getSprite() {
		return sprite;
	}
	
	public boolean isValid() {
		return !this.equals(PokerHand.NONE);
	}
	
	public static PokerHand valueOf(int id) {
		for (PokerHand hand : PokerHand.values()) {
			if (hand.getId() == id) {
				return hand;
			}
		}
		
		return PokerHand.NONE;
	}
	
}
