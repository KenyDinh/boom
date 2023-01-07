package dev.boom.common.enums;

public enum SurveyQuestionType {
	
	OPTION_SELECT,
	GIVING_ANSWER,
	YES_NO,
	NUMERAL_LIST,
	DATE_PICKER,
	OPTION_LIST,
	MYSTERY_GIFT_BOX,
	;
	
	public static SurveyQuestionType valueOf(byte type) {
		for (SurveyQuestionType qType : SurveyQuestionType.values()) {
			if (qType.ordinal() == type) {
				return qType;
			}
		}
		return SurveyQuestionType.GIVING_ANSWER;
	}
}
