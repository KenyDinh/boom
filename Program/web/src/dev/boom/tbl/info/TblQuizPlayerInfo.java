package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblQuizPlayerInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "quiz_player_info";
	private static final String PRIMARY_KEY = "user_id";

	private long user_id;
	private String username;
	private long quiz_id;
	private byte status;
	private String answer;
	private byte correct_count;
	private int correct_point;
	private Date updated;

	public TblQuizPlayerInfo() {
		this.user_id = 0;
		this.username = "";
		this.quiz_id = 0;
		this.status = 0;
		this.answer = "";
		this.correct_count = 0;
		this.correct_point = 0;
		this.updated = new Date();
		Sync();
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getQuiz_id() {
		return quiz_id;
	}

	public void setQuiz_id(long quiz_id) {
		this.quiz_id = quiz_id;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public byte getCorrect_count() {
		return correct_count;
	}

	public void setCorrect_count(byte correct_count) {
		this.correct_count = correct_count;
	}

	public int getCorrect_point() {
		return correct_point;
	}

	public void setCorrect_point(int correct_point) {
		this.correct_point = correct_point;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public List<String> getSubKey() {
		return null;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
