/**
 *
 */
package dev.boom.dao;

import java.sql.Connection;
import java.util.List;

public interface IDaoFactory {
	public List<DaoValue> select(Connection conn, List<DaoValue> self);
	public List<DaoValue> select(Connection conn, DaoValue self);
	public int insert(Connection conn, DaoValue self);
	public int delete(Connection conn, DaoValue self);
	public int update(Connection conn, DaoValue self);
	public int count(Connection conn, DaoValue self);

	/**
	 * @param conn
	 * @param self
	 * @return
	 */
	public long max(Connection conn, DaoValue self);

	/**
	 * @param conn
	 * @param self
	 * @return
	 */
	public long min(Connection conn, DaoValue self);

	public int getReadKey();
}
