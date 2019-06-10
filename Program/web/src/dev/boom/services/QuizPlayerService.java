package dev.boom.services;

import java.util.List;

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

	public static QuizPlayerInfo getQuizPlayerByQuizId(long quizId, String options) {
		TblQuizPlayerInfo tblQuizPlayer = new TblQuizPlayerInfo();
		tblQuizPlayer.Set("quiz_id", quizId);
		if (options != null & !options.isEmpty()) {
			tblQuizPlayer.setSelectOption(options);
		}
		List<DaoValue> list = CommonDaoService.select(tblQuizPlayer);
		if (list == null || list.isEmpty()) {
			return null;
		}

		return new QuizPlayerInfo((TblQuizPlayerInfo) list.get(0));
	}
}
