package dev.boom.dao.data;

import java.lang.reflect.Field;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.dao.core.IDaoFactory;
import dev.boom.dao.fix.FixData;
import dev.boom.services.CommonDaoService;
import dev.boom.tbl.data.TblSurveyValidCodeData;

public class DaoSurveyValidCodeData extends FixData implements IDaoFactory {

	@Override
	public int getReadKey() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<DaoValue> select(DaoValue dao) {
		return CommonDaoService._SelectFix(dao);
	}

	@Override
	public Object insert(DaoValue dao) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(DaoValue dao) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(DaoValue dao) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count(DaoValue dao) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long max(DaoValue dao) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long min(DaoValue dao) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected List<Field> getField() {
		TblSurveyValidCodeData data = new TblSurveyValidCodeData();
		return data.getFieldList();
	}
	
}
