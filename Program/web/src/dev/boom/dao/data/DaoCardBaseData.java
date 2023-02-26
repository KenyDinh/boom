package dev.boom.dao.data;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.dao.IDaoFactory;
import dev.boom.dao.fix.FixData;
import dev.boom.tbl.data.TblCardBaseData;

public class DaoCardBaseData extends FixData implements IDaoFactory {

	@Override
	public List<DaoValue> select(Connection conn, List<DaoValue> self) {
		return CommonDaoFactory._SelectFix(self);
	}

	@Override
	public List<DaoValue> select(Connection conn, DaoValue self) {
		return CommonDaoFactory._SelectFix(self);
	}

	@Override
	public int insert(Connection conn, DaoValue self) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(Connection conn, DaoValue self) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Connection conn, DaoValue self) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(Connection conn, DaoValue self) {
		return CommonDaoFactory._CountFix(self);
	}

	@Override
	public long max(Connection conn, DaoValue self) {
		return CommonDaoFactory._MaxFix(self);
	}

	@Override
	public long min(Connection conn, DaoValue self) {
		return CommonDaoFactory._MinFix(self);
	}

	@Override
	public int getReadKey() {
		return 0;
	}

	@Override
	protected List<Field> getField() {
		TblCardBaseData tblData = new TblCardBaseData();
		return Arrays.asList(tblData.getClassField());
	}

}
