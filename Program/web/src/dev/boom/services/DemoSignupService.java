package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblDemoSignupInfo;

public class DemoSignupService {
	
	public static int FLAG_NEW_ENTRY = 0;

	private DemoSignupService() {
	}
	
	public static List<DemoSignupInfo> getDemoSignupList() {
		TblDemoSignupInfo info = new TblDemoSignupInfo();
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<DemoSignupInfo> ret = new ArrayList<DemoSignupInfo>();
		for (DaoValue dao : list) {
			ret.add(new DemoSignupInfo((TblDemoSignupInfo)dao));
		}
		
		return ret;
	}
	
	public static DemoSignupInfo getDemoSignupById(int id) {
		TblDemoSignupInfo info = new TblDemoSignupInfo();
		info.setId(id);
		List<DaoValue> daos = CommonDaoService.select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return new DemoSignupInfo((TblDemoSignupInfo) daos.get(0));
	}
	
	public static boolean update(DemoSignupInfo DemoSignupInfo) {
		return CommonDaoService.update(DemoSignupInfo.TblDemoSignupInfo());
	}
	
	public static boolean createNewSignup(String gameName, String speakerName, String description) {
		TblDemoSignupInfo tblDemoSignupInfo = new TblDemoSignupInfo();
		tblDemoSignupInfo.setGame_name(gameName);
		tblDemoSignupInfo.setSpeaker_name(speakerName);
		tblDemoSignupInfo.setDescription(description);
		tblDemoSignupInfo.setFlag(FLAG_NEW_ENTRY);
		
		Integer id = (Integer) CommonDaoService.insert(tblDemoSignupInfo);
		return (id != null && id > 0);
	}
	
	public static boolean delete(DemoSignupInfo DemoSignupInfo) {
		return CommonDaoService.delete(DemoSignupInfo.TblDemoSignupInfo());
	}
}
