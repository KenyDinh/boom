package dev.boom.common.game;

import dev.boom.services.Quiz;
import dev.boom.services.QuizPlayer;
import dev.boom.services.QuizService;

public class QuizFunc {
	
	public static String canJoinQuiz(QuizPlayer quizPlayer, Quiz quiz) {
		if (quizPlayer == null || quiz == null) {
			return "Null parameters!";
		}
		if (quizPlayer.getQuizId() == quiz.getId()) {
			return "Already joined this quiz!";
		}
		if (quizPlayer.getStatus() != QuizPlayerStatus.FINISHED.getStatus()) {
			if (quizPlayer.getQuizId() > 0 && QuizService.getInsessionQuizById(quizPlayer.getQuizId()) != null) {
				return "Already joined in another quiz!";
			}
		}
		if (quiz.getPlayerNum() >= quiz.getMaxPlayer()) {
			return "Full player, can not join!";
		}
		return null;
	}
	
	public static String canQuitQuiz(QuizPlayer quizPlayer, Quiz quiz) {
		if (quizPlayer == null || quiz == null) {
			return "Null parameters!";
		}
		if (quizPlayer.getQuizId() != quiz.getId()) {
			return "User is not in this quiz!";
		}
		if (quiz.getStatus() == QuizStatus.FINISHED.getStatus()) {
			return "This quiz is ended!";
		}
		if (quizPlayer.getStatus() == QuizPlayerStatus.FINISHED.getStatus()) {
			return "User already quit!";
		}
		return null;
	}
	
	public static String getParamDescription(String param) {
		if (param == null || param.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		switch (param) {
		case QuizDefine.PARAM_Q_LANG:
			sb.append("Type of quiz");
			break;
		default:
			break;
		}
		return "";
	}
}
