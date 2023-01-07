package dev.boom.common.enums;

public enum DeviceStatus {
	AVAILABLE((byte) 0, "MSG_GENERAL_DEVICE_STATUS_AVAILABLE", "Available"), 
	PENDING((byte) 1, "MSG_GENERAL_DEVICE_STATUS_PENDING", "Pending"),
	UNAVAILABLE((byte) 2, "MSG_GENERAL_DEVICE_STATUS_UNAVAILABLE", "In use"),
	CHANGE_PENDING((byte) 3, "MSG_GENERAL_DEVICE_STATUS_CHANGE_PENDING", "Confirming"),
	RETURN_PENDING((byte) 4, "MSG_GENERAL_DEVICE_STATUS_RETURN_PENDING", "Updating"),
	;

	private byte status;
	private String label;
	private String msg;

	private DeviceStatus(byte status, String label, String msg) {
		this.status = status;
		this.label = label;
		this.msg = msg;
	}

	public byte getStatus() {
		return status;
	}

	public String getLabel() {
		return label;
	}

	public String getMsg() {
		return msg;
	}

	public static DeviceStatus valueOf(byte status) {
		for (DeviceStatus deviceStatus : DeviceStatus.values()) {
			if (deviceStatus.getStatus() == status) {
				return deviceStatus;
			}
		}

		return DeviceStatus.AVAILABLE;
	}
}
