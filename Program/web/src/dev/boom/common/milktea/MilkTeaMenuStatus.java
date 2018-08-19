package dev.boom.common.milktea;

public enum MilkTeaMenuStatus {
	INIT((byte)0,"", "", 0),
	OPENING((byte)1,"MSG_MILK_TEA_MENU_OPENING", "MSG_MILK_TEA_NO_MENU_OPENING", 3),
	DELIVERING((byte)2,"MSG_MILK_TEA_MENU_DELIVERING", "MSG_MILK_TEA_NO_MENU_DELIVERING", 4),
	COMPLETED((byte)3,"MSG_MILK_TEA_MENU_COMPLETED", "MSG_MILK_TEA_NO_MENU_COMPLETED", 5),
	CANCELED((byte)4,"", "", 0),
	;
	
	private byte status;
	private String label;
	private String noMenuLabel;
	private int maxGridElemPerRow; 
	
	private MilkTeaMenuStatus(byte status, String label, String noMenuLabel, int maxGridElemPerRow) {
		this.status = status;
		this.label = label;
		this.noMenuLabel = noMenuLabel;
		this.maxGridElemPerRow = maxGridElemPerRow;
	}
	
	public byte getStatus() {
		return status;
	}

	public String getLabel() {
		return label;
	}

	public String getNoMenuLabel() {
		return noMenuLabel;
	}

	public int getMaxGridElemPerRow() {
		return maxGridElemPerRow;
	}

	public static MilkTeaMenuStatus valueOf(int status) {
		for (MilkTeaMenuStatus stt : MilkTeaMenuStatus.values()) {
			if (stt.ordinal() == status) {
				return stt;
			}
		}
		return MilkTeaMenuStatus.INIT;
	}
}
