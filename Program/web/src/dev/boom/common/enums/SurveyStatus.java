package dev.boom.common.enums;

public enum SurveyStatus {
	INIT,
	IN_SESSION,
	FINISHED,
	;
	
	public static SurveyStatus valueOf(byte status) {
		for (SurveyStatus ss : SurveyStatus.values()) {
			if (ss.ordinal() == status) {
				return ss;
			}
		}
		return SurveyStatus.INIT;
	}
}
