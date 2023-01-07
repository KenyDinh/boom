package dev.boom.tbl.info;

import dev.boom.dao.core.DaoValueInfo;

public class TblCarouselInfo extends DaoValueInfo {

	private static final long serialVersionUID = 1L;
	private static final String TABLE_NAME = "carousel_info";
	private static final String PRIMARY_KEY = "id";

	private int id;
	private String name;
	private String description;
	private String url;
	private byte local;
	private byte available;

	public TblCarouselInfo() {
		this.id = 0;
		this.name = "";
		this.description = "";
		this.url = "";
		this.local = 0;
		this.available = 0;
		Sync();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public byte getLocal() {
		return local;
	}

	public void setLocal(byte local) {
		this.local = local;
	}

	public byte getAvailable() {
		return available;
	}

	public void setAvailable(byte available) {
		this.available = available;
	}

	public String getPrimaryKey() {
		return PRIMARY_KEY;
	}

	public String getTableName() {
		return TABLE_NAME;
	}
}
