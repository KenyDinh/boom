package dev.boom.game.boom;

import java.util.ArrayList;
import java.util.List;

import dev.boom.services.BoomGameItem;

public class BoomGameBomb extends BoomSprite {

	private long playerId;
	private int type;
	private long start;
	private long end;
	private int xSize = 1;
	private int ySize = 1;
	private int damage = 10;
	private int confusionDuration;
	private long groupId;

	public BoomGameBomb(long playerId, int imageID, int x, int y, int width, int height, int sizeX, int sizeY, int damage) {
		super(imageID, x, y, width, height);
		this.playerId = playerId;
		this.xSize = sizeX;
		this.ySize = sizeY;
		this.damage = damage;
	}
	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public int getxSize() {
		return xSize;
	}

	public void setxSize(int xSize) {
		this.xSize = xSize;
	}

	public int getySize() {
		return ySize;
	}

	public void setySize(int ySize) {
		this.ySize = ySize;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getConfusionDuration() {
		return confusionDuration;
	}

	public void setConfusionDuration(int confusionDuration) {
		this.confusionDuration = confusionDuration;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	@Override
	public int getAdjustX() {
		return BoomGameManager.BOOM_GAME_BOOM_ADJUST_SIZE;
	}

	@Override
	public int getAdjustY() {
		return BoomGameManager.BOOM_GAME_BOOM_ADJUST_SIZE * 2;
	}

	@Override
	public int getAdjustWidth() {
		return -(BoomGameManager.BOOM_GAME_BOOM_ADJUST_SIZE * 2);
	}
	
	@Override
	public int getAdjustHeight() {
		return -(BoomGameManager.BOOM_GAME_BOOM_ADJUST_SIZE * 2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof BoomGameBomb) {
			BoomGameBomb comp = (BoomGameBomb) obj;
			return (getPlayerId() == comp.getPlayerId() && getStart() == comp.getStart() && getEnd() == comp.getEnd());
		}
		return false;
	}
	
	private String getKey() {
		return String.format("B-%d-%d", getX(), getY());
	}

	public boolean isValid() {
		if (is(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_EXPLODED)) {
			return false;
		}
		return true;
	}
	
	public void update(int maxX, int maxY, List<BoomSprite> collisions, List<BoomPlayer> players,
			List<BoomGameBomb> bombs) {
		if (!isValid() || getDirection() < 0) {
			return;
		}
		int newX = getX();
		int newY = getY();
		int adjX = getAdjustX(), adjY = getAdjustY(), adjW = getAdjustWidth(), adjH = getAdjustHeight();
		int boostSpeed = BoomGameManager.BOOM_GAME_BOMB_THROWN_SPEED;
		if (isPushed()) {
			boostSpeed = BoomGameManager.BOOM_GAME_BOMB_PUSHED_SPEED;
		}
		if (getDirection() == BoomDirectionEnum.UP.ordinal()) {
			newY -= boostSpeed;
		} else if (getDirection() == BoomDirectionEnum.DOWN.ordinal()) {
			newY += boostSpeed;
		} else if (getDirection() == BoomDirectionEnum.LEFT.ordinal()) {
			newX -= boostSpeed;
		} else if (getDirection() == BoomDirectionEnum.RIGHT.ordinal()) {
			newX += boostSpeed;
		}
		if (newX < 0) {
			setX(0);
			removeDirection();
			return;
		}
		if (newX + getWidth() > maxX) {
			setX(maxX - getWidth());
			removeDirection();
			return;
		}
		if (newY < 0) {
			setY(0);
			removeDirection();
			return;
		}
		if (newY + getHeight() > maxY) {
			setY(maxY - getHeight());
			removeDirection();
			return;
		}
		for (BoomSprite bs : collisions) {
			if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
				continue;
			}
			if (BoomUtils.checkCollision(newX + adjX, newY + adjY, getWidth() + adjW, getHeight() + adjH, bs)) {
				correctPossion(boostSpeed, bs);
				removeDirection();
				return;
			}
		}
		for (BoomPlayer bp : players) {
			if (bp.getId() == getPlayerId() || bp.isDead()) {
				continue;
			}
			if (BoomUtils.checkCollision(newX + adjX, newY + adjY, getWidth() + adjW, getHeight() + adjH, bp)) {
				correctPossion(boostSpeed, bp);
				removeDirection();
				return;
			}
		}
		for (BoomGameBomb bomb : bombs) {
			if (this.equals(bomb) || !bomb.isValid()) {
				continue;
			}
			if (BoomUtils.checkCollision(newX + adjX, newY + adjY, getWidth() + adjW, getHeight() + adjH, bomb)) {
				if (!isPushed()) {
					correctPossion(boostSpeed, bomb);
					removeDirection();
				} else {
					bomb.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_PUSHED);
					bomb.setDirection(getDirection());
					bomb.update(maxX, maxY, collisions, players, bombs);
					bomb.removeFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_PUSHED);
					bomb.removeDirection();
				}
				return;
			}
		}
		setX(newX);
		setY(newY);
	}
	
	private void correctPossion(int boostSpeed, BoomSprite collision) {
		int addX = 0, addY = 0;
		if (getDirection() == BoomDirectionEnum.UP.ordinal()) {
			addY = -1;
		} else if (getDirection() == BoomDirectionEnum.DOWN.ordinal()) {
			addY = 1;
		} else if (getDirection() == BoomDirectionEnum.LEFT.ordinal()) {
			addX = -1;
		} else if (getDirection() == BoomDirectionEnum.RIGHT.ordinal()) {
			addX = 1;
		}
		while (boostSpeed > 0) {
			if (BoomUtils.checkCollision(getX() + addX + getAdjustX(), getY() + addY + getAdjustY(), getWidth() + getAdjustWidth(), getHeight() + getAdjustHeight(), collision)) {
				return;
			}
			addX(addX);
			addY(addY);
			boostSpeed -= 1;
		}
	}
	
	public void setDirection(int direction) {
		super.setDirection(direction);
		this.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_THROWN);
	}
	
	public boolean isThrown() {
		return this.is(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_THROWN);
	}
	
	public boolean isPushed() {
		return this.is(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_PUSHED);
	}
	
	public boolean isFastExplode() {
		return this.is(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_FAST_EXPLODE);
	}
	
	public boolean isConfused() {
		return this.is(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_CONFUSED);
	}
	
	public List<BoomGameBombEffect> getEffectsList() {
		List<BoomGameBombEffect> effects = new ArrayList<>();
		if (isFastExplode()) {
			effects.add(new BoomGameBombEffect(BoomGameItemEffect.QUICK_BOMBER));
		}
		if (isConfused()) {
			effects.add(new BoomGameBombEffect(BoomGameItemEffect.CONFUSION));
		}
		return effects;
	}
	
	public void extendDuration(long extension) {
		if (!isValid()) {
			return;
		}
		if (extension <= 0) {
			return;
		}
		long current = getEnd();
		setEnd(current + extension);
	}
	
	public boolean checkExplode() {
		if (!isValid()) {
			return false;
		}
		return (getEnd() <= System.nanoTime());
	}

	public void explode(int maxX, int maxY, List<BoomSprite> explosions, List<BoomSprite> collisions,
			List<BoomPlayer> players, List<BoomGameBomb> bombs, List<BoomItem> itemsList, BoomIDGenerator idGenerator, BoomGameItemUtils itemUtils, BoomPlayerScore playerScore) {
		if (!isValid() || explosions == null) {
			return;
		}
		addFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_EXPLODED);
		BoomSprite centerBx = new BoomBombExplosion(1, getX(), getY(), getWidth(), getHeight());
		explosions.add(centerBx);
		checkExplosionCollision(centerBx, collisions, players, itemsList, idGenerator, itemUtils, playerScore);
		// getAdjustHeight()/2 = 8;
		// getAdjustWidth()/2 = 8;
		// if the thickness of obstacle is not more than (getAdjustHeight()/2), then the obstacle can not prevent explosion
		for (int i = 0; i < xSize; i++) {
			BoomSprite bx = new BoomBombExplosion(1, getX() + (i + 1) * (getWidth() + getAdjustWidth()/2), getY(), getWidth(), getHeight());
			explosions.add(bx);
			for (BoomGameBomb bomb : bombs) {
				if (BoomUtils.checkCollision(bx, bomb)) {
					bomb.explode(maxX, maxY, explosions, collisions, players, bombs, itemsList, idGenerator, itemUtils, playerScore);
				}
			}
			if (checkExplosionCollision(bx, collisions, players, itemsList, idGenerator, itemUtils, playerScore)) {
				break;
			}
			if (bx.getX() + bx.getAdjustX() < 0 || bx.getX() + bx.getAdjustX() + bx.getWidth() + bx.getAdjustWidth() > maxX ||
					bx.getY() + bx.getAdjustY() < 0 || bx.getY() + bx.getAdjustY() + bx.getHeight() + bx.getAdjustHeight() > maxY) {
				break;
			}
		}
		for (int i = 0; i < xSize; i++) {
			BoomSprite bx = new BoomBombExplosion(1, getX() - (i + 1) * (getWidth() + getAdjustWidth()/2), getY(), getWidth(), getHeight());
			explosions.add(bx);
			for (BoomGameBomb bomb : bombs) {
				if (BoomUtils.checkCollision(bx, bomb)) {
					bomb.explode(maxX, maxY, explosions, collisions, players, bombs, itemsList, idGenerator, itemUtils, playerScore);
				}
			}
			if (checkExplosionCollision(bx, collisions, players, itemsList, idGenerator, itemUtils, playerScore)) {
				break;
			}
			if (bx.getX() + bx.getAdjustX() < 0 || bx.getX() + bx.getAdjustX() + bx.getWidth() + bx.getAdjustWidth() > maxX ||
					bx.getY() + bx.getAdjustY() < 0 || bx.getY() + bx.getAdjustY() + bx.getHeight() + bx.getAdjustHeight() > maxY) {
				break;
			}
		}
		for (int i = 0; i < ySize; i++) {
			BoomSprite bx = new BoomBombExplosion(1, getX(), getY() + (i + 1) * (getHeight() + getAdjustHeight()/2), getWidth(), getHeight());
			explosions.add(bx);
			for (BoomGameBomb bomb : bombs) {
				if (BoomUtils.checkCollision(bx, bomb)) {
					bomb.explode(maxX, maxY, explosions, collisions, players, bombs, itemsList, idGenerator, itemUtils, playerScore);
				}
			}
			if (checkExplosionCollision(bx, collisions, players, itemsList, idGenerator, itemUtils, playerScore)) {
				break;
			}
			if (bx.getX() + bx.getAdjustX() < 0 || bx.getX() + bx.getAdjustX() + bx.getWidth() + bx.getAdjustWidth() > maxX ||
					bx.getY() + bx.getAdjustY() < 0 || bx.getY() + bx.getAdjustY() + bx.getHeight() + bx.getAdjustHeight() > maxY) {
				break;
			}
		}
		for (int i = 0; i < ySize; i++) {
			BoomSprite bx = new BoomBombExplosion(1, getX(), getY() - (i + 1) * (getHeight() + getAdjustHeight()/2), getWidth(), getHeight());
			explosions.add(bx);
			for (BoomGameBomb bomb : bombs) {
				if (BoomUtils.checkCollision(bx, bomb)) {
					bomb.explode(maxX, maxY, explosions, collisions, players, bombs, itemsList, idGenerator, itemUtils, playerScore);
				}
			}
			if (checkExplosionCollision(bx, collisions, players, itemsList, idGenerator, itemUtils, playerScore)) {
				break;
			}
			if (bx.getX() + bx.getAdjustX() < 0 || bx.getX() + bx.getAdjustX() + bx.getWidth() + bx.getAdjustWidth() > maxX ||
					bx.getY() + bx.getAdjustY() < 0 || bx.getY() + bx.getAdjustY() + bx.getHeight() + bx.getAdjustHeight() > maxY) {
				break;
			}
		}
	}

	private boolean checkExplosionCollision(BoomSprite bx, List<BoomSprite> collisions, List<BoomPlayer> players,
			List<BoomItem> itemsList, BoomIDGenerator idGenerator, BoomGameItemUtils itemUtils, BoomPlayerScore playerScore) {
		boolean ret = false;
		
		for (BoomItem bi : itemsList) {
			if (BoomUtils.isDestroyableItem(bi.getItemId()) && BoomUtils.checkCollision(bi, bx)) {
				bi.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_ITEM_GONE);
			}
		}
		for (BoomSprite bs : collisions) {
			if (BoomUtils.checkCollision(bx, bs)) {
				if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE)) {
					if (!bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
						BoomGameItem item = itemUtils.getSpawnItem(players);
						if (item != null) {
							itemsList.add(new BoomItem(idGenerator.getNextItemId(), item.getId(), item.getImageID(), bs.getX(), bs.getY(),
									bs.getWidth(), bs.getHeight()));
						}
					}
					bs.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED);
				}
				ret = true;
			}
		}
		if (!ret) {
			String key = getKey();
			for (BoomPlayer bp : players) {
				if (bp.isDead()) {
					continue;
				}
				if (bp.isBombExploded(key)) {
					continue;
				}
				if (BoomUtils.checkCollision(bx, bp)) {
					bp.addBombExploded(key);
					if (isConfused()) {
						long start = System.nanoTime();
						long end = start + BoomGameManager.NANO_SECOND * getConfusionDuration();
						bp.applyConfuseEffect(start, end);
					}
					if (!bp.checkShieldProtectorEffect()) {
						int nDamage = getDamage();
						boolean sameGroup = (getGroupId() > 0 && getGroupId() == bp.getGroupId());
						if (sameGroup) {
							// 50% damage to ally
							nDamage = (nDamage * BoomGameManager.BOMB_DAMAGE_TO_ALLY_RATE) / 100;
						}
						nDamage = bp.checkDamageBlockedEffect(nDamage);
						if (nDamage > 0) {
							if (!bp.checkDamageAbsorbEffect(nDamage)) {
								bp.subHp(nDamage);
								//
								if (bp.isDead() && bp.getId() != getPlayerId() && !sameGroup) {
									playerScore.addScore(getPlayerId(), BoomGameManager.BOOM_GAME_REWARD_POINT_ON_KILL);
								}
							}
						}
					}
					ret = true;
				}
			}
		}

		return ret;
	}
}
