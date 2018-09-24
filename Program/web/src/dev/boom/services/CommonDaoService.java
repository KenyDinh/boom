package dev.boom.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import dev.boom.common.CommonMethod;
import dev.boom.connect.HibernateSessionFactory;
import dev.boom.connect.MySQLDialect;
import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.dao.core.DaoValueData;
import dev.boom.dao.core.IDaoFactory;
import dev.boom.dao.fix.FixData;
import dev.boom.dao.fix.FixDataBase;

public class CommonDaoService {

	private static Map<String, IDaoFactory> daoData = new LinkedHashMap<>();
	private static Object _lock = new Object();

	private CommonDaoService() {
	}

	public static Object insert(DaoValue dao) {
		try {
			IDaoFactory factory = getDaoFactory(dao);
			if (factory == null) {
				GameLog.getInstance().info("DaoFactory is null");
				return null;
			}
			return factory.insert(dao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean update(DaoValue dao) {
		try {
			IDaoFactory factory = getDaoFactory(dao);
			if (factory == null) {
				GameLog.getInstance().info("DaoFactory is null");
				return false;
			}
			return factory.update(dao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean update(List<DaoValue> list) {
		if (list != null && list.size() == 1) {
			if (list.get(0).isInsert()) {
				return (insert(list.get(0)) != null);
			} else if (list.get(0).isDelete()) {
				return delete(list.get(0));
			}
			return update(list.get(0));
		}
		return _Transactions(list);
	}

	public static boolean delete(DaoValue dao) {
		try {
			IDaoFactory factory = getDaoFactory(dao);
			if (factory == null) {
				GameLog.getInstance().info("DaoFactory is null");
				return false;
			}
			return factory.delete(dao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static List<DaoValue> select(DaoValue dao) {
		try {
			IDaoFactory factory = getDaoFactory(dao);
			if (factory == null) {
				GameLog.getInstance().info("DaoFactory is null");
				return null;
			}
			return factory.select(dao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static long count(DaoValue dao) {
		try {
			IDaoFactory factory = getDaoFactory(dao);
			if (factory == null) {
				GameLog.getInstance().info("DaoFactory is null");
				return 0;
			}
			return factory.count(dao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long max(DaoValue dao) {
		try {
			IDaoFactory factory = getDaoFactory(dao);
			if (factory == null) {
				GameLog.getInstance().info("DaoFactory is null");
				return 0;
			}
			return factory.max(dao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long min(DaoValue dao) {
		try {
			IDaoFactory factory = getDaoFactory(dao);
			if (factory == null) {
				GameLog.getInstance().info("DaoFactory is null");
				return 0;
			}
			return factory.min(dao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// ---------------------------------------------------------------------------- //

	/*
	 * 
	 */
	public static Object _Insert(DaoValue dao) {
		Object ret = null;
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			GameLog.getInstance().info(dao.getInsertClause());
			ret = session.save(dao);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}
		return ret;
	}

	/**
	 * Auto sync if success otherwise do unsync
	 * 
	 * @param dao
	 * @return
	 */
	public static boolean _Update(DaoValue dao) {
		boolean result = false;
		String updateClause = dao.getUpdateClause();
		if (updateClause.isEmpty()) {
			return true;
		}
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			GameLog.getInstance().info(String.format("UPDATE %s %s", dao.getTableName(), updateClause));
			int ret = session.createQuery(String.format("UPDATE %s %s", dao.getClass().getSimpleName(), updateClause)).executeUpdate();
			if (ret <= 0) {
				tx.rollback();
				GameLog.getInstance().info("Nothing Changed!");
			} else {
				tx.commit();
				dao.Sync();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static List<DaoValue> _Select(DaoValue dao) {
		List<DaoValue> ret = null;
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String sql = "FROM " + dao.getClass().getSimpleName();
			String whereClause = dao.getUpdateWhereClause();
			if (!whereClause.isEmpty()) {
				sql += whereClause;
			}
			Query query = session.createQuery(correctBitwiseOperation(sql));
			String option = "";
			if (dao.getLimit() > 0) {
				option += " LIMIT " + dao.getLimit();
				query.setMaxResults(dao.getLimit());
				option += " OFFSET " + dao.getOffset();
				query.setFirstResult(dao.getOffset());
			}
			GameLog.getInstance().info(String.format("SELECT * FROM %s", (dao.getTableName() + whereClause + option)));
			List list = query.list();
			tx.commit();
			if (list != null && !list.isEmpty()) {
				ret = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					DaoValue value = (DaoValue) list.get(i);
					value.Sync();
					ret.add(value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}
		return ret;
	}

	public static boolean _Delete(DaoValue dao) {
		boolean result = false;
		String deleteClause = dao.getUpdateWhereClause();
		if (deleteClause.isEmpty()) {
			return false;
		}
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			GameLog.getInstance().info(String.format("DELETE FROM %s", dao.getTableName() + deleteClause));
			int ret = session.createQuery(String.format("DELETE FROM %s", dao.getClass().getSimpleName() + deleteClause)).executeUpdate();
			if (ret <= 0) {
				tx.rollback();
				GameLog.getInstance().info("Nothing Changed!");
			} else {
				tx.commit();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static long _Count(DaoValue dao) {
		long count = 0;
		String sf = "";
		if (dao.getSelectedField() != null) {
			for (String field : dao.getSelectedField()) {
				if (!sf.isEmpty()) {
					sf += ",";
				}
				sf += field;
				break;
			}
		}
		if (sf.isEmpty()) {
			sf = dao.getPrimaryKey();
			if (sf.isEmpty()) {
				sf = "*";
			}
		}
		String whereClause = dao.getUpdateWhereClause();
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			GameLog.getInstance().info(String.format("SELECT COUNT(%s) FROM %s", sf, (dao.getTableName() + whereClause)));
			String sql = "SELECT COUNT(" + sf + ") FROM " + dao.getClass().getSimpleName() + dao.getUpdateWhereClause();
			List<Long> list = session.createQuery(sql).list();
			if (list != null && list.size() > 0) {
				count = Long.valueOf(String.valueOf(list.get(0)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}

		return count;
	}

	@SuppressWarnings("unchecked")
	public static long _Max(DaoValue dao) {
		long count = 0;
		String sf = "";
		if (dao.getSelectedField() != null) {
			for (String field : dao.getSelectedField()) {
				if (!sf.isEmpty()) {
					sf += ",";
				}
				sf += field;
				break;
			}
		}
		if (sf.isEmpty()) {
			return count;
		}
		String whereClause = dao.getUpdateWhereClause();
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			GameLog.getInstance().info(String.format("SELECT MAX(%s) FROM %s", sf, (dao.getTableName() + whereClause)));
			String sql = "SELECT MAX(" + sf + ") FROM " + dao.getClass().getSimpleName() + dao.getUpdateWhereClause();
			List<Long> list = session.createQuery(sql).list();
			if (list != null && list.size() > 0) {
				count = Long.valueOf(String.valueOf(list.get(0)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}

		return count;
	}

	@SuppressWarnings("unchecked")
	public static long _Min(DaoValue dao) {
		long count = 0;
		String sf = "";
		if (dao.getSelectedField() != null) {
			for (String field : dao.getSelectedField()) {
				if (!sf.isEmpty()) {
					sf += ",";
				}
				sf += field;
				break;
			}
		}
		if (sf.isEmpty()) {
			return count;
		}
		String whereClause = dao.getUpdateWhereClause();
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			GameLog.getInstance().info(String.format("SELECT MIN(%s) FROM %s", sf, (dao.getTableName() + whereClause)));
			String sql = "SELECT MIN(" + sf + ") FROM " + dao.getClass().getSimpleName() + dao.getUpdateWhereClause();
			List<Long> list = session.createQuery(sql).list();
			if (list != null && list.size() > 0) {
				count = Long.valueOf(String.valueOf(list.get(0)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}

		return count;
	}

	// ---------------------------------------------------------------------------- //
	
	public static List<DaoValue> _SelectFix(DaoValue dao) {
		FixData fixData = (FixData) FixDataBase.getInstance(dao.getClass().getSimpleName().substring(3));
		Map<Integer, DaoValue> mapData = fixData.getData();
		try {
			List<Field> fieldList = dao.getFieldList();
			List<DaoValue> ret = new ArrayList<>();
			for (Integer key : mapData.keySet()) {
				DaoValue daoValue = mapData.get(key);
				boolean valid = true;
				for (Field field : fieldList) {
					field.setAccessible(true);
					Object o1 = field.get(dao.getOriginal());
					Object o2 = field.get(dao);
					
					if (o1.equals(o2)) {
						continue;
					}
					if (!field.get(daoValue).equals(o2)) {
						valid = false;
						break;
					}
				}
				if (valid) {
					ret.add(daoValue);
				}
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	// ---------------------------------------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public static List<Object> executeQuery(String sqlQuery) {
		List<Object> list = null;
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			GameLog.getInstance().info(sqlQuery);
			list = session.createQuery(toHQLQuery(sqlQuery)).list();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}

		return list;
	}
	
	public static List<Object> executeNativeSQLQuery(String sqlQuery) {
		return executeNativeSQLQuery(sqlQuery, null);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Object> executeNativeSQLQuery(String sqlQuery, List<String> commands) {
		List<Object> list = null;
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			if (commands != null && !commands.isEmpty()) {
				for (String command : commands) {
					GameLog.getInstance().info(command);
					session.createSQLQuery(command).executeUpdate();
				}
			}
			GameLog.getInstance().info(sqlQuery);
			list = session.createSQLQuery(sqlQuery).list();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}

		return list;
	}

	@SuppressWarnings({ "unchecked" })
	public static List<Object> selectWithFields(DaoValue dao) {
		List<Object> list = null;
		String sf = "";
		if (dao.getSelectedField() != null) {
			for (String field : dao.getSelectedField()) {
				if (!sf.isEmpty()) {
					sf += ",";
				}
				sf += field;
			}
		}
		String whereClause = dao.getUpdateWhereClause();
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			GameLog.getInstance().info(String.format("SELECT %s FROM %s", sf, (dao.getTableName() + whereClause)));
			String sql = "SELECT " + sf + " FROM " + dao.getClass().getSimpleName() + dao.getUpdateWhereClause();
			list = session.createQuery(correctBitwiseOperation(sql)).list();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}

		return list;
	}

	public static boolean _Transactions(List<DaoValue> list) {
		Session session = HibernateSessionFactory.openSession();
		Transaction tx = null;
		try {
			GameLog.getInstance().info("Transaction Begin!");
			tx = session.beginTransaction();
			for (DaoValue dao : list) {
				int ret = -1;
				if (dao.isInsert()) {
					GameLog.getInstance().info(dao.getInsertClause());
					Object obj = (Object) session.save(dao);
					if (obj != null) {
						if (Long.parseLong(obj.toString()) > 0) {
							ret = 1;
						}
					}
				} else if (dao.isDelete()) {
					String del = dao.getUpdateWhereClause();
					if (del.isEmpty()) {
						continue;
					}
					String query = String.format("DELETE FROM %s", dao.getClass().getSimpleName() + del);
					GameLog.getInstance().info(String.format("DELETE FROM %s", dao.getTableName() + del));
					ret = session.createQuery(query).executeUpdate();
				} else {
					String upd = dao.getUpdateClause();
					if (upd.isEmpty()) {
						continue;
					}
					String query = String.format("UPDATE %s %s", dao.getClass().getSimpleName(), upd);
					GameLog.getInstance().info(String.format("UPDATE %s %s", dao.getTableName(), upd));
					ret = session.createQuery(query).executeUpdate();
				}
				if (ret <= 0) {
					GameLog.getInstance().info("Transaction fail!");
					tx.rollback();
					return false;
				}
			}
			tx.commit();
			GameLog.getInstance().info("Transaction Commit!");
			for (DaoValue d : list) {
				d.Sync();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibernateSessionFactory.closeSession(session);
		}
		return false;
	}

	private static String toHQLQuery(String sql) {
		String hql = new String(sql);
		String reg = "(FROM|from|JOIN|join|UPDATE|update)\\s\\w+";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(sql);
		while (matcher.find()) {
			String tbl_name = matcher.group().replaceAll("(FROM|from|JOIN|join|UPDATE|update)\\s", "");
			String info_name = "";
			if (tbl_name.indexOf("_") > 0) {
				for (String word : tbl_name.split("_")) {
					info_name += CommonMethod.capital(word);
				}
			}
			if (info_name.length() > 0) {
				hql = hql.replace(tbl_name, info_name);
			}
		}
		return correctBitwiseOperation(hql);
	}

	private static String correctBitwiseOperation(String sql) {
		if (sql.indexOf("&") <= 0) {
			return sql;
		}
		return sql.replaceAll("(?<=\\W)(\\w+)\\s*&\\s*(\\w+)(?=(\\W|$))", MySQLDialect.BITWISE_FUNCTION_AND + "($1,$2)");
	}

	private static IDaoFactory getDaoFactory(DaoValue dao) {
		if (dao instanceof DaoValueData) {
			return (IDaoFactory) FixDataBase.getInstance(dao.getClass().getSimpleName().substring(3));
		}
		synchronized (_lock) {
			String strName = dao.getClass().getSimpleName();
			IDaoFactory daoFactory = daoData.get(strName);
			if (daoFactory == null) {
				String className = "dev.boom.dao.info.Dao" + strName.substring(3);
				try {
					Class<? extends Object> clazz = Class.forName(className);
					daoFactory = (IDaoFactory) clazz.newInstance();
					daoData.put(strName, daoFactory);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			return daoFactory;
		}
	}

}
