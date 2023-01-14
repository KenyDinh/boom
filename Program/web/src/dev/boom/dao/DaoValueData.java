/**
 *
 */
package dev.boom.dao;

import java.lang.reflect.Field;

public class DaoValueData extends DaoValue {
	@Override
	public Object Get(String label) {
		Field variables[] = getClassField();
		String internName = label.intern();

		for (int i = 0; i < variables.length; i++) {
			if (variables[i].getName() != internName) {
				continue;
			}

			try {
				if (variables[i].getType() == String.class) {
					String strEscape = variables[i].get(getFieldWrite()).toString().replaceAll("''", "'");
					strEscape = strEscape.replaceAll("\\\\\\\\", "\\\\");
					return strEscape;
				} else {
					return variables[i].get(getFieldWrite());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		return null;
	}

	@Override
	public void SetLimit(int nLimit) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void SetSelectOption(String Option) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void SetUpdateOption(String Option) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void SetDeleteOption(String Option) {
		throw new UnsupportedOperationException();
	}
}
