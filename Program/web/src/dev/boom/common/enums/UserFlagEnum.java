package dev.boom.common.enums;

public enum UserFlagEnum {
	INVALID(0, "MSG_GENERAL_NONE"),
	ACTIVE(0x00001, "MSG_GENERAL_USER_FLAG_ACTIVE"),
	MILKTEA_BANNED(0x00002, "MSG_GENERAL_USER_FLAG_MILKTEA_BANNED"),
	PWD_CHANGE(0x00004, "MSG_GENERAL_USER_FLAG_PWD_CHANGED"),
	DEVICE_BANNED(0x00008, "MSG_GENERAL_USER_FLAG_DEP_DEVICE_BANNED"),
	COMMENT_BANNED(0x00010, "MSG_GENERAL_USER_FLAG_COMMENT_BANNED"),
	BOOM_INSPECTOR(0x00020, "MSG_GENERAL_USER_FLAG_BOOM_INSPECTOR"),
	;
	
	private int flag;
	private String label;
	
	private UserFlagEnum(int flag, String label) {
		this.flag = flag;
		this.label = label;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public String getLabel() {
		return label;
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
	
	public static UserFlagEnum valueOf(int index) {
		for (UserFlagEnum userFlag : UserFlagEnum.values()) {
			if (userFlag.ordinal() == index) {
				return userFlag;
			}
		}
		return UserFlagEnum.INVALID;
	}
}
