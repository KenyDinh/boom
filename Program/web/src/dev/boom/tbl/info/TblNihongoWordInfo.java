package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.dao.core.IDaoValue;

public class TblNihongoWordInfo extends DaoValueInfo implements IDaoValue {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "nihongo_word_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private String word;
	private String sideword;
	private int wordtype;
	private String meaning;
	private String description;
	private int reference;
	private Date created;
	private Date updated;

	public TblNihongoWordInfo() {
		this.id = 0;
		this.word = "";
		this.sideword = "";
		this.wordtype = 0;
		this.meaning = "";
		this.description = "";
		this.reference = 0;
		this.created = new Date();
		this.updated = this.created;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getSideword() {
		return sideword;
	}

	public void setSideword(String sideword) {
		this.sideword = sideword;
	}

	public int getWordtype() {
		return wordtype;
	}

	public void setWordtype(int wordtype) {
		this.wordtype = wordtype;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getReference() {
		return reference;
	}

	public void setReference(int reference) {
		this.reference = reference;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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
