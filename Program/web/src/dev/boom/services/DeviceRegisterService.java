
package dev.boom.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblDeviceRegisterInfo;

public class DeviceRegisterService {

	private DeviceRegisterService() {
	}
	
	public static DeviceRegister getDeviceRegisterById(long id) {
		TblDeviceRegisterInfo info = new TblDeviceRegisterInfo();
		info.Set("id", id);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		
		return new DeviceRegister((TblDeviceRegisterInfo) list.get(0));
	}
	
	public static DeviceRegister getDeviceRegisterByUserId(int deviceId, long uid) {
		TblDeviceRegisterInfo info = new TblDeviceRegisterInfo();
		info.Set("device_id", deviceId);
		info.Set("user_id", uid);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() == 0) {
			return null;
		}
		
		return new DeviceRegister((TblDeviceRegisterInfo) list.get(0));
	}
	
	public static List<DeviceRegister> ListAllDeviceRegisterLog() {
		return ListAllDeviceRegisterLog(0);
	}
	
	public static List<DeviceRegister> ListAllDeviceRegisterLog(int deviceID) {
		return ListAllDeviceRegisterLog(deviceID, null, -1, -1);
	}

	public static List<DeviceRegister> ListAllDeviceRegisterLog(int deviceID, String option, int limit, int offset) {
		TblDeviceRegisterInfo info = new TblDeviceRegisterInfo();
		info.setSelectOption("WHERE id > 0");
		if (deviceID > 0) {
			info.setSelectOption("AND device_id = " + deviceID);
		}
		if (option != null && !option.isEmpty()) {
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
		List<DeviceRegister> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new DeviceRegister((TblDeviceRegisterInfo) dao));
		}

		return ret;
	}
	
	public static Map<Integer, List<DeviceRegister>> getRegisterListById(List<Integer> deviceIdList) {
		if (deviceIdList == null || deviceIdList.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int id : deviceIdList) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}
		TblDeviceRegisterInfo info = new TblDeviceRegisterInfo();
		info.setSelectOption("WHERE id > 0 AND expired = 0");
		info.setSelectOption("AND device_id IN (" + sb.toString() + ")");
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		Map<Integer, List<DeviceRegister>> ret = new HashMap<>();
		for (DaoValue dao : list) {
			DeviceRegister dr = new DeviceRegister((TblDeviceRegisterInfo) dao);
			if (ret.containsKey(dr.getDevice_id())) {
				ret.get(dr.getDevice_id()).add(dr);
			} else {
				List<DeviceRegister> drList = new ArrayList<>();
				drList.add(dr);
				ret.put(dr.getDevice_id(), drList);
			}
		}
		
		return ret;
	}
}
