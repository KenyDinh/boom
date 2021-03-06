package dev.boom.common.enums;

public enum UserFlagEnum {
	INVALID(0),
	ADMINISTRATOR(0x00001),
	ACTIVE(0x00002),
	MILKTEA_BANNED(0x00004),
	PWD_CHANGE(0x00008),
	DEV_DP(0x00010), // DEV department
	QA_DP(0x00020), // QA department
	CG_DP(0x00040), // CG department
	;
	
	private int flag;
	
	private UserFlagEnum(int flag) {
		this.flag = flag;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public boolean isValid(int flag) {
		return (boolean) ((this.flag & flag) != 0);
	}
	
	public int setFlag(int flag) {
		return (flag | this.flag);
	}
	
	public int removeFlag(int flag) {
		return (flag & ~this.flag);
	}
}
