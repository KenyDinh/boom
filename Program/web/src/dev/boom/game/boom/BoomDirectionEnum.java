package dev.boom.game.boom;

public enum BoomDirectionEnum {

	UP,
	DOWN,
	LEFT,
	RIGHT,
	;
	
	public static BoomDirectionEnum reverse(BoomDirectionEnum direction) {
		switch (direction) {
		case UP:
			return BoomDirectionEnum.DOWN;
		case DOWN:
			return BoomDirectionEnum.UP;
		case LEFT:
			return BoomDirectionEnum.RIGHT;
		case RIGHT:
			return BoomDirectionEnum.LEFT;
		default:
			return BoomDirectionEnum.UP;
		}
	}
	
}
