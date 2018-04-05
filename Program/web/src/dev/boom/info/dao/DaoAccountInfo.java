package dev.boom.info.dao;

import java.util.List;

import dev.boom.core.dao.DaoValue;
import dev.boom.core.dao.IDaoFactory;
import dev.boom.services.CommonDaoService;

public class DaoAccountInfo implements IDaoFactory {

	private final int readKey = 1;
	
	@Override
	public List<DaoValue> select(DaoValue dao) {
		return CommonDaoService._Select(dao);
	}

	@Override
	public boolean insert(DaoValue dao) {
		return CommonDaoService._Insert(dao);
	}

	@Override
	public boolean update(DaoValue dao) {
		return CommonDaoService._Update(dao);
	}

	@Override
	public boolean delete(DaoValue dao) {
		return CommonDaoService._Delete(dao);
	}

	@Override
	public int getReadKey() {
		return readKey;
	}

}
