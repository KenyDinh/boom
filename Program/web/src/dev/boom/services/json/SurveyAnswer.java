package dev.boom.services.json;

public class SurveyAnswer {

	public byte question_index;
	public String answer;

	public SurveyAnswer() {
	}

	public byte getQuestion_index() {
		return question_index;
	}

	public void setQuestion_index(byte question_index) {
		this.question_index = question_index;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
