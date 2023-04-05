package dev.boom.game.boom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import dev.boom.common.CommonMethod;
import dev.boom.services.BoomGameItem;

public class BoomPlayer extends BoomSprite {

	private long id;
	private String name;
	private String fullname;
	private double hp;
	private int velocity;
	private boolean isMoving;
	private BoomCharater character;
	private int damage;
	private int atkSpeed;
	private int atkRange;
	private int movementSpeed;
	private boolean hasCharClasses;
	private boolean hasPhantomEffect;
	private long lastActiveAbilityTime;
	private int currentGauge = 0;
	private BoomGameBomb lastCreatedBomb;
	private Map<Integer, BoomPlayerItemEffect> effectsList;
	private Stack<BoomGameBomb> bombCreationRequest;
	private Queue<BoomPlayerAbility> activeAbilities;
	private int bombType;
	private long groupId;
	private String groupName;
	private BoomGameRevivalParam revivalParam;
	private List<BoomDirectionEnum> directionList;// for confuse effect
	private Set<String> cacheBombExploded;

	public BoomPlayer(long id, String name, BoomCharater character, int x, int y, int width, int height) {
		super(character.getId(), x, y, width, height);
		this.id = id;
		this.fullname = name;
		this.name = this.fullname.split("\\.")[0];
		this.effectsList = new HashMap<>();
		this.activeAbilities = new LinkedList<>();
		this.lastActiveAbilityTime = System.nanoTime();
		this.bombCreationRequest = new Stack<>();
		this.revivalParam = new BoomGameRevivalParam();
		this.directionList = Arrays.asList(BoomDirectionEnum.UP, BoomDirectionEnum.DOWN, BoomDirectionEnum.LEFT, BoomDirectionEnum.RIGHT);
		this.cacheBombExploded = new HashSet<>();
		init(character);
	}
	
