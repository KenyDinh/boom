package dev.boom.game.boom;

public class BoomCollision extends BoomSprite {

	// DO NOT set for flag attribute. (will be conflict with TREE flag)
	private static final int COLLISION_FLAG_TOP = 0x01;
	private static final int COLLISION_FLAG_RIGHT = 0x02;
	private static final int COLLISION_FLAG_BOTTOM = 0x04;
	private static final int COLLISION_FLAG_LEFT = 0x08;
	
	private int collisionFlag = 0;
	
	public BoomCollision(int imageID, int x, int y, int width, int height) {
		super(imageID, x, y, width, height);
		// TODO Auto-generated constructor stub
	}
	
	public void setFlagTop() {
		this.collisionFlag |= COLLISION_FLAG_TOP;
	}
	
	public void setFlagRight() {
		this.collisionFlag |= COLLISION_FLAG_RIGHT;
	}
	
	public void setFlagBottom() {
		this.collisionFlag |= COLLISION_FLAG_BOTTOM;
	}
	
	public void setFlagLeft() {
		this.collisionFlag |= COLLISION_FLAG_LEFT;
	}
	
	private boolean checkFlag(int flag) {
		return (this.collisionFlag & flag) != 0;
	}

	@Override
	public int getAdjustX() {
		if (checkFlag(COLLISION_FLAG_LEFT)) {
			return BoomGameManager.BOOM_GAME_COLLISION_ADJUST_SIZE;
		}
		return 0;
	}

	@Override
	public int getAdjustY() {
		if (checkFlag(COLLISION_FLAG_TOP)) {
			return BoomGameManager.BOOM_GAME_COLLISION_ADJUST_SIZE;
		}
		return 0;
	}

	@Override
	public int getAdjustWidth() {
		int size = 0;
		if (checkFlag(COLLISION_FLAG_LEFT)) {
			size -= BoomGameManager.BOOM_GAME_COLLISION_ADJUST_SIZE;
		}
		if (checkFlag(COLLISION_FLAG_RIGHT)) {
			size -= BoomGameManager.BOOM_GAME_COLLISION_ADJUST_SIZE;
		}
		return size;
	}

	@Override
	public int getAdjustHeight() {
		int size = 0;
		if (checkFlag(COLLISION_FLAG_TOP)) {
			size -= BoomGameManager.BOOM_GAME_COLLISION_ADJUST_SIZE;
		}
		if (checkFlag(COLLISION_FLAG_BOTTOM)) {
			size -= BoomGameManager.BOOM_GAME_COLLISION_ADJUST_SIZE;
		}
		return size;
	}
	
}
