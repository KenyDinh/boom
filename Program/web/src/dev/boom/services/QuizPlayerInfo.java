package dev.boom.services;

import java.util.Date;

import dev.boom.common.game.QuizPlayerStatus;
import dev.boom.tbl.info.TblQuizPlayerInfo;

public class QuizPlayerInfo {

	private TblQuizPlayerInfo tblQuizPlayerInfo = null;

	public QuizPlayerInfo() {
		this.tblQuizPlayerInfo = new TblQuizPlayerInfo();
	}

	public QuizPlayerInfo(TblQuizPlayerInfo tblQuizPlayerInfo) {
		this.tblQuizPlayerInfo = tblQuizPlayerInfo;
	}

	public TblQuizPlayerInfo getTblQuizPlayerInfo() {
		return tblQuizPlayerInfo;
	}

	public long getUserId() {
		return this.tblQuizPlayerInfo.getUser_id();
	}

	public void setUserId(long user_id) {
		this.tblQuizPlayerInfo.setUser_id(user_id);
	}
	public String getUsername() {
		return this.tblQuizPlayerInfo.getUsername();
	}

	public void setUsername(String username) {
		this.tblQuizPlayerInfo.setUsername(username);
	}
	public long getQuizId() {
		return this.tblQuizPlayerInfo.getQuiz_id();
	}

	public void setQuizId(long quiz_id) {
		this.tblQuizPlayerInfo.setQuiz_id(quiz_id);
	}

	public byte getStatus() {
		return this.tblQuizPlayerInfo.getStatus();
	}

	public void setStatus(byte status) {
		this.tblQuizPlayerInfo.setStatus(status);
	}

	public String getAnswer() {
		return this.tblQuizPlayerInfo.getAnswer();
	}

	public void setAnswer(String answer) {
		this.tblQuizPlayerInfo.setAnswer(answer);
	}

	public byte getCorrectCount() {
		return this.tblQuizPlayerInfo.getCorrect_count();
	}

	public void setCorrectCount(byte correct_count) {
		this.tblQuizPlayerInfo.setCorrect_count(correct_count);
	}

	public int getCorrectPoint() {
		return this.tblQuizPlayerInfo.getCorrect_point();
	}

	public void setCorrectPoint(int correct_point) {
		this.tblQuizPlayerInfo.setCorrect_point(correct_point);
	}

	public Date getUpdated() {
		return this.tblQuizPlayerInfo.getUpdated();
	}

	public void setUpdated(Date updated) {
		this.tblQuizPlayerInfo.setUpdated(updated);
	}

	public void initNewQuiz(QuizInfo quizInfo) {
		if (quizInfo == null) {
			return;
		}
		setQuizId(quizInfo.getId());
		setStatus(QuizPlayerStatus.INITIALIZED.getStatus());
		setAnswer("");
		setCorrectCount((byte)0);
		setCorrectPoint(0);
	}
}
