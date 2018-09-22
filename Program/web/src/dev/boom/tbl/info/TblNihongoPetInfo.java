package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.dao.core.DaoValueInfo;

public class TblNihongoPetInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "nihongo_pet_info";
	private static final String PRIMARY_KEY = "id";

	private long id;
	private String pet_name;
	private int max_level;
	private Date created;
	private Date updated;

	public TblNihongoPetInfo() {
		this.id = 0;
		this.pet_name = "";
		this.max_level = 0;
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

	public String getPet_name() {
		return pet_name;
	}

	public void setPet_name(String pet_name) {
		this.pet_name = pet_name;
	}

	public int getMax_level() {
		return max_level;
	}

	public void setMax_level(int max_level) {
		this.max_level = max_level;
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
