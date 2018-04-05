package dev.boom.core.dao;

import java.util.List;

public interface IDaoFactory {
	
	public int getReadKey();
	
	public List<DaoValue> select(DaoValue dao);

	public boolean insert(DaoValue dao);
	
	public boolean update(DaoValue dao);
	
	public boolean delete(DaoValue dao);
	
}
