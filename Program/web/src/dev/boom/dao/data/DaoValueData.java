package dev.boom.dao.data;

import java.util.List;

import dev.boom.dao.core.DaoValue;

public class DaoValueData extends DaoValue{

	private static final long serialVersionUID = 1L;

	@Override
	public String getUpdateWhereClause() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getInsertClause() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getUpdateClause() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSelectOption() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSelectOption(String selectOption) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLimit() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLimit(int limit) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getOffset() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setOffset(int offset) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getSelectedField() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addSelectedField(String fieldName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearSelectedField() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addDistinctCountField(String fieldName) {
		throw new UnsupportedOperationException();
	}
	
}
