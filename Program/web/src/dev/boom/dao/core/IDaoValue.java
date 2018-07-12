package dev.boom.dao.core;

import java.util.List;

public interface IDaoValue {
	
	public List<String> getSubKey();
	
	public String getPrimaryKey();
	
	public String getTableName();
	
}
