package dev.boom.common.game;

public enum QuizPlayerStatus {
	INITIALIZED((byte)0),
	PLAYING((byte)1),
	FINISHED((byte)2),
	;
	
	private byte status;
	
	private QuizPlayerStatus(byte status) {
		this.status = status;
	}
	
	public byte getStatus() {
		return status;
	}
}
