package dev.boom.game.boom;

public class BoomPlayerAbility {
	
	private int id;
	private int damage;
	private int range;
	private BoomPlayerItemEffect effect;
	
	public BoomPlayerAbility(int id, int damage, int range, BoomPlayerItemEffect effect) {
		super();
		this.id = id;
		this.damage = damage;
		this.range = range;
		this.effect = effect;
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
	
}
