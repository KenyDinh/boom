package dev.boom.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.common.game.QuizSubject;
import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.data.TblQuizJapaneseData;
import dev.boom.tbl.data.TblQuizOptionJapaneseData;
import dev.boom.tbl.data.TblQuizOptionProgrammingData;
import dev.boom.tbl.data.TblQuizOptionToeicData;
import dev.boom.tbl.data.TblQuizProgrammingData;
import dev.boom.tbl.data.TblQuizToeicData;

public class QuizDataService {

	public static List<QuizData> getRandomQuizDataList(QuizSubject subject, byte type, byte level, int num) {
		if (num <= 0) {
			return null;
		}
		DaoValue selectDao = getTblQuizDataBySubject(subject);
		if (selectDao == null) {
			GameLog.getInstance().error("[QuizDataService] No tbl data found for quiz subject : " + subject.name());
			return null;
		}
		if (type > 0) {
			selectDao.Set("type", type);
		}
		if (level > 0) {
			selectDao.Set("level", level);
		}
		GameLog.getInstance().info(String.format("[QuizDataService] (getRandomQuizDataList) generate quiz data => subject : %s,  type : %d, level : %d, num : %d", subject.getName(), type, level, num));
		List<DaoValue> list = CommonDaoService.select(selectDao);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() < num) {
			return null;
		}
		List<QuizData> ret = new ArrayList<>();
		int len = list.size();
		int size = Math.min(num, len - num);
		int r_index, min, max;
		for (int i = 0; i < size; i++) {
			r_index = CommonMethod.randomNumber(i, len - 1);
			Collections.swap(list, i, r_index);
		}
		min = (len < 2 * num ? len - num : 0);
		max = min + num;
		for (; min < max; min++) {
			ret.add(new QuizData(list.get(min)));
		}
		return ret;
	}

	public static QuizData getQuizDataById(QuizSubject subject, int id) {
		DaoValue selectDao = getTblQuizDataBySubject(subject);
		if (selectDao == null) {
			return null;
		}
		selectDao.Set("id", id);
		List<DaoValue> list = CommonDaoService.select(selectDao);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return new QuizData(list.get(0));
	}

	public static List<QuizOptionData> getQuizOptionDataByQId(QuizSubject subject, int quizDataId) {
		DaoValue selectDao = getTblQuizOptionDataBySubject(subject);
		selectDao.Set("quiz_data_id", quizDataId);
		List<DaoValue> list = CommonDaoService.select(selectDao);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<QuizOptionData> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new QuizOptionData(dao));
		}
		Collections.sort(ret, new Comparator<QuizOptionData>() {

			@Override
			public int compare(QuizOptionData o1, QuizOptionData o2) {
				return o1.getId() - o2.getId();
			}
		});
		return ret;
	}
	
	private static DaoValue getTblQuizDataBySubject(QuizSubject subject) {
		if (subject == null) {
			return null;
		}
		switch (subject) {
		case Toeic:
			return new TblQuizToeicData();
		case Programming:
			return new TblQuizProgrammingData();
		case Japanese:
			return new TblQuizJapaneseData();
		default:
			break;
		}
		return null;
	}
	
	private static DaoValue getTblQuizOptionDataBySubject(QuizSubject subject) {
		if (subject == null) {
			return null;
		}
		switch (subject) {
		case Toeic:
			return new TblQuizOptionToeicData();
		case Programming:
			return new TblQuizOptionProgrammingData();
		case Japanese:
			return new TblQuizOptionJapaneseData();
		default:
			break;
		}
		return null;
	}
}
