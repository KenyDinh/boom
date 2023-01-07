package dev.boom.game.boom;

import java.util.List;

public class BoomSprite {

	private long id;
	private int imageID;
	private int x;
	private int y;
	private int width;
	private int height;
	private int flag;
	private int direction = -1;
	private BoomSpriteState state;

	public BoomSprite() {
		super();
	}

	public BoomSprite(int imageID, int x, int y, int width, int height) {
		super();
		this.imageID = imageID;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.state = BoomSpriteState.IDLE;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void addFlag(int flag) {
		setFlag(getFlag() | flag);
	}

	public void removeFlag(int flag) {
		setFlag(getFlag() & ~flag);
	}

	public boolean is(int flag) {
		return (getFlag() & flag) != 0;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void removeDirection() {
		this.direction = -1;
	}

	public BoomSpriteState getState() {
		return state;
	}

	public void setState(BoomSpriteState state) {
		this.state = state;
	}

	public void addX(int change) {
		this.x += change;
	}

	public void addY(int change) {
		this.y += change;
	}

	public void update(int maxX, int maxY, List<BoomSprite> collisions, List<BoomPlayer> players,
			List<BoomGameBomb> bombs, List<BoomItem> itemsList, BoomIDGenerator idGenerator,
			BoomGameItemUtils itemUtils, BoomPlayerScore playerScore) {
	}

	public int getAdjustX() {
		if (is(BoomGameManager.BOOM_SPRITE_FLAG_TREE)) {
			return BoomGameManager.BOOM_GAME_COLLISION_ADJUST_SIZE;
		}
		return 0;
	}

	public int getAdjustY() {
		if (is(BoomGameManager.BOOM_SPRITE_FLAG_TREE)) {
			return BoomGameManager.BOOM_GAME_COLLISION_ADJUST_SIZE;
		}
		return 0;
	}

	public int getAdjustWidth() {
		if (is(BoomGameManager.BOOM_SPRITE_FLAG_TREE)) {
			return -(BoomGameManager.BOOM_GAME_COLLISION_ADJUST_SIZE * 2);
		}
		return 0;
	}

	public int getAdjustHeight() {
		if (is(BoomGameManager.BOOM_SPRITE_FLAG_TREE)) {
			return -(BoomGameManager.BOOM_GAME_COLLISION_ADJUST_SIZE * 2);
		}
		return 0;
	}

}
