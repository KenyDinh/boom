package dev.boom.game.boom;

import dev.boom.common.CommonMethod;

public class BoomPlayerItemEffect {

	private int id;
	private BoomGameItemEffect effectType;
	private int effectParam;
	private long start;
	private long end;
	private long lastCheck;
	private int curStack;
	private int baseEffectParam;
	private BoomGamePlayerEffectParam param;

	public BoomPlayerItemEffect() {
		super();
		this.param = new BoomGamePlayerEffectParam();
	}

	public BoomPlayerItemEffect(BoomGameItemEffect effectType, int effectParam, long start, long end) {
		this(effectType, effectParam, start, end, new BoomGamePlayerEffectParam());
	}
	
	public BoomPlayerItemEffect(BoomGameItemEffect effectType, int effectParam, long start, long end, BoomGamePlayerEffectParam param) {
		super();
		this.id = effectType.getId();
		this.effectType = effectType;
		this.effectParam = effectParam;
		this.start = start;
		this.end = end;
		if (effectType.getMaxStack() > 0) {
			this.curStack = 1;
		}
		this.lastCheck = System.nanoTime();
		this.baseEffectParam = effectParam;
		this.param = param;
	}
	
	public BoomGamePlayerEffectParam getParam() {
		return param;
	}

	public void setParam(BoomGamePlayerEffectParam param) {
		this.param = param;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BoomGameItemEffect getEffectType() {
		return effectType;
	}

	public void setEffectType(BoomGameItemEffect effectType) {
		this.effectType = effectType;
	}

	public int getEffectParam() {
		return effectParam;
	}

	public void setEffectParam(int effectParam) {
		this.effectParam = effectParam;
	}
	
	public boolean subEffectParam(int amount) {
		this.effectParam -= amount;
		if (this.effectParam < 0) {
			return false;
		}
		if (getEffectType().getMaxStack() > 0 && this.baseEffectParam > 0) {
			this.curStack = (this.effectParam - 1) / this.baseEffectParam + 1;
		}
		return true;
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
	
	public long getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(long lastCheck) {
		this.lastCheck = lastCheck;
	}
	
	public void extendDuration(long extension) {
		if (extension <= 0) {
			return;
		}
		start += extension;
		end += extension;
	}

	public boolean hasEffect(long currentTime) {
		if (effectType == BoomGameItemEffect.SHEILD_PROTECTOR 
			|| effectType == BoomGameItemEffect.WARRIOR_AMOR
			|| effectType == BoomGameItemEffect.MAGICAL_REVIVAL) {
			return effectParam > 0;
		}
		if (effectType == BoomGameItemEffect.BOMB_SHOOTER) {
			return (effectParam > 0 && end > currentTime);
		}
		if (start == end) {// unlimited time
			return true;
		}
		if (start > currentTime) {
			return false;
		}
		if (end < currentTime) {
			return false;
		}
		return true;
	}
	
	public boolean hasEffect() {
		return hasEffect(System.nanoTime());
	}
	
	public boolean isEndSoon(long currentTime) {
		if (effectType == BoomGameItemEffect.SHEILD_PROTECTOR) {
			if (effectParam == 1) {
				return true;
			}
		}
		int secLeft = (int)((end - currentTime) / BoomGameManager.NANO_SECOND);
		if (effectType == BoomGameItemEffect.BOMB_SHOOTER) {
			if (effectParam == 1 || secLeft < 3) {
				return true;
			}
		}
		if (start == end) {
			return false;
		}
		return secLeft < 3;
	}
	
	public int getCurStack() {
		return curStack;
	}

	public boolean addOrRefreshEffect(BoomPlayerItemEffect effect) {
		if (getEffectType().getMaxStack() > 0) {
			return add(effect);
		}
		return refresh(effect);
	}
	
	private void reset() {
		this.curStack = 0;
		this.effectParam = 0;
	}
	
	public boolean add(BoomPlayerItemEffect effect) {
		if (!this.getEffectType().equals(effect.getEffectType())) {
			return false;
		}
		if (!hasEffect()) {
			reset();
		}
		if (effectType == BoomGameItemEffect.INCREASE_SPEED) {
			if (curStack >= getEffectType().getMaxStack()) {
				return false;
			}
		}
		this.start = effect.getStart();
		this.end = effect.getEnd();
		this.effectParam = CommonMethod.minmax(0, this.effectParam + effect.getEffectParam(), baseEffectParam * getEffectType().getMaxStack());
		if (curStack < getEffectType().getMaxStack()) {
			curStack++;
		}
		return true;
	}
	
	public boolean refresh(BoomPlayerItemEffect effect) {
		if (!this.getEffectType().equals(effect.getEffectType())) {
			return false;
		}
		if (!hasEffect()) {
			reset();
		} else if (effectType == BoomGameItemEffect.MAGICAL_REVIVAL) {
			return false;
		}
		this.start = effect.getStart();
		this.end = effect.getEnd();
		this.effectParam = effect.getEffectParam();
		return true;
	}

}
