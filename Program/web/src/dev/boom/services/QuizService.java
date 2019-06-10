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
import dev.boom.tbl.info.TblQuizInfo;

public class QuizService {

	public static QuizInfo getInsessionQuizByName(String name) {
		TblQuizInfo tblQuizInfo = new TblQuizInfo();
		tblQuizInfo.Set("name", name);
		tblQuizInfo.Set("status", QuizStatus.IN_SESSION.getStatus());
		tblQuizInfo.setSelectOption("AND expired < NOW()");

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
		tblQuizInfo.setSelectOption("AND expired < NOW()");

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
		tblQuizInfo.setSelectOption("AND expired < NOW()");

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
		quizPlayer.initNewQuiz(quiz);
		List<DaoValue> updateList = new ArrayList<>();
		updateList.add(quiz.getTblQuizInfo());
		updateList.add(quizPlayer.getTblQuizPlayerInfo());

		if (!CommonDaoService.update(updateList)) {
			return false;
		}
		return true;
	}
	
	public static boolean startQuiz(QuizInfo quiz) {
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			GameLog.getInstance().info("Transaction Begin!");
			tx = session.beginTransaction();
			int ret = session.createQuery(String.format("UPDATE QuizInfo SET status = %d WHERE id = %d AND status = %d", QuizStatus.IN_SESSION.getStatus(), quiz.getId(), quiz.getStatus())).executeUpdate();
			if (ret <= 0) {
				tx.rollback();
				return false;
			}
			ret = session.createQuery(String.format("UPDATE QuizPlayerInfo SET status = %d WHERE quiz_id = %d AND status = %d", QuizPlayerStatus.PLAYING.getStatus(), quiz.getId(), QuizPlayerStatus.INITIALIZED.getStatus())).executeUpdate();
			if (ret <= 0) {
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
		return false;
	
	}
}
