package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.game.QuizPlayerStatus;
import dev.boom.tbl.info.TblQuizPlayerInfo;

public class QuizPlayer {
	private TblQuizPlayerInfo quizPlayerInfo;

	public QuizPlayer() {
		quizPlayerInfo = new TblQuizPlayerInfo();
	}

	public QuizPlayer(TblQuizPlayerInfo quizPlayerInfo) {
		this.quizPlayerInfo = quizPlayerInfo;
	}

	public TblQuizPlayerInfo getQuizPlayerInfo() {
		return quizPlayerInfo;
	}

	public long getUserId() {
		return (Long) quizPlayerInfo.Get("user_id");
	}

	public void setUserId(long userId) {
		quizPlayerInfo.Set("user_id", userId);
	}

	public String getUsername() {
		return (String) quizPlayerInfo.Get("username");
	}

	public void setUsername(String username) {
		quizPlayerInfo.Set("username", username);
	}

	public long getQuizId() {
		return (Long) quizPlayerInfo.Get("quiz_id");
	}

	public void setQuizId(long quizId) {
		quizPlayerInfo.Set("quiz_id", quizId);
	}

	public byte getStatus() {
		return (Byte) quizPlayerInfo.Get("status");
	}

	public void setStatus(byte status) {
		quizPlayerInfo.Set("status", status);
	}

	public byte getRetry() {
		return (Byte) quizPlayerInfo.Get("retry");
	}

	public void setRetry(byte retry) {
		if (retry < 0) {
			retry = 0;
		}
		quizPlayerInfo.Set("retry", retry);
	}

	public String getAnswer() {
		return (String) quizPlayerInfo.Get("answer");
	}

	public void setAnswer(String answer) {
		quizPlayerInfo.Set("answer", answer);
	}

	public byte getCorrectCount() {
		return (Byte) quizPlayerInfo.Get("correct_count");
	}

	public void setCorrectCount(byte correctCount) {
		quizPlayerInfo.Set("correct_count", correctCount);
	}

	public int getCorrectPoint() {
		return (Integer) quizPlayerInfo.Get("correct_point");
	}

	public void setCorrectPoint(int correctPoint) {
		quizPlayerInfo.Set("correct_point", correctPoint);
	}

	public String getUpdated() {
		return (String) quizPlayerInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		quizPlayerInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}
	
	public void incCorrectCount() {
		byte correctCount = getCorrectCount();
		correctCount++;
		setCorrectCount((byte) correctCount);
	}
	
	public void incCorrectPoint() {
		int correctPoint = getCorrectPoint();
		correctPoint++;
		setCorrectPoint(correctPoint);
	}
	
	public void initNewQuiz(Quiz quizInfo) {
		if (quizInfo == null) {
			return;
		}
		setQuizId(quizInfo.getId());
		setStatus(QuizPlayerStatus.INITIALIZED.getStatus());
		setAnswer("");
		setCorrectCount((byte) 0);
		setCorrectPoint(0);
//		setRetry(quizInfo.getRetry());
	}
	
}