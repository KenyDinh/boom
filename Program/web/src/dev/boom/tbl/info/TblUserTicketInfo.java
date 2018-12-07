package dev.boom.tbl.info;

import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblUserTicketInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "user_ticket_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long user_id;
	private short ticket_type;
	private short ticket_remain;
	private int total_num;

	public TblUserTicketInfo() {
		this.id = 0;
		this.user_id = 0;
		this.ticket_type = 0;
		this.ticket_remain = 0;
		this.total_num = 0;
		Sync();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public short getTicket_type() {
		return ticket_type;
	}

	public void setTicket_type(short ticket_type) {
		this.ticket_type = ticket_type;
	}

	public short getTicket_remain() {
		return ticket_remain;
	}

	public void setTicket_remain(short ticket_remain) {
		this.ticket_remain = ticket_remain;
	}

	public int getTotal_num() {
		return total_num;
	}

	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}

	public List<String> getSubKey() {
		return null;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
