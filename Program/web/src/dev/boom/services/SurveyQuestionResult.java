package dev.boom.services;

import dev.boom.tbl.info.TblSurveyQuestionInfo;

public class SurveyQuestionResult {

	private TblSurveyQuestionInfo questionInfo = null;
	private long voteCount = 0;
	
	public SurveyQuestionResult(TblSurveyQuestionInfo questionInfo) {
		this.questionInfo = questionInfo;
	}

	public long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(long voteCount) {
		this.voteCount = voteCount;
	}

	public TblSurveyQuestionInfo getQuestionInfo() {
		return questionInfo;
	}
	
	
	
}
