package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import dev.boom.common.CommonDefine;
import dev.boom.common.game.QuizJapaneseLevel;
import dev.boom.common.game.QuizPlayerStatus;
import dev.boom.common.game.QuizStatus;
import dev.boom.common.game.QuizSubject;
import dev.boom.connect.HibernateSessionFactory;
import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.socket.func.PatpatCommandType;
import dev.boom.socket.func.PatpatFunc;
import dev.boom.socket.func.PatpatOutgoingMessage;
import dev.boom.tbl.info.TblQuizInfo;

public class QuizService {

	public static QuizInfo getInsessionQuizByName(String name) {
		TblQuizInfo tblQuizInfo = new TblQuizInfo();
		tblQuizInfo.Set("name", name);
		tblQuizInfo.setSelectOption("AND status IN (" + QuizStatus.IN_SESSION.getStatus() + "," + QuizStatus.BREAK_TIME.getStatus() + ")");
		tblQuizInfo.setSelectOption("AND expired > NOW() ORDER BY id ASC");

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
		tblQuizInfo.setSelectOption("AND expired > NOW() ORDER BY id ASC");

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
		boolean ret = false;
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
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}
		showQuizDetail(quiz, false);
		
		return ret;
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
	
	public static boolean hasQuestion(QuizInfo quizInfo) {
		if (quizInfo == null || quizInfo.isFinish()) {
			return false;
		}
		byte currentQ = quizInfo.getCurrentQuestion();
		List<Integer> questionList = quizInfo.getQuizDataIds();
		if (questionList == null || questionList.isEmpty()) {
			return false;
		}
		if (currentQ + 1 > questionList.size()) {
			return false;
		}
		return true;
	}
	
	public static boolean showQuizAnswer(QuizInfo quizInfo) {
		if (quizInfo == null) {
			return false;
		}
		String data = QuizLogService.getQuizLogData(quizInfo);
		PatpatOutgoingMessage message = new PatpatOutgoingMessage();
		message.setChannel(quizInfo.getName());
		message.setText(data);
		if (!PatpatFunc.sendMessageToChannel(PatpatFunc.convertOutgoingMessage(message))) {
			GameLog.getInstance().error("[nextQuizQuestion] send Quiz result error!");
			return false;
		}
		return true;
	}
	
