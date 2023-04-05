package dev.boom.game.boom;

import java.util.List;

import dev.boom.services.BoomGameItem;

public class BoomPlayerSkillEffect extends BoomSprite {

	private long playerId;
	private long groupId;
	private int damage;
	
	public BoomPlayerSkillEffect(long pid, long gid, int damage, int imageID, int x, int y, int width, int height) {
		super(imageID, x, y, width, height);
		setId(System.nanoTime());
		this.playerId = pid;
		this.groupId = gid;
		this.damage = damage;
	}

	@Override
	public int getAdjustX() {
		return BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE;
	}

	@Override
	public int getAdjustY() {
		return BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE * 2;
	}

	@Override
	public int getAdjustWidth() {
		return -(BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE * 2);
	}

	@Override
	public int getAdjustHeight() {
		return -(BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE * 2);
	}
	
	public boolean isValid() {
		if (is(BoomGameManager.BOOM_SPRITE_FLAG_ABILITY_REMOVED)) {
			return false;
		}
		return true;
	}
	
	private boolean isPush() {
		return is(BoomGameManager.BOOM_SPRITE_FLAG_ABILITY_EFFECT_PUSH);
	}
	
	private void removeEffectSkill() {
		removeDirection();
		setFlag(BoomGameManager.BOOM_SPRITE_FLAG_ABILITY_REMOVED);
	}
	@Override
	public void update(int maxX, int maxY, List<BoomSprite> collisions, List<BoomPlayer> players,
			List<BoomGameBomb> bombs, List<BoomItem> itemsList, BoomIDGenerator idGenerator, BoomGameItemUtils itemUtils, BoomPlayerScore playerScore) {
		if (!isValid() || getDirection() < 0) {
			return;
		}
		int newX = getX();
		int newY = getY();
		int boostSpeed = 24;
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
			removeEffectSkill();
			return;
		}
		if (newX + getWidth() > maxX) {
			setX(maxX - getWidth());
			removeEffectSkill();
			return;
		}
		if (newY < 0) {
			setY(0);
			removeEffectSkill();
			return;
		}
		if (newY + getHeight() > maxY) {
			setY(maxY - getHeight());
			removeEffectSkill();
			return;
		}
		for (BoomItem bi : itemsList) {
			if (BoomUtils.isDestroyableItem(bi.getItemId()) && BoomUtils.checkCollision(newX + getAdjustX(), newY + getAdjustY(), getWidth() + getAdjustWidth(), getHeight() + getAdjustHeight(), bi)) {
				bi.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_ITEM_GONE);
			}
		}
		for (BoomSprite bs : collisions) {
			if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE) && bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
				continue;
			}
			if (BoomUtils.checkCollision(newX + getAdjustX(), newY + getAdjustY(), getWidth() + getAdjustWidth(), getHeight() + getAdjustHeight(), bs)) {
				if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE)) {
					bs.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED);
					BoomGameItem item = itemUtils.getSpawnItem(players);
					if (item != null) {
						itemsList.add(new BoomItem(idGenerator.getNextItemId(), item.getId(), item.getImageID(), bs.getX(), bs.getY(),
								bs.getWidth(), bs.getHeight()));
					}
				}
				removeEffectSkill();
				return;
			}
		}
		for (BoomPlayer bp : players) {
			if (bp.getId() == playerId || bp.isDead()) {
				continue;
			}
			if (BoomUtils.checkCollision(newX + getAdjustX(), newY + getAdjustY(), getWidth() + getAdjustWidth(), getHeight() + getAdjustHeight(), bp)) {
				if (!bp.checkShieldProtectorEffect()) {
					int nDamage = bp.checkDamageBlockedEffect(damage);
					if (nDamage > 0) {
						if (!bp.checkDamageAbsorbEffect(nDamage)) {
							boolean sameGroup = (groupId > 0 && groupId == bp.getGroupId());
							bp.subHp(nDamage);
							//
							if (bp.isDead() && !sameGroup) {
								playerScore.addScore(getId(), BoomGameManager.BOOM_GAME_REWARD_POINT_ON_KILL);
							}
						}
					}
				}
				removeEffectSkill();
				return;
			}
		}
		for (BoomGameBomb bomb : bombs) {
			if (!bomb.isValid()) {
				continue;
			}
			if (BoomUtils.checkCollision(newX + getAdjustX(), newY + getAdjustY(), getWidth() + getAdjustWidth(), getHeight() + getAdjustHeight(), bomb)) {
				if (isPush()) {
					bomb.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_PUSHED);
					bomb.setDirection(getDirection());
				} else {
					bomb.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_EXPLODED);
				}
				removeEffectSkill();
				return;
			}
		}
		setX(newX);
		setY(newY);
	}
}
