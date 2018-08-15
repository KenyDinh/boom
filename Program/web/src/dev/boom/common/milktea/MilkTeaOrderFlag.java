package dev.boom.common.milktea;

public enum MilkTeaOrderFlag {
	
	PLACED,
	PAID,
	VOTE,
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
}
