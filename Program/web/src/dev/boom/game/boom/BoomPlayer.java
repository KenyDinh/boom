package dev.boom.game.boom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import dev.boom.common.CommonMethod;
import dev.boom.services.BoomGameItem;

public class BoomPlayer extends BoomSprite {

	private long id;
	private String name;
	private String fullname;
	private double hp;
	private int velocity;
	private BoomCharater character;
	private int damage;
	private int atkSpeed;
	private int atkRange;
	private int movementSpeed;
	private boolean isFreeMove;
	private boolean hasCharClasses;
	private boolean hasActiveCloak;
	private long lastActiveAbilityTime;
	private int currentGauge = 0;
	private BoomGameBomb lastCreatedBomb;
	private Map<Integer, BoomPlayerItemEffect> effectsList;
	private Stack<BoomGameBomb> bombCreationRequest;
	private Queue<BoomPlayerAbility> activeAbilities;
	private int bombType;

	public BoomPlayer(long id, String name, BoomCharater character, int x, int y, int width, int height) {
		super(character.getId(), x, y, width, height);
		this.id = id;
		this.fullname = name;
		this.name = this.fullname.split("\\.")[0];
		this.effectsList = new HashMap<>();
		this.activeAbilities = new LinkedList<>();
		this.lastActiveAbilityTime = System.nanoTime();
		bombCreationRequest = new Stack<>();
		init(character);
	}
	
	public void init(BoomCharater character) {
		this.character = character;
		this.setImageID(this.character.getId());
		effectsList.clear();
		if (hasCharClasses()) {
			if (this.character.getType() == BoomCharacterType.MONK.ordinal()) {
				addEffect(new BoomPlayerItemEffect(BoomGameItemEffect.MONK_ATTACK.getId(), BoomGameItemEffect.MONK_ATTACK, 0, 0, 0));
			} else if (this.character.getType() == BoomCharacterType.WARRIOR.ordinal()) {
				addEffect(new BoomPlayerItemEffect(BoomGameItemEffect.WARRIOR_ATTACK.getId(), BoomGameItemEffect.WARRIOR_ATTACK, 0, 0, 0));
			}
		} else {
			this.character = BoomCharater.CHARACTER_00;
		}
		this.hp = this.character.getHp();
		this.damage = this.character.getDamage();
		this.atkSpeed = this.character.getAttackSpeed();
		this.atkRange = this.character.getAttackRange();
		this.movementSpeed = this.character.getMovementSpeed();
		this.lastActiveAbilityTime = System.nanoTime();
	}
	
