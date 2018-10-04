package dev.boom.services;

import java.util.Map;

import dev.boom.dao.core.DaoValue;
import dev.boom.dao.fix.FixData;
import dev.boom.dao.fix.FixDataBase;
import dev.boom.tbl.data.TblCannonBlockData;

public class CannonBlockDataService {
	private CannonBlockDataService() {
		// TODO Auto-generated constructor stub
	}

	public static CannonBlockData getCannonBlockDataById(short id) {
		if (id < 0) {
			return null;
		}

		FixData fixData = (FixData) FixDataBase.getInstance("CannonBlockData");
		Map<Integer, DaoValue> cannonBlockData = fixData.getData();
		TblCannonBlockData tblCannonBlockData = (TblCannonBlockData) cannonBlockData.get((int) id);
		if (tblCannonBlockData == null) {
			return null;
		}
		return new CannonBlockData(tblCannonBlockData);
	}
}
