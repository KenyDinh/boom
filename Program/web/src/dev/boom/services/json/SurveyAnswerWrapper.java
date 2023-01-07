package dev.boom.services.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.arnx.jsonic.JSON;

public class SurveyAnswerWrapper {

	public List<SurveyAnswer> survey_answer;

	public SurveyAnswerWrapper() {
	}

	public List<SurveyAnswer> getSurvey_answer() {
		return survey_answer;
	}

	public void setSurvey_answer(List<SurveyAnswer> survey_answer) {
		this.survey_answer = survey_answer;
	}
	
	public void addAnswer(byte questionIndex, String answer) {
		SurveyAnswer sa = new SurveyAnswer();
		sa.setQuestion_index(questionIndex);
		sa.setAnswer(answer);
		if (survey_answer == null) {
			survey_answer = new ArrayList<>();
		}
		for (SurveyAnswer exist : survey_answer) {
			if (exist.getQuestion_index() == questionIndex) {
				exist.setAnswer(answer);
				return;
			}
		}
		survey_answer.add(sa);
	}
	
	public String getAnswer(byte questionIndex) {
		if (survey_answer == null || survey_answer.isEmpty()) {
			return null;
		}
		for (SurveyAnswer exist : survey_answer) {
			if (exist.getQuestion_index() == questionIndex) {
				return exist.getAnswer();
			}
		}
		
		return null;
	}
	
	public String toString() {
		return JSON.encode(this);
	}
	
	public static SurveyAnswerWrapper parse(String result) {
		if (StringUtils.isEmpty(result)) {
			return null;
		}
		try {
			return JSON.decode(result, SurveyAnswerWrapper.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
