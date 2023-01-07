package dev.boom.common.enums;

public enum ManageLogType {
	NONE((byte) 0, "MSG_GENERAL_NONE"),
	ADD_MENU((byte) 1, "MSG_GENERAL_LOG_MILKTEA_MENU_ADD"),
	UPDATE_MENU((byte) 2, "MSG_GENERAL_LOG_MILKTEA_MENU_UPDATE"),
	ADD_USER((byte) 3, "MSG_GENERAL_LOG_USER_ADD"),
	UPDATE_USER((byte) 4, "MSG_GENERAL_LOG_USER_UPDATE"),
	EVENT_UPDATE((byte) 5, "MSG_GENERAL_LOG_EVENT_UPDATE"),
	GAME_UPDATE((byte) 6, "MSG_GENERAL_LOG_GAME_UPDATE"),
	ADD_DEVICE((byte) 7, "MSG_GENERAL_LOG_DEVICE_ADD"),
	REQUEST_DEVICE((byte) 8, "MSG_GENERAL_LOG_DEVICE_REQUEST"),
	REQUEST_RETURN((byte) 9, "MSG_GENERAL_LOG_DEVICE_REQUEST_RETURN"),
	CHECKOUT_DEVICE((byte) 10, "MSG_GENERAL_LOG_DEVICE_CHECKOUT"),
	ACCEPT_REQUEST((byte) 11, "MSG_GENERAL_LOG_DEVICE_ACCEPT"),
	DECLINE_REQUEST((byte) 12, "MSG_GENERAL_LOG_DEVICE_DECLINE"),
	RETURN_DEVICE((byte) 13, "MSG_GENERAL_LOG_DEVICE_RETURN"),
	REMOVE_DEVICE((byte) 14, "MSG_GENERAL_LOG_DEVICE_REMOVE"),
	CANCEL_DEVICE((byte) 15, "MSG_GENERAL_LOG_DEVICE_CANCEL"),
	UPDATE_DEVICE((byte) 16, "MSG_GENERAL_LOG_DEVICE_UPDATE"),
	;

	private byte type;
	private String label;

	private ManageLogType(byte type, String label) {
		this.type = type;
		this.label = label;
	}

	public byte getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}
	
	public static ManageLogType valueOf(byte type) {
		for (ManageLogType mt : ManageLogType.values()) {
			if (mt.getType() == type) {
				return mt;
			}
		}
		
		return ManageLogType.NONE;
	}
	
}
