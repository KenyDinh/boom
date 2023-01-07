package dev.boom.services;

import dev.boom.tbl.info.TblDemoSignupInfo;

public class DemoSignupInfo {

	private TblDemoSignupInfo info = null;

	public DemoSignupInfo(TblDemoSignupInfo info) {
		this.info = info;
	}

	public DemoSignupInfo() {
		this.info = new TblDemoSignupInfo();
	}

	public TblDemoSignupInfo TblDemoSignupInfo() {
		return info;
	}

	public int getId() {
		return this.info.getId();
	}

	public String getGameName() {
		return this.info.getGame_name();
	}
	
	public String getSpeakerName() {
		return this.info.getSpeaker_name();
	}
	
	public String getDescription() {
		return this.info.getDescription();
	}

}
