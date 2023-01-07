package dev.boom.common.game;

import dev.boom.common.CommonDefine;

public class QuizDefine {

	public static final long DEFAULT_TIME_PER_QUESTION = 10 * CommonDefine.MILLION_SECOND_SECOND;
	public static final byte DEFAULT_QUESTION_NUM = 5;
	public static final byte DEFAULT_PLAYER_NUM = 10;
	public static final byte MAX_PLAYER = 30;
	public static final byte MIN_PLAYER = 1;
	public static final byte MAX_QUESTION = 30;
	public static final byte MIN_QUESTION = 3;
	public static final byte MAX_RETRY = 2;
	public static final byte MIN_RETRY = 0;
	public static final long MAX_TIME_PER_QUESTION = 120; //SECOND
	public static final long MIN_TIME_PER_QUESTION = 10; //SECOND
	
	public static final String PARAM_Q_NUM = "q_num";
	public static final String PARAM_Q_TIME = "q_time";
	public static final String PARAM_Q_PLAYER = "q_player";
	public static final String PARAM_Q_LANG = "q_lang";
	public static final String PARAM_Q_LEVEL = "q_level";
	public static final String PARAM_Q_RETRY = "q_retry";
	public static final String PARAM_Q_DESC = "q_desc";
	
}
