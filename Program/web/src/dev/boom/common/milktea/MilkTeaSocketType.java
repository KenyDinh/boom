package dev.boom.common.milktea;

public enum MilkTeaSocketType {
	INVALID(0),
	MENU_LIST(1),
	MENU_DETAIL(2),
	ALL(99),
	;
	
	private int type;
	
	private MilkTeaSocketType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static MilkTeaSocketType valueOf(int type) {
		for (MilkTeaSocketType msgType : MilkTeaSocketType.values()) {
			if (msgType.getType() == type) {
				return msgType;
			}
		}
		return MilkTeaSocketType.INVALID;
	}
}
