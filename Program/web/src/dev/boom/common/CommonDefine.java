package dev.boom.common;

public class CommonDefine {
	
	private CommonDefine() {
	}
	
	public static final String DATE_FORMAT_PATTERN_DB = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_PATTERN = "yyyy/MM/dd HH:mm:ss";
	public static final String DATE_FORMAT_PATTERN_WITHOUT_TIME = "yyyy/MM/dd";
	public static final String DATE_REGEX_PATTERN = "[0-9]{4}[/-][0-1][0-9][/-][0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9]";
	public static final String DATE_REGEX_PATTERN_2 = "[0-9]{4}-[0-1][0-9]-[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9]";
	public static final String DEFAULT_DATE_TIME = "1970-01-01 00:00:00";
	public static final int MAX_MILKTEA_VOTING_STAR = 5;
	public static final long MILLION_SECOND_SECOND = 1000;
	public static final long MILLION_SECOND_MINUTE = 60 * MILLION_SECOND_SECOND;
	public static final long MILLION_SECOND_HOUR = 60 * MILLION_SECOND_MINUTE;
	public static final long MILLION_SECOND_DAY = 24 * MILLION_SECOND_HOUR;
	public static final long SOCKET_SESSION_INTERVAL = MILLION_SECOND_DAY;
	
	public static final long KING_OF_CHASUA_MIN_ORDER_NUM = 4;
	public static final long MAX_LENGTH_USERNAME = 32;
	public static final long MAX_LENGTH_PASSWORD = 64;
	
	public static final long MAX_DECK_CARD = 5;
	
	public static final String GAME_DEMO_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
	
}
