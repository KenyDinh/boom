package dao.boom.data;

import dev.boom.core.dao.DaoValue;

public class DaoValueData extends DaoValue{

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
	
}
