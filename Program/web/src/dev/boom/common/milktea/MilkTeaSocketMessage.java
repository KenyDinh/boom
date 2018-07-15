package dev.boom.common.milktea;

public enum MilkTeaSocketMessage {
	INVALID(0),
	UPDATE_MENU_LIST(1),
	UPDATE_MENU_DETAIL(2),
	UPDATE_ORDER_LIST(3),
	;
	
	private int id;
	
	private MilkTeaSocketMessage(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static MilkTeaSocketMessage valueOf(int id) {
		for (MilkTeaSocketMessage mtsg : MilkTeaSocketMessage.values()) {
			if (mtsg.getId() == id) {
				return mtsg;
			}
		}
		return MilkTeaSocketMessage.INVALID;
	}
}
