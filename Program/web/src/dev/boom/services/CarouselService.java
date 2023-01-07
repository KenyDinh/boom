package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblCarouselInfo;

public class CarouselService {

	private CarouselService() {
	}
	
	public static List<CarouselInfo> getCarouselList(int limit) {
		TblCarouselInfo info = new TblCarouselInfo();
		info.setSelectOption("WHERE id > 0 and available <> 0");
		if (limit > 0) {
			info.setSelectOption("ORDER BY RAND()");
			info.setLimit(limit);
		}
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<CarouselInfo> ret = new ArrayList<CarouselInfo>();
		for (DaoValue dao : list) {
			ret.add(new CarouselInfo((TblCarouselInfo)dao));
		}
		
		return ret;
	}
}
