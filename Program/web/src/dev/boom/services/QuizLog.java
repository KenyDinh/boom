package dev.boom.services;

import dev.boom.tbl.info.TblQuizLogInfo;

public class QuizLog {
	private TblQuizLogInfo quizLogInfo;

	public QuizLog() {
		quizLogInfo = new TblQuizLogInfo();
	}

	public QuizLog(TblQuizLogInfo quizLogInfo) {
		this.quizLogInfo = quizLogInfo;
	}

	public TblQuizLogInfo getQuizLogInfo() {
		return quizLogInfo;
	}

	public int getId() {
		return (Integer) quizLogInfo.Get("id");
	}

	public void setId(int id) {
		quizLogInfo.Set("id", id);
	}

	public long getQuizId() {
		return (Long) quizLogInfo.Get("quiz_id");
	}

	public void setQuizId(long quizId) {
		quizLogInfo.Set("quiz_id", quizId);
	}

	public byte getQuestionIndex() {
		return (Byte) quizLogInfo.Get("question_index");
	}

	public void setQuestionIndex(byte questionIndex) {
		quizLogInfo.Set("question_index", questionIndex);
	}

	public long getUserId() {
		return (Long) quizLogInfo.Get("user_id");
	}

	public void setUserId(long userId) {
		quizLogInfo.Set("user_id", userId);
	}

	public String getUsername() {
		return (String) quizLogInfo.Get("username");
	}

	public void setUsername(String username) {
		quizLogInfo.Set("username", username);
	}

	public String getCorrectAnswer() {
		return (String) quizLogInfo.Get("correct_answer");
	}

	public void setCorrectAnswer(String correctAnswer) {
		quizLogInfo.Set("correct_answer", correctAnswer);
	}

	public String getPlayerAnswer() {
		return (String) quizLogInfo.Get("player_answer");
	}

	public void setPlayerAnswer(String playerAnswer) {
		quizLogInfo.Set("player_answer", playerAnswer);
	}
	
	public boolean isCorrect() {
		return (getCorrectAnswer().equalsIgnoreCase(getPlayerAnswer()));
	}

}

