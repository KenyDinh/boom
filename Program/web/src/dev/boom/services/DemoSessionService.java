package dev.boom.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.boom.common.CommonDefine;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblDemoSessionInfo;

public class DemoSessionService {
	
	public static final long GAME_DEMO_DURATION_HOUR = 1;
	public static final long GAME_DEMO_DURATION_DEFAULT = GAME_DEMO_DURATION_HOUR * CommonDefine.MILLION_SECOND_HOUR;

	private DemoSessionService() {
	}

	public static List<DemoSession> getDemoSessionListAll(String option) {
		TblDemoSessionInfo tblInfo = new TblDemoSessionInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<DemoSession> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new DemoSession((TblDemoSessionInfo) dao));
		}

		return ret;
	}

	public static List<DemoSession> getDemoSessionListAll() {
		return getDemoSessionListAll(null);
	}
	
	public static List<DemoSession> getDemoSessionList() {
		TblDemoSessionInfo info = new TblDemoSessionInfo();
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<DemoSession> ret = new ArrayList<DemoSession>();
		for (DaoValue dao : list) {
			ret.add(new DemoSession((TblDemoSessionInfo)dao));
		}
		
		return ret;
	}
	
	public static DemoSession getDemoSessionById(int id) {
		TblDemoSessionInfo info = new TblDemoSessionInfo();
		info.Set("id", id);
		List<DaoValue> daos = CommonDaoFactory.Select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return new DemoSession((TblDemoSessionInfo) daos.get(0));
	}
	
	public static boolean update(DemoSession DemoSession) {
		return (CommonDaoFactory.Update(DemoSession.getDemoSessionInfo()) > 0);
	}
	
	public static boolean createNewSession(String demoLocation, Date demoDate, String demoContent) {
		TblDemoSessionInfo tblDemoSession = new TblDemoSessionInfo();
		tblDemoSession.Set("demo_location", demoLocation);
		tblDemoSession.Set("demo_time", demoDate);
		tblDemoSession.Set("content", demoContent);
		
		Integer id = (Integer) CommonDaoFactory.Insert(tblDemoSession);
		return (id != null && id > 0);
	}
	
	public static boolean delete(DemoSession DemoSession) {
		return (CommonDaoFactory.Delete(DemoSession.getDemoSessionInfo()) > 0);
	}
}

