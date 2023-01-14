package dev.boom.common.game;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import dev.boom.common.CommonDefine;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.services.Quiz;
import dev.boom.services.QuizService;

public class QuizTimer extends Timer {

	private static final long TIME_PREPARE = 5 * CommonDefine.MILLION_SECOND_SECOND;
	private static final long TIME_BREAK = 5 * CommonDefine.MILLION_SECOND_SECOND;
	private static final long TIME_NOTICE = 5 * CommonDefine.MILLION_SECOND_SECOND;
	public static final int STEP_PREPARE = 1;
	public static final int STEP_SHOW_Q = 2;
	public static final int STEP_SHOW_NOTICE = 3;
	public static final int STEP_BREAK_TIME = 4;
	public static final int STEP_END = 9;
	private long quizId;
	private long delay;
	private int step;
	
	public QuizTimer(long quizId) {
		this.quizId = quizId;
		this.delay = 0;
		this.step = STEP_PREPARE;
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
		Quiz quizInfo = QuizService.getInsessionQuizById(quizId);
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
		switch (step) {
		case STEP_PREPARE:
			QuizService.showQuizDetail(quizInfo, true);
			GameLog.getInstance().info("[QuizTimer] show quiz detail!");
			this.step = STEP_SHOW_Q;
			this.delay = TIME_PREPARE;
			break;
		case STEP_SHOW_Q:
			if (!QuizService.nextQuizQuestion(quizInfo)) {
				if (!QuizService.endQuiz(quizInfo)) {
					GameLog.getInstance().warn("(QuizTimer) end quiz failed!, id: " + quizId);
				}
				quizInfo.setStatus(QuizStatus.FINISHED.getStatus());
				this.step = STEP_END;
				QuizService.showQuizAnswer(quizInfo);
				return;
			}
			long nNotice = quizInfo.getTimePerQuestion() - TIME_NOTICE;
			if (nNotice > 0) {
				this.step = STEP_SHOW_NOTICE;
				this.delay = nNotice;
			} else {
				this.step = STEP_BREAK_TIME;
				this.delay = quizInfo.getTimePerQuestion();
			}
			break;
		case STEP_SHOW_NOTICE:
			QuizService.showNotice(quizInfo);
			this.step = STEP_BREAK_TIME;
			this.delay = TIME_NOTICE;
			break;
		case STEP_BREAK_TIME:
			quizInfo.setStatus(QuizStatus.BREAK_TIME.getStatus());
			if (CommonDaoFactory.Update(quizInfo.getQuizInfo()) < 0) {
				GameLog.getInstance().error("[QuizTimer] enter break time failed!");
				return;
			}
			QuizService.showQuizAnswer(quizInfo);
			// update log
			GameLog.getInstance().info("[QuizTimer] show answer --- break time");
			this.step = STEP_SHOW_Q;
			this.delay = TIME_BREAK;
			break;
		default:
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

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}
	
	
	
}
