package dev.boom.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.boom.common.enums.ManageLogType;
import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblManageLogInfo;

public class ManageLogService {

	private ManageLogService() {
	}
	
	public static boolean createManageLog(TblManageLogInfo manageLogInfo) {
		Long id = (Long)CommonDaoService.insert(manageLogInfo);
		return (id != null && id > 0);
	}
	public static boolean createManageLog(UserInfo userInfo, ManageLogType manageLogType) {
		return createManageLog(userInfo, manageLogType, "");
	}
	
	public static boolean createManageLog(UserInfo userInfo, ManageLogType manageLogType, String param) {
		return createManageLog(userInfo, manageLogType.getType(), param);
	}
	
	public static boolean createManageLog(UserInfo userInfo, byte type, String param) {
		TblManageLogInfo manageLogInfo = new TblManageLogInfo();
		manageLogInfo.setUser_id(userInfo.getId());
		manageLogInfo.setUsername(userInfo.getUsername());
		manageLogInfo.setType(type);
		manageLogInfo.setParam(param);
		return createManageLog(manageLogInfo);
	}
	
	public static List<ManageLog> getManageLogList(int limit, int offset) {
		return getManageLogList(null, limit, offset);
	}
	
	public static List<ManageLog> getManageLogList(String option, int limit, int offset) {
		TblManageLogInfo info = new TblManageLogInfo();
		if (option != null && option.length() > 0) {
			info.setSelectOption(option);
		}
		if (limit > 0) {
			info.setLimit(limit);
			if (offset >= 0) {
				info.setOffset(offset);
			}
		}
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<ManageLog> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new ManageLog((TblManageLogInfo) dao));
		}
		
		return ret;
	}
}
