package dev.boom.services;

import dev.boom.tbl.info.TblQuizLogInfo;

public class QuizLog {

	private TblQuizLogInfo quizLogInfo;

	public QuizLog(TblQuizLogInfo quizLogInfo) {
		this.quizLogInfo = quizLogInfo;
	}

	public QuizLog() {
	}

	public TblQuizLogInfo getQuizLogInfo() {
		return quizLogInfo;
	}
	
	public byte getQuestIndex() {
		return this.quizLogInfo.getQuestion_index();
	}

	public String getUsername() {
		return this.quizLogInfo.getUsername();
	}
	
	public String getCorrectAnswer() {
		return this.quizLogInfo.getCorrect_answer();
	}
	
	public String getPlayerAnswer() {
		return this.quizLogInfo.getPlayer_answer();
	}
	
	public boolean isCorrect() {
		return (getCorrectAnswer().equalsIgnoreCase(getPlayerAnswer()));
	}
}
