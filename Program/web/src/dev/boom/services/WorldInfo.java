package dev.boom.services;

import dev.boom.common.enums.EventFlagEnum;
import dev.boom.tbl.info.TblWorldInfo;

public class WorldInfo {

	private TblWorldInfo info = null;

	public WorldInfo(TblWorldInfo info) {
		this.info = info;
	}

	public WorldInfo() {
		this.info = new TblWorldInfo();
	}
	
	public TblWorldInfo getTblInfo() {
		return this.info;
	}
	
	public long getId() {
		return this.info.getId();
	}

	public void setId(long id) {
		this.info.setId(id);
	}

	public int getEventFlag() {
		return this.info.getEvent_flag();
	}

	public void setEventFlag(int event_flag) {
		this.info.setEvent_flag(event_flag);
	}
	
	public void addEventFlag(int index) {
		if (index <= 0) {
			return;
		}
		setEventFlag(getEventFlag() | (1 << (index - 1)));
	}
	
	public void addEventFlag(EventFlagEnum eventFlag) {
		addEventFlag(eventFlag.getIndex());
	}
	
	public boolean isActiveEventFlag(int index) {
		if (index <= 0) {
			return false;
		}
		return (getEventFlag() & (1 << (index - 1))) > 0;
	}
	
	public boolean isActiveEventFlag(EventFlagEnum eventFlag) {
		return isActiveEventFlag(eventFlag.getIndex());
	}
}
