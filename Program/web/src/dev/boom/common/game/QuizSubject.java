package dev.boom.common.game;

public enum QuizSubject {
	NONE((byte) 0, "", 0, 0, new String[] {}), 
	Toeic((byte) 1, "toeic", 100, 990, new String[] {QuizDefine.PARAM_Q_NUM,QuizDefine.PARAM_Q_TIME,QuizDefine.PARAM_Q_PLAYER,QuizDefine.PARAM_Q_RETRY,QuizDefine.PARAM_Q_DESC}),
	Programming((byte) 2, "programming", 0, 0, new String[] {QuizDefine.PARAM_Q_LANG,QuizDefine.PARAM_Q_NUM,QuizDefine.PARAM_Q_TIME,QuizDefine.PARAM_Q_PLAYER,QuizDefine.PARAM_Q_RETRY}),
	Japanese((byte) 3, "japanese", 1, 5, new String[] {QuizDefine.PARAM_Q_LEVEL,QuizDefine.PARAM_Q_NUM,QuizDefine.PARAM_Q_TIME,QuizDefine.PARAM_Q_PLAYER,QuizDefine.PARAM_Q_RETRY,QuizDefine.PARAM_Q_DESC}),
	;

	private byte subject;
	private String name;
	private int minLevel;
	private int maxLevel;
	private String[] options;
	
	private QuizSubject(byte subject, String name, int minLevel, int maxLevel, String[] options) {
		this.subject = subject;
		this.name = name;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.options = options;
	}

	public byte getSubject() {
		return subject;
	}

	public String getName() {
		return name;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public String[] getOptions() {
		return options;
	}

	public static QuizSubject getSubjectByName(String name) {
		QuizSubject[] list = QuizSubject.values();
		for (QuizSubject _subject : list) {
			if (_subject.getName().equalsIgnoreCase(name)) {
				return _subject;
			}
		}
		return QuizSubject.NONE;
	}
	
	public static QuizSubject valueOf(byte subject) {
		QuizSubject[] list = QuizSubject.values();
		for (QuizSubject _subject : list) {
			if (_subject.getSubject() == subject) {
				return _subject;
			}
		}
		return QuizSubject.NONE;
	}
	
	public String getParameter(String param, String[] options) {
		if (param == null || param.isEmpty()) {
			return null;
		}
		if (options == null || options.length == 0) {
			return null;
		}
		int index = -1;
		for (int i = 0; i < getOptions().length; i++) {
			if (param.equals(getOptions()[i])) {
				index = i;
			}
		}
		if (index == -1) {
			return null;
		}
		++index; // 1st is subject key
		if (options.length <= index) {
			return null;
		}
		String ret = options[index] ;
		if (ret == null || ret.isEmpty()) {
			return null;
		}
		
		return ret;
	}
	
}
