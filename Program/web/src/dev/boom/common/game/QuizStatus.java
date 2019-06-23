package dev.boom.common.game;

public enum QuizStatus {
	PREPARING,
	IN_SESSION,
	PAUSE,
	FINISHED,
	;
	
	public byte getStatus() {
		return (byte)this.ordinal();
	}
}
