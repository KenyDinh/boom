/**
 *
 */
package dev.boom.dao;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonDefine;

public class DaoValue {
	private IDaoValue selfDaoValue;
	private List<String> listSelectField;
	private String strOption = "";
	private String strDeleteOption = "";
	private String strUpdateSetClauseOption = "";

	public DaoValue() {
		listSelectField = new ArrayList<String>();
	}

	public DaoValue(IDaoValue ISelf) {
		selfDaoValue = ISelf;
	}

	public IDaoValue Cast() {
		return selfDaoValue;
	}

	public void Sync() {
		selfDaoValue.Sync();
	}

	public String getTblName() {
		return selfDaoValue.getTblName();
	}

	public String getPrimaryKey() {
		return selfDaoValue.getPrimaryKey();
	}

	public String getSubKey() {
		return selfDaoValue.getSubKey();
	}

	public String getForeignKey(String strKey) {
		return selfDaoValue.getForeignKey(strKey);
	}

	protected Field[] getClassField() {
		return selfDaoValue.getClassField();
	}

	protected Object getFieldRead() {
		return selfDaoValue.getFieldRead();
	}

	protected Object getFieldWrite() {
		return selfDaoValue.getFieldWrite();
	}

	public int getTableFieldCount() {
		if (listSelectField != null) {
			if (listSelectField.size() != 0) {
				return listSelectField.size();
			}
		}
		return getClassField().length;
	}

	public Object Get(String label) {
		return null;
	}

	public String GetInsertValuesClause() {
		return null;
	}

	public String GetUpdateSetClause() {
		return null;
	}

	public List<String> GetSelectWhereClause() {
		return null;
	}

	public String GetUpdateWhereClause() {
		return null;
	}

	public void AddSelectField(String strField) {
		if (strField == null) {
			return;
		}

		if (strField.equals("")) {
			return;
		}

		listSelectField.add(strField);
	}
	
	public Map<String, Object> toMapObject() {
		Map<String, Object> mapData = new HashMap<>();
		Field variables[] = getClassField();
		for (int i = 0 ; i < variables.length ; i++) {
			String label = variables[i].getName();
			mapData.put(label, Get(label));
		}
		return mapData;
	}

	public String GetSelectField() {
		if (listSelectField != null) {
			if (listSelectField.size() != 0) {
				String strResult = "";
				for (String strUnit : listSelectField) {
					if (strResult.equals("") == false) {
						strResult += ",";
					}
					strResult += strUnit;
				}

				return strResult;
			}
		}

		return "*";
	}

	public String GetSelectFieldWithTableName() {
		if (listSelectField != null) {
			if (listSelectField.size() != 0) {
				String strResult = "";
				for (String strUnit : listSelectField) {
					if (strResult.equals("") == false) {
						strResult += ",";
					}
					strResult += getTblName() + "." + strUnit;
				}

				return strResult;
			}
		}

		return (getTblName() + ".*");
	}

	public void ClearOption() {
		strOption = "";
	}

	public void SetLimit(int nLimit) {
		strOption += (" LIMIT " + nLimit);
	}
	
	public void SetOffset(int nOffset) {
		strOption += (" OFFSET " + nOffset);
	}

	public void SetSelectOption(String Option) {
		strOption += (" " + Option);
	}

	public String GetSelectOption() {
		return strOption;
	}

	public void SetUpdateOption(String Option) {
		strOption += (" " + Option);
	}

	public String GetUpdateOption() {
		return strOption;
	}

	public void SetDeleteOption(String Option) {
		strDeleteOption += (" " + Option);
	}

	public String GetDeleteOption() {
		return strDeleteOption;
	}

	/**
	 *
	 */
	public void ClearUpdateSetClauseOption() {
		strUpdateSetClauseOption = "";
	}

	/**
	 * @param option
	 */
	public void SetUpdateSetClauseOption(String option) {
		strUpdateSetClauseOption = option;
	}

	/**
	 * @return
	 */
	public String GetUpdateSetClauseOption() {
		return strUpdateSetClauseOption;
	}

	public void Set(String label, Object value) {
		assert (!StringUtils.isBlank(label));

		Field variables[] = getClassField();
		String internName = label.intern();
		SimpleDateFormat sdf = new SimpleDateFormat(CommonDefine.DATE_FORMAT_PATTERN);

		for (int i = 0 ; i < variables.length ; i++) {
			if (variables[i].getName() != internName) {
				continue;
			}

			try {
				if (variables[i].getType() == String.class) {
					if (value instanceof Date) {
						variables[i].set(getFieldWrite(), sdf.format((Date) value).toString());
					} else {
						if(value == null){
							return;
						}
						String strEscape = value.toString().replaceAll("'", "''");
						strEscape = strEscape.replaceAll("\\\\", "\\\\\\\\");
						variables[i].set(getFieldWrite(), strEscape);
					}
				} else if (variables[i].getType() == Byte.TYPE) {
					variables[i].set(getFieldWrite(), Byte.parseByte(value.toString()));
				} else if (variables[i].getType() == Short.TYPE) {
					variables[i].set(getFieldWrite(), Short.parseShort(value.toString()));
				} else {
					variables[i].set(getFieldWrite(), value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}

	public void Change(String label, Object old_value, Object new_value) {
		Field variables[] = getClassField();
		String internName = label.intern();

		for (int i = 0 ; i < variables.length ; i++) {
			if (variables[i].getName() != internName) {
				continue;
			}

			try {
				if (variables[i].getType() == String.class) {
					String strEscape = old_value.toString().replaceAll("'", "''");
					strEscape = strEscape.replaceAll("\\\\", "\\\\\\\\");
					variables[i].set(getFieldRead(), strEscape);

					strEscape = new_value.toString().replaceAll("'", "''");
					strEscape = strEscape.replaceAll("\\\\", "\\\\\\\\");
					variables[i].set(getFieldWrite(), strEscape);
				} else if (variables[i].getType() == Byte.TYPE) {
					variables[i].set(getFieldRead(), Byte.parseByte(old_value.toString()));
					variables[i].set(getFieldWrite(), Byte.parseByte(new_value.toString()));
				} else if (variables[i].getType() == Short.TYPE) {
					variables[i].set(getFieldRead(), Short.parseShort(old_value.toString()));
					variables[i].set(getFieldWrite(), Short.parseShort(new_value.toString()));
				} else {
					variables[i].set(getFieldRead(), old_value);
					variables[i].set(getFieldWrite(), new_value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}

	/**
	 *
	 * Primary Key 0 for Insert
	 * @return
	 */
	public boolean isInsert() {
		Field variables[] = getClassField();

		for (int i = 0 ; i < variables.length ; i++) {
			if (variables[i].getName() != getPrimaryKey()) {
				continue;
			}

			try {
				return variables[i].get(getFieldRead()).toString().equals("0");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 *
	 * Primary Key -1 for Delete
	 * @return
	 */
	public boolean isDelete() {
		Field variables[] = getClassField();

		for (int i = 0 ; i < variables.length ; i++) {
			if (variables[i].getName() != getPrimaryKey()) {
				continue;
			}

			try {
				return variables[i].get(getFieldRead()).toString().equals("-1");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public void setDelete() {
		Field variables[] = getClassField();

		for (int i = 0 ; i < variables.length ; i++) {
			if (variables[i].getName() != getPrimaryKey()) {
				continue;
			}

			try {
				variables[i].set(getFieldRead(), "-1");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
