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

public class Quiz {
	private TblQuizInfo quizInfo;

	public Quiz() {
		quizInfo = new TblQuizInfo();
	}

	public Quiz(TblQuizInfo quizInfo) {
		this.quizInfo = quizInfo;
	}

	public TblQuizInfo getQuizInfo() {
		return quizInfo;
	}

	public long getId() {
		return (Long) quizInfo.Get("id");
	}

	public void setId(long id) {
		quizInfo.Set("id", id);
	}

	public long getHost() {
		return (Long) quizInfo.Get("host");
	}

	public void setHost(long host) {
		quizInfo.Set("host", host);
	}

	public String getName() {
		return (String) quizInfo.Get("name");
	}

	public void setName(String name) {
		quizInfo.Set("name", name);
	}

	public byte getSubject() {
		return (Byte) quizInfo.Get("subject");
	}

	public void setSubject(byte subject) {
		quizInfo.Set("subject", subject);
	}

	public byte getLevel() {
		return (Byte) quizInfo.Get("level");
	}

	public void setLevel(byte level) {
		quizInfo.Set("level", level);
	}

	public byte getMaxPlayer() {
		return (Byte) quizInfo.Get("max_player");
	}

	public void setMaxPlayer(byte maxPlayer) {
		quizInfo.Set("max_player", maxPlayer);
	}

	public byte getQuestionNum() {
		return (Byte) quizInfo.Get("question_num");
	}

	public void setQuestionNum(byte questionNum) {
		quizInfo.Set("question_num", questionNum);
	}

	public long getTimePerQuestion() {
		return (Long) quizInfo.Get("time_per_question");
	}

	public void setTimePerQuestion(long timePerQuestion) {
		quizInfo.Set("time_per_question", timePerQuestion);
	}

	public byte getStatus() {
		return (Byte) quizInfo.Get("status");
	}

	public void setStatus(byte status) {
		quizInfo.Set("status", status);
	}

	public byte getRetry() {
		return (Byte) quizInfo.Get("retry");
	}

	public void setRetry(byte retry) {
		quizInfo.Set("retry", retry);
	}

	public byte getFlag() {
		return (Byte) quizInfo.Get("flag");
	}

	public void setFlag(byte flag) {
		quizInfo.Set("flag", flag);
	}
	
	public boolean isShowDescription() {
		return (getFlag() > 0);
	}

	public byte getPlayerNum() {
		return (Byte) quizInfo.Get("player_num");
	}

	public void setPlayerNum(byte playerNum) {
		quizInfo.Set("player_num", playerNum);
	}

	public byte getCurrentQuestion() {
		return (Byte) quizInfo.Get("current_question");
	}

	public void setCurrentQuestion(byte currentQuestion) {
		quizInfo.Set("current_question", currentQuestion);
	}

	public String getCurrentOptionOrder() {
		return (String) quizInfo.Get("current_option_order");
	}

	public void setCurrentOptionOrder(String currentOptionOrder) {
		quizInfo.Set("current_option_order", currentOptionOrder);
	}

	public String getQuestionData() {
		return (String) quizInfo.Get("question_data");
	}

	public void setQuestionData(String questionData) {
		quizInfo.Set("question_data", questionData);
	}

	public String getCreated() {
		return (String) quizInfo.Get("created");
	}

	public void setCreated(String created) {
		quizInfo.Set("created", created);
	}

	public Date getCreatedDate() {
		String strCreated = getCreated();
		if (strCreated == null) {
			return null;
		}
		return CommonMethod.getDate(strCreated, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getExpired() {
		return (String) quizInfo.Get("expired");
	}

	public void setExpired(String expired) {
		quizInfo.Set("expired", expired);
	}

	public Date getExpiredDate() {
		String strExpired = getExpired();
		if (strExpired == null) {
			return null;
		}
		return CommonMethod.getDate(strExpired, CommonDefine.DATE_FORMAT_PATTERN);
	}

	public String getUpdated() {
		return (String) quizInfo.Get("updated");
	}

	public void setUpdated(String updated) {
		quizInfo.Set("updated", updated);
	}

	public Date getUpdatedDate() {
		String strUpdated = getUpdated();
		if (strUpdated == null) {
			return null;
		}
		return CommonMethod.getDate(strUpdated, CommonDefine.DATE_FORMAT_PATTERN);
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
		return getExpiredDate().before(now);
	}
	
	public void updateExpired() {
		long current = getExpiredDate().getTime() - getCreatedDate().getTime();
		long newTime = getTimePerQuestion() * getQuestionNum() + 10 * CommonDefine.MILLION_SECOND_MINUTE;
		if (newTime > current) {
			setExpired(CommonMethod.getFormatDateString(new Date(getCreatedDate().getTime() + newTime)));
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

		return null;
	}

}

