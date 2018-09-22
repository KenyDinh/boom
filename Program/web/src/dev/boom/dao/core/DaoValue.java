package dev.boom.dao.core;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import dev.boom.common.CommonMethod;

public abstract class DaoValue implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	@Transient
	protected DaoValue original;

	@Transient
	protected String selectOption;

	@Transient
	protected int limit;

	@Transient
	protected int offset;

	@Transient
	protected List<String> selectedFields;
	
	// ------------------------------------------------------- //

	public final void Sync() {
		if (this.original == null) {
			try {
				this.original = (DaoValue) this.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			for (Field field : getFieldList()) {
				field.setAccessible(true);
				field.set(this.original, field.get(this));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public final void UnSync() {
		if (this.original == null) {
			try {
				this.original = (DaoValue) this.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			for (Field field : getFieldList()) {
				field.setAccessible(true);
				field.set(this, field.get(this.original));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public final DaoValue getOriginal() {
		if (original == null) {
			throw new NullPointerException("Object is not sync yet!");
		}
		return original;
	}

	// ------------------------------------------------------- //

	public List<String> getSubKey() {
		return null;
	}

	public String getPrimaryKey() {
		return null;
	}

	public String getTableName() {
		return null;
	}
	
	protected Object getSafeFieldValue(Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
		Object o = field.get(object);
		if (field.getType() == String.class) {
			String safeValue = o.toString().replaceAll("'", "''");
			safeValue = safeValue.replaceAll("\\\\", "\\\\\\\\");
			return safeValue;
		}
		return o;
	}

	// ------------------------------------------------------- //

	public abstract String getUpdateWhereClause();

	public abstract String getInsertClause();

	public abstract String getUpdateClause();

	public abstract String getSelectOption();

	public abstract void setSelectOption(String selectOption);

	public abstract int getLimit();

	public abstract void setLimit(int limit);

	public abstract int getOffset();

	public abstract void setOffset(int offset);

	public abstract List<String> getSelectedField();

	public abstract void addSelectedField(String fieldName);

	public abstract void addDistinctCountField(String fieldName);

	public abstract void clearSelectedField();

	// ------------------------------------------------------- //

	public Map<String, Object> toMapObject() {
		Map<String, Object> mapData = new HashMap<>();
		try {
			for (Field field : getFieldList()) {
				field.setAccessible(true);
				Object o = field.get(this);
				if (o.getClass() == Date.class) {
					mapData.put(field.getName(), CommonMethod.getFormatDateString((Date) o));
				} else {
					mapData.put(field.getName(), field.get(this));
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return mapData;
	}

	public final List<Field> getFieldList() {
		List<Field> ret = new ArrayList<>();
		Field[] allFields = this.getClass().getDeclaredFields();
		for (Field field : allFields) {
			field.setAccessible(true);
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
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

	// ------------------------------------------------------- //

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

	// ------------------------------------------------------- //

}
