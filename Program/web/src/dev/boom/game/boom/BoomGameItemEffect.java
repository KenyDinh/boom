package dev.boom.game.boom;

public enum BoomGameItemEffect {

	NONE(0, -1),
	RECOVER_HP(1, -1),
	INCREASE_SPEED(2, 131, 3),
	BOOST_BOMB_RANGE(3, -1),
	BOOST_BOMB_DAMAGE(4, -1),
	BOOST_BOOM_POOL_SIZE(5, -1),
	SHEILD_PROTECTOR(6, 98),
	INVISIBLE_MAN(7, 158),
	HUNTER_TRAP(8, 174),
	TAKE_DAMAGE(9, -1),
	ABSORB_DAMAGE(10, 53),
	BOMB_SHOOTER(11, 20),
	IRON_FIST(12, 95),
	QUICK_BOMBER(13, 175),
	SUPER_GLUE(14, 61),
	BOOST_SPEED(15, -1),
	DARKNESS_COVER(16, -1),
	IRON_SWORD(17, 82),
	BOOST_ATTACK_SPEED(18, -1),
	BOOST_ATTACK_DAMAGE(19, -1),
	BOOST_ATTACK_RANGE(20, -1),
	WARRIOR_AMOR(21, 117, 5),
	WARRIOR_ATTACK(22, 82),
	MONK_ATTACK(23, 95),
	BLEEDING(24, 11),
	;

	private int id;
	private int imageID;
	private int maxStack;

	private BoomGameItemEffect(int id, int imageID) {
		this.id = id;
		this.imageID = imageID;
		this.maxStack = 0;
	}
	
	private BoomGameItemEffect(int id, int imageID, int maxStack) {
		this.id = id;
		this.imageID = imageID;
		this.maxStack = maxStack;
	}

	public int getId() {
		return id;
	}
	
	public int getImageID() {
		return imageID;
	}

	public int getMaxStack() {
		return maxStack;
	}

	public static BoomGameItemEffect valueOf(int id) {
		for (BoomGameItemEffect effect : BoomGameItemEffect.values()) {
			if (effect.getId() == id) {
				return effect;
			}
		}
		return BoomGameItemEffect.NONE;
	}

}
