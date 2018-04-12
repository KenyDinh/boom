package dev.boom.core.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

public class DaoValue implements Cloneable {

	@Transient
	protected DaoValue original;
	
	@Transient
	protected String selectOption;
	
	public final void saveOriginal() {
		try {
			this.original = (DaoValue) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	protected List<String> getSubKey() {
		return null;
	}

	public final DaoValue getOriginal() {
		return original;
	}

	protected String getPrimaryKey() {
		return null;
	}

	protected String getTableName() {
		return null;
	}
	
	public String getRealTableName() {
		return getTableName();
	}

	public String getUpdateWhereClause() {
		return null;
	}
	
	public String getInsertClause() {
		return null;
	}

	public String getUpdateClause() {
		return null;
	}
	
	public void setSelectOption(String option) {
	}
	
	public final List<Field> getFieldList() {
		List<Field> ret = new ArrayList<>();
		Field[] allFields = this.getClass().getDeclaredFields();
		for (Field field : allFields) {
			if (field.isAccessible()) {
				continue;
			}
			ret.add(field);
		}
		return ret;
	}

	public final void Set(String name, Object value) {
		for (Field field : getFieldList()) {
			field.setAccessible(true);
			if (field.getName().equals(name.intern())) {
				try {
					field.set(this, value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	public final Object Get(String name) {
		for (Field field : getFieldList()) {
			field.setAccessible(true);
			if (field.getName().equals(name.intern())) {
				try {
					return field.get(this);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		return null;
	}

	public boolean isInsert() {
		try {
			for (Field field : getFieldList()) {
				field.setAccessible(true);
				if (field.getName().equals(getPrimaryKey())) {
					if (field.get(this.getOriginal()).toString().equals("0")) {
						return true;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isDelete() {
		try {
			for (Field field : getFieldList()) {
				field.setAccessible(true);
				if (field.getName().equals(getPrimaryKey())) {
					if (field.get(this.getOriginal()).toString().equals("-1")) {
						return true;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void setDelete() {
		try {
			for (Field field : getFieldList()) {
				field.setAccessible(true);
				if (field.getName().equals(getPrimaryKey())) {
					if (field.getType() == Long.TYPE) {
						field.set(this.getOriginal(), new Long(-1));
					} else if (field.getType() == Integer.TYPE) {
						field.set(this.getOriginal(), new Integer(-1));
					} else if (field.getType() == Short.TYPE) {
						field.set(this.getOriginal(), -1);
					} else if (field.getType() == Byte.TYPE) {
						field.set(this.getOriginal(), -1);
					} else {
						field.set(this.getOriginal(), "-1");
					}
					return;
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
