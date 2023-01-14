/**
 *
 */
package dev.boom.dao;

import java.sql.Connection;
import java.util.List;

public class DaoFactory {
	private IDaoFactory selfIDaoFactory;

	public DaoFactory(IDaoFactory ISelf) {
		selfIDaoFactory = ISelf;
	}

	public List<DaoValue> select(Connection conn, List<DaoValue> self) {
		return selfIDaoFactory.select(conn, self);
	}

	public List<DaoValue> select(Connection conn, DaoValue self) {
		return selfIDaoFactory.select(conn, self);
	}

	public int insert(Connection conn, DaoValue self) {
		return selfIDaoFactory.insert(conn, self);
	}

	public int delete(Connection conn, DaoValue self) {
		return selfIDaoFactory.delete(conn, self);
	}

	public int update(Connection conn, DaoValue self) {
		return selfIDaoFactory.update(conn, self);
	}

	public int count(Connection conn, DaoValue self) {
		return selfIDaoFactory.count(conn, self);
	}

	/**
	 * @param conn
	 * @param self
	 * @return
	 */
	public long max(Connection conn, DaoValue self) {
		return selfIDaoFactory.max(conn, self);
	}

	/**
	 * @param conn
	 * @param self
	 * @return
	 */
	public long min(Connection conn, DaoValue self) {
		return selfIDaoFactory.min(conn, self);
	}

	public final Connection getConnectionRead() {
		int nKey = selfIDaoFactory.getReadKey();
		if (nKey == 0) { // fix data.
			return null;
		}

		return DBService.getInstance().getConnectionRead(nKey);
	}

	public static final Connection getConnectionMain() {
		return DBService.getInstance().getConnectionMain();
	}

	public static final void releaseConnection(Connection conn) {
		DBService.getInstance().releaseConnection(conn);
	}

	public static final boolean begin(Connection conn) {
		return DBService.getInstance().begin(conn);
	}

	public static final boolean commit(Connection conn) {
		return DBService.getInstance().commit(conn);
	}

	public static final boolean rollback(Connection conn) {
		return DBService.getInstance().rollback(conn);
	}
}
