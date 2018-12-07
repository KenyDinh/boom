package dev.boom.services;

import java.util.ArrayList;
import java.util.List;

import dev.boom.dao.core.DaoValue;
import dev.boom.tbl.info.TblUserTicketInfo;

public class UserTicketService {

	public static List<UserTicketInfo> getUserTicketInfo(long userId) {
		TblUserTicketInfo info = new TblUserTicketInfo();
		info.setUser_id(userId);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<UserTicketInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new UserTicketInfo((TblUserTicketInfo) dao));
		}
		return ret;
	}
	
	public static UserTicketInfo getUserTicketInfo(long userId, short ticketType) {
		TblUserTicketInfo info = new TblUserTicketInfo();
		info.setUser_id(userId);
		info.setTicket_type(ticketType);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return new UserTicketInfo((TblUserTicketInfo) list.get(0));
	}
	
	
	public static List<UserTicketInfo> getUserTicketInfo(long userId, List<Short> ticketTypeList, String option) {
		TblUserTicketInfo info = new TblUserTicketInfo();
		info.setUser_id(userId);
		if (ticketTypeList != null && !ticketTypeList.isEmpty()) {
			String options = "";
			for (short ticketType : ticketTypeList) {
				if (!options.isEmpty()) {
					options += ",";
				}
				options += ticketType;
			}
			if (options.isEmpty()) {
				info.setSelectOption("AND ticket_type IN(" + options + ")");
			}
		}
		if (option != null && !option.isEmpty()) {
			info.setSelectOption(option);
		}
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<UserTicketInfo> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new UserTicketInfo((TblUserTicketInfo) dao));
		}
		return ret;
	}
}
