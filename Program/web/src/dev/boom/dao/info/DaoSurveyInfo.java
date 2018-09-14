package dev.boom.dao.info;

import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.dao.core.IDaoFactory;
import dev.boom.services.CommonDaoService;

public class DaoSurveyInfo implements IDaoFactory {
	
	private final int readKey = 1;

	@Override
	public List<DaoValue> select(DaoValue dao) {
		return CommonDaoService._Select(dao);
	}

	@Override
	public Object insert(DaoValue dao) {
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

	@Override
	public long count(DaoValue dao) {
		return CommonDaoService._Count(dao);
	}

	@Override
	public long max(DaoValue dao) {
		return CommonDaoService._Max(dao);
	}

	@Override
	public long min(DaoValue dao) {
		return CommonDaoService._Min(dao);
	}
	
}
