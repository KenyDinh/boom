package dev.boom.game.boom;

public enum BoomCharater {

	CHARACTER_00(0,0,100,100,20,100,100,1000,100,1000,8,16),
	CHARACTER_01(1,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_02(2,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_03(3,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_04(4,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_05(5,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_06(6,2,30,30,8,20,50,200,75,100,6,16),
	CHARACTER_07(7,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_08(8,2,30,30,8,20,50,200,75,100,6,16),
	CHARACTER_09(9,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_10(10,2,30,30,8,20,50,200,75,100,6,16),
	CHARACTER_11(11,2,30,30,8,20,50,200,75,100,6,16),
	CHARACTER_12(12,1,40,40,4,15,100,400,50,100,6,16),
	CHARACTER_13(13,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_14(14,1,40,40,4,15,100,400,50,100,6,16),
	CHARACTER_15(15,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_16(16,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_17(17,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_18(18,1,40,40,4,15,100,400,50,100,6,16),
	CHARACTER_19(19,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_20(20,0,100,100,20,100,200,1000,100,1000,8,16),
	CHARACTER_21(21,1,40,40,4,15,100,400,50,100,6,16),
	CHARACTER_22(22,1,40,40,4,15,100,400,50,100,6,16),
	CHARACTER_23(23,1,40,40,4,15,100,400,50,100,6,16),
	CHARACTER_24(24,1,40,40,4,15,100,400,50,100,6,16),
	CHARACTER_25(25,2,30,30,8,20,50,200,75,100,6,16),
	CHARACTER_26(26,2,30,30,8,20,50,200,75,100,6,16),
	CHARACTER_27(27,2,30,30,8,20,50,200,75,100,6,16),
	CHARACTER_28(28,1,40,40,4,15,100,400,50,100,6,16),
	CHARACTER_29(29,2,30,30,8,20,50,200,75,100,6,16),
	CHARACTER_30(30,2,30,30,8,20,50,200,75,100,6,16),
	CHARACTER_31(31,1,40,40,4,15,100,400,50,100,6,16),
	CHARACTER_32(32,1,40,40,4,15,100,400,50,100,6,16),
	;
	
	private int id;
	private int type;
	private int hp;
	private int maxHP;
	private int damage;
	private int maxDamage;
	private int attackSpeed;
	private int maxAttackSpeed;
	private int attackRange;
	private int maxAttakRange;
	private int movementSpeed;
	private int maxMovementSpeed;
	
	public static final int BOOM_GAME_PLAYER_INIT_HP = 100;
	public static final int BOOM_GAME_PLAYER_MAX_HP = 100;
	public static final int BOOM_GAME_PLAYER_INIT_SPEED = 8;
	public static final int BOOM_GAME_PLAYER_MAX_SPEED = 24;
	public static final int BOOM_GAME_PLAYER_BOMB_INIT_DMG = 20;
	public static final int BOOM_GAME_PLAYER_BOMB_MAX_DMG = 100;
	public static final int BOOM_GAME_PLAYER_BOMB_INIT_SIZE = 1;
	public static final int BOOM_GAME_PLAYER_BOMB_MAX_SIZE = 10;
	public static final int BOOM_GAME_MAX_BOMB_POOL_SIZE = 10;
	public static final int BOOM_GAME_INIT_BOMB_POOL_SIZE = 1;
	
	private BoomCharater(int id, int type, int hp, int maxHP, int damage, int maxDamage, int attackSpeed,
			int maxAttackSpeed, int attackRange, int maxAttakRange, int movementSpeed, int maxMovementSpeed) {
		this.id = id;
		this.type = type;
		this.hp = hp;
		this.maxHP = maxHP;
		this.damage = damage;
		this.maxDamage = maxDamage;
		this.attackSpeed = attackSpeed;
		this.maxAttackSpeed = maxAttackSpeed;
		this.attackRange = attackRange;
		this.maxAttakRange = maxAttakRange;
		this.movementSpeed = movementSpeed;
		this.maxMovementSpeed = maxMovementSpeed;
	}
	public int getId() {
		return id;
	}
	public int getType() {
		return type;
	}
	public int getHp() {
		return hp;
	}
	public int getMaxHP() {
		return maxHP;
	}
	public int getDamage() {
		return damage;
	}
	public int getMaxDamage() {
		return maxDamage;
	}
	public int getAttackSpeed() {
		return attackSpeed;
	}
	public int getMaxAttackSpeed() {
		return maxAttackSpeed;
	}
	public int getAttackRange() {
		return attackRange;
	}
	public int getMaxAttakRange() {
		return maxAttakRange;
	}
	public int getMovementSpeed() {
		return movementSpeed;
	}
	public int getMaxMovementSpeed() {
		return maxMovementSpeed;
	}
	
	public String getImage() {
		return String.format("pipo-nekonin%03d.png", getId());
	}
	
	public String getFormatStat(int stat) {
		return String.valueOf((double)stat / 100); 
	}
	
	public static BoomCharater getById(int id) {
		for (BoomCharater bc : BoomCharater.values()) {
			if (bc.getId() == id) {
				return bc;
			}
		}
		return BoomCharater.CHARACTER_00;
	}

}
