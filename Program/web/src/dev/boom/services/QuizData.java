package dev.boom.services;

import dev.boom.tbl.data.TblQuizData;

public class QuizData {

	private TblQuizData tblQuizData;

	public QuizData(TblQuizData tblQuizData) {
		this.tblQuizData = tblQuizData;
	}

	public QuizData() {
	}

	public int getId() {
		return this.tblQuizData.getId();
	}

	public short getLevel() {
		return this.tblQuizData.getLevel();
	}

	public byte getType() {
		return this.tblQuizData.getType();
	}

	public String getLabel() {
		return this.tblQuizData.getLabel();
	}
}
