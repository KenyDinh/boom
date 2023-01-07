package dev.boom.common.game;

public enum QuizStatus {
	PREPARING,
	IN_SESSION,
	BREAK_TIME,
	FINISHED,
	;
	
	public byte getStatus() {
		return (byte)this.ordinal();
	}
}
