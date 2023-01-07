package dev.boom.game.boom;

public enum BoomGameItemTargetType {
	
	NONE(0),
	BOMBER(1),
	MELEE(2),
	;
	
	private int id;

	private BoomGameItemTargetType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static BoomGameItemTargetType valueOf(int id) {
		for (BoomGameItemTargetType itemType : BoomGameItemTargetType.values()) {
			if (itemType.getId() == id) {
				return itemType;
			}
		}
		return BoomGameItemTargetType.NONE;
	}
}
