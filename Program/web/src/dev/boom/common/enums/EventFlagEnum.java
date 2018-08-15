package dev.boom.common.enums;

public enum EventFlagEnum {
	
	INVALID(0,""),
	REGISTER(1,""),
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
