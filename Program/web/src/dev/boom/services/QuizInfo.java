package dev.boom.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.game.QuizStatus;
import dev.boom.common.game.QuizSubject;
import dev.boom.core.GameLog;
import dev.boom.tbl.info.TblQuizInfo;

public class QuizInfo {

	private TblQuizInfo tblQuizInfo = null;

	public QuizInfo() {
		this.tblQuizInfo = new TblQuizInfo();
	}

	public QuizInfo(TblQuizInfo tblQuizInfo) {
		this.tblQuizInfo = tblQuizInfo;
	}

	public TblQuizInfo getTblQuizInfo() {
		return tblQuizInfo;
	}

	public long getId() {
		return this.tblQuizInfo.getId();
	}

	public void setId(long id) {
		this.tblQuizInfo.setId(id);
	}
	
	public long getHost() {
		return this.tblQuizInfo.getHost();
	}

	public void setHost(long host) {
		this.tblQuizInfo.setHost(host);
	}

	public String getName() {
		return this.tblQuizInfo.getName();
	}

	public void setName(String name) {
		this.tblQuizInfo.setName(name);
	}

	public byte getLevel() {
		return this.tblQuizInfo.getLevel();
	}

	public void setLevel(byte level) {
		this.tblQuizInfo.setLevel(level);
	}

	public byte getQuestionNum() {
		return this.tblQuizInfo.getQuestion_num();
	}

	public void setQuestionNum(byte question_num) {
		this.tblQuizInfo.setQuestion_num(question_num);
	}

	public long getTimePerQuestion() {
		return this.tblQuizInfo.getTime_per_question();
	}

	public void setTimePerQuestion(long time_per_question) {
		this.tblQuizInfo.setTime_per_question(time_per_question);
	}

	public byte getStatus() {
		return this.tblQuizInfo.getStatus();
	}

	public void setStatus(byte status) {
		this.tblQuizInfo.setStatus(status);
	}

	public byte getRetry() {
		return this.tblQuizInfo.getRetry();
	}

	public void setRetry(byte retry) {
		this.tblQuizInfo.setRetry(retry);
	}
	
	public byte getFlag() {
		return (Byte) this.tblQuizInfo.getFlag();
	}
	
	public void setFlag(byte flag) {
		this.tblQuizInfo.setFlag(flag);
	}

	public boolean isShowDescription() {
		return (getFlag() > 0);
	}
	
	public byte getMaxPlayer() {
		return this.tblQuizInfo.getMax_player();
	}

	public void setMaxPlayer(byte max_player) {
		this.tblQuizInfo.setMax_player(max_player);;
	}

	public byte getPlayerNum() {
		return this.tblQuizInfo.getPlayer_num();
	}

	public void setPlayerNum(byte player_num) {
		this.tblQuizInfo.setPlayer_num(player_num);;
	}
	
	public byte getCurrentQuestion() {
		return this.tblQuizInfo.getCurrent_question();
	}

	/**
	 * current question index
	 * @param current_question
	 */
	public void setCurrentQuestion(byte current_question) {
		this.tblQuizInfo.setCurrent_question(current_question);
	}

	public byte getSubject() {
		return this.tblQuizInfo.getSubject();
	}

	public void setSubject(byte subject) {
		this.tblQuizInfo.setSubject(subject);
	}

	public String getCurrentOptionOrder() {
		return this.tblQuizInfo.getCurrent_option_order();
	}
	
	public void setCurrentOptionOrder(String current_option_order) {
		this.tblQuizInfo.setCurrent_option_order(current_option_order);
	}
	
	public List<Integer> getCurrentOptionIndexList() {
		String questOrder = getCurrentOptionOrder();
		if (questOrder == null || questOrder.isEmpty()) {
			return null;
		}
		List<Integer> ret = new ArrayList<>();
		String[] optionArray = questOrder.split(",");
		for (String strIndex : optionArray) {
			if (!StringUtils.isNotBlank(strIndex) || !StringUtils.isNumeric(strIndex)) {
				return null;
			}
			ret.add(Integer.parseInt(strIndex));
		}
		return ret;
	}
	
	public String getQuestionData() {
		return this.tblQuizInfo.getQuestion_data();
	}

	/**
	 * Question id list in string, separate by comma
	 * @param question_data
	 */
	public void setQuestionData(String question_data) {
		this.tblQuizInfo.setQuestion_data(question_data);
	}

	public Date getCreated() {
		return this.tblQuizInfo.getCreated();
	}

	public void setCreated(Date created) {
		this.tblQuizInfo.setCreated(created);
	}

	public Date getExpired() {
		return this.tblQuizInfo.getExpired();
	}