	public static boolean showQuizDetail(QuizInfo quizInfo, boolean start) {
		if (quizInfo == null || quizInfo.isFinish()) {
			return false;
		}
		List<QuizPlayerInfo> playerList = QuizPlayerService.getQuizPlayerListByQuizId(quizInfo.getId(), "AND status <> " + QuizPlayerStatus.FINISHED.getStatus() + " ORDER BY updated ASC");
		StringBuilder sb = new StringBuilder();
		String hostName = "";
		for (QuizPlayerInfo player : playerList) {
			if (player.getUserId() == quizInfo.getHost()) {
				hostName = player.getUsername();
			}
			sb.append("@").append(player.getUsername()).append("\n");
		}
		String pListName = sb.toString();
		sb.setLength(0);
		if (start) {
			sb.append("Prepare starting Quiz : `" + QuizSubject.valueOf(quizInfo.getSubject()).getName() + "`");
		} else {
			sb.append("Current Quiz info : `" + QuizSubject.valueOf(quizInfo.getSubject()).getName() + "`");
		}
		sb.append("\n");
		sb.append("Host of Quiz: " + hostName);
		if (quizInfo.getSubject() == QuizSubject.Japanese.getSubject()) {
			sb.append("\n");
			sb.append("Level of Quiz: " + QuizJapaneseLevel.valueOf(quizInfo.getLevel()).getName());
		}
		sb.append("\n");
		sb.append("Number of Q: " + quizInfo.getQuestionNum());
		sb.append("\n");
		sb.append("Time limit per Q: " + (quizInfo.getTimePerQuestion() / CommonDefine.MILLION_SECOND_SECOND) + "(s)");
		sb.append("\n");
		sb.append("Re-answer per Q: " + (quizInfo.getRetry() - 1) + " time(s)");
		sb.append("\n");
		sb.append("Show Q description: " + (quizInfo.isShowDescription() ? "YES" : "NO"));
		sb.append("\n");
		sb.append("List Players: \n");
		sb.append(pListName);
		if (start) {
			sb.append("Give me your answer by using this command : `/patpat [option]`");
			sb.append("\n");
			sb.append("- `option` is one of `A,B,C,D,E`");
			sb.append("\n");
			sb.append(":star2: ---Good luck--- :star2:");
		} else {
			sb.append("\n");
			sb.append("Join the quiz by using this command : `" + PatpatCommandType.QUIZ_JOIN.getFullCommand() + "`");
		}
		PatpatOutgoingMessage message = new PatpatOutgoingMessage();
		message.setChannel(quizInfo.getName());
		message.setText(sb.toString());
		if (!PatpatFunc.sendMessageToChannel(PatpatFunc.convertOutgoingMessage(message))) {
			GameLog.getInstance().error("[nextQuizQuestion] send question info error!");
			return false;
		}
		return true;
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
		if (currentQ + 1 > questionList.size()) {
			GameLog.getInstance().info("[nextQuizQuestion] no more question!");
			return false;
		}
		int nextQId = questionList.get(currentQ); // start from 0
		QuizData nextQuestionData = QuizDataService.getQuizDataById(QuizSubject.valueOf(quizInfo.getSubject()), nextQId);
		if (nextQuestionData == null) {
			GameLog.getInstance().error("[nextQuizQuestion] next question is null!");
			return false;
		}
		if (!quizInfo.initQuizOptionOrder(nextQuestionData)) {
			GameLog.getInstance().error("[nextQuizQuestion] init question option order failure!");
			return false;
		}
		List<Integer> optionIndex = quizInfo.getCurrentOptionIndexList();
		if (optionIndex == null || optionIndex.isEmpty()) {
			GameLog.getInstance().error("[nextQuizQuestion] question option order not found!");
			return false;
		}
		/////////////////////////////////////////////
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			GameLog.getInstance().info("Transaction Begin!");
			tx = session.beginTransaction();
			quizInfo.setCurrentQuestion((byte) (currentQ + 1));
			quizInfo.setStatus(QuizStatus.IN_SESSION.getStatus());
			if (!CommonDaoService.update(session, quizInfo.getTblQuizInfo())) {
				GameLog.getInstance().error("[nextQuizQuestion] Update quiz status in_session failed!");
				tx.rollback();
				return false;
			}
			String query = String.format("UPDATE quiz_player_info SET retry = %d WHERE quiz_id = %d AND status <> %d", quizInfo.getRetry(), quizInfo.getId(), QuizPlayerStatus.FINISHED.getStatus());
			CommonDaoService.executeQueryUpdate(session, query);
			tx.commit();
			GameLog.getInstance().info("Transaction Commit!");
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}
		/////////////////////////////////////////////
		quizInfo.getTblQuizInfo().Sync();
		String channel = quizInfo.getName();
		if (channel != null && !channel.isEmpty()) {
			PatpatOutgoingMessage message = new PatpatOutgoingMessage();
			message.setChannel(channel);
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Question %d (#Q_%d) : ", quizInfo.getCurrentQuestion(), quizInfo.getCurrentQuestion())).append(PatpatFunc.getMessage(nextQuestionData.getLabel()));
			List<QuizOptionData> optionList = QuizDataService.getQuizOptionDataByQId(QuizSubject.valueOf(quizInfo.getSubject()), nextQId);
			sb.append("\n");
			char cOpt = 'A';
			for (int index : optionIndex) {
				if (index > optionList.size() || index <= 0) {
					GameLog.getInstance().warn("[nextQuizQuestion] option index exceed option data length, index : " + index + " > data length: " + optionList.size());
					continue;
				}
				sb.append(String.valueOf(cOpt)).append(". ").append(PatpatFunc.getMessage(optionList.get(index - 1).getOption())).append("\n");
				cOpt = (char) (cOpt + 1);
			}
			message.setText(sb.toString());
			if (!PatpatFunc.sendMessageToChannel(PatpatFunc.convertOutgoingMessage(message))) {
				GameLog.getInstance().error("[nextQuizQuestion] send question error!");
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean showNotice(QuizInfo quizInfo) {
		if (quizInfo == null || quizInfo.isFinish()) {
			return false;
		}
		String channel = quizInfo.getName();
		if (channel != null && !channel.isEmpty()) {
			PatpatOutgoingMessage message = new PatpatOutgoingMessage();
			message.setChannel(channel);
			message.setText(String.format("### 🠖 5 seconds left for answering Question %d 🠔", quizInfo.getCurrentQuestion()));
			if (!PatpatFunc.sendMessageToChannel(PatpatFunc.convertOutgoingMessage(message))) {
				GameLog.getInstance().error("[nextQuizQuestion] send notice message failed!");
				return false;
			}
		}
		return true;
	}
}
