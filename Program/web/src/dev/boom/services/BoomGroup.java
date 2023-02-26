package dev.boom.services;

import dev.boom.tbl.info.TblBoomGroupInfo;

public class BoomGroup {
	private TblBoomGroupInfo boomGroupInfo;

	public BoomGroup() {
		boomGroupInfo = new TblBoomGroupInfo();
	}

	public BoomGroup(TblBoomGroupInfo boomGroupInfo) {
		this.boomGroupInfo = boomGroupInfo;
	}

	public TblBoomGroupInfo getBoomGroupInfo() {
		return boomGroupInfo;
	}

	public long getId() {
		return (Long) boomGroupInfo.Get("id");
	}

	public void setId(long id) {
		boomGroupInfo.Set("id", id);
	}

	public String getName() {
		return (String) boomGroupInfo.Get("name");
	}

	public void setName(String name) {
		boomGroupInfo.Set("name", name);
	}

}

