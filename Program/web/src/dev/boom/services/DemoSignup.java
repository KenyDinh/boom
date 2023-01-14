package dev.boom.services;

import dev.boom.tbl.info.TblDemoSignupInfo;

public class DemoSignup {
	private TblDemoSignupInfo demoSignupInfo;

	public DemoSignup() {
		demoSignupInfo = new TblDemoSignupInfo();
	}

	public DemoSignup(TblDemoSignupInfo demoSignupInfo) {
		this.demoSignupInfo = demoSignupInfo;
	}

	public TblDemoSignupInfo getDemoSignupInfo() {
		return demoSignupInfo;
	}

	public int getId() {
		return (Integer) demoSignupInfo.Get("id");
	}

	public void setId(int id) {
		demoSignupInfo.Set("id", id);
	}

	public String getGameName() {
		return (String) demoSignupInfo.Get("game_name");
	}

	public void setGameName(String gameName) {
		demoSignupInfo.Set("game_name", gameName);
	}

	public String getSpeakerName() {
		return (String) demoSignupInfo.Get("speaker_name");
	}

	public void setSpeakerName(String speakerName) {
		demoSignupInfo.Set("speaker_name", speakerName);
	}

	public String getDescription() {
		return (String) demoSignupInfo.Get("description");
	}

	public void setDescription(String description) {
		demoSignupInfo.Set("description", description);
	}

	public int getFlag() {
		return (Integer) demoSignupInfo.Get("flag");
	}

	public void setFlag(int flag) {
		demoSignupInfo.Set("flag", flag);
	}

}

