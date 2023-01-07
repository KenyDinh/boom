package dev.boom.tbl.info;

import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblQuizLogInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "quiz_log_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long quiz_id;
	private byte question_index;
	private long user_id;
	private String username;
	private String correct_answer;
	private String player_answer;

	public TblQuizLogInfo() {
		this.id = 0;
		this.quiz_id = 0;
		this.question_index = 0;
		this.user_id = 0;
		this.username = "";
		this.correct_answer = "";
		this.player_answer = "";
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getQuiz_id() {
		return quiz_id;
	}

	public void setQuiz_id(long quiz_id) {
		this.quiz_id = quiz_id;
	}

	public byte getQuestion_index() {
		return question_index;
	}

	public void setQuestion_index(byte question_index) {
		this.question_index = question_index;
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

	public String getCorrect_answer() {
		return correct_answer;
	}

	public void setCorrect_answer(String correct_answer) {
		this.correct_answer = correct_answer;
	}

	public String getPlayer_answer() {
		return player_answer;
	}

	public void setPlayer_answer(String player_answer) {
		this.player_answer = player_answer;
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
