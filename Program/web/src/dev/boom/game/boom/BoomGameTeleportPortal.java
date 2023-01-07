package dev.boom.game.boom;

import java.util.List;

public class BoomGameTeleportPortal extends BoomSprite {

	private int linkedId;
	private BoomGameTeleportPortal linkedPortal;
	private long lastActive;
	private long timeWait = BoomGameManager.BOOM_GAME_PORTAL_TIME_WAIT;
	
	public BoomGameTeleportPortal(int linkedId, int imageID, int x, int y, int width, int height) {
		super(imageID, x, y, width, height);
		this.lastActive = 0;
		this.linkedId = linkedId;
		this.linkedPortal = null;
	}
	
	@Override
	public int getAdjustX() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public int getAdjustY() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public int getAdjustWidth() {
		// TODO Auto-generated method stub
		return -8;
	}

	@Override
	public int getAdjustHeight() {
		// TODO Auto-generated method stub
		return -8;
	}
	

	public int getLinkedId() {
		return linkedId;
	}

	public BoomGameTeleportPortal getLinkedPortal() {
		return linkedPortal;
	}

	public void setLinkedPortal(BoomGameTeleportPortal linkedPortal) {
		this.linkedPortal = linkedPortal;
	}

	public long getLastActive() {
		return lastActive;
	}
	
	public long getNextActive() {
		return (getLastActive() + timeWait * BoomGameManager.NANO_SECOND);
	}

	public void setLastActive(long lastActive) {
		this.lastActive = lastActive;
	}
	
	public long getTimeWait() {
		return timeWait;
	}

	public void setTimeWait(long timeWait) {
		this.timeWait = timeWait;
	}

	public void activate() {
		if (this.linkedPortal == null) {
			return;
		}
		long cur = System.nanoTime();
		setLastActive(cur);
		this.linkedPortal.setLastActive(cur);
	}

	public boolean isPortalOpen() {
		return isPortalOpen(System.nanoTime());
	}
	
	public boolean isPortalOpen(long currentTime) {
		return (currentTime > getNextActive());
	}
	
	public boolean canTeleport(BoomPlayer target, int maxX, int maxY, List<BoomSprite> collisions, List<BoomPlayer> players, List<BoomGameBomb> bombs) {
		if (this.linkedPortal == null) {
			return false;
		}
		int newX = this.linkedPortal.getX();
		int newY = this.linkedPortal.getY();
		int width = target.getWidth();
		int height = target.getHeight();
		int adjX = target.getAdjustX();
		int adjY = target.getAdjustY();
		int adjW = target.getAdjustWidth();
		int adjH = target.getAdjustHeight();
		if (newX < 0 || newX > maxX) {
			return false;
		}
		if (newY < 0 || newY > maxY) {
			return false;
		}
		for (BoomSprite bs : collisions) {
			if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
				continue;
			}
			if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bs)) {
				return false;
			}
		}
		for (BoomPlayer bp : players) {
			if (target.equals(bp) || bp.isDead()) {
				continue;
			}
			if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bp)) {
				return false;
			}
		}
		for (BoomGameBomb bomb : bombs) {
			if (!bomb.isValid()) {
				continue;
			}
			if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bomb)) {
				return false;
			}
		}
		
		return true;
	}
}
