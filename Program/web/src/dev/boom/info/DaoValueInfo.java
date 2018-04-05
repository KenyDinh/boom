package dev.boom.info;

import java.lang.reflect.Field;

import dev.boom.core.dao.DaoValue;

public class DaoValueInfo extends DaoValue {

	public DaoValueInfo() {
		super();
	}

	@Override
	public String getUpdateWhereClause() {
		StringBuilder sb = new StringBuilder();
		try {
			for (Field field : getFieldList()) {
				field.setAccessible(true);
				Object o1 = field.get(getOriginal());
				Object o2 = field.get(this);
				if (field.getName().equals(this.getPrimaryKey()) && !o2.toString().equals("0")) {
					sb.append(field.getName()).append(" = ");
					if (o2.getClass() == String.class) {
						sb.append("'").append(o2).append("'");
					} else {
						sb.append(o2);
					}
					continue;
				}
				if (o1.equals(o2)) {
					continue;
				}
				if (sb.length() > 0) {
					sb.append(" AND ");
				}
				sb.append(field.getName()).append(" = ");
				if (o2.getClass() == String.class) {
					sb.append("'").append(o2).append("'");
				} else {
					sb.append(o2);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (selectOption != null) {
			if (sb.length() > 0) {
				sb.append(" AND ");
			}
			sb.append(selectOption);
		}
		return sb.toString();
	}
	
	@Override
	public String getInsertClause() {
		StringBuilder sb = new StringBuilder();
		try {
			for (Field field : getFieldList()) {
				field.setAccessible(true);
				Object o1 = field.get(this);
				if (sb.length() > 0) {
					sb.append(",");
				}
				if (field.getName().equals(this.getPrimaryKey()) && o1.toString().equals("0")) {
					sb.append("default");
					continue;
				}
				if (o1.getClass() == String.class) {
					sb.append("'").append(o1).append("'");
				} else {
					sb.append(o1);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return String.format("INSERT INTO %s VALUES(%s)", this.getTableName(), sb.toString());
	}
	
	@Override
	public String getUpdateClause() {
		StringBuilder whereClause = new StringBuilder();
		StringBuilder setClause = new StringBuilder();
		try {
			for (Field field : getFieldList()) {
				field.setAccessible(true);
				Object o1 = field.get(getOriginal());
				Object o2 = field.get(this);
				if (setClause.length() > 0) {
					setClause.append(", ");
				}
				if (field.getName().equals(this.getPrimaryKey())) {
					if (o1.toString().equals("0")) {
						return "";
					}
					whereClause.append(field.getName()).append(" = ");
					if (o1.getClass() == String.class) {
						whereClause.append("'").append(o1).append("'");
					} else {
						whereClause.append(o1);
					}
					continue;
				}
				if (o1.equals(o2)) {
					continue;
				}
				if (whereClause.length() > 0) {
					whereClause.append(" AND ");
				}
				whereClause.append(field.getName()).append(" = ");
				setClause.append(field.getName()).append(" = ");
				if (o1.getClass() == String.class) {
					whereClause.append("'").append(o1).append("'");
					setClause.append("'").append(o2).append("'");
				} else {
					whereClause.append(o1);
					setClause.append(o2);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (setClause.length() == 0) {
			return "";
		}
		return String.format("SET %s WHERE %s", setClause.toString(), whereClause.toString());
	}
	
	@Override
	public void setSelectOption(String option) {
		selectOption = option;
	}
	
}
