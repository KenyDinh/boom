package dev.boom.dao.core;

import java.util.List;

public interface IDaoFactory {
	
	public int getReadKey();
	
	public List<DaoValue> select(DaoValue dao);

	public Object insert(DaoValue dao);
	
	public boolean update(DaoValue dao);
	
	public boolean delete(DaoValue dao);
	
	public long count(DaoValue dao);
	
	public long max(DaoValue dao);

	public long min(DaoValue dao);
	
}
