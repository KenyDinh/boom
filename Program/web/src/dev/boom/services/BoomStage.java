package dev.boom.services;

import dev.boom.tbl.info.TblBoomStageInfo;

public class BoomStage {
	private TblBoomStageInfo boomStageInfo;

	public BoomStage() {
		boomStageInfo = new TblBoomStageInfo();
	}

	public BoomStage(TblBoomStageInfo boomStageInfo) {
		this.boomStageInfo = boomStageInfo;
	}

	public TblBoomStageInfo getBoomStageInfo() {
		return boomStageInfo;
	}

	public long getId() {
		return (Long) boomStageInfo.Get("id");
	}

	public void setId(long id) {
		boomStageInfo.Set("id", id);
	}

	public long getSeasonId() {
		return (Long) boomStageInfo.Get("season_id");
	}

	public void setSeasonId(long seasonId) {
		boomStageInfo.Set("season_id", seasonId);
	}

	public String getName() {
		return (String) boomStageInfo.Get("name");
	}

	public void setName(String name) {
		boomStageInfo.Set("name", name);
	}

	public int getMaxPlayer() {
		return (Integer) boomStageInfo.Get("max_player");
	}

	public void setMaxPlayer(int maxPlayer) {
		boomStageInfo.Set("max_player", maxPlayer);
	}

}

