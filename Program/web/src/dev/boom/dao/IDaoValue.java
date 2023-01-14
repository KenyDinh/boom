/**
 *
 */
package dev.boom.dao;

import java.lang.reflect.Field;

public interface IDaoValue {
	public interface Fields {
	}

	public void Sync();

	public String getTblName();

	public String getPrimaryKey();

	public String getSubKey();

	public String getForeignKey(String strKey);

	public Field[] getClassField();

	public Object getFieldRead();

	public Object getFieldWrite();

	public Fields getInstance();
}
