package dev.boom.services;

import dev.boom.common.CommonDefine;
import dev.boom.tbl.info.TblUserTicketInfo;

public class UserTicketInfo {

	private TblUserTicketInfo info = null;

	public UserTicketInfo(TblUserTicketInfo info) {
		this.info = info;
	}

	public UserTicketInfo() {
		this.info = new TblUserTicketInfo();
	}
	
	public TblUserTicketInfo getTblInfo() {
		return this.info;
	}
	
	public long getId() {
		return this.info.getId();
	}

	public void setId(long id) {
		this.info.setId(id);
	}

	public long getUser_id() {
		return this.info.getUser_id();
	}

	public void setUser_id(long userId) {
		this.info.setUser_id(userId);
	}

	public short getTicketType() {
		return this.info.getTicket_type();
	}

	public void setTicket_type(short ticketType) {
		this.info.setTicket_type(ticketType);
	}

	public short getTicketRemain() {
		return this.info.getTicket_remain();
	}

	public void setTicketRemain(short ticketRemain) {
		if (ticketRemain < 0) {
			ticketRemain = 0;
		}
		if (ticketRemain > CommonDefine.MAX_TICKET_NUM) {
			ticketRemain = CommonDefine.MAX_TICKET_NUM;
		}
		this.info.setTicket_remain(ticketRemain);
	}

	public int getTotalNum() {
		return this.info.getTotal_num();
	}

	public void setTotalNum(int totalNum) {
		this.info.setTotal_num(totalNum);
	}

}
