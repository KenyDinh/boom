package dev.boom.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.boom.common.CommonDefine;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblDemoSessionInfo;

public class DemoSessionService {
	
	public static final long GAME_DEMO_DURATION_HOUR = 1;
	public static final long GAME_DEMO_DURATION_DEFAULT = GAME_DEMO_DURATION_HOUR * CommonDefine.MILLION_SECOND_HOUR;

	private DemoSessionService() {
	}
	
	public static List<DemoSessionInfo> getDemoSessionList() {
		TblDemoSessionInfo info = new TblDemoSessionInfo();
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<DemoSessionInfo> ret = new ArrayList<DemoSessionInfo>();
		for (DaoValue dao : list) {
			ret.add(new DemoSessionInfo((TblDemoSessionInfo)dao));
		}
		
		return ret;
	}
	
	public static DemoSessionInfo getDemoSessionById(int id) {
		TblDemoSessionInfo info = new TblDemoSessionInfo();
		info.setId(id);
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return new DemoSessionInfo((TblDemoSessionInfo) daos.get(0));
	}
	
	public static boolean update(DemoSessionInfo demoSessionInfo) {
		return CommonDaoService.update(demoSessionInfo.TblDemoSessionInfo());
	}
	
	public static boolean createNewSession(String demoLocation, Date demoDate, String demoContent) {
		TblDemoSessionInfo tblDemoSessionInfo = new TblDemoSessionInfo();
		tblDemoSessionInfo.setDemo_location(demoLocation);
		tblDemoSessionInfo.setDemo_time(demoDate);
		tblDemoSessionInfo.setContent(demoContent);
		
		Integer id = (Integer) CommonDaoService.insert(tblDemoSessionInfo);
		return (id != null && id > 0);
	}
	
	public static boolean delete(DemoSessionInfo demoSessionInfo) {
		return CommonDaoService.delete(demoSessionInfo.TblDemoSessionInfo());
	}
}
