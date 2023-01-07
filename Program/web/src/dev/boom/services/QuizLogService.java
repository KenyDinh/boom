package dev.boom.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.game.QuizSubject;
import dev.boom.dao.core.DaoValue;
import dev.boom.socket.func.PatpatFunc;
import dev.boom.tbl.info.TblQuizLogInfo;

public class QuizLogService {
	
	public static final String CORRECT_ICON = ":white_check_mark:";
	public static final String WRONG_ICON = ":x:";

	private QuizLogService() {
	}
	
	public static String getQuizLogData(QuizInfo quiz) {
		StringBuilder sb = new StringBuilder();
		TblQuizLogInfo info = new TblQuizLogInfo();
		info.Set("quiz_id", quiz.getId());
		boolean isInsession = (!quiz.isFinish() && quiz.getCurrentQuestion() > 0);
		if (isInsession) {
			byte curQ = quiz.getCurrentQuestion();
			info.Set("question_index", curQ);
			//description
			sb.append("The correct answer : " + quiz.getCorrectAnswer());
			sb.append("\n");
			if (quiz.isShowDescription()) {
				List<Integer> questionList = quiz.getQuizDataIds();
				if (questionList != null && !questionList.isEmpty()) {
					if (curQ >= 1 && curQ <= questionList.size()) {
						QuizData questionData = QuizDataService.getQuizDataById(QuizSubject.valueOf(quiz.getSubject()), questionList.get(curQ - 1));
						if (questionData != null) {
							String descLabel = questionData.getDescriptionLabel();
							if (descLabel != null) {
								String descMsg = PatpatFunc.getMessage(questionData.getDescriptionLabel());
								if (StringUtils.isNotBlank(descMsg)) {
									sb.append("```");
									sb.append("\n");
									sb.append(descMsg);
									sb.append("\n");
									sb.append("```");
									sb.append("\n");
								}
							}
						}
					}
				}
			}
		}
		info.setSelectOption("ORDER BY question_index ASC, id ASC");
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			sb.append("🠖 No one answered 🠔");
			return sb.toString();
		}
		byte qStart = 1;
		byte qEnd = quiz.getQuestionNum();
		if (isInsession) {
			qStart = quiz.getCurrentQuestion();
			qEnd = qStart;
		}
		Map<String, String> mapAnswerCheck = new HashMap<>();
		Set<String> setPlayer = new HashSet<>();
		String key;
		String value;
		for (DaoValue dao : list) {
			QuizLog quizLog = new QuizLog((TblQuizLogInfo) dao);
			key = String.format("%s_%d", quizLog.getUsername(), quizLog.getQuestIndex());
			if (quizLog.isCorrect()) {
				value = CORRECT_ICON;
			} else {
				value = WRONG_ICON;
			}
			mapAnswerCheck.put(key, value);
			setPlayer.add(quizLog.getUsername());
		}
		sb.append("\n");
		if (isInsession) {
			sb.append("🠖 Question Result 🠔");
		} else {
			sb.append("🠖 Quiz Result 🠔");
		}
		sb.append("\n");
		sb.append("\n");
		sb.append("|#Player|");
		for (int questIndex = qStart; questIndex <= qEnd; questIndex++) {
			sb.append("#Q_").append(questIndex).append("|");
		}
		if (!isInsession) {
			sb.append("#Total").append("|");
		}
		sb.append("\n");
		sb.append("|:---:|");
		for (int questIndex = qStart; questIndex <= qEnd; questIndex++) {
			sb.append(":---:|");
		}
		if (!isInsession) {
			sb.append(":---:|");
		}
		sb.append("\n");
		for (String playerName : setPlayer) {
			int total = 0;
			sb.append("|@").append(playerName).append("|");
			for (int questIndex = qStart; questIndex <= qEnd; questIndex++) {
				key = String.format("%s_%d", playerName, questIndex);
				if (mapAnswerCheck.containsKey(key)) {
					String icon = mapAnswerCheck.get(key);
					if (icon.equals(CORRECT_ICON)) {
						total++;
					}
					sb.append(icon).append("|");
				} else {
					sb.append("?|");
				}
			}
			if (!isInsession) {
				sb.append(total).append("|");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
