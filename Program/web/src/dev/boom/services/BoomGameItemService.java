package dev.boom.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.data.TblBoomGameItemData;

public class BoomGameItemService {

	public static BoomGameItem getItemById(int id) {
		TblBoomGameItemData data = new TblBoomGameItemData();
		data.Set("id", id);
		List<DaoValue> list = CommonDaoFactory.Select(data);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return new BoomGameItem((TblBoomGameItemData) list.get(0));
	}
	
	public static List<BoomGameItem> getItemsList() {
		TblBoomGameItemData data = new TblBoomGameItemData();
		List<DaoValue> list = CommonDaoFactory.Select(data);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<BoomGameItem> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new BoomGameItem((TblBoomGameItemData) dao));
		}
		return ret;
	}
	
	public static Map<Integer, BoomGameItem> getItemsMap() {
		TblBoomGameItemData data = new TblBoomGameItemData();
		List<DaoValue> list = CommonDaoFactory.Select(data);
		if (list == null || list.isEmpty()) {
			return null;
		}
		Map<Integer, BoomGameItem> ret = new HashMap<>();
		for (DaoValue dao : list) {
			BoomGameItem item = new BoomGameItem((TblBoomGameItemData) dao);
			ret.put(item.getId(), item);
		}
		return ret;
	}
}