	public void setExpired(Date expired) {
		this.tblQuizInfo.setExpired(expired);
	}

	public Date getUpdated() {
		return this.tblQuizInfo.getUpdated();
	}

	public void setUpdated(Date updated) {
		this.tblQuizInfo.setUpdated(updated);
	}

	public boolean isPreparing() {
		return (getStatus() == QuizStatus.PREPARING.getStatus());
	}
	
	public boolean isInSession() {
		return (getStatus() == QuizStatus.IN_SESSION.getStatus());
	}
	
	public boolean isBreakTime() {
		return (getStatus() == QuizStatus.BREAK_TIME.getStatus());
	}
	
	public boolean isFinish() {
		return (getStatus() == QuizStatus.FINISHED.getStatus());
	}
	
	public boolean isExpired(Date now) {
		return getExpired().before(now);
	}
	
	public void updateExpired() {
		long current = getExpired().getTime() - getCreated().getTime();
		long newTime = getTimePerQuestion() * getQuestionNum() + 10 * CommonDefine.MILLION_SECOND_MINUTE;
		if (newTime > current) {
			setExpired(new Date(getCreated().getTime() + newTime));
		}
	}
	
	public boolean initQuizData(List<QuizData> data) {
		if (data == null || data.isEmpty()) {
			return false;
		}
		StringBuilder sb = new StringBuilder();
		for (QuizData quizData : data) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(quizData.getId());
		}
		setQuestionData(sb.toString());
		setCurrentQuestion((byte)0);
		if (!initQuizOptionOrder(data.get(0))) {
			return false;
		}
		return true;
	}
	
	public boolean initQuizOptionOrder(QuizData quizData) {
		if (quizData == null) {
			return false;
		}
		List<QuizOptionData> optionList = QuizDataService.getQuizOptionDataByQId(QuizSubject.valueOf(getSubject()), quizData.getId());
		if (optionList == null || optionList.isEmpty()) {
			return false;
		}
		List<String> optionIndex = new ArrayList<>();
		for (int i = 0; i < optionList.size(); i++) {
			optionIndex.add(String.valueOf(i + 1));
		}
		if (!isShowDescription() && getSubject() != QuizSubject.Programming.getSubject()) {
			Collections.shuffle(optionIndex);
		}
		setCurrentOptionOrder(String.join(",", optionIndex));
		return true;
	}
	
	public List<Integer> getQuizDataIds() {
		String qList = getQuestionData();
		if (qList == null || qList.isEmpty()) {
			return null;
		}
		List<Integer> ret = new ArrayList<>();
		String[] arr = qList.split(",");
		if (arr.length != getQuestionNum()) {
			GameLog.getInstance().warn("[getQuizDataIds] number of question not match!");
		}
		for (String strId : arr) {
			if (CommonMethod.isValidNumeric(strId, 1, Integer.MAX_VALUE)) {
				ret.add(Integer.valueOf(strId));
			}
		}
		return ret;
	}
	
	public String getCorrectAnswer() {
		int curQIndex = getCurrentQuestion();
		if (curQIndex <= 0) {
			GameLog.getInstance().error("curent q index wrong");
			return null;
		}
		List<Integer> quizData = getQuizDataIds();
		if (quizData == null || quizData.isEmpty()) {
			GameLog.getInstance().error("quiz data id is null");
			return null;
		}
		if (quizData.size() < curQIndex) {
			GameLog.getInstance().error("quiz data size is invalid");
			return null;
		}
		QuizData curQuizData = QuizDataService.getQuizDataById(QuizSubject.valueOf(getSubject()), quizData.get(curQIndex - 1));
		if (curQuizData == null) {
			GameLog.getInstance().error("current quiz data is null");
			return null;
		}
		List<QuizOptionData> optionDatas = QuizDataService.getQuizOptionDataByQId(QuizSubject.valueOf(getSubject()), curQuizData.getId());
		if (optionDatas == null || optionDatas.isEmpty()) {
			GameLog.getInstance().error("Option data is null");
			return null;
		}
		List<Integer> optionIndex = getCurrentOptionIndexList();
		if (optionIndex == null || optionIndex.isEmpty()) {
			GameLog.getInstance().error("Option index null");
			return null;
		}
		for (int i = 0; i < optionIndex.size(); i++) {
			if (optionIndex.get(i) > optionDatas.size() || optionIndex.get(i) <= 0) {
				continue;
			}
			if (optionDatas.get(optionIndex.get(i) - 1).isCorrectAnswer()) {
				return String.valueOf((char) (i + 'A'));
			}
		}
		GameLog.getInstance().error("LOLOLOLOLL");
		return null;
	}
	
}
