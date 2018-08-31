package dev.boom.common.milktea;

public enum MilkteaMenuFlag {

	INVALID(0,"",""),
	DEV_SHOW(0x00001,"MSG_MILK_TEA_MENU_SHOW_DEV","DEV"),
	QA_SHOW(0x00002,"MSG_MILK_TEA_MENU_SHOW_QA","QA"),
	CG_SHOW(0x00004,"MSG_MILK_TEA_MENU_SHOW_CG","CG"),
	;
	
	private int flag;
	private String label;
	private String name;

	private MilkteaMenuFlag(int flag, String label, String name) {
		this.flag = flag;
		this.label = label;
		this.name= name;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isValid(int flag) {
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
