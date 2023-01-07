package dev.boom.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.boom.common.game.QuizPlayerStatus;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblQuizPlayerInfo;

public class QuizPlayerService {

	public static QuizPlayerInfo getQuizPlayerById(long userId) {
		TblQuizPlayerInfo tblQuizPlayer = new TblQuizPlayerInfo();
		tblQuizPlayer.Set("user_id", userId);

		List<DaoValue> list = CommonDaoService.select(tblQuizPlayer);
		if (list == null || list.isEmpty()) {
			return null;
		}

		return new QuizPlayerInfo((TblQuizPlayerInfo) list.get(0));
	}
	
	public static List<QuizPlayerInfo> getQuizPlayerListByQuizId(long quizId, String options) {
		TblQuizPlayerInfo tblQuizPlayer = new TblQuizPlayerInfo();
		tblQuizPlayer.Set("quiz_id", quizId);
		if (options != null & !options.isEmpty()) {
			tblQuizPlayer.setSelectOption(options);
		}
		List<DaoValue> list = CommonDaoService.select(tblQuizPlayer);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<QuizPlayerInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new QuizPlayerInfo((TblQuizPlayerInfo) dao));
		}

		return ret;
	}

	public static QuizPlayerInfo getQuizPlayerByQuizId(long quizId, String options) {
		TblQuizPlayerInfo tblQuizPlayer = new TblQuizPlayerInfo();
		tblQuizPlayer.Set("quiz_id", quizId);
		if (options != null & !options.isEmpty()) {
			tblQuizPlayer.setSelectOption(options);
		}
		tblQuizPlayer.setLimit(1);
		List<DaoValue> list = CommonDaoService.select(tblQuizPlayer);
		if (list == null || list.isEmpty()) {
			return null;
		}

		return new QuizPlayerInfo((TblQuizPlayerInfo) list.get(0));
	}
	
	public static boolean isAllPlayerFinishTurn(long quizId) {
		TblQuizPlayerInfo tblQuizPlayer = new TblQuizPlayerInfo();
		tblQuizPlayer.Set("quiz_id", quizId);
		tblQuizPlayer.setSelectOption("AND status <> " + QuizPlayerStatus.FINISHED.getStatus() + " AND retry > 0");
		return (CommonDaoService.count(tblQuizPlayer) == 0);
	}
}
