package dev.boom.services;

import dev.boom.common.enums.EventFlagEnum;
import dev.boom.common.game.GameTypeEnum;
import dev.boom.common.tools.ToolsEnum;
import dev.boom.tbl.info.TblWorldInfo;

public class World {
	private TblWorldInfo worldInfo;

	public World() {
		worldInfo = new TblWorldInfo();
	}

	public World(TblWorldInfo worldInfo) {
		this.worldInfo = worldInfo;
	}

	public TblWorldInfo getWorldInfo() {
		return worldInfo;
	}

	public long getId() {
		return (Long) worldInfo.Get("id");
	}

	public void setId(long id) {
		worldInfo.Set("id", id);
	}

	public int getEventFlag() {
		return (Integer) worldInfo.Get("event_flag");
	}

	public void setEventFlag(int eventFlag) {
		worldInfo.Set("event_flag", eventFlag);
	}

	public int getGameFlag() {
		return (Integer) worldInfo.Get("game_flag");
	}

	public void setGameFlag(int gameFlag) {
		worldInfo.Set("game_flag", gameFlag);
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
	
	public void addGameFlag(int index) {
		if (index <= 0) {
			return;
		}
		setGameFlag(getGameFlag() | (1 << (index - 1)));
	}
	
	public void addGameFlag(GameTypeEnum gameTypeEnum) {
		addGameFlag(gameTypeEnum.getIndex());
	}
	
	public boolean isActiveGameFlag(int index) {
		if (index <= 0) {
			return false;
		}
		return (getGameFlag() & (1 << (index - 1))) > 0;
	}
	
	public boolean isActiveGameFlag(GameTypeEnum gameTypeEnum) {
		return isActiveGameFlag(gameTypeEnum.getIndex());
	}
	
	public boolean isActiveToolsFlag(int index) {
		return true;
	}
	
	public boolean isActiveToolsFlag(ToolsEnum tool) {
		return true;
	}
	
}

