package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.data.TblQuizData;

public class QuizDataService {

	public static List<QuizData> getRandomQuizDataList(int num) {
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
		List<QuizData> ret = new ArrayList<>();
		int len = list.size();
		int size = Math.min(num, len - num);
		int r_index, min, max;
		DaoValue temp;
		DaoValue cur;
		for (int i = 0; i < size; i++) {
			r_index = CommonMethod.randomNumber(i, len - 1);
			temp = list.get(i);
			cur = list.get(r_index);
			list.set(i, cur);
			list.set(r_index, temp);
		}
		min = (len < 2 * num ? len - num : 0);
		max = min + num;
		for (; min < max; min++) {
			ret.add(new QuizData((TblQuizData) list.get(min)));
		}
		return ret;
	}

}
