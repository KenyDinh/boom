package dev.boom.common.game;

import dev.boom.services.QuizInfo;
import dev.boom.services.QuizPlayerInfo;
import dev.boom.services.QuizService;

public class QuizFun {

	public static String canJoinQuiz(QuizPlayerInfo quizPlayer, QuizInfo quiz) {
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
		return null;
	}
	
	public static String canQuitQuiz(QuizPlayerInfo quizPlayer, QuizInfo quiz) {
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
}
