package dev.boom.common.enums;

public enum UserRole {
	
	USER(0, "MSG_GENERAL_USER_ROLE_USER"),
	ADMINISTRATOR(1, "MSG_GENERAL_USER_ROLE_ADMINISTRATOR"),
	MILKTEA_ADMIN(2, "MSG_GENERAL_USER_ROLE_MILKTEA_ADMIN"),
	DEVICE_ADMIN(3, "MSG_GENERAL_USER_ROLE_DEVICE_ADMIN"),
	VOTE_ADMIN(4, "MSG_GENERAL_USER_ROLE_VOTE_ADMIN"),
	;
	
	private int role;
	private String label;

	private UserRole(int role, String label) {
		this.role = role;
		this.label = label;
	}

	public int getRole() {
		return role;
	}

	public String getLabel() {
		return label;
	}

	public static UserRole valueOf(int role) {
		for (UserRole userFlag : UserRole.values()) {
			if (userFlag.ordinal() == role) {
				return userFlag;
			}
		}
		return UserRole.USER;
	}
	
}
