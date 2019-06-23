package dev.boom.common.game;

public enum QuizSubject {
	NONE((byte) 0, "", 0, 0), 
	TOEIC((byte) 1, "toeic", 100, 990),
	;

	private byte subject;
	private String name;
	private int minLevel;
	private int maxLevel;

	public static final int MIN_QUIZ_SUBJECT = TOEIC.getSubject();
	public static final int MAX_QUIZ_SUBJECT = TOEIC.getSubject();

	private QuizSubject(byte subject, String name, int minLevel, int maxLevel) {
		this.subject = subject;
		this.name = name;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
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

	public static QuizSubject getSubjectByName(String name) {
		QuizSubject[] list = QuizSubject.values();
		for (QuizSubject _subject : list) {
			if (_subject.getName().equalsIgnoreCase(name)) {
				return _subject;
			}
		}
		return QuizSubject.NONE;
	}
}