	public void init(BoomCharater character) {
		this.character = character;
		this.setImageID(this.character.getId());
		effectsList.clear();
		if (hasCharClasses()) {
			if (this.character.getType() == BoomCharacterType.MONK.ordinal()) {
				addEffect(new BoomPlayerItemEffect(BoomGameItemEffect.MONK_ATTACK, 0, 0, 0));
			} else if (this.character.getType() == BoomCharacterType.WARRIOR.ordinal()) {
				addEffect(new BoomPlayerItemEffect(BoomGameItemEffect.WARRIOR_ATTACK, 0, 0, 0));
			}
		} else {
			this.character = BoomCharater.CHARACTER_00;
		}
		this.initGroupEffect();
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
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
		int shp = (int)Math.round(hp);
		if (shp == 0 && hp > 0) {
			shp = 1;
		}
		return shp;
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
	
	public void recoverHP(int amount) {
		if (amount <= 0) {
			return;
		}
		if (checkIfHasValidEffect(BoomGameItemEffect.BLEEDING)) {
			amount = amount / 2;
		}
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

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
		BoomPlayerItemEffect effect = getValidEffect(checkEffect);
		return (effect != null);
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
		int amount = (effect.getEffectParam() * damage) / 100;
		recoverHP(amount);
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
		BoomPlayerItemEffect effect = getEffect(checkEffect);
		if (effect == null) {
			return null;
		}
		if (!effect.hasEffect()) {
			return null;
		}
		return effect;
	}
	
	private BoomPlayerItemEffect getEffect(BoomGameItemEffect checkEffect) {
		if (this.effectsList == null || this.effectsList.isEmpty()) {
			return null;
		}
		if (!this.effectsList.containsKey(checkEffect.getId())) {
			return null;
		}
		BoomPlayerItemEffect effect = this.effectsList.get(checkEffect.getId());
		return effect;
	}

	@Override
	public BoomSpriteState getState() {
		if (isDead()) {
			return BoomSpriteState.DEAD;
		}
		if (this.velocity > 0) {
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
	
	public void addBombExploded(String key) {
		cacheBombExploded.add(key);
	}
	
	public boolean isBombExploded(String key) {
		return cacheBombExploded.contains(key);
	}
	
	public void clearCacheBombExploded() {
		cacheBombExploded.clear();
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
			checkGauge = ((BoomGameManager.BOOM_GAME_GAUGE_MAX_VALUE * passTime) / ((BoomGameManager.NANO_SECOND / this.atkSpeed) * 100));// attack speed default multiply by 100
		} else {
			checkGauge = ((BoomGameManager.BOOM_GAME_GAUGE_MAX_VALUE * passTime) / (BoomGameManager.NANO_SECOND * BoomGameManager.BOOM_GAME_MELEE_ABILITY_DEFAULT_SPEED));
		}
		if (checkGauge > BoomGameManager.BOOM_GAME_GAUGE_MAX_VALUE) {
			this.currentGauge = BoomGameManager.BOOM_GAME_GAUGE_MAX_VALUE;
		} else if (checkGauge < 0) {
			this.currentGauge = 0;
		} else {
			this.currentGauge = (int)checkGauge;
		}
	}
	
	public void activateMainAbility() {
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
			requestBombCreation();
			return;
		}
	}
	
	private void activeAbility(BoomGameItemEffect effectType) {
		if (!activeAbilities.isEmpty()) {
			return;
		}
		if (this.currentGauge < BoomGameManager.BOOM_GAME_GAUGE_MAX_VALUE) {
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
		int bleedingDuration = 0;
		if (effectType == BoomGameItemEffect.MONK_ATTACK || effectType == BoomGameItemEffect.WARRIOR_ATTACK) {
			BoomPlayerItemEffect hexdrinkerEffect = getValidEffect(BoomGameItemEffect.HEXDRINKER);
			if (hexdrinkerEffect != null) {
				bleedingDuration = hexdrinkerEffect.getEffectParam();
			}
		}
		if (effectType == BoomGameItemEffect.MONK_ATTACK || effectType == BoomGameItemEffect.IRON_FIST) {
			activeAbilities.add(new BoomPlayerAbility(1, damage, range, validEffect, bleedingDuration)); // 1 is punch (boom-image.js -> EFFECT_IMAGE)
		} else {
			activeAbilities.add(new BoomPlayerAbility(2, damage, range, validEffect, bleedingDuration)); // 2 is sword (boom-image.js -> EFFECT_IMAGE)
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
			adjX = BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE;
			adjY = BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE;
			adjW = -(BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE * 2);
			adjH = -(BoomGameManager.BOOM_GAME_PLAYER_ADJUST_SIZE * 2);
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
			boolean meleeAttack = false;
			if (effectType == BoomGameItemEffect.MONK_ATTACK || effectType == BoomGameItemEffect.WARRIOR_ATTACK) {
				ba = new BoomSprite(ability.getId(), newX, newY, getWidth(), getHeight());
				tmp = new BoomSprite(ability.getId(), newX + adjX, newY + adjY, getWidth() + adjW, getHeight() + adjH);
				meleeAttack = true;
			} else {
				ba = new BoomPlayerSkillEffect(getId(), getGroupId(), ability.getDamage(), ability.getId(), newX, newY, getWidth(), getHeight());
				tmp = ba;
			}
			if (ability.isBleeding()) {
				ba.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_ABILITY_BLEEDDING);
			}
			boolean hit = false;
			for (BoomItem bi : itemsList) {
				if (BoomUtils.isDestroyableItem(bi.getItemId()) && BoomUtils.checkCollision(tmp, bi)) {
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
					long start = System.nanoTime();
					long end;
					if (ability.isBleeding()) {
						end = start + BoomGameManager.NANO_SECOND * ability.getBleedingDuration();
						bp.addEffect(new BoomPlayerItemEffect(BoomGameItemEffect.BLEEDING, 0, start, end, new BoomGamePlayerEffectParam(getId(), getGroupId())));
					}
					boolean sameGroup = (getGroupId() > 0 && getGroupId() == bp.getGroupId());
					if (meleeAttack && sameGroup) {
						// ally melee attack (take no damage but effect for 2 seconds)
						end = start + BoomGameManager.NANO_SECOND * BoomGameManager.BOOM_GAME_ALLY_ATTACK_EFFECT_LASTS;
						if (BoomUtils.boolRandom()) {
							bp.applyConfuseEffect(start, end);
						} else {
							bp.addEffect(new BoomPlayerItemEffect(BoomGameItemEffect.SUPER_GLUE, 0, start, end));
						}
					} else {
						if (!bp.checkShieldProtectorEffect()) {
							int nDamage = bp.checkDamageBlockedEffect(ability.getDamage());
							if (nDamage > 0) {
								if (!bp.checkDamageAbsorbEffect(nDamage)) {
									bp.subHp(nDamage);
									//
									if (bp.isDead() && !sameGroup) {
										playerScore.addScore(getId(), BoomGameManager.BOOM_GAME_REWARD_POINT_ON_KILL);
									}
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
	
	public void requestBombCreation() {
		if (isDead()) {
			return;
		}
		if (bombCreationRequest.isEmpty()) {
			BoomGameBomb bomb = new BoomGameBomb(getId(), getBombType(), 0, 0, getWidth(), getHeight(), getBombSize(), getBombSize(), this.getDamage());
			bomb.setGroupId(getGroupId());
			if (checkIfHasValidEffect(BoomGameItemEffect.QUICK_BOMBER)) {
				bomb.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_FAST_EXPLODE);
			}
			BoomPlayerItemEffect holyPoder = getValidEffect(BoomGameItemEffect.HOLYPOWDER);
			if (holyPoder != null) {
				bomb.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_BOMD_CONFUSED);
				bomb.setConfusionDuration(holyPoder.getEffectParam());
			}
			bombCreationRequest.push(bomb);
		}
	}
	
	public void checkAndCreateNewBomb(List<BoomSprite> collisions, List<BoomGameBomb> bombs, List<BoomPlayer> playersList, BoomIDGenerator idGenerator) {
		if (isDead()) {
			return;
		}
		if (bombCreationRequest.isEmpty()) {
			return;
		}
		int bx = getX();
		int by = getY();
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
		for (BoomSprite bs : collisions) {
			if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
				continue;
			}
			if (BoomUtils.checkCollision(this, bs)) {
				return;
			}
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
	
	public void movePlayer(BoomDirectionEnum direction) {
		if (isDead()) {
			return;
		}
		if (direction == null) {
			//this.velocity = 0;
			this.isMoving = false;
			return;
		}
		this.isMoving = true;
		if (checkIfHasValidEffect(BoomGameItemEffect.CONFUSION)) {
			direction = this.directionList.get(direction.ordinal() % this.directionList.size());
		}
		//this.velocity = this.movementSpeed;
		setDirection(direction.ordinal());
	}
	
	public void update(int maxX, int maxY, List<BoomSprite> collisions, List<BoomPlayer> players,
			List<BoomGameBomb> bombs, List<BoomItem> itemsList, List<BoomGameTeleportPortal> portals, BoomGameItemUtils itemUtils, List<BoomSprite> fireWalls, List<BoomItem> specialItems, BoomPlayerScore playerScore) {
		if (isDead()) {
			if (revivalParam.getRevivalCount() == 0) {
				checkRevivalState(players);
				for (BoomPlayer bp : players) {
					if (this.equals(bp) || bp.isDead()) {
						continue;
					}
					if (BoomUtils.checkCollision(this, bp)) {
						checkForRevival(bp);
					}
				}
			}
			return;
		}
		updateSpecialAbilityGauge();
		checkFireWall(fireWalls);
		checkPhantomEffect(collisions, players, bombs);
		checkBleedingEffect(playerScore);
		checkSpecialtItems(specialItems, itemUtils);
		if (checkTeleportPlayer(maxX, maxY, collisions, players, bombs, itemsList, portals, itemUtils)) {
			return;
		}
		if (checkIfHasValidEffect(BoomGameItemEffect.HUNTER_TRAP)) {
			return;
		}
		if (this.isMoving) {
			this.velocity = Math.min(this.movementSpeed, this.velocity + BoomGameManager.BOOM_PLAYER_ACCELERATION);
		} else {
			this.velocity = Math.max(0, this.velocity - 2 * BoomGameManager.BOOM_PLAYER_ACCELERATION);
		}
		if ( this.velocity > 0 ) {
			int newX = getX();
			int newY = getY();
			int width = getWidth();
			int height = getHeight();
			int adjX = getAdjustX(), adjY = getAdjustY(), adjW = getAdjustWidth(), adjH = getAdjustHeight();
			int boostSpeed = this.velocity + getSpeedBoost() + getSpeedAdd();
			if (boostSpeed > this.getMaxSpeed()) {
				boostSpeed = this.getMaxSpeed();
			}
			// highest priority
			if (checkIfHasValidEffect(BoomGameItemEffect.SUPER_GLUE)) {
				boostSpeed = this.movementSpeed / 2;
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
			if (newX + adjX < 0) {
				correctPossion(maxX, maxY, new BoomSprite(0, 0, 0, 0, maxY), collisions, null, null);
				//setX(0);
				return;
			}
			if (newX + adjX + width + adjW > maxX) {
				correctPossion(maxX, maxY, new BoomSprite(0, maxX, 0, 0, maxY), collisions, null, null);
				//setX(maxX - width);
				return;
			}
			if (newY + adjY < 0) {
				correctPossion(maxX, maxY, new BoomSprite(0, 0, 0, maxX, 0), collisions, null, null);
				//setY(0);
				return;
			}
			if (newY + adjY + height + adjH > maxY) {
				correctPossion(maxX, maxY, new BoomSprite(0, 0, maxY, maxX, 0), collisions, null, null);
				//setY(maxY - height);
				return;
			}
			// walk through everything
			if (!hasPhantomEffect) {
				for (BoomSprite bs : collisions) {
					if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
						continue;
					}
					if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bs)) {
						correctPossion(maxX, maxY, bs, collisions, players, bombs);
						slideOnCollision(maxX, maxY, newX + adjX, newY + adjY, width + adjW, height + adjH, bs.getX() + bs.getAdjustX(), bs.getY() + bs.getAdjustY(), bs.getWidth() + bs.getAdjustWidth(), bs.getHeight() + bs.getAdjustHeight(), collisions, players, bombs, itemsList, itemUtils);
						return;
					}
				}
				boolean b = false;
				for (BoomPlayer bp : players) {
					if (this.equals(bp) || bp.isDead()) {
						continue;
					}
					if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bp)) {
						if (revivalParam.checkLastRevival(bp)) {
							b = true;
							continue;
						}
						correctPossion(maxX, maxY, bp, null, players, bombs);
						return;
					}
				}
				if (!b) {
					revivalParam.clearLastRevival();
				}
				
				b = false;
				for (BoomGameBomb bomb : bombs) {
					if (!bomb.isValid()) {
						continue;
					}
					if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bomb)) {
						if (this.lastCreatedBomb != null && this.lastCreatedBomb.equals(bomb)) {
							b = true;
							continue;
						}
						correctPossion(maxX, maxY, bomb, null, null, bombs);
						return;
					}
				}
				if (!b) {
					lastCreatedBomb = null;
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
			setX(newX);
			setY(newY);
		}
	}
	
	private void slideOnCollision(int maxX, int maxY, int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2, 
			List<BoomSprite> collisions, List<BoomPlayer> players, List<BoomGameBomb> bombs, List<BoomItem> itemsList, BoomGameItemUtils itemUtils) {
		final int slideValue = 2;
		final int slideCheck = 16;
		int newX = getX();
		int newY = getY();
		int width = getWidth();
		int height = getHeight();
		int adjX = getAdjustX(), adjY = getAdjustY(), adjW = getAdjustWidth(), adjH = getAdjustHeight();
		if (getDirection() == BoomDirectionEnum.UP.ordinal() || getDirection() == BoomDirectionEnum.DOWN.ordinal()) {
			if (x1 + w1 - x2 <= slideCheck) {
				newX -= slideValue;
			} else if (x2 + w2 - x1 <= slideCheck) {
				newX += slideValue;
			}
		} else if (getDirection() == BoomDirectionEnum.LEFT.ordinal() || getDirection() == BoomDirectionEnum.RIGHT.ordinal()) {
			if (y1 + h1 - y2 <= slideCheck) {
				newY -= slideValue;
			} else if (y2 + h2 - y1 <= slideCheck) {
				newY += slideValue;
			}
		}
		if (newX + adjX < 0 || newX + adjX + width + adjW > maxX || 
				newY + adjY < 0 || newY + adjY + height + adjH > maxY) {
			return;
		}
		for (BoomSprite bs : collisions) {
			if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
				continue;
			}
			if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bs)) {
				return;
			}
		}
		for (BoomPlayer bp : players) {
			if (this.equals(bp) || bp.isDead()) {
				continue;
			}
			if (BoomUtils.checkCollision(newX + adjX, newY + adjY, width + adjW, height + adjH, bp)) {
				return;
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
				return;
			}
		}
		if (!b) {
			lastCreatedBomb = null;
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
		setX(newX);
		setY(newY);
	}
	
	private void checkSpecialtItems(List<BoomItem> specialItems, BoomGameItemUtils itemUtils) {
		if (specialItems == null || specialItems.isEmpty()) {
			return;
		}
		for (BoomItem bi : specialItems) {
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
	}
	
	private void checkBleedingEffect(BoomPlayerScore playerScore) {
		BoomPlayerItemEffect effect = getValidEffect(BoomGameItemEffect.BLEEDING);
		
		if (effect != null) {
			double damage = (double)(BoomGameManager.BLEEDING_PARAM_EFFECT_DAMAGE * getMaxHp()) / BoomGameManager.BLEEDING_PARAM_EFFECT_ADJUST;
			if (damage > 0) {
				subHp(damage);
				if (effect.getParam().getPlayerId() > 0) {
					boolean sameGroup = (effect.getParam().getGroupId() > 0 && effect.getParam().getGroupId() == getGroupId());
					if (isDead() && !sameGroup) {
						playerScore.addScore(effect.getParam().getPlayerId(), BoomGameManager.BOOM_GAME_REWARD_POINT_ON_KILL);
					}
				}
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
	
	private void checkPhantomEffect(List<BoomSprite> collisions, List<BoomPlayer> players, List<BoomGameBomb> bombs) {
		BoomPlayerItemEffect effect = getValidEffect(BoomGameItemEffect.PHANTOM);
		if (effect != null) {
			hasPhantomEffect = true;
			return;
		}
		if (!hasPhantomEffect) {
			return;
		}
		// effect already expired here
		boolean isCollisionState = isInCollisionState(collisions, players, bombs);
		if (!isCollisionState) {
			hasPhantomEffect = false;
			return;
		}
		double damage = (double)(BoomGameManager.PHANTOM_SIDE_EFFECT_DAMAGE * getMaxHp()) / BoomGameManager.PHANTOM_SIDE_EFFECT_ADJUST;
		if (damage > 0) {
			subHp(damage);
		}
	}
	
	private boolean isInCollisionState(List<BoomSprite> collisions, List<BoomPlayer> players, List<BoomGameBomb> bombs) {
		for (BoomSprite bs : collisions) {
			if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
				continue;
			}
			if (BoomUtils.checkCollision(this, bs)) {
				return true;
			}
		}
		for (BoomPlayer bp : players) {
			if (this.equals(bp) || bp.isDead()) {
				continue;
			}
			if (BoomUtils.checkCollision(this, bp)) {
				return true;
			}
		}
		for (BoomGameBomb bomb : bombs) {
			if (!bomb.isValid()) {
				continue;
			}
			if (BoomUtils.checkCollision(this, bomb)) {
				if (this.lastCreatedBomb != null && this.lastCreatedBomb.equals(bomb)) {
					continue;
				}
				return true;
			}
		}
		return false;
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
	
	private void correctPossion(int maxX, int maxY, BoomSprite target, List<BoomSprite> collisions, List<BoomPlayer> players, List<BoomGameBomb> bombs) {
		int boostSpeed = 0;
		int mx = 0, my = 0;
		if (getDirection() == BoomDirectionEnum.UP.ordinal()) {
			boostSpeed = (getY() + getAdjustY()) - (target.getY() + target.getAdjustY() + target.getHeight() + target.getAdjustHeight());
			my = -1;
		} else if (getDirection() == BoomDirectionEnum.DOWN.ordinal()) {
			boostSpeed = (target.getY() + target.getAdjustY()) - (getY() + getAdjustY() + getHeight() + getAdjustHeight());
			my = 1;
		} else if (getDirection() == BoomDirectionEnum.LEFT.ordinal()) {
			boostSpeed = (getX() + getAdjustX()) - (target.getX() + target.getAdjustX() + target.getWidth() + target.getAdjustWidth());
			mx = -1;
		} else if (getDirection() == BoomDirectionEnum.RIGHT.ordinal()) {
			boostSpeed = (target.getX() + target.getAdjustX()) - (getX() + getAdjustX() + getHeight() + getAdjustHeight());
			mx = 1;
		}
		if (boostSpeed <= 0) {
			return;
		}
		int addX = 0, addY = 0;
		while (boostSpeed > 0) {
			addX = (boostSpeed * mx);
			addY = (boostSpeed * my);
			boolean canMove = true;
			if (getX() + getAdjustX() + addX < 0 || getX() + getAdjustX() + addX + getHeight() + getAdjustHeight() > maxX || 
					getY() + getAdjustY() + addY < 0 || getY() + getAdjustY() + getHeight() + getAdjustHeight() + addY > maxY) {
				canMove = false;;
			}
			if (canMove && collisions != null) {
				for (BoomSprite bs : collisions) {
					if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
						continue;
					}
					if (BoomUtils.checkCollision(getX() + addX + getAdjustX(), getY() + addY + getAdjustY(), getWidth() + getAdjustWidth(), getHeight() + getAdjustHeight(), bs)) {
						canMove = false;
						break;
					}
				}
			}
			if (canMove && players != null) {
				for (BoomPlayer bp : players) {
					if (this.equals(bp) || bp.isDead()) {
						continue;
					}
					if (BoomUtils.checkCollision(getX() + addX + getAdjustX(), getY() + addY + getAdjustY(), getWidth() + getAdjustWidth(), getHeight() + getAdjustHeight(), bp)) {
						canMove = false;
						break;
					}
				}
			}
			if (canMove && bombs != null) {
				for (BoomGameBomb bomb : bombs) {
					if (!bomb.isValid()) {
						continue;
					}
					if (BoomUtils.checkCollision(getX() + addX + getAdjustX(), getY() + addY + getAdjustY(), getWidth() + getAdjustWidth(), getHeight() + getAdjustHeight(), bomb)) {
						canMove = false;
						break;
					}
				}
			}
			if (canMove) {
				addX(addX);
				addY(addY);
				break;
			}
			boostSpeed--;
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
				if (effectParam > 0) {
					int amount = CommonMethod.min(getMaxHp(), effectParam * getMaxHp() / 100);
					recoverHP(amount);
				}
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
						ret = replaceOrAddEffect(new BoomPlayerItemEffect(effect, effectParam, start, end), BoomGameItemEffect.IRON_SWORD.getId());;
					} else {
						ret = replaceOrAddEffect(new BoomPlayerItemEffect(effect, effectParam, start, end), BoomGameItemEffect.IRON_FIST.getId());;
					}
				}
			}
				break;
			case QUICK_BOMBER:
			case HOLYPOWDER:
			case BOMB_SHOOTER: {
				if (isBomber()) {
					long start = System.nanoTime();
					long end = start + BoomGameManager.NANO_SECOND * item.getItemEffectDuration(i + 1);
					ret = addEffect(new BoomPlayerItemEffect(effect, effectParam, start, end));
				}
			}
				break;
			case HEXDRINKER: {
				if (!isBomber()) {
					long start = System.nanoTime();
					long end = start + BoomGameManager.NANO_SECOND * item.getItemEffectDuration(i + 1);
					ret = addEffect(new BoomPlayerItemEffect(effect, effectParam, start, end));
				}
			}
				break;
			case INCREASE_SPEED: // just to display status icon for boots of speed
			case SHEILD_PROTECTOR:
			case INVISIBLE_MAN:
			case HUNTER_TRAP:
			case ABSORB_DAMAGE:
			case SUPER_GLUE:
			case BOOST_SPEED:
			case WARRIOR_AMOR:
			case PHANTOM:
			case MAGICAL_REVIVAL: {
				long start = System.nanoTime();
				long end = start + BoomGameManager.NANO_SECOND * item.getItemEffectDuration(i + 1);
				ret = addEffect(new BoomPlayerItemEffect(effect, effectParam, start, end));
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
	
	private void checkRevivalState(List<BoomPlayer> players) {
		if (revivalParam.getRevivalId() <= 0) {
			return;
		}
		for (BoomPlayer bp : players) {
			if (this.equals(bp) || bp.isDead()) {
				continue;
			}
			if (bp.getId() == revivalParam.getRevivalId() && !BoomUtils.checkCollision(this, bp)) {
				revivalParam.reset();
				return;
			}
		}
	}
	
	private void checkForRevival(BoomPlayer source) {
		if (!isDead()) {
			// still alive
			return;
		}
		if (source == null) {
			// no subject
			return;
		}
		if (getGroupId() <= 0 || source.getGroupId() <= 0) {
			// no group
			return;
		}
		if (getGroupId() != source.getGroupId()) {
			// different group
			return;
		}
		if (revivalParam.getRevivalId() > 0 && revivalParam.getRevivalId() != source.getId()) {
			// different person
			return;
		}
		BoomPlayerItemEffect effect = source.getValidEffect(BoomGameItemEffect.MAGICAL_REVIVAL);
		if (effect == null) {
			return;
		}
		if (revivalParam.getRevivalId() == 0) {
			revivalParam.init(source);
		}
		else if (revivalParam.getRevivalId() == source.getId()) {
			if (!revivalParam.check(source)) {
				revivalParam.reset();
				return;
			}
		}
		// increase gauge
		revivalParam.incRevivalGauge();
		if (revivalParam.isGaugeMax()) {
			revive();
			revivalParam.setLastRevival(source);
			source.revivalParam.setLastRevival(this);
			effect.subEffectParam(1);
			return;
		}
	}
	
	private void revive() {
		setHp(getMaxHp());
		Map<Integer, BoomPlayerItemEffect> newEffectsList = new HashMap<>();
		if (this.effectsList != null && !this.effectsList.isEmpty()) {
			for (Integer id : this.effectsList.keySet()) {
				BoomPlayerItemEffect eff = this.effectsList.get(id);
				switch (eff.getEffectType()) {
				case INCREASE_SPEED:
				case MONK_ATTACK:
				case WARRIOR_ATTACK:
					newEffectsList.put(eff.getId(), eff);
					break;
				default:
					break;
				}
			}
			this.effectsList = newEffectsList;
		}
		removeFlag(BoomGameManager.BOOM_SPRITE_FLAG_PLAYERS_DEAD);
		long start = System.nanoTime();
		long end = start + BoomGameManager.NANO_SECOND * 2;
		addEffect(new BoomPlayerItemEffect(BoomGameItemEffect.PHANTOM, 0, start, end));
		revivalParam.incRevivalCount();
	}
	
	public void applyConfuseEffect(long start, long end) {
		addEffect(new BoomPlayerItemEffect(BoomGameItemEffect.CONFUSION, 0, start, end));
		Collections.shuffle(directionList);// confuse
	}
	
	public int getReviveGauge() {
		if (!isDead()) {
			return 0;
		}
		return revivalParam.getRevivalGauge();
	}
	
	public boolean isAvailableForRevival() {
		// DO NOT CHANGE UNLESS YOU KNOW HOW RANKING WORKS AND YOU CAN FIX IT
		return (revivalParam.getRevivalCount() <= 0); 
	}
	
	public boolean triggerRevivalRankingCheck() {
		if (revivalParam.isRankingCheckFlag()) {
			return false;
		}
		if (revivalParam.getRevivalCount() <= 0) {
			return false;
		}
		revivalParam.setRankingCheckFlag(true);
		return true;
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
	
	public void initGroupEffect() {
		if (this.groupId <= 0) {
			return;
		}
		addEffect(new BoomPlayerItemEffect(BoomGameItemEffect.MAGICAL_REVIVAL, 1, 0, 0));
	}
}
