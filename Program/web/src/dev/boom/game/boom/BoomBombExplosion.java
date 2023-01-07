package dev.boom.game.boom;

public class BoomBombExplosion extends BoomSprite {

	public BoomBombExplosion(int imageID, int x, int y, int width, int height) {
		super(imageID, x, y, width, height);
	}

	@Override
	public int getAdjustX() {
		return BoomGameManager.BOOM_GAME_BOMB_EXPLOSION_ADJUST_SIZE;
	}

	@Override
	public int getAdjustY() {
		return BoomGameManager.BOOM_GAME_BOMB_EXPLOSION_ADJUST_SIZE * 2;
	}

	@Override
	public int getAdjustWidth() {
		return -(BoomGameManager.BOOM_GAME_BOMB_EXPLOSION_ADJUST_SIZE * 2);
	}
	
	@Override
	public int getAdjustHeight() {
		return -(BoomGameManager.BOOM_GAME_BOMB_EXPLOSION_ADJUST_SIZE * 2);
	}

}
