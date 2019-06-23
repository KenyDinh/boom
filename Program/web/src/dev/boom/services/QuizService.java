package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import dev.boom.common.game.QuizPlayerStatus;
import dev.boom.common.game.QuizStatus;
import dev.boom.connect.HibernateSessionFactory;
import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.socket.func.PatpatFunc;
import dev.boom.socket.func.PatpatOutgoingMessage;
import dev.boom.tbl.info.TblQuizInfo;

public class QuizService {

	public static QuizInfo getInsessionQuizByName(String name) {
		TblQuizInfo tblQuizInfo = new TblQuizInfo();
		tblQuizInfo.Set("name", name);
		tblQuizInfo.Set("status", QuizStatus.IN_SESSION.getStatus());
		tblQuizInfo.setSelectOption("AND expired > NOW()");

		List<DaoValue> list = CommonDaoService.select(tblQuizInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			GameLog.getInstance().warn("[QuizService](getQuizInfoByName) more than 1 quiz info");
		}

		return new QuizInfo((TblQuizInfo) list.get(0));
	}
	
	public static QuizInfo getNotFinishQuizByName(String name) {
		TblQuizInfo tblQuizInfo = new TblQuizInfo();
		tblQuizInfo.Set("name", name);
		tblQuizInfo.setSelectOption("AND status <> " + QuizStatus.FINISHED.getStatus());
		tblQuizInfo.setSelectOption("AND expired > NOW()");

		List<DaoValue> list = CommonDaoService.select(tblQuizInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			GameLog.getInstance().warn("[QuizService](getQuizInfoByName) more than 1 quiz info");
		}

		return new QuizInfo((TblQuizInfo) list.get(0));
	}

	public static QuizInfo getQuizById(long id) {
		TblQuizInfo tblQuizInfo = new TblQuizInfo();
		tblQuizInfo.Set("id", id);

		List<DaoValue> list = CommonDaoService.select(tblQuizInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		return new QuizInfo((TblQuizInfo) list.get(0));
	}

	public static QuizInfo getInsessionQuizById(long id) {
		TblQuizInfo tblQuizInfo = new TblQuizInfo();
		tblQuizInfo.Set("id", id);
		tblQuizInfo.setSelectOption("AND status <> " + QuizStatus.FINISHED.getStatus());
		tblQuizInfo.setSelectOption("AND expired > NOW()");

		List<DaoValue> list = CommonDaoService.select(tblQuizInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		return new QuizInfo((TblQuizInfo) list.get(0));
	}

	/////////////////////////
	public static boolean createQuiz(QuizPlayerInfo quizPlayer, QuizInfo quiz) {
		if (quiz == null || quizPlayer == null) {
			return false;
		}
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			GameLog.getInstance().info("Transaction Begin!");
			tx = session.beginTransaction();
			quiz.setPlayerNum((byte) 1);
			Long id = (Long) CommonDaoService.insert(session, quiz.getTblQuizInfo());
			if (id != null && id.longValue() > 0) {
				quizPlayer.initNewQuiz(quiz);
				quizPlayer.setQuizId(id.longValue());
				if (quizPlayer.getTblQuizPlayerInfo().isInsert()) {
					if (CommonDaoService.insert(session, quizPlayer.getTblQuizPlayerInfo()) == null) {
						GameLog.getInstance().error("[createQuiz] create quiz player fail!");
						tx.rollback();
						return false;
					}
				} else {
					if (!CommonDaoService.update(session, quizPlayer.getTblQuizPlayerInfo())) {
						GameLog.getInstance().error("[createQuiz] create quiz player fail!");
						tx.rollback();
						return false;
					}
				}
			} else {
				GameLog.getInstance().error("[createQuiz] create quiz fail!");
				tx.rollback();
				return false;
			}
			tx.commit();
			GameLog.getInstance().info("Transaction Commit!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}
		return true;
	}
	
	public static boolean startQuiz(QuizInfo quiz) {
		List<String> list = new ArrayList<>();
		if (!quiz.isInSession()) {
			list.add(String.format("UPDATE quiz_info SET status = %d WHERE id = %d", QuizStatus.IN_SESSION.getStatus(), quiz.getId()));
		}
		list.add(String.format("UPDATE quiz_player_info SET status = %d WHERE quiz_id = %d", QuizPlayerStatus.PLAYING.getStatus(), quiz.getId()));
		return CommonDaoService.executeQueryUpdate(list);
	}
	
	public static boolean endQuiz(QuizInfo quiz) {
		List<String> list = new ArrayList<>();
		if (!quiz.isFinish()) {
			list.add(String.format("UPDATE quiz_info SET status = %d WHERE id = %d", QuizStatus.FINISHED.getStatus(), quiz.getId()));
		}
		list.add(String.format("UPDATE quiz_player_info SET status = %d WHERE quiz_id = %d", QuizPlayerStatus.FINISHED.getStatus(), quiz.getId()));
		return CommonDaoService.executeQueryUpdate(list);
	}
	
	public static boolean nextQuizQuestion(QuizInfo quizInfo) {
		if (quizInfo == null || quizInfo.isFinish()) {
			return false;
		}
		byte currentQ = quizInfo.getCurrentQuestion();
		List<Integer> questionList = quizInfo.getQuizDataIds();
		if (questionList == null || questionList.isEmpty()) {
			GameLog.getInstance().error("[nextQuizQuestion] no question data found!");
			return false;
		}
		if (currentQ + 1 >= questionList.size()) {
			GameLog.getInstance().info("[nextQuizQuestion] no more question!");
			return false;
		}
		int nextQId = questionList.get(currentQ + 1);
		QuizData nextQuestionData = QuizDataService.getQuizDataById(nextQId);
		if (nextQuestionData == null) {
			GameLog.getInstance().error("[nextQuizQuestion] next question is null!");
			return false;
		}
		String channel = quizInfo.getName();
		if (channel != null && !channel.isEmpty()) {
			PatpatOutgoingMessage message = new PatpatOutgoingMessage();
			message.setChannel(channel);
			message.setMessage(nextQuestionData.getLabel());
//			if (!PatpatFunc.sendMessageToChannel(PatpatFunc.convertOutgoingMessage(message))) {
//				GameLog.getInstance().error("[nextQuizQuestion] send question error!");
//				return false;
//			}
			System.out.println(PatpatFunc.convertOutgoingMessage(message));
		}
		quizInfo.setCurrentQuestion((byte) (currentQ + 1));
		if (!CommonDaoService.update(quizInfo.getTblQuizInfo())) {
			GameLog.getInstance().error("[nextQuizQuestion] update quiz failed!");
			return false;
		}
		return true;
	}
}
