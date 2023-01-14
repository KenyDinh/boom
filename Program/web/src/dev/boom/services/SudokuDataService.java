package dev.boom.services;

import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.data.TblSudokuData;

public class SudokuDataService {

	public static SudokuData getRandomSudokuData(byte level) {
		TblSudokuData data = new TblSudokuData();
		if (level > 0) {
			data.Set("level", level);
		}
		List<DaoValue> list = CommonDaoFactory.Select(data);
		if (list == null || list.isEmpty()) {
			return null;
		}
		int size = list.size();
		if (size == 1) {
			return new SudokuData((TblSudokuData) list.get(0));
		}
		int r = CommonMethod.randomNumber(0, size - 1);
		return new SudokuData((TblSudokuData) list.get(r));
	}
}
