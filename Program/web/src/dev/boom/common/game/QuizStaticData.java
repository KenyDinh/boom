package dev.boom.common.game;

import java.util.HashMap;
import java.util.Map;

public class QuizStaticData {

	private static Map<Long, QuizTimer> quizTimerMap = new HashMap<>();
	
	public static void addQuizTimer(QuizTimer quizTimer) {
		quizTimerMap.put(quizTimer.getQuizId(), quizTimer);
	}
	
	public static QuizTimer getQuizTimerByQuizId(long quizId) {
		return quizTimerMap.get(quizId);
	}
	
	public static void removeQuizTimer(QuizTimer quizTimer) {
		if (quizTimerMap.containsKey(quizTimer.getQuizId())) {
			quizTimerMap.remove(quizTimer.getQuizId());
		}
	}
}
