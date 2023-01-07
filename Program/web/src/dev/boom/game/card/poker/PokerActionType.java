package dev.boom.game.card.poker;

public enum PokerActionType {
	NONE(),
	BET(),
	RAISE(),
	CALL(),
	ALL_IN(),
	FOLD(),
	;
	
	public static PokerActionType valueOf(int id) {
		for (PokerActionType type : PokerActionType.values()) {
			if (type.ordinal() == id) {
				return type;
			}
		}
		
		return PokerActionType.NONE;
	}
}
