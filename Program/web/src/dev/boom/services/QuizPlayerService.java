package dev.boom.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.boom.common.game.QuizPlayerStatus;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblQuizPlayerInfo;

public class QuizPlayerService {

	private QuizPlayerService() {
	}

	public static QuizPlayer getQuizPlayerById(long userId) {
		TblQuizPlayerInfo tblQuizPlayer = new TblQuizPlayerInfo();
		tblQuizPlayer.Set("user_id", userId);

		List<DaoValue> list = CommonDaoFactory.Select(tblQuizPlayer);
		if (list == null || list.isEmpty()) {
			return null;
		}

		return new QuizPlayer((TblQuizPlayerInfo) list.get(0));
	}
	
	public static List<QuizPlayer> getQuizPlayerListByQuizId(long quizId, String options) {
		TblQuizPlayerInfo tblQuizPlayer = new TblQuizPlayerInfo();
		tblQuizPlayer.Set("quiz_id", quizId);
		if (options != null & !options.isEmpty()) {
			tblQuizPlayer.SetSelectOption(options);
		}
		List<DaoValue> list = CommonDaoFactory.Select(tblQuizPlayer);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<QuizPlayer> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new QuizPlayer((TblQuizPlayerInfo) dao));
		}

		return ret;
	}

	public static QuizPlayer getQuizPlayerByQuizId(long quizId, String options) {
		TblQuizPlayerInfo tblQuizPlayer = new TblQuizPlayerInfo();
		tblQuizPlayer.Set("quiz_id", quizId);
		if (options != null & !options.isEmpty()) {
			tblQuizPlayer.SetSelectOption(options);
		}
		tblQuizPlayer.SetLimit(1);
		List<DaoValue> list = CommonDaoFactory.Select(tblQuizPlayer);
		if (list == null || list.isEmpty()) {
			return null;
		}

		return new QuizPlayer((TblQuizPlayerInfo) list.get(0));
	}
	
	public static boolean isAllPlayerFinishTurn(long quizId) {
		TblQuizPlayerInfo tblQuizPlayer = new TblQuizPlayerInfo();
		tblQuizPlayer.Set("quiz_id", quizId);
		tblQuizPlayer.SetSelectOption("AND status <> " + QuizPlayerStatus.FINISHED.getStatus() + " AND retry > 0");
		return (CommonDaoFactory.Count(tblQuizPlayer) == 0);
	}
	
}

