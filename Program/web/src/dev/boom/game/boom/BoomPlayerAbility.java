package dev.boom.game.boom;

public class BoomPlayerAbility {
	
	private int id;
	private int damage;
	private int range;
	private BoomPlayerItemEffect effect;
	private int bleedingDuration;
	
	public BoomPlayerAbility(int id, int damage, int range, BoomPlayerItemEffect effect, int bleedingDuration) {
		super();
		this.id = id;
		this.damage = damage;
		this.range = range;
		this.effect = effect;
		this.bleedingDuration = bleedingDuration;
	}
	
	public int getId() {
		return id;
	}
	
	public int getDamage() {
		return damage;
	}

	public int getRange() {
		return range;
	}
	
	public BoomPlayerItemEffect getEffect() {
		return effect;
	}

	public int getBleedingDuration() {
		return bleedingDuration;
	}

	public boolean isBleeding() {
		return (getBleedingDuration() > 0);
	}
	
}
