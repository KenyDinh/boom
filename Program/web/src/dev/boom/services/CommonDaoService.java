package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import dev.boom.connect.HibernateSessionFactory;
import dev.boom.core.GameLog;
import dev.boom.core.dao.DaoValue;
import dev.boom.core.dao.IDaoFactory;

public class CommonDaoService {
	
	public static boolean insert(DaoValue dao) {
		try {
			IDaoFactory factory = getDaoFactory(dao);
			if (factory == null) {
				GameLog.getInstance().info("DaoFactory is null");
				return false;
			}
			return factory.insert(dao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
	
	/*
	 * 
	 * 
	 */
	public static boolean _Insert(DaoValue dao) {
		boolean result = false;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		GameLog.getInstance().info("Session open");
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			GameLog.getInstance().info(dao.getInsertClause());
			session.save(dao);
			tx.commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
			GameLog.getInstance().info("Session close");
		}
		return result;
	}

	public static boolean _Update(DaoValue dao) {
		boolean result = false;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		GameLog.getInstance().info("Session open");
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String updateClause = dao.getUpdateClause();
			if (updateClause.isEmpty()) {
				return true;
			}
			GameLog.getInstance().info(String.format("UPDATE %s %s", dao.getRealTableName(), updateClause));
			int ret = session.createQuery(String.format("UPDATE %s %s", dao.getClass().getSimpleName(), updateClause)).executeUpdate();
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
			session.close();
			GameLog.getInstance().info("Session close");
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static List<DaoValue> _Select(DaoValue dao) {
		List<DaoValue> ret = null;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		GameLog.getInstance().info("Session open");
		try {
			String sql = "FROM " + dao.getClass().getSimpleName();
			String whereClause = dao.getUpdateWhereClause();
			if (!whereClause.isEmpty()) {
				sql += " WHERE " + whereClause;
			}
			GameLog.getInstance().info(String.format("SELECT * FROM %s WHERE %s", dao.getRealTableName(), whereClause));
			List list = session.createQuery(sql).list();
			if (list != null && !list.isEmpty()) {
				ret = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					DaoValue value = (DaoValue) list.get(i);
					value.saveOriginal();
					ret.add(value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			GameLog.getInstance().info("Session close");
		}
		return ret;
	}
	
	public static boolean _Delete(DaoValue dao) {
		boolean result = false;
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		GameLog.getInstance().info("Session open");
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String deleteClause = dao.getUpdateWhereClause();
			if (deleteClause.isEmpty()) {
				return true;
			}
			GameLog.getInstance().info(String.format("DELETE FROM %s WHERE %s", dao.getRealTableName(), deleteClause));
			int ret = session.createQuery(String.format("DELETE FROM %s WHERE %s", dao.getClass().getSimpleName(), deleteClause)).executeUpdate();
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
			session.close();
			GameLog.getInstance().info("Session close");
		}
		return result;
	}

	public static boolean _Transactions(List<DaoValue> list) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
		GameLog.getInstance().info("Session open");
		Transaction tx = null;
		try {
			GameLog.getInstance().info("Transaction Query Start");
			List<String> logs = getTransactionQueries(list);
			if (logs != null) {
				tx = session.beginTransaction();
				for (String log : logs) {
					GameLog.getInstance().info(log);
				}
				for (DaoValue dao : list) {
					int ret = -1;
					if (dao.isInsert()) {
						ret = (Integer) session.save(dao);
					} else if (dao.isDelete()) {
						ret = session.createQuery(String.format("DELETE FROM %s WHERE %s", dao.getClass().getSimpleName(), dao.getUpdateWhereClause())).executeUpdate();
					} else {
						ret = session.createQuery(String.format("UPDATE %s %s", dao.getClass().getSimpleName(), dao.getUpdateClause())).executeUpdate();
					}
					if (ret <= 0) {
						GameLog.getInstance().info("Transaction Query Result : -1");
						tx.rollback();
						return false;
					}
				}
				tx.commit();
				GameLog.getInstance().info("Transaction Query Result : 0");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
			GameLog.getInstance().info("Session close");
		}
		return false;
	}

	private static List<String> getTransactionQueries(List<DaoValue> list) {
		List<String> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			if (dao.isInsert()) {
				ret.add(dao.getInsertClause());
			} else if (dao.isDelete()) {
				String del = dao.getUpdateWhereClause();
				if (del.isEmpty()) {
					return null;
				}
				ret.add(String.format("DELETE FROM %s WHERE %s", dao.getClass().getSimpleName(), del));
			} else {
				String update = dao.getUpdateClause();
				if (update.isEmpty()) {
					return null;
				}
				ret.add(String.format("UPDATE %s %s", dao.getClass().getSimpleName(), update));
			}
		}

		return ret;
	}
	
	private static IDaoFactory getDaoFactory(DaoValue dao) {
		String classPath = dao.getClass().getName();
		String name = classPath.replace(dao.getClass().getSimpleName(), String.format("dao.Dao%s", dao.getClass().getSimpleName()));
		try {
			Class<? extends Object> clazz = Class.forName(name);
			IDaoFactory o = (IDaoFactory) clazz.newInstance();
			return o;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
