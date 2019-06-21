package dev.boom.services;

import java.util.Date;
import java.util.List;

import dev.boom.common.game.QuizStatus;
import dev.boom.tbl.info.TblQuizInfo;

public class QuizInfo {

	private TblQuizInfo tblQuizInfo = null;

	public QuizInfo() {
		this.tblQuizInfo = new TblQuizInfo();
	}

	public QuizInfo(TblQuizInfo tblQuizInfo) {
		this.tblQuizInfo = tblQuizInfo;
	}

	public TblQuizInfo getTblQuizInfo() {
		return tblQuizInfo;
	}

	public long getId() {
		return this.tblQuizInfo.getId();
	}

	public void setId(long id) {
		this.tblQuizInfo.setId(id);
	}
	
	public long getHost() {
		return this.tblQuizInfo.getHost();
	}

	public void setHost(long host) {
		this.tblQuizInfo.setHost(host);
	}

	public String getName() {
		return this.tblQuizInfo.getName();
	}

	public void setName(String name) {
		this.tblQuizInfo.setName(name);
	}

	public byte getLevel() {
		return this.tblQuizInfo.getLevel();
	}

	public void setLevel(byte level) {
		this.tblQuizInfo.setLevel(level);
	}

	public byte getQuestionNum() {
		return this.tblQuizInfo.getQuestion_num();
	}

	public void setQuestionNum(byte question_num) {
		this.tblQuizInfo.setQuestion_num(question_num);
	}

	public long getTimePerQuestion() {
		return this.tblQuizInfo.getTime_per_question();
	}

	public void setTimePerQuestion(long time_per_question) {
		this.tblQuizInfo.setTime_per_question(time_per_question);
	}

	public byte getStatus() {
		return this.tblQuizInfo.getStatus();
	}

	public void setStatus(byte status) {
		this.tblQuizInfo.setStatus(status);
	}

	public byte getMaxPlayer() {
		return this.tblQuizInfo.getMax_player();
	}

	public void setMaxPlayer(byte max_player) {
		this.tblQuizInfo.setMax_player(max_player);;
	}

	public byte getPlayerNum() {
		return this.tblQuizInfo.getPlayer_num();
	}

	public void setPlayerNum(byte player_num) {
		this.tblQuizInfo.setPlayer_num(player_num);;
	}
	
	public byte getCurrentQuestion() {
		return this.tblQuizInfo.getCurrent_question();
	}

	public void setCurrentQuestion(byte current_question) {
		this.tblQuizInfo.setCurrent_question(current_question);
	}

	public byte getSubject() {
		return this.tblQuizInfo.getSubject();
	}

	public void setSubject(byte subject) {
		this.tblQuizInfo.setSubject(subject);
	}

	public String getCurrentQuestionData() {
		return this.tblQuizInfo.getCurrent_question_data();
	}

	public void setCurrentQuestionData(String current_question_data) {
		this.tblQuizInfo.setCurrent_question_data(current_question_data);
	}

	public String getQuestionData() {
		return this.tblQuizInfo.getQuestion_data();
	}

	public void setQuestionData(String question_data) {
		this.tblQuizInfo.setQuestion_data(question_data);
	}

	public Date getCreated() {
		return this.tblQuizInfo.getCreated();
	}

	public void setCreated(Date created) {
		this.tblQuizInfo.setCreated(created);
	}

	public Date getExpired() {
		return this.tblQuizInfo.getExpired();
	}

	public void setExpired(Date expired) {
		this.tblQuizInfo.setExpired(expired);
	}

	public Date getUpdated() {
		return this.tblQuizInfo.getUpdated();
	}

	public void setUpdated(Date updated) {
		this.tblQuizInfo.setUpdated(updated);
	}

	public boolean isPreparing() {
		return (getStatus() == QuizStatus.PREPARING.getStatus());
	}
	
	public boolean isInSession() {
		return (getStatus() == QuizStatus.IN_SESSION.getStatus());
	}
	
	public boolean isFinish() {
		return (getStatus() == QuizStatus.FINISHED.getStatus());
	}
	
	public boolean initQuizData(List<QuizData> data) {
		if (data == null || data.isEmpty()) {
			return false;
		}
		StringBuilder sb = new StringBuilder();
		for (QuizData quizData : data) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(quizData.getId());
		}
		setQuestionData(sb.toString());
		setCurrentQuestion((byte)0);
		setCurrentQuestionData(String.valueOf(data.get(0).getId()));
		return true;
	}
	
}
