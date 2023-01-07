package dev.boom.common.milktea;

public enum MilkteaMenuFlag {

	INVALID(0,"MSG_GENERAL_NONE"),
	IGNORE_VALIDATION(0x00001,"MSG_MILK_TEA_MENU_IGNORE_VALIDATION"),
	;
	
	private int flag;
	private String label;

	private MilkteaMenuFlag(int flag, String label) {
		this.flag = flag;
		this.label = label;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public String getLabel() {
		return label;
	}
	
	public boolean isValidFlag(int flag) {
		return (this.flag & flag) > 0;
	}
	
	public static MilkteaMenuFlag valueOf(int flag) {
		for (MilkteaMenuFlag mmf : MilkteaMenuFlag.values()) {
			if (mmf.getFlag() == flag) {
				return mmf;
			}
		}
		return MilkteaMenuFlag.INVALID;
	}
	
}
