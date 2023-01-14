package dev.boom.dao.info;

import java.sql.Connection;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.dao.IDaoFactory;

public class DaoMenuInfo implements IDaoFactory {
	private static final int nReadKey = 1;

	@Override
	public List<DaoValue> select(Connection conn, List<DaoValue> self) {
		return CommonDaoFactory._Select(conn, self);
	}

	@Override
	public List<DaoValue> select(Connection conn, DaoValue self) {
		return CommonDaoFactory._Select(conn, self);
	}

	@Override
	public int insert(Connection conn, DaoValue self) {
		return CommonDaoFactory._Insert(conn, self);
	}

	@Override
	public int delete(Connection conn, DaoValue self) {
		return CommonDaoFactory._Delete(conn, self);
	}

	@Override
	public int update(Connection conn, DaoValue self) {
		return CommonDaoFactory._Update(conn, self);
	}

	@Override
	public int count(Connection conn, DaoValue self) {
		return CommonDaoFactory._Count(conn, self);
	}

	@Override
	public long max(Connection conn, DaoValue self) {
		return CommonDaoFactory._Max(conn, self);
	}

	@Override
	public long min(Connection conn, DaoValue self) {
		return CommonDaoFactory._Min(conn, self);
	}

	@Override
	public int getReadKey() {
		return nReadKey;
	}

}

