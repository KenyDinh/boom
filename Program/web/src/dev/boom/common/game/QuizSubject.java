package dev.boom.common.game;

public enum QuizSubject {
	NONE((byte) 0, ""),
	TOEIC((byte) 1, "toeic"),
	;
	
	private byte subject;
	private String name;
	
	public static final int MIN_QUIZ_SUBJECT = TOEIC.getSubject();
	public static final int MAX_QUIZ_SUBJECT = TOEIC.getSubject();
	
	private QuizSubject(byte subject, String name) {
		this.subject = subject;
		this.name = name;
	}
	
	public byte getSubject() {
		return subject;
	}
	
	public String getName() {
		return name;
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
