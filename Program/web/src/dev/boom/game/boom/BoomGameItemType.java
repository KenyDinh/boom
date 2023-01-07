package dev.boom.game.boom;

public enum BoomGameItemType {
	
	NONE(0),
	NORMAL(1),
	NEGATIVE(2),
	HIDDEN(3),
	;
	
	private int id;

	private BoomGameItemType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static BoomGameItemType valueOf(int id) {
		for (BoomGameItemType itemType : BoomGameItemType.values()) {
			if (itemType.getId() == id) {
				return itemType;
			}
		}
		return BoomGameItemType.NONE;
	}
}
