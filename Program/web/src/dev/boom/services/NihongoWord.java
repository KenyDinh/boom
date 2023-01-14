package dev.boom.services;

import java.util.Date;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.tbl.info.TblNihongoWordInfo;

public class NihongoWord {
	private TblNihongoWordInfo nihongoWordInfo;

	public NihongoWord() {
		nihongoWordInfo = new TblNihongoWordInfo();
	}

	public NihongoWord(TblNihongoWordInfo nihongoWordInfo) {
		this.nihongoWordInfo = nihongoWordInfo;
	}

	public TblNihongoWordInfo getNihongoWordInfo() {
		return nihongoWordInfo;
	}

	public long getId() {
		return (Long) nihongoWordInfo.Get("id");
	}

	public void setId(long id) {
		nihongoWordInfo.Set("id", id);
	}

	public String getWord() {
		return (String) nihongoWordInfo.Get("word");
	}

	public void setWord(String word) {
		nihongoWordInfo.Set("word", word);
	}

	public String getSideword() {
		return (String) nihongoWordInfo.Get("sideword");
	}

	public void setSideword(String sideword) {
		nihongoWordInfo.Set("sideword", sideword);
	}

	public int getWordtype() {
		return (Integer) nihongoWordInfo.Get("wordtype");
	}

	public void setWordtype(int wordtype) {
		nihongoWordInfo.Set("wordtype", wordtype);
	}

	public String getMeaning() {
		return (String) nihongoWordInfo.Get("meaning");
	}

	public void setMeaning(String meaning) {
		nihongoWordInfo.Set("meaning", meaning);
	}

	public String getDescription() {
		return (String) nihongoWordInfo.Get("description");
	}

	public void setDescription(String description) {
		nihongoWordInfo.Set("description", description);
	}

	public int getReference() {
		return (Integer) nihongoWordInfo.Get("reference");
	}

	public void setReference(int reference) {
		nihongoWordInfo.Set("reference", reference);
	}

	public String getCreated() {
		return (String) nihongoWordInfo.Get("created");
	}

	public void setCreated(String created) {
		nihongoWordInfo.Set("created", created);
	}

	public Date getCreatedDate() {
		String strCreated = getCreated();
		if (strCreated == null) {
			return null;
		}
		return CommonMethod.getDate(strCreated, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getUpdated() {
		return (String) nihongoWordInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		nihongoWordInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
	}

}

