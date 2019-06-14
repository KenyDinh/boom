package dev.boom.dao.info;

import java.util.List;

import org.hibernate.Session;

import dev.boom.dao.core.DaoValue;
import dev.boom.dao.core.IDaoFactory;
import dev.boom.services.CommonDaoService;

public class DaoQuizInfo implements IDaoFactory {

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
	
	@Override
	public List<DaoValue> select(Session session, DaoValue dao) {
		return CommonDaoService._Select(session, dao);
	}
	
	@Override
	public Object insert(Session session, DaoValue dao) {
		return CommonDaoService._Insert(session, dao);
	}
	
	@Override
	public boolean update(Session session, DaoValue dao) {
		return CommonDaoService._Update(session, dao);
	}
	
	@Override
	public boolean delete(Session session, DaoValue dao) {
		return CommonDaoService._Delete(session, dao);
	}
	
	@Override
	public long count(Session session, DaoValue dao) {
		return CommonDaoService._Count(session, dao);
	}
	
	@Override
	public long max(Session session, DaoValue dao) {
		return CommonDaoService._Max(session, dao);
	}
	
	@Override
	public long min(Session session, DaoValue dao) {
		return CommonDaoService._Min(session, dao);
	}
	
}
