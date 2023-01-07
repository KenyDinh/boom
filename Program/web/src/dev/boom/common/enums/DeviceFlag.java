package dev.boom.common.enums;

public enum DeviceFlag {
	ADMIN(1,"MSG_GENERAL_DEVICE_PERMISSION_ADMIN"),
	DEPT(2,"MSG_GENERAL_DEVICE_PERMISSION_DEPT"),
	ALL(3,"MSG_GENERAL_DEVICE_PERMISSION_ALL"),
	;
	
	private int index;
	private String label;

	private DeviceFlag(int index, String label) {
		this.index = index;
		this.label = label;
	}

	public int getIndex() {
		return index;
	}
	
	public String getLabel() {
		return label;
	}
	
	public int getFlag() {
		return (1 << (getIndex() - 1));
	}

	public boolean isValid(int flag) {
		return (flag & (1 << (index - 1))) > 0;
	}
	
	public static DeviceFlag valueOf(int index) {
		for (DeviceFlag df : DeviceFlag.values()) {
			if (df.getIndex() == index) {
				return df;
			}
		}
		
		return DeviceFlag.ADMIN;
	}
}
