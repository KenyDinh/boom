package dev.boom.common;

public class CommonDefine {
	
	private CommonDefine() {
	}
	
	public static final String DATE_FORMAT_PATTERN_DB = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_PATTERN = "yyyy/MM/dd HH:mm:ss";
	public static final String DATE_REGEX_PATTERN = "[0-9]{4}/[0-1][0-9]/[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9]";
	public static final int MAX_MILKTEA_VOTING_STAR = 5;
	public static final long MILLION_SECOND_SECOND = 1000;
	public static final long MILLION_SECOND_MINUTE = 60 * MILLION_SECOND_SECOND;
	public static final long MILLION_SECOND_HOUR = 60 * MILLION_SECOND_MINUTE;
	public static final long MILLION_SECOND_DAY = 24 * MILLION_SECOND_HOUR;
	public static final long SOCKET_SESSION_INTERVAL = MILLION_SECOND_DAY;
	
	public static final long KING_OF_CHASUA_MIN_ORDER_NUM = 4;
	
	
}
