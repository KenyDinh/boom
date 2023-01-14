package dev.boom.tbl.info;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dev.boom.dao.DaoValueInfo;
import dev.boom.dao.IDaoValue;

public class TblNihongoWordInfo extends DaoValueInfo implements IDaoValue {

	private static final String TABLE_NAME = "nihongo_word_info";
	private static final String PRIMARY_KEY = "id";
	private static final String SUB_KEY = ""; // <><>
	private static Map<String, String> mapForeignKey = new HashMap<String, String>();

	public final class Fields implements IDaoValue.Fields {

		public long id;
		public String word;
		public String sideword;
		public int wordtype;
		public String meaning;
		public String description;
		public int reference;
		public String created;
		public String updated;

		public Fields() {
			id = 0;
			word = "";
			sideword = "";
			wordtype = 0;
			meaning = "";
			description = "";
			reference = 0;
			created = "";
			updated = "";
		}
	}

	private Fields fieldRead;

	private Fields fieldWrite;

	private static Field[] fields;

	public TblNihongoWordInfo() {
		fieldRead = new Fields();
		fieldWrite = new Fields();

		if (fields == null) {
			fields = fieldRead.getClass().getFields();
		}
	}

	public String getTblName() {
		return TABLE_NAME;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getSubKey() {
		return SUB_KEY;
	}

	public String getForeignKey(String strKey) {
		return mapForeignKey.get(strKey);
	}

	public Field[] getClassField() {
		return fields;
	}

	public Object getFieldRead() {
		return (Object) fieldRead;
	}

	public Object getFieldWrite() {
		return (Object) fieldWrite;
	}

	public Fields getInstance() {
		return fieldWrite;
	}

	public void Sync() {
		fieldRead.id = fieldWrite.id;
		fieldRead.word = fieldWrite.word;
		fieldRead.sideword = fieldWrite.sideword;
		fieldRead.wordtype = fieldWrite.wordtype;
		fieldRead.meaning = fieldWrite.meaning;
		fieldRead.description = fieldWrite.description;
		fieldRead.reference = fieldWrite.reference;
		fieldRead.created = fieldWrite.created;
		fieldRead.updated = fieldWrite.updated;
	}
}

