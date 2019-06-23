package dev.boom.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.data.TblQuizData;

public class QuizDataService {

	public static List<Integer> getRandomQuizDataIdList(int num) {
		if (num <= 0) {
			return null;
		}
		TblQuizData tbl = new TblQuizData();
		List<DaoValue> list = CommonDaoService.select(tbl);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() < num) {
			return null;
		}
		List<Integer> ret = new ArrayList<>();
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
			ret.add(((TblQuizData) list.get(min)).getId());
		}
		return ret;
	}

	public static QuizData getQuizDataById(int id) {
		TblQuizData tbl = new TblQuizData();
		tbl.Set("id", id);
		List<DaoValue> list = CommonDaoService.select(tbl);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return new QuizData((TblQuizData) list.get(0));
	}

}
