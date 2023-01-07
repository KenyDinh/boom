package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.common.CommonDefine;
import dev.boom.common.game.QuizDefine;
import dev.boom.dao.core.DaoValueInfo;

public class TblQuizInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "quiz_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long host;
	private String name;
	private byte subject;
	private byte level;
	private byte max_player;
	private byte question_num;
	private long time_per_question;
	private byte status;
	private byte retry;
	private byte flag;
	private byte player_num;
	private byte current_question;
	private String current_option_order;
	private String question_data;
	private Date created;
	private Date expired;
	private Date updated;

	public TblQuizInfo() {
		this.id = 0;
		this.host = 0;
		this.name = "";
		this.subject = 0;
		this.level = 0;
		this.max_player = QuizDefine.DEFAULT_PLAYER_NUM;
		this.question_num = QuizDefine.DEFAULT_QUESTION_NUM;
		this.time_per_question = QuizDefine.DEFAULT_TIME_PER_QUESTION;
		this.status = 0;
		this.retry = 1;
		this.flag = 0;
		this.player_num = 0;
		this.current_question = 0;
		this.current_option_order = "";
		this.question_data = "";
		this.created = new Date();
		this.expired = new Date(this.created.getTime() + 15 * CommonDefine.MILLION_SECOND_MINUTE);
		this.updated = this.created;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getHost() {
		return host;
	}

	public void setHost(long host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public byte getQuestion_num() {
		return question_num;
	}

	public void setQuestion_num(byte question_num) {
		this.question_num = question_num;
	}

	public long getTime_per_question() {
		return time_per_question;
	}

	public void setTime_per_question(long time_per_question) {
		this.time_per_question = time_per_question;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public byte getRetry() {
		return retry;
	}

	public void setRetry(byte retry) {
		this.retry = retry;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public byte getMax_player() {
		return max_player;
	}

	public void setMax_player(byte max_player) {
		this.max_player = max_player;
	}

	public byte getPlayer_num() {
		return player_num;
	}

	public void setPlayer_num(byte player_num) {
		this.player_num = player_num;
	}

	public byte getCurrent_question() {
		return current_question;
	}

	public void setCurrent_question(byte current_question) {
		this.current_question = current_question;
	}

	public byte getSubject() {
		return subject;
	}

	public void setSubject(byte subject) {
		this.subject = subject;
	}

	public String getCurrent_option_order() {
		return current_option_order;
	}

	public void setCurrent_option_order(String current_option_order) {
		this.current_option_order = current_option_order;
	}

	public String getQuestion_data() {
		return question_data;
	}

	public void setQuestion_data(String question_data) {
		this.question_data = question_data;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getExpired() {
		return expired;
	}

	public void setExpired(Date expired) {
		this.expired = expired;
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
