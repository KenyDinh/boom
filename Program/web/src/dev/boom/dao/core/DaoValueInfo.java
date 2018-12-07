package dev.boom.dao.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.boom.common.CommonMethod;

public class DaoValueInfo extends DaoValue {

	private static final long serialVersionUID = 1L;

	public DaoValueInfo() {
		super();
	}

	@Override
	public String getUpdateWhereClause() {
		StringBuilder sb = new StringBuilder();
		try {
			for (Field field : getFieldList()) {
				field.setAccessible(true);
				Object o1 = getSafeFieldValue(field, getOriginal());
				Object o2 = getSafeFieldValue(field, this);
				if (field.getName().equals(this.getPrimaryKey())) {
					if (!o2.toString().equals("0")) {
						sb.append(field.getName()).append(" = ");
						if (field.getType() == String.class) {
							sb.append("'").append(o2).append("'");
						} else if (field.getType() == Date.class) {
							sb.append("'").append(CommonMethod.getFormatDateString((Date) o2)).append("'");
						} else {
							sb.append(o2);
						}
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
				if (field.getType() == String.class) {
					sb.append("'").append(o2).append("'");
				} else if (field.getType() == Date.class) {
					sb.append("'").append(CommonMethod.getFormatDateString((Date) o2)).append("'");
				} else {
					sb.append(o2);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		String wh = " ";
		if (sb.length() > 0) {
			wh += "WHERE ";
		}
		if (selectOption != null && !selectOption.trim().isEmpty()) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(selectOption.trim());
		}
		return wh + sb.toString();
	}

	@Override
	public String getInsertClause() {
		StringBuilder sb = new StringBuilder();
		try {
			for (Field field : getFieldList()) {
				field.setAccessible(true);
				Object o1 = getSafeFieldValue(field, this);
				if (sb.length() > 0) {
					sb.append(",");
				}
				if (field.getName().equals(this.getPrimaryKey()) && o1.toString().equals("0")) {
					sb.append("DEFAULT");
					continue;
				}
				if (field.getType() == String.class) {
					sb.append("'").append(o1).append("'");
				} else if (field.getType() == Date.class) {
					sb.append("'").append(CommonMethod.getFormatDateString((Date) o1)).append("'");
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
				Object o1 = getSafeFieldValue(field, getOriginal());
				Object o2 = getSafeFieldValue(field, this);
				if (field.getName().equals(this.getPrimaryKey())) {
					if (o1.toString().equals("0")) {
						return "";
					}
					whereClause.append(field.getName()).append(" = ");
					if (field.getType() == String.class) {
						whereClause.append("'").append(o1).append("'");
					} else if (field.getType() == Date.class) {
						whereClause.append("'").append(CommonMethod.getFormatDateString((Date) o1)).append("'");
					} else {
						whereClause.append(o1);
					}
					continue;
				}
				if (o1.equals(o2)) {
					continue;
				}
				if (setClause.length() > 0) {
					setClause.append(", ");
				}
				if (whereClause.length() > 0) {
					whereClause.append(" AND ");
				}
				whereClause.append(field.getName()).append(" = ");
				setClause.append(field.getName()).append(" = ");
				if (field.getType() == String.class) {
					whereClause.append("'").append(o1).append("'");
					setClause.append("'").append(o2).append("'");
				} else if (field.getType() == Date.class) {
					whereClause.append("'").append(CommonMethod.getFormatDateString((Date) o1)).append("'");
					setClause.append("'").append(CommonMethod.getFormatDateString((Date) o2)).append("'");
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
	public String getSelectOption() {
		return selectOption;
	}

	@Override
	public void setSelectOption(String selectOption) {
		if (selectOption == null || selectOption.isEmpty()) {
			return;
		}
		if (this.selectOption == null) {
			this.selectOption = "";
		}
		this.selectOption += (" " + selectOption);
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public List<String> getSelectedField() {
		return selectedFields;
	}

	@Override
	public void addSelectedField(String fieldName) {
		if (fieldName == null || fieldName.isEmpty()) {
			return;
		}
		if (selectedFields == null) {
			selectedFields = new ArrayList<>();
		}
		selectedFields.add(fieldName.trim());
	}

	@Override
	public void clearSelectedField() {
		if (selectedFields != null && selectedFields.size() > 0) {
			selectedFields.clear();
		}
	}
	
	/**
	 * Can only add one field at once.
	 * Any field added before will be remove.
	 */
	@Override
	public void addDistinctCountField(String fieldName) {
		if (fieldName == null || fieldName.isEmpty()) {
			return;
		}
		if (selectedFields == null) {
			selectedFields = new ArrayList<>();
		}
		selectedFields.clear();
		selectedFields.add("DISTINCT " + fieldName);
	}


}
