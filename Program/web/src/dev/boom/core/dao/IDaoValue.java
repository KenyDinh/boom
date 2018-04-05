package dev.boom.core.dao;

import java.util.List;

public interface IDaoValue {
	
	public List<String> getSubKey();
	
	public String getPrimaryKey();
	
	public String getTableName();
	
}
