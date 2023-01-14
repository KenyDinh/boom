package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblCarouselInfo;

public class CarouselService {

	private CarouselService() {
	}

	public static List<Carousel> getCarouselListAll(String option) {
		TblCarouselInfo tblInfo = new TblCarouselInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<Carousel> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Carousel((TblCarouselInfo) dao));
		}

		return ret;
	}

	public static List<Carousel> getCarouselListAll() {
		return getCarouselListAll(null);
	}
	
	public static List<Carousel> getCarouselList(int limit) {
		TblCarouselInfo info = new TblCarouselInfo();
		info.SetSelectOption("WHERE id > 0 and available <> 0");
		if (limit > 0) {
			info.SetSelectOption("ORDER BY RAND()");
			info.SetLimit(limit);
		}
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Carousel> ret = new ArrayList<Carousel>();
		for (DaoValue dao : list) {
			ret.add(new Carousel((TblCarouselInfo)dao));
		}
		
		return ret;
	}
	
}

