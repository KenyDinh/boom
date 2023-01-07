package dev.boom.services;

import dev.boom.dao.core.DaoValue;

public class QuizOptionData {

	private DaoValue tblQuizOptionData;

	public QuizOptionData() {
	}

	public QuizOptionData(DaoValue tblQuizOptionData) {
		this.tblQuizOptionData = tblQuizOptionData;
	}

	public int getId() {
		return (Integer) tblQuizOptionData.Get("id");
	}

	public int getQuizDataId() {
		return (Integer) tblQuizOptionData.Get("quiz_data_id");
	}

	public String getOption() {
		return (String) this.tblQuizOptionData.Get("option");
	}
	
	public boolean isCorrectAnswer() {
		return ((Integer) this.tblQuizOptionData.Get("correct") != 0);
	}

}