	public boolean isBomber() {
		if (hasCharClasses()) {
			return this.character.getType() == BoomCharacterType.BOMBER.ordinal();
		}
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BoomPlayer) {
			return this.getId() == ((BoomPlayer) obj).getId();
		}
		return false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void updateNameWithIndex(int index) {
		setName(String.format("%s%d", getName(), index));
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	public int getSendHp() {
		return (int)Math.round(hp);
	}

	public double getHp() {
		return hp;
	}

	public void setHp(double hp) {
		this.hp = hp;
		if (this.hp <= 0) {
			addFlag(BoomGameManager.BOOM_SPRITE_FLAG_PLAYERS_DEAD);
		}
	}

	public void subHp(double amount) {
		double newHP = getHp() - amount;
		if (newHP > getMaxHp()) {
			newHP = getMaxHp();
		}
		if (newHP < 0.1) {
			newHP = 0;
		}
		setHp(newHP);
	}

	public void addHp(int amount) {
		double newHP = getHp() + amount;
		if (newHP > getMaxHp()) {
			newHP = getMaxHp();
		}
		if (newHP < 0.1) {
			newHP = 0;
		}
		setHp(newHP);
	}

	public int getMaxHp() {
		return character.getMaxHP();
	}

	public int getSpeed() {
		return this.movementSpeed;
	}

	public int getMaxSpeed() {
		return character.getMaxMovementSpeed();
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public void addAttakRange(int percentage) {
		int newAtkRange = this.atkRange + (this.character.getAttackRange() * percentage) / 100;
		this.atkRange = CommonMethod.minmax(this.atkRange, newAtkRange, this.character.getMaxAttakRange());
	}

	public int getBombSize() {
		return this.atkRange / 100;
	}

	public void addDamage(int percentage) {
		int newDmg = this.damage + (this.character.getDamage() * percentage) / 100;
		this.damage = CommonMethod.minmax(this.damage, newDmg, this.character.getMaxDamage());
	}

	public int getDamage() {
		return this.damage;
	}

	public void addAttackSpeed(int percentage) {
		int newAtkSpeed = this.atkSpeed + (this.character.getAttackSpeed() * percentage) / 100;
		this.atkSpeed = CommonMethod.minmax(this.atkSpeed, newAtkSpeed, this.character.getMaxAttackSpeed());
	}
	
	public int getBoomPoolSize() {
		return this.atkSpeed / 100;
	}

	public boolean isFreeMove() {
		return isFreeMove;
	}

	public void setFreeMove(boolean isFreeMove) {
		this.isFreeMove = isFreeMove;
	}
	
	public boolean hasCharClasses() {
		return hasCharClasses;
	}

	public void setCharClasses(boolean hasCharClasses) {
		this.hasCharClasses = hasCharClasses;
		init(BoomCharater.getById(getImageID()));
	}
	
	public int getBombType() {
		return bombType;
	}

	public void setBombType(int bombType) {
		this.bombType = bombType;
	}

	@Override
	public int getAdjustX() {
		if (isFreeMove) {
			return BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE;
		}
		return super.getAdjustX();
	}

	@Override
	public int getAdjustY() {
		if (isFreeMove) {
			return BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE * 2;
		}
		return super.getAdjustY();
	}

	@Override
	public int getAdjustWidth() {
		if (isFreeMove) {
			return -(BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE * 2);
		}
		return super.getAdjustWidth();
	}

	@Override
	public int getAdjustHeight() {
		if (isFreeMove) {
			return -(BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE * 2);
		}
		return super.getAdjustHeight();
	}

	private int getSpeedAdd() {
		if (this.effectsList == null || this.effectsList.isEmpty()) {
			return 0;
		}
		if (!this.effectsList.containsKey(BoomGameItemEffect.INCREASE_SPEED.getId())) {
			return 0;
		}
		BoomPlayerItemEffect effect = this.effectsList.get(BoomGameItemEffect.INCREASE_SPEED.getId());
		if (effect.hasEffect()) {
			return effect.getEffectParam();
		}
		return 0;
	}

	private int getSpeedBoost() {
		if (this.effectsList == null || this.effectsList.isEmpty()) {
			return 0;
		}
		if (!this.effectsList.containsKey(BoomGameItemEffect.BOOST_SPEED.getId())) {
			return 0;
		}
		BoomPlayerItemEffect effect = this.effectsList.get(BoomGameItemEffect.BOOST_SPEED.getId());
		if (effect.hasEffect()) {
			return effect.getEffectParam();
		}
		return 0;
	}
	
	public boolean checkIfHasValidEffect(BoomGameItemEffect checkEffect) {
		if (this.effectsList == null || this.effectsList.isEmpty()) {
			return false;
		}
		if (!this.effectsList.containsKey(checkEffect.getId())) {
			return false;
		}
		BoomPlayerItemEffect effect = this.effectsList.get(checkEffect.getId());
		return effect.hasEffect();
	}
	
	public int checkDamageBlockedEffect(int damage) {
		if (this.effectsList == null || this.effectsList.isEmpty()) {
			return damage;
		}
		if (!this.effectsList.containsKey(BoomGameItemEffect.WARRIOR_AMOR.getId())) {
			return damage;
		}
		BoomPlayerItemEffect effect = this.effectsList.get(BoomGameItemEffect.WARRIOR_AMOR.getId());
		if (effect.hasEffect()) {
			int ret = CommonMethod.max(0, damage - effect.getEffectParam());
			effect.subEffectParam(damage);
			return ret;
		}
		return damage;
	}
	
	public boolean checkShieldProtectorEffect() {
		if (this.effectsList == null || this.effectsList.isEmpty()) {
			return false;
		}
		if (!this.effectsList.containsKey(BoomGameItemEffect.SHEILD_PROTECTOR.getId())) {
			return false;
		}
		BoomPlayerItemEffect effect = this.effectsList.get(BoomGameItemEffect.SHEILD_PROTECTOR.getId());
		if (effect.hasEffect()) {
			return effect.subEffectParam(1);
		}
		return false;
	}
	
	public boolean checkDamageAbsorbEffect(int damage) {
		if (this.effectsList == null || this.effectsList.isEmpty()) {
			return false;
		}
		if (!this.effectsList.containsKey(BoomGameItemEffect.ABSORB_DAMAGE.getId())) {
			return false;
		}
		BoomPlayerItemEffect effect = this.effectsList.get(BoomGameItemEffect.ABSORB_DAMAGE.getId());
		if (!effect.hasEffect()) {
			return false;
		}
		int recoverHP = (effect.getEffectParam() * damage) / 100;
		addHp(recoverHP);
		return true;
	}
	
	public boolean checkGodHandEffect() {
		if (this.effectsList == null || this.effectsList.isEmpty()) {
			return false;
		}
		if (!this.effectsList.containsKey(BoomGameItemEffect.BOMB_SHOOTER.getId())) {
			return false;
		}
		BoomPlayerItemEffect effect = this.effectsList.get(BoomGameItemEffect.BOMB_SHOOTER.getId());
		if (effect.hasEffect()) {
			return effect.subEffectParam(1);
		}
		return false;
	}
	
	private BoomPlayerItemEffect getValidEffect(BoomGameItemEffect checkEffect) {
		if (this.effectsList == null || this.effectsList.isEmpty()) {
			return null;
		}
		if (!this.effectsList.containsKey(checkEffect.getId())) {
			return null;
		}
		BoomPlayerItemEffect effect = this.effectsList.get(checkEffect.getId());
		if (!effect.hasEffect()) {
			return null;
		}
		return effect;
	}

	@Override
	public BoomSpriteState getState() {
		if (isDead()) {
			return BoomSpriteState.DEAD;
		}
		if (this.velocity > 0 || ( !isFreeMove() && isPlayerNotInSideBlock() )) {
			if (getDirection() == BoomDirectionEnum.UP.ordinal()) {
				return BoomSpriteState.MOVE_UP;
			}
			if (getDirection() == BoomDirectionEnum.DOWN.ordinal()) {
				return BoomSpriteState.MOVE_DOWN;
			}
			if (getDirection() == BoomDirectionEnum.LEFT.ordinal()) {
				return BoomSpriteState.MOVE_LEFT;
			}
			if (getDirection() == BoomDirectionEnum.RIGHT.ordinal()) {
				return BoomSpriteState.MOVE_RIGHT;
			}
		} else {
			if (getDirection() == BoomDirectionEnum.UP.ordinal()) {
				return BoomSpriteState.IDLE_UP;
			}
			if (getDirection() == BoomDirectionEnum.DOWN.ordinal()) {
				return BoomSpriteState.IDLE_DOWN;
			}
			if (getDirection() == BoomDirectionEnum.LEFT.ordinal()) {
				return BoomSpriteState.IDLE_LEFT;
			}
			if (getDirection() == BoomDirectionEnum.RIGHT.ordinal()) {
				return BoomSpriteState.IDLE_RIGHT;
			}
		}

		return BoomSpriteState.IDLE;
	}
	
	public List<Map<String, Object>> getEffectIdsList() {
		List<Map<String, Object>> effectIdsList = new ArrayList<>();
		long time = System.nanoTime();
		for (Integer id : this.effectsList.keySet()) {
			BoomPlayerItemEffect eff = this.effectsList.get(id);
			if (eff.hasEffect(time)) {
				Map<String, Object> map = new HashMap<>();
				map.put("id", eff.getEffectType().getImageID());
				map.put("blk", (eff.isEndSoon(time) ? 1 : 0));
				map.put("stk", eff.getCurStack());
				effectIdsList.add(map);
			}
		}
		return effectIdsList;
	}

	public int getCurrentGauge() {
		return currentGauge;
	}

	public boolean isDead() {
		return is(BoomGameManager.BOOM_SPRITE_FLAG_PLAYERS_DEAD);
	}
	
	public boolean isRankingChecked() {
		return is(BoomGameManager.BOOM_SPRITE_FLAG_PLAYERS_RANKING);
	}
	
	public boolean checkIfHaveSpecialAbilityWithGauge() {
		if (this.character.getType() == BoomCharacterType.MONK.ordinal() || this.character.getType() == BoomCharacterType.WARRIOR.ordinal()) {
			return true;
		}
		return (checkIfHasValidEffect(BoomGameItemEffect.IRON_FIST) || checkIfHasValidEffect(BoomGameItemEffect.IRON_SWORD));
	}
	
	private void updateSpecialAbilityGauge() {
		if (!checkIfHaveSpecialAbilityWithGauge()) {
			return;
		}
		long currentTime = System.nanoTime();
		long passTime = currentTime - this.lastActiveAbilityTime;
		long checkGauge = 0;
		if (this.character.getType() == BoomCharacterType.MONK.ordinal() || this.character.getType() == BoomCharacterType.WARRIOR.ordinal()) {
			checkGauge = ((1000 * passTime) / ((BoomGameManager.NANO_SECOND / this.atkSpeed) * 100));
		} else {
			checkGauge = ((1000 * passTime) / (BoomGameManager.NANO_SECOND * 2));
		}
		if (checkGauge > 1000) {
			this.currentGauge = 1000;
		} else if (checkGauge < 0) {
			this.currentGauge = 0;
		} else {
			this.currentGauge = (int)checkGauge;
		}
	}
	
	public void activateMainAbility(List<BoomGameBomb> bombs) {
		if (this.character.getType() == BoomCharacterType.MONK.ordinal()) {
			activeAbility(BoomGameItemEffect.MONK_ATTACK);
			return;
		}
		if (this.character.getType() == BoomCharacterType.WARRIOR.ordinal()) {
			activeAbility(BoomGameItemEffect.WARRIOR_ATTACK);
			return;
		}
		if (this.character.getType() == BoomCharacterType.BOMBER.ordinal()) {
			if (checkIfHasValidEffect(BoomGameItemEffect.IRON_FIST)) {
				activeAbility(BoomGameItemEffect.IRON_FIST);
				return;
			}
			if (checkIfHasValidEffect(BoomGameItemEffect.IRON_SWORD)) {
				activeAbility(BoomGameItemEffect.IRON_SWORD);
				return;
			}
			requestBombCreation(bombs);
			return;
		}
	}
	
	private void activeAbility(BoomGameItemEffect effectType) {
		if (!activeAbilities.isEmpty()) {
			return;
		}
		if (this.currentGauge < 1000) {
			return;
		}
		this.currentGauge = 0;
		lastActiveAbilityTime = System.nanoTime();
		BoomPlayerItemEffect validEffect = getValidEffect(effectType);
		if (validEffect == null) {
			return;
		}
		int range = this.getWidth();
		int damage = this.character.getDamage();
		if (this.character.getType() == BoomCharacterType.MONK.ordinal() || this.character.getType() == BoomCharacterType.WARRIOR.ordinal()) {
			range = ((this.atkRange * this.getWidth()) / 100);
			damage = getDamage();
		}
		if (effectType == BoomGameItemEffect.MONK_ATTACK || effectType == BoomGameItemEffect.IRON_FIST) {
			activeAbilities.add(new BoomPlayerAbility(1, damage, range, validEffect));
		} else {
			activeAbilities.add(new BoomPlayerAbility(2, damage, range, validEffect));
		}
	}
	
	public List<BoomSprite> checkActiveAbility(List<BoomSprite> collisions, List<BoomPlayer> players,
			List<BoomGameBomb> bombs, List<BoomItem> itemsList, BoomIDGenerator idGenerator, BoomGameItemUtils itemUtils, BoomPlayerScore playerScore) {
		if (activeAbilities.isEmpty()) {
			return Collections.emptyList();
		}
		List<BoomSprite> ret = new ArrayList<>();
		BoomPlayerAbility ability = activeAbilities.poll();
		BoomGameItemEffect effectType = ability.getEffect().getEffectType();
		switch (effectType) {
		case MONK_ATTACK:
		case WARRIOR_ATTACK:
		case IRON_FIST:
		case IRON_SWORD: {
			int newX = getX();
			int newY = getY();
			int adjX = 0, adjY = 0, adjW = 0, adjH = 0;
			if (isFreeMove()) {
				adjX = BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE;
				adjY = BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE;
				adjW = -(BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE * 2);
				adjH = -(BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE * 2);
			}
			int boostArea = ability.getRange();
			BoomSpriteState sState = BoomSpriteState.IDLE_DOWN;
			if (getDirection() == BoomDirectionEnum.UP.ordinal()) {
				newY -= boostArea;
				sState = BoomSpriteState.IDLE_UP;
			} else if (getDirection() == BoomDirectionEnum.DOWN.ordinal()) {
				sState = BoomSpriteState.IDLE_DOWN;
				newY += boostArea;
			} else if (getDirection() == BoomDirectionEnum.LEFT.ordinal()) {
				sState = BoomSpriteState.IDLE_LEFT;
				newX -= boostArea;
			} else if (getDirection() == BoomDirectionEnum.RIGHT.ordinal()) {
				sState = BoomSpriteState.IDLE_RIGHT;
				newX += boostArea;
			}
			BoomSprite ba, tmp;
			if (effectType == BoomGameItemEffect.MONK_ATTACK || effectType == BoomGameItemEffect.WARRIOR_ATTACK) {
				ba = new BoomSprite(ability.getId(), newX, newY, getWidth(), getHeight());
				tmp = new BoomSprite(ability.getId(), newX + adjX, newY + adjY, getWidth() + adjW, getHeight() + adjH);
			} else {
				ba = new BoomPlayerSkillEffect(getId(), ability.getDamage(), ability.getId(), newX, newY, getWidth(), getHeight());
				tmp = ba;
			}
			boolean hit = false;
			for (BoomItem bi : itemsList) {
				if (BoomUtils.checkCollision(tmp, bi)) {
					bi.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_ITEM_GONE);
					hit = true;
				}
			}
			for (BoomSprite bs : collisions) {
				if (!bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE) || bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
					continue;
				}
				if (BoomUtils.checkCollision(tmp, bs)) {
					BoomGameItem item = itemUtils.getSpawnItem(players);
					if (item != null) {
						itemsList.add(new BoomItem(idGenerator.getNextItemId(), item.getId(), item.getImageID(), bs.getX(), bs.getY(), bs.getWidth(), bs.getHeight()));
					}
					bs.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED);
					hit = true;
				}
			}
			for (BoomPlayer bp : players) {
				if (bp.isDead() || this.equals(bp)) {
					continue;
				}
				if (BoomUtils.checkCollision(tmp, bp)) {
					if (!bp.checkShieldProtectorEffect()) {
						int nDamage = bp.checkDamageBlockedEffect(ability.getDamage());
						if (nDamage > 0) {
							if (!bp.checkDamageAbsorbEffect(nDamage)) {
								bp.subHp(nDamage);
								//
								if (bp.isDead()) {
									playerScore.addScore(getId(), BoomGameManager.BOOM_GAME_REWARD_POINT_ON_KILL);
								}
							}
						}
					}
					hit = true;
				}
			}
			for (BoomGameBomb bomb : bombs) {
				if (!bomb.isValid()) {
					continue;
				}
				if (BoomUtils.checkCollision(tmp, bomb)) {
					if (effectType == BoomGameItemEffect.MONK_ATTACK || effectType == BoomGameItemEffect.IRON_FIST) {
						bomb.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_PUSHED);
						bomb.setDirection(getDirection());
					} else {
						bomb.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_EXPLODED);
					}
					hit = true;
				}
			}
			ba.setState(sState);
			
