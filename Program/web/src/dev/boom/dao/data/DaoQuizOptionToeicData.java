package dev.boom.dao.data;

import java.lang.reflect.Field;
import java.util.List;

import org.hibernate.Session;

import dev.boom.dao.core.DaoValue;
import dev.boom.dao.core.IDaoFactory;
import dev.boom.dao.fix.FixData;
import dev.boom.services.CommonDaoService;
import dev.boom.tbl.data.TblQuizOptionToeicData;

public class DaoQuizOptionToeicData extends FixData implements IDaoFactory {

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
		TblQuizOptionToeicData data = new TblQuizOptionToeicData();
		return data.getFieldList();
	}

	@Override
	public List<DaoValue> select(Session session, DaoValue dao) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object insert(Session session, DaoValue dao) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Session session, DaoValue dao) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Session session, DaoValue dao) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count(Session session, DaoValue dao) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long max(Session session, DaoValue dao) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long min(Session session, DaoValue dao) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
