package dev.boom.tbl.info;

import java.util.Date;
import java.util.List;

import dev.boom.dao.core.IDaoValue;

public class TblNihongoProgressInfo extends DaoValueInfo implements IDaoValue {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "nihongo_progress_info";
	private static final String PRIMARY_KEY = "id";
	
	private long id;
	private long user_id;
	private int test_id;
	private int progress;
	private Date created;
	private Date updated;
	
	public TblNihongoProgressInfo() {
		this.id = 0;
		this.user_id = 0;
		this.test_id = 0;
		this.progress = 0;
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

	public int getTest_id() {
		return test_id;
	}

	public void setTest_id(int test_id) {
		this.test_id = test_id;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
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