			if (effectType == BoomGameItemEffect.MONK_ATTACK || effectType == BoomGameItemEffect.WARRIOR_ATTACK) {
			} else if (!hit){
				ba.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_ABILITY_MOVING);
				if (effectType == BoomGameItemEffect.IRON_FIST) {
					ba.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_ABILITY_EFFECT_PUSH);
				}
				ba.setDirection(getDirection());
			}
			ret.add(ba);
		}
			break;
		default:
			break;
		}
		return ret;
	}
	
	public void requestBombCreation(List<BoomGameBomb> bombs) {
		if (isDead()) {
			return;
		}
 		
//		int curBombCount = 0;
//		for (BoomGameBomb bomb : bombs) {
//			if (!bomb.isValid()) {
//				continue;
//			}
//			if (bomb.getPlayerId() == this.id) {
//				curBombCount++;
//			}
//		}
//		if (curBombCount >= getBoomPoolSize()) {
//			return;
//		}
		if (bombCreationRequest.isEmpty()) {
			BoomGameBomb bomb = new BoomGameBomb(getId(), getBombType(), 0, 0, getWidth(), getHeight(), getBombSize(), getBombSize(), this.getDamage());
			if (checkIfHasValidEffect(BoomGameItemEffect.QUICK_BOMBER)) {
				bomb.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_FAST_EXPLODE);
			}
			bombCreationRequest.push(bomb);
		}
	}
	
	public void checkAndCreateNewBomb(List<BoomGameBomb> bombs, List<BoomPlayer> playersList, BoomIDGenerator idGenerator) {
		if (isDead()) {
			return;
		}
		if (bombCreationRequest.isEmpty()) {
			return;
		}
		int bx = getX();
		int by = getY();
		int modX = bx % getWidth();
		int modY = by % getHeight();
		if (getDirection() == BoomDirectionEnum.UP.ordinal()) {
			if (!isFreeMove()) {
				if (modY > 0 && modY <= getHeight() / 2) {
					return;
				}
				if (modY != 0) {
					by += getHeight() - modY;
				}
			}
		} else if (getDirection() == BoomDirectionEnum.DOWN.ordinal()) {
			if (!isFreeMove()) {
				if (modY >= getHeight() / 2) {
					return;
				}
				by -= modY;
			}
		} else if (getDirection() == BoomDirectionEnum.LEFT.ordinal()) {
			if (!isFreeMove()) {
				if (modX > 0 && modX <= getWidth() / 2) {
					return;
				}
				if (modX != 0) {
					bx += getWidth() - modX;
				}
			}
		} else if (getDirection() == BoomDirectionEnum.RIGHT.ordinal()) {
			if (!isFreeMove()) {
				if (modX >= getWidth() / 2) {
					return;
				}
				bx -= modX;
			}
		}
		BoomGameBomb boom = bombCreationRequest.pop();
		boom.setX(bx);
		boom.setY(by);
		int curBombCount = 0;
		for (BoomGameBomb bomb : bombs) {
			if (!bomb.isValid()) {
				continue;
			}
			if (BoomUtils.checkCollision(boom, bomb)) {
				return;
			}
			if (bomb.getPlayerId() == this.id) {
				curBombCount++;
			}
		}
		if (curBombCount >= getBoomPoolSize()) {
			return;
		}
		for (BoomPlayer bp : playersList) {
			if (this.equals(bp) || bp.isDead()) {
				continue;
			}
			if (BoomUtils.checkCollision(boom, bp)) {
				return;
			}
		}
		if (checkGodHandEffect()) {
			boom.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_THROWN);
			boom.setDirection(getDirection());
		}
		long start = System.nanoTime();
		boom.setStart(start);
		if (boom.isFastExplode()) {
			boom.setEnd(start + (long)(BoomGameManager.NANO_SECOND * BoomGameManager.BOOM_GAME_BOMB_DELAY_EXPLODE_WITH_FAST_EXPLOSION)); // 1.5 second
		} else {
			boom.setEnd(start + (long)(BoomGameManager.NANO_SECOND * BoomGameManager.BOOM_GAME_BOMB_DELAY_EXPLODE)); // 3 second
		}
		boom.setId(idGenerator.getNextBombId());
		bombs.add(boom);
		lastCreatedBomb = boom;
	}
	
	private boolean isOppositeOrSameDirection(BoomDirectionEnum direction) {
		if (getDirection() == direction.ordinal()) {
			return true;
		}
		if (getDirection() == BoomDirectionEnum.UP.ordinal() && direction == BoomDirectionEnum.DOWN) {
			return true;
		}
		if (getDirection() == BoomDirectionEnum.DOWN.ordinal() && direction == BoomDirectionEnum.UP) {
			return true;
		}
		if (getDirection() == BoomDirectionEnum.LEFT.ordinal() && direction == BoomDirectionEnum.RIGHT) {
			return true;
		}
		if (getDirection() == BoomDirectionEnum.RIGHT.ordinal() && direction == BoomDirectionEnum.LEFT) {
			return true;
		}
		return false;
	}
	
	public void movePlayer(BoomDirectionEnum direction) {
		if (isDead()) {
			return;
		}
		if (direction == null) {
			this.velocity = 0;
			return;
		}
		if (!isOppositeOrSameDirection(direction) && !isFreeMove() && isPlayerNotInSideBlock()) {
			this.velocity = 0;
			return;
		} else {
			this.velocity = this.movementSpeed;
			setDirection(direction.ordinal());
		}
	}

	public void update(int maxX, int maxY, List<BoomSprite> collisions, List<BoomPlayer> players,
			List<BoomGameBomb> bombs, List<BoomItem> itemsList, List<BoomGameTeleportPortal> portals, BoomGameItemUtils itemUtils, List<BoomSprite> fireWalls) {
		if (isDead()) {
			return;
		}
		updateSpecialAbilityGauge();
		checkFireWall(fireWalls);
		if (checkTeleportPlayer(maxX, maxY, collisions, players, bombs, itemsList, portals, itemUtils)) {
			return;
		}
		if (checkIfHasValidEffect(BoomGameItemEffect.HUNTER_TRAP)) {
			return;
		}
		if ( this.velocity != 0 || ( !isFreeMove() && isPlayerNotInSideBlock() ) ) {
			int newX = getX();
			int newY = getY();
			int width = getWidth();
			int height = getHeight();
			int adjX = getAdjustX(), adjY = getAdjustY(), adjW = getAdjustWidth(), adjH = getAdjustHeight();
			int boostSpeed = (this.velocity == 0 ? this.movementSpeed : this.velocity) + getSpeedBoost() + getSpeedAdd();
			if (boostSpeed > this.getMaxSpeed()) {
				boostSpeed = this.getMaxSpeed();
			}
			// highest priority
			if (checkIfHasValidEffect(BoomGameItemEffect.SUPER_GLUE)) {
				boostSpeed = this.movementSpeed / 2;
			}
			if (getDirection() == BoomDirectionEnum.UP.ordinal()) {
				if (this.velocity == 0) {
					boostSpeed = CommonMethod.min(boostSpeed, (getY() % getHeight()));
				}
				newY -= boostSpeed;
			} else if (getDirection() == BoomDirectionEnum.DOWN.ordinal()) {
				if (this.velocity == 0) {
					boostSpeed = CommonMethod.min(boostSpeed, getHeight() - (getY() % getHeight()));
				}
				newY += boostSpeed;
			} else if (getDirection() == BoomDirectionEnum.LEFT.ordinal()) {
				if (this.velocity == 0) {
					boostSpeed = CommonMethod.min(boostSpeed, (getX() % getWidth()));
				}
				newX -= boostSpeed;
			} else if (getDirection() == BoomDirectionEnum.RIGHT.ordinal()) {
				if (this.velocity == 0) {
					boostSpeed = CommonMethod.min(boostSpeed, getWidth() - (getX() % getWidth()));
				}
				newX += boostSpeed;
			}
			if (newX < 0) {
				setX(0);
				return;
			}
			if (newX + width > maxX) {
				setX(maxX - width);
				return;
			}
			if (newY < 0) {
				setY(0);
				return;
			}
			if (newY + height > maxY) {
				setY(maxY - height);
				return;
			}
			// walk through everything
			if (checkIfHasValidEffect(BoomGameItemEffect.DARKNESS_COVER)) {
				hasActiveCloak = true;
				setX(newX);
				setY(newY);
				return;
			} else if (hasActiveCloak) {
				hasActiveCloak = false;
				updayePlayerPosition(collisions, players, bombs, itemsList);
				return;
			}
			for (BoomSprite bs : collisions) {
				if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
					continue;
				}
				if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bs)) {
					correctPossion(boostSpeed, bs);
					return;
				}
			}
			for (BoomPlayer bp : players) {
				if (this.equals(bp) || bp.isDead()) {
					continue;
				}
				if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bp)) {
					correctPossion(boostSpeed, bp);
					return;
				}
			}
			for (BoomItem bi : itemsList) {
				if (!bi.isValid()) {
					continue;
				}
				if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bi)) {
					if (applyItem(bi, itemUtils)) {
						bi.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_ITEM_GONE);
						break;
					}
				}
			}
			boolean b = false;
			for (BoomGameBomb bomb : bombs) {
				if (!bomb.isValid()) {
					continue;
				}
				if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bomb)) {
					if (this.lastCreatedBomb != null && this.lastCreatedBomb.equals(bomb)) {
						b = true;
						continue;
					}
					correctPossion(boostSpeed, bomb);
					return;
				}
			}
			setX(newX);
			setY(newY);
			if (!b) {
				lastCreatedBomb = null;
			}
		}
	}
	
	private void checkFireWall(List<BoomSprite> fireWalls) {
		for (BoomSprite bs : fireWalls) {
			if (BoomUtils.checkCollision(this, bs)) {
				double damage = (double)(BoomGameManager.FIRE_WALL_SUDDEN_DEATH_DAMAGE * getMaxHp()) / BoomGameManager.FIRE_WALL_SUDDEN_DEATH_ADJUST;
				if (damage > 0) {
					subHp(damage);
				}
				return;
			}
		}
	}
	
	private boolean checkTeleportPlayer(int maxX, int maxY, List<BoomSprite> collisions, List<BoomPlayer> players,
			List<BoomGameBomb> bombs, List<BoomItem> itemsList, List<BoomGameTeleportPortal> portals, BoomGameItemUtils itemUtils) {
		for (BoomGameTeleportPortal portal : portals) {
			if (BoomUtils.checkCollision(this, portal)) {
				if (portal.isPortalOpen() && portal.canTeleport(this, maxX, maxY, collisions, players, bombs)) {
					portal.activate();
					setX(portal.getLinkedPortal().getX());
					setY(portal.getLinkedPortal().getY());
					for (BoomItem bi : itemsList) {
						if (!bi.isValid()) {
							continue;
						}
						if (BoomUtils.checkCollision(this, bi)) {
							if (applyItem(bi, itemUtils)) {
								bi.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_ITEM_GONE);
								break;
							}
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	private void updayePlayerPosition(List<BoomSprite> collisions, List<BoomPlayer> players,
			List<BoomGameBomb> bombs, List<BoomItem> itemsList) {
		boolean check = false;
//		int step = 0;
//		int attemp = 0;
//		do {
//			
//			step += 4;
//			attemp++;
//		} while(!check && attemp < 192);
//		for (BoomItem bi : itemsList) {
//			if (!bi.isValid()) {
//				continue;
//			}
//			if (BoomUtils.checkCollision(this, bi)) {
//				applyItem(bi);
//				bi.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_ITEM_GONE);
//				break;
//			}
//		}
	}
	
	private boolean isPlayerNotInSideBlock() {
		return (getX() % getWidth() != 0 || getY() % getHeight() != 0);
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
		int adjX = getAdjustX(), adjY = getAdjustY(), adjW = getAdjustWidth(), adjH = getAdjustHeight();
		while (boostSpeed > 0) {
			if (BoomUtils.checkCollision(getX() + addX + adjX, getY() + addY + adjY, getWidth() + adjW, getHeight() + adjH, collision)) {
				return;
			}
			addX(addX);
			addY(addY);
			boostSpeed -= 1;
		}
	}

	private boolean applyItem(BoomItem bi, BoomGameItemUtils itemUtils) {
		boolean ret = false;
		BoomGameItem item = itemUtils.getBoomGameItemById(bi.getItemId());
		for (int i = 0; i < BoomGameItem.MAX_ITEM_EFFECT; i++) {
			int effectID = item.getItemEffectId(i + 1);
			if (effectID <= 0) {
				continue;
			}
			int effectParam = item.getItemEffectParam(i + 1);
			BoomGameItemEffect effect = BoomGameItemEffect.valueOf(effectID);
			if (effect == BoomGameItemEffect.NONE) {
				continue;
			}
			switch (effect) {
			case TAKE_DAMAGE:
			{
				int damage = checkDamageBlockedEffect((getMaxHp() * effectParam) / 100);
				if (damage > 0) {
					if (!checkDamageAbsorbEffect(damage)) {
						subHp(damage);
					}
				}
				ret = true;
			}
				break;
			case RECOVER_HP:
			{
				addHp(effectParam);
				ret = true;
			}
				break;
			case BOOST_BOMB_RANGE:
			{
				if (isBomber()) {
					addAttakRange(effectParam);
					ret = true;
				}
			}
				break;
			case BOOST_BOMB_DAMAGE:
			{
				if (isBomber()) {
					addDamage(effectParam);
					ret = true;
				}
			}
				break;
			case BOOST_BOOM_POOL_SIZE:
			{
				if (isBomber()) {
					addAttackSpeed(effectParam);
					ret = true;
				}
			}
				break;
			case BOOST_ATTACK_SPEED:
			{
				if (!isBomber()) {
					addAttackSpeed(effectParam);
					ret = true;
				}
			}
				break;
			case BOOST_ATTACK_DAMAGE:
			{
				if (!isBomber()) {
					addDamage(effectParam);
					ret = true;
				}
			}
				break;
			case BOOST_ATTACK_RANGE:
			{
				if (!isBomber()) {
					addAttakRange(effectParam);
					ret = true;
				}
			}
				break;
			case IRON_FIST:
			case IRON_SWORD: {
				if (isBomber()) {
					long start = System.nanoTime();
					long end = start + BoomGameManager.NANO_SECOND * item.getItemEffectDuration(i + 1);
					if (effect == BoomGameItemEffect.IRON_FIST) {
						ret = replaceOrAddEffect(new BoomPlayerItemEffect(effectID, effect, effectParam, start, end), BoomGameItemEffect.IRON_SWORD.getId());;
					} else {
						ret = replaceOrAddEffect(new BoomPlayerItemEffect(effectID, effect, effectParam, start, end), BoomGameItemEffect.IRON_FIST.getId());;
					}
				}
			}
				break;
			case QUICK_BOMBER:
			case BOMB_SHOOTER: {
				if (isBomber()) {
					long start = System.nanoTime();
					long end = start + BoomGameManager.NANO_SECOND * item.getItemEffectDuration(i + 1);
					ret = addEffect(new BoomPlayerItemEffect(effectID, effect, effectParam, start, end));
				}
			}
				break;
			case INCREASE_SPEED: // to display status icon
			case SHEILD_PROTECTOR:
			case INVISIBLE_MAN:
			case HUNTER_TRAP:
			case ABSORB_DAMAGE:
			case SUPER_GLUE:
			case BOOST_SPEED:
			case WARRIOR_AMOR:
			case DARKNESS_COVER: {
				long start = System.nanoTime();
				long end = start + BoomGameManager.NANO_SECOND * item.getItemEffectDuration(i + 1);
				ret = addEffect(new BoomPlayerItemEffect(effectID, effect, effectParam, start, end));
			}
				break;
			default:
				break;
			}
		}
		return ret;
	}
	
	private boolean replaceOrAddEffect(BoomPlayerItemEffect source, int targetID) {
		if (this.effectsList.containsKey(targetID)) {
			this.effectsList.remove(targetID);
		}
		return addEffect(source);
	}

	private boolean addEffect(BoomPlayerItemEffect effect) {
		if (!this.effectsList.containsKey(effect.getId())) {
			this.effectsList.put(effect.getId(), effect);
			return true;
		}
		BoomPlayerItemEffect storedEffect = this.effectsList.get(effect.getId());
		return storedEffect.addOrRefreshEffect(effect);
	}
	
	public void extendEffectDuration(long extension) {
		if (extension <= 0) {
			return;
		}
		if (this.effectsList == null || this.effectsList.isEmpty()) {
			return;
		}
		for (Integer id : this.effectsList.keySet()) {
			BoomPlayerItemEffect eff = this.effectsList.get(id);
			eff.extendDuration(extension);
		}
	}
}
