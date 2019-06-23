package dev.boom.common.game;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import dev.boom.core.GameLog;
import dev.boom.services.QuizInfo;
import dev.boom.services.QuizService;

public class QuizTimer extends Timer {

	private long quizId;
	private long delay;
	
	public QuizTimer(long quizId, long delay) {
		this.quizId = quizId;
		this.delay = delay;
	}
	
	public void start() {
		this.schedule(new TimerTask() {
			@Override
			public void run() {
				execute();
			}
		}, delay);
	}
	
	private void execute() {
		GameLog.getInstance().info("(QuizTimer) execute!");
		QuizInfo quizInfo = QuizService.getInsessionQuizById(quizId);
		if (quizInfo == null) {
			GameLog.getInstance().error("(QuizTimer) no quiz found! id: " + quizId);
			return;
		}
		if (quizInfo.getPlayerNum() <= 0) {
			GameLog.getInstance().error("(QuizTimer) no player in this quiz, id: " + quizId);
			return;
		}
		if (quizInfo.isExpired(new Date())) {
			GameLog.getInstance().error("(QuizTimer) quiz is expired, id: " + quizId);
			if (!QuizService.endQuiz(quizInfo)) {
				GameLog.getInstance().warn("(QuizTimer) end quiz failed!, id: " + quizId);
			}
			return;
		}
		if (!QuizService.nextQuizQuestion(quizInfo)) {
			GameLog.getInstance().error("(QuizTimer) next question error, id: " + quizId);
			if (!QuizService.endQuiz(quizInfo)) {
				GameLog.getInstance().warn("(QuizTimer) end quiz failed!, id: " + quizId);
			}
			return;
		}
		start();
	}

	public long getQuizId() {
		return quizId;
	}

	public void setQuizId(long quizId) {
		this.quizId = quizId;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}
	
}
