package dev.boom.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.boom.common.CommonMethod;
import dev.boom.common.enums.ManageLogType;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.tbl.info.TblManageLogInfo;

public class ManageLogService {

	private ManageLogService() {
	}

	public static boolean createManageLog(TblManageLogInfo manageLogInfo) {
		return (CommonDaoFactory.Insert(manageLogInfo) > 0);
	}
	public static boolean createManageLog(User userInfo, ManageLogType manageLogType) {
		return createManageLog(userInfo, manageLogType, "");
	}
	
	public static boolean createManageLog(User userInfo, ManageLogType manageLogType, String param) {
		return createManageLog(userInfo, manageLogType.getType(), param);
	}
	
	public static boolean createManageLog(User userInfo, byte type, String param) {
		TblManageLogInfo manageLogInfo = new TblManageLogInfo();
		manageLogInfo.Set("user_id", userInfo.getId());
		manageLogInfo.Set("username", userInfo.getUsername());
		manageLogInfo.Set("type", type);
		manageLogInfo.Set("param", param);
		manageLogInfo.Set("created", CommonMethod.getFormatStringNow());
		return createManageLog(manageLogInfo);
	}
	
	public static List<ManageLog> getManageLogList(int limit, int offset) {
		return getManageLogList(null, limit, offset);
	}
	
	public static List<ManageLog> getManageLogList(String option, int limit, int offset) {
		TblManageLogInfo info = new TblManageLogInfo();
		if (option != null && option.length() > 0) {
			info.SetSelectOption(option);
		}
		if (limit > 0) {
			info.SetLimit(limit);
			if (offset >= 0) {
				info.SetOffset(offset);
			}
		}
		List<DaoValue> list = CommonDaoFactory.Select(info);
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

