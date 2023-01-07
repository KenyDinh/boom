package dev.boom.tbl.info;

import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblQuizQuestionInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "quiz_question_info";
	private static final String PRIMARY_KEY = "id";

	public int id;
	public byte subject;
	public byte type;
	public short level;
	public String content;
	public String description;

	public TblQuizQuestionInfo() {
		this.id = 0;
		this.subject = 0;
		this.level = 0;
		this.type = 0;
		this.content = "";
		this.description = "";
		Sync();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getSubject() {
		return subject;
	}

	public void setSubject(byte subject) {
		this.subject = subject;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
