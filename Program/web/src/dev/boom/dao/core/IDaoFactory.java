package dev.boom.dao.core;

import java.util.List;

import org.hibernate.Session;

public interface IDaoFactory {
	
	public int getReadKey();
	
	public List<DaoValue> select(DaoValue dao);

	public Object insert(DaoValue dao);
	
	public boolean update(DaoValue dao);
	
	public boolean delete(DaoValue dao);
	
	public long count(DaoValue dao);
	
	public long max(DaoValue dao);

	public long min(DaoValue dao);
	
	public List<DaoValue> select(Session session, DaoValue dao);

	public Object insert(Session session, DaoValue dao);
	
	public boolean update(Session session, DaoValue dao);
	
	public boolean delete(Session session, DaoValue dao);
	
	public long count(Session session, DaoValue dao);
	
	public long max(Session session, DaoValue dao);

	public long min(Session session, DaoValue dao);
	
}
