package dev.boom.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum DeviceType {
	NONE((byte) 0, "MSG_GENERAL_DEVICE_TYPE_NONE", "no_image.png", false), 
	PS5((byte) 1, "MSG_GENERAL_DEVICE_TYPE_PS5", "ps5.png", true),
	PS4((byte) 2, "MSG_GENERAL_DEVICE_TYPE_PS4", "ps4.png", true), 
	SWITCH((byte) 3, "MSG_GENERAL_DEVICE_TYPE_SWITCH", "switch.png", true), 
	XBOX((byte) 4, "MSG_GENERAL_DEVICE_TYPE_XBOX", "xbox.png", false),
	;

	private byte type;
	private String label;
	private String image;
	private boolean available;

	private DeviceType(byte type, String label, String image, boolean available) {
		this.type = type;
		this.label = label;
		this.image = image;
		this.available = available;
	}

	public byte getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}

	public String getImage() {
		return image;
	}

	public boolean isAvailable() {
		return available;
	}

	public static DeviceType valueOf(byte type) {
		for (DeviceType deviceType : DeviceType.values()) {
			if (deviceType.getType() == type) {
				return deviceType;
			}
		}

		return DeviceType.NONE;
	}
	
	public static List<DeviceType> getAvailableType() {
		List<DeviceType> ret = new ArrayList<>();
		for (DeviceType deviceType : DeviceType.values()) {
			if (deviceType.isAvailable()) {
				ret.add(deviceType);
			}
		}
		
		return ret;
	}

}
