package dev.boom.common.milktea;

public enum MilkTeaOrderFlag {
	
	PAY,
	VOTE,
	;
	
	public boolean isValidFlag(int flag) {
		return ((flag & (1 << this.ordinal())) != 0);
	}
	
	public int setValidFlag(int flag) {
		return (flag | (1 << this.ordinal()));
	}
}
