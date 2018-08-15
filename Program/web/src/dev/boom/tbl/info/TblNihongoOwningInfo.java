package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.dao.core.IDaoValue;

public class TblNihongoOwningInfo extends DaoValueInfo implements IDaoValue {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "nihongo_owning_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private long user_id;
	private long pet_id;
	private int current_level;
	private Date created;
	private Date updated;

	public TblNihongoOwningInfo() {
		this.id = 0;
		this.user_id = 0;
		this.pet_id = 0;
		this.current_level = 0;
		this.created = new Date();
		this.updated = this.created;
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

	public long getPet_id() {
		return pet_id;
	}

	public void setPet_id(long pet_id) {
		this.pet_id = pet_id;
	}

	public int getCurrent_level() {
		return current_level;
	}

	public void setCurrent_level(int current_level) {
		this.current_level = current_level;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
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
