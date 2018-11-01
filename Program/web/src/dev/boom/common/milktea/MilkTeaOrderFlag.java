package dev.boom.common.milktea;

public enum MilkTeaOrderFlag {
	
	PLACED,		//1
	PAID,		//2
	VOTE,		//3
	VOTE_CRON,	//4
	CANCELED,	//5
	KOC_TICKET,	//6
	KOC_VALID,	//7
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
