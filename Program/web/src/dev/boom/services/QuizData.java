package dev.boom.services;

import dev.boom.dao.core.DaoValue;

public class QuizData {

	private DaoValue tblQuizData;

	public QuizData(DaoValue tblQuizData) {
		this.tblQuizData = tblQuizData;
	}

	public QuizData() {
	}

	public int getId() {
		return (Integer) tblQuizData.Get("id");
	}

	public short getLevel() {
		return (Short) tblQuizData.Get("level");
	}

	public byte getType() {
		return (Byte) tblQuizData.Get("type");
	}

	public String getLabel() {
		return (String) tblQuizData.Get("label");
	}
	
	public String getDescriptionLabel() {
		return (String) tblQuizData.Get("desc_label");
	}
}
