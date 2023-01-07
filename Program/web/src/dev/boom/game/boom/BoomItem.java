package dev.boom.game.boom;

public class BoomItem extends BoomSprite {

	private int itemId;
	
	public BoomItem() {
		super();
	}

	public BoomItem(int itemId, int imageID, int x, int y, int width, int height) {
		super(imageID, x, y, width, height);
		this.itemId = itemId;
	}
	
	public BoomItem(long id, int itemId, int imageID, int x, int y, int width, int height) {
		super(imageID, x, y, width, height);
		setId(id);
		this.itemId = itemId;
	}

	public int getItemId() {
		return itemId;
	}

	public boolean isValid() {
		if (is(BoomGameManager.BOOM_SPRITE_FLAG_ITEM_GONE)) {
			return false;
		}
		return true;
	}

	@Override
	public int getAdjustX() {
		// TODO Auto-generated method stub
		return BoomGameManager.BOOM_GAME_ITEM_ADJUST_SIZE;
	}

	@Override
	public int getAdjustY() {
		// TODO Auto-generated method stub
		return BoomGameManager.BOOM_GAME_ITEM_ADJUST_SIZE;
	}

	@Override
	public int getAdjustWidth() {
		// TODO Auto-generated method stub
		return -(2 * BoomGameManager.BOOM_GAME_ITEM_ADJUST_SIZE);
	}

	@Override
	public int getAdjustHeight() {
		// TODO Auto-generated method stub
		return -(2 * BoomGameManager.BOOM_GAME_ITEM_ADJUST_SIZE);
	}
	
}
