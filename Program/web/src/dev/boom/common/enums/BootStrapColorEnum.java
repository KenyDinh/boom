package dev.boom.common.enums;

public enum BootStrapColorEnum {
	PRIMARY("#375a7f"),
	SUCCESS("#00bc8c"),
	DANGER("#E74C3C"),
	WARNING("#F39C12"),
	INFO("#3498DB"),
	;
	
	private String colorCode;
	
	private BootStrapColorEnum(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getColorCode() {
		return colorCode;
	}
	
}
