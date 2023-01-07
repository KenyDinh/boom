package dev.boom.common.enums;

public enum SurveyOptionType {
	
	IMAGE,
	TEXT,
	;
	
	public static SurveyOptionType valueOf(byte type) {
		for (SurveyOptionType oType : SurveyOptionType.values()) {
			if (oType.ordinal() == type) {
				return oType;
			}
		}
		return SurveyOptionType.TEXT;
	}
}
