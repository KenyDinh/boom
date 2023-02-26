/**
 *
 */
package dev.boom.dao;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

//import org.apache.click.util.ClickUtils;
import org.apache.commons.lang.StringEscapeUtils;


public class DaoValueInfo extends DaoValue {

	@Override
	public String GetInsertValuesClause() {
		String strPrimaryKey = getPrimaryKey();// fieldWrite.GetKey();
		StringBuilder strClause = new StringBuilder();

		Field variables[] = getClassField();
		try {
			for (int i = 0; i < variables.length; i++) {
				Object objRead = variables[i].get(getFieldRead());
				Object objWrite = variables[i].get(getFieldWrite());

				String strToken[] = (variables[i].toString()).split("\\$Fields.");

				if (strClause.length() > 0) {
					strClause.append(", ");
				}

				if (strPrimaryKey.equals(strToken[1])) {
					if ((objRead.equals(objWrite)) == true) {
						strClause.append("DEFAULT");
						continue;
					}
				}

				if ((objRead.getClass() == String.class) || (objRead.getClass() == Date.class)) {
					strClause.append("'").append(objWrite).append("'");
				} else {
					strClause.append(objWrite);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strClause.toString();
	}

	@Override
	public String GetUpdateSetClause() {
		StringBuilder strClause = new StringBuilder();
		String strSubKey = getSubKey();

		Field variables[] = getClassField();
		try {
			for (int i = 0; i < variables.length; i++) {
				Object objRead = variables[i].get(getFieldRead());
				Object objWrite = variables[i].get(getFieldWrite());

				if ((objRead.equals(objWrite)) == true) {
					continue;
				}

				if (strClause.length() > 0) {
					strClause.append(", ");
				}

				String strToken[] = (variables[i].toString()).split("\\$Fields.");
				if (strSubKey.contains(("<" + strToken[1] + ">"))) { // Update
					if ((objRead.getClass() == String.class) || (objRead.getClass() == Date.class)) {
						strClause.append(strToken[1]).append(" = '").append(objRead).append("'");
					} else {
						strClause.append(strToken[1]).append(" = ").append(objRead);
					}
					continue;
				}

				if ((objRead.getClass() == String.class) || (objRead.getClass() == Date.class)) {
					strClause.append(strToken[1]).append(" = '").append(objWrite).append("'");
				} else {
					strClause.append(strToken[1]).append(" = ").append(objWrite);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strClause.toString();
	}

	@Override
	public List<String> GetSelectWhereClause() {
		List<String> strClauseList = new ArrayList<String>();

		Field variables[] = getClassField();
		try {
			for (int i = 0; i < variables.length; i++) {
				Object objRead = variables[i].get(getFieldRead());
				Object objWrite = variables[i].get(getFieldWrite());

				if ((objRead.equals(objWrite)) == true) {
					continue;
				}

				String strToken[] = (variables[i].toString()).split("\\$Fields.");

				if ((objRead.getClass() == String.class) || (objRead.getClass() == Date.class)) {
					strClauseList.add((strToken[1] + " = '" + objWrite + "'"));
				} else {
					strClauseList.add((strToken[1] + " = " + objWrite));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strClauseList;
	}

	@Override
	public String GetUpdateWhereClause() {
		String strPrimaryKey = getPrimaryKey(); // fieldWrite.GetKey();
		String strSubKey = getSubKey();
		StringBuilder strClause = new StringBuilder();

		Field variables[] = getClassField();
		try {
			for (int i = 0; i < variables.length; i++) {
				Object objRead = variables[i].get(getFieldRead());
				Object objWrite = variables[i].get(getFieldWrite());

				String strToken[] = (variables[i].toString()).split("\\$Fields.");
				if (strPrimaryKey.equals(strToken[1]) || strSubKey.contains(("<" + strToken[1] + ">"))) { // Update
					// Keyは特別に処理.
					if (strClause.length() > 0) {
						strClause.append(" AND ");
					}

					if ((objRead.getClass() == String.class) || (objRead.getClass() == Date.class)) {
						strClause.append(strToken[1]).append(" = '").append(objWrite).append("'");
					} else {
						strClause.append(strToken[1]).append(" = ").append(objWrite);
					}
					continue;
				}

				if ((objRead.equals(objWrite)) == true) {
					continue;
				}

				if (strClause.length() > 0) {
					strClause.append(" AND ");
				}

				if ((objRead.getClass() == String.class) || (objRead.getClass() == Date.class)) {
					strClause.append(strToken[1]).append(" = '").append(objRead).append("'");
				} else {
					strClause.append(strToken[1]).append(" = ").append(objRead);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strClause.toString();
	}

	@Override
	public Object Get(String label) {
		Field variables[] = getClassField();
		String internName = label.intern();

		for (int i = 0 ; i < variables.length ; i++) {
			if (variables[i].getName() != internName) {
				continue;
			}

			try {
				if (variables[i].getType() == String.class) {
					String strEscape = "";
					strEscape = StringEscapeUtils.escapeHtml(variables[i].get(getFieldWrite()).toString()).replaceAll("''", "'");
					strEscape = strEscape.replaceAll("\\\\\\\\", "\\\\");
					return strEscape;
				}
				else {
					return variables[i].get(getFieldWrite());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		return null;
	}
}
