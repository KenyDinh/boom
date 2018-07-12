package dev.boom.common.milktea;

public enum MilkTeaMenuStatus {
	INIT,
	OPENING,
	DELIVERING,
	COMPLETED,
	CANCELED,
	;
	
	public static MilkTeaMenuStatus valueOf(int status) {
		for (MilkTeaMenuStatus stt : MilkTeaMenuStatus.values()) {
			if (stt.ordinal() == status) {
				return stt;
			}
		}
		return MilkTeaMenuStatus.INIT;
	}
}
