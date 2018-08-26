package dev.boom.common.enums;

public enum EventFlagEnum {
	
	INVALID(0,""),
	REGISTRATON(1,""),
	ORDER_VOTING(2,""),
	RATING_CRONJOB(3,""),
	;
	
	private int index;
	private String label;
	
	private EventFlagEnum(int index, String label) {
		this.index = index;
		this.label = label;
	}

	public int getIndex() {
		return index;
	}

	public String getLabel() {
		return label;
	}
	
}
