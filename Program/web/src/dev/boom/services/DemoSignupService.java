package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblDemoSignupInfo;

public class DemoSignupService {
	
	public static int FLAG_NEW_ENTRY = 0;

	private DemoSignupService() {
	}

	public static List<DemoSignup> getDemoSignupListAll(String option) {
		TblDemoSignupInfo tblInfo = new TblDemoSignupInfo();

		if (option != null && !option.isEmpty()) {
			tblInfo.SetSelectOption(option);
		}

		List<DaoValue> list = CommonDaoFactory.Select(tblInfo);
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<DemoSignup> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new DemoSignup((TblDemoSignupInfo) dao));
		}

		return ret;
	}

	public static List<DemoSignup> getDemoSignupListAll() {
		return getDemoSignupListAll(null);
	}
	
	public static List<DemoSignup> getDemoSignupList() {
		TblDemoSignupInfo info = new TblDemoSignupInfo();
		List<DaoValue> list = CommonDaoFactory.Select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<DemoSignup> ret = new ArrayList<DemoSignup>();
		for (DaoValue dao : list) {
			ret.add(new DemoSignup((TblDemoSignupInfo)dao));
		}
		
		return ret;
	}
	
	public static DemoSignup getDemoSignupById(int id) {
		TblDemoSignupInfo info = new TblDemoSignupInfo();
		info.Set("id", id);
		List<DaoValue> daos = CommonDaoFactory.Select(info);
		if (daos == null || daos.isEmpty() || daos.size() != 1) {
			return null;
		}
		return new DemoSignup((TblDemoSignupInfo) daos.get(0));
	}
	
	public static boolean update(DemoSignup DemoSignupInfo) {
		return (CommonDaoFactory.Update(DemoSignupInfo.getDemoSignupInfo()) > 0);
	}
	
	public static boolean createNewSignup(String gameName, String speakerName, String description) {
		TblDemoSignupInfo tblDemoSignupInfo = new TblDemoSignupInfo();
		tblDemoSignupInfo.Set("game_name", gameName);
		tblDemoSignupInfo.Set("speaker_name", speakerName);
		tblDemoSignupInfo.Set("description", description);
		tblDemoSignupInfo.Set("flag", FLAG_NEW_ENTRY);
		
		Integer id = (Integer) CommonDaoFactory.Insert(tblDemoSignupInfo);
		return (id != null && id > 0);
	}
	
	public static boolean delete(DemoSignup DemoSignupInfo) {
		return (CommonDaoFactory.Delete(DemoSignupInfo.getDemoSignupInfo()) > 0);
	}
	
}

