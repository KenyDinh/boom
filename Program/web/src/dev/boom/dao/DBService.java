package dev.boom.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import dev.boom.common.CommonDefine;
import dev.boom.core.GameLog;

public final class DBService {
	private static final class SingletonHolder {
		static final DBService singleton = new DBService();
	}

	private int nConnectionCount = 0;
	private DataSource srcMain = null;
	private DataSource srcRead[] = null;
	private int srcReadErrCount[] = null;

	private DBService() {
		try {
			srcMain = getDataSourceMain();
		} catch (ClassNotFoundException e) {
			GameLog.getInstance().error(String.format("[DB] Init : Main DB Source Reserve Failed."));
			return;
		}

//		if (ShopifyProperties.AGENT_MODE ) {
//			GameLog.getInstance().info("Agent Mode");
//			return;
//		}
//		else if (srcMain == null) {
//			GameLog.getInstance().error(String.format("[DB] Init : Main DB Source Reserve Failed."));
//		}
//		else {
//			GameLog.getInstance().info(String.format("[DB] Init : Main DB Source Reserved."));
//		}

		srcRead = new DataSource[CommonDefine.MAX_SLAVE_DB];
		srcReadErrCount = new int[CommonDefine.MAX_SLAVE_DB];
		for (int i = 0 ; i < CommonDefine.MAX_SLAVE_DB ; i++) {
			srcRead[i] = getDataSourceRead(i);
			srcReadErrCount[i] = 0;
			if (srcRead[i] == null) {
				GameLog.getInstance().error(String.format("[DB] Init : Read Slave DB(%d) Source Reserve Failed.", (i + 1)));
			}
			else {
				GameLog.getInstance().info(String.format("[DB] Init : Read Slave DB(%d) Source Reserved.", (i + 1)));
			}
		}
	}

	public static DBService getInstance() {
		return SingletonHolder.singleton;
	}

	public Connection getConnectionMain() {
		Connection conn = null;

		try {
//			if (ShopifyProperties.AGENT_MODE) {
//				try {
//					Class.forName("com.mysql.jdbc.Driver");
//				} catch (ClassNotFoundException e2) {
//					e2.printStackTrace();
//					return null;
//				}
//
//				conn = DriverManager.getConnection(ShopifyProperties.AGENT_MYSQL_DATA_SOURCE_MASTER, ShopifyProperties.AGENT_DB_USER, ShopifyProperties.AGENT_DB_PASS);
//			}
//			else {
				conn = srcMain.getConnection();
//			}

			if (conn != null) {
				GameLog.getInstance().debug(String.format("[DB] Connection(%2d) : Main Reserved.", ++nConnectionCount));
			}
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 特別処理。MainでSelectする時のみ使うようにする。
	 * getConnectionRead(-1)時に呼ばれるようにする
	 * @return
	 */
	public Connection getConnectionMainForRead() {
		Connection conn = null;

		try {
			conn = srcMain.getConnection();
			if (conn != null) {
				GameLog.getInstance().debug(String.format("[DB] Connection(%2d) : Main(for Read) Reserved.", ++nConnectionCount));
			}
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Connection getConnectionRead(int nKey) {
		Connection conn = null;

		try {
			int nReadIndex = (nKey + (CommonDefine.MAX_SLAVE_DB - 1)) % CommonDefine.MAX_SLAVE_DB;

//			if (ShopifyProperties.AGENT_MODE) {
//				try {
//					Class.forName("com.mysql.jdbc.Driver");
//				} catch (ClassNotFoundException e2) {
//					e2.printStackTrace();
//					return null;
//				}
//				conn = DriverManager.getConnection(ShopifyProperties.AGENT_MYSQL_DATA_SOURCE_SLAVE, ShopifyProperties.AGENT_DB_USER, ShopifyProperties.AGENT_DB_PASS);
//			}
//			else {
				if (nKey == -1) { // MainDB参照処理
					return getConnectionMainForRead();
				}

				if (srcRead[nReadIndex] != null) {
					conn = srcRead[nReadIndex].getConnection();
					if (conn == null) {
						GameLog.getInstance().error(String.format("[DB] Connection : Read Slave DB(%d) Get Failed. Error Count(%d)", nReadIndex, srcReadErrCount[nReadIndex]));
						if (++srcReadErrCount[nReadIndex] > CommonDefine.MAX_SLAVE_DB_ERROR_COUNT) {
							srcRead[nReadIndex] = null;
						}
					}
				}
				if (conn == null) { // some slave db down. search other slave from low traffic
					for (nReadIndex = 0; nReadIndex < CommonDefine.MAX_SLAVE_DB; nReadIndex++) {
						if (srcRead[nReadIndex] != null) {
							conn = srcRead[nReadIndex].getConnection();
							if (conn != null) {
								break;
							}
						}
					}
				}
//			}

			if (conn != null) {
				GameLog.getInstance().debug(String.format("[DB] Connection(%2d) : Read(%d) Reserved. Key=%d", ++nConnectionCount, nReadIndex, nKey));
			}
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void releaseConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();

				GameLog.getInstance().debug(String.format("[DB] Connection(%2d) : Released.", --nConnectionCount));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean begin(Connection conn) {
		if (conn == null) {
			return false;
		}

		try {
			Statement st = conn.createStatement();
			st.executeUpdate("START TRANSACTION");
			st.close();

			GameLog.getInstance().debug("[DB] Transaction : Begin.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean commit(Connection conn) {
		if (conn == null) {
			return false;
		}

		try {
			Statement st = conn.createStatement();
			st.executeUpdate("COMMIT");
			st.close();


			GameLog.getInstance().debug("[DB] Transaction : Commit.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean rollback(Connection conn) {
		if (conn == null) {
			return false;
		}

		try {
			Statement st = conn.createStatement();
			st.executeUpdate("ROLLBACK");
			st.close();

			GameLog.getInstance().debug("[DB] Transaction : Rollback.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	private DataSource getDataSourceMain() throws ClassNotFoundException {
//		if ( ShopifyProperties.AGENT_MODE ) {
//			return null;
//		}

		InitialContext initCon = null;
		DataSource ds = null;

		try {
			initCon = new InitialContext();
			ds = (DataSource) initCon.lookup("java:comp/env/jdbc/boom");
		} catch (NamingException e) {
			throw new ClassNotFoundException("java application");
		}
		return ds;
	}

	private DataSource getDataSourceRead(int nIndex) {
		InitialContext initCon = null;
		DataSource ds = null;
		try {
			String resName = "java:comp/env/jdbc/boom_slave" + (nIndex + 1);
			initCon = new InitialContext();
			ds = (DataSource) initCon.lookup(resName);
		} catch (NamingException e) {
			e.printStackTrace();
			if (initCon != null) {
				try {
					initCon.close();
				} catch (NamingException ex) {
					ex.printStackTrace();
				}
			}
		}
		return ds;
	}
}

