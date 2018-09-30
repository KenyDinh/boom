package dev.boom.common.milktea;

public enum MilkTeaOrderFlag {
	
	PLACED,
	PAID,
	VOTE,
	VOTE_CRON,
	CANCELED,
	;
	
	public boolean isValidFlag(int flag) {
		return ((flag & (1 << this.ordinal())) != 0);
	}
	
	public int getValidFlag(int flag) {
		return (flag | (1 << this.ordinal()));
	}
	
	public int getBitMask() {
		return (1 << this.ordinal());
	}
	
	public static MilkTeaOrderFlag valueOf(int flag) {
		for (MilkTeaOrderFlag orderFlag : MilkTeaOrderFlag.values()) {
			if (orderFlag.getBitMask() == flag) {
				return orderFlag;
			}
		}
		return null;
	}
}
