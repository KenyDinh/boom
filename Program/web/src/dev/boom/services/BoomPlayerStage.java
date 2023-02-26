package dev.boom.services;

import dev.boom.tbl.info.TblBoomPlayerStageInfo;

public class BoomPlayerStage {
	private TblBoomPlayerStageInfo boomPlayerStageInfo;

	public BoomPlayerStage() {
		boomPlayerStageInfo = new TblBoomPlayerStageInfo();
	}

	public BoomPlayerStage(TblBoomPlayerStageInfo boomPlayerStageInfo) {
		this.boomPlayerStageInfo = boomPlayerStageInfo;
	}

	public TblBoomPlayerStageInfo getBoomPlayerStageInfo() {
		return boomPlayerStageInfo;
	}

	public long getId() {
		return (Long) boomPlayerStageInfo.Get("id");
	}

	public void setId(long id) {
		boomPlayerStageInfo.Set("id", id);
	}

	public long getStageId() {
		return (Long) boomPlayerStageInfo.Get("stage_id");
	}

	public void setStageId(long stageId) {
		boomPlayerStageInfo.Set("stage_id", stageId);
	}

	public long getPlayerId() {
		return (Long) boomPlayerStageInfo.Get("player_id");
	}

	public void setPlayerId(long playerId) {
		boomPlayerStageInfo.Set("player_id", playerId);
	}

	public String getPlayerName() {
		return (String) boomPlayerStageInfo.Get("player_name");
	}

	public void setPlayerName(String playerName) {
		boomPlayerStageInfo.Set("player_name", playerName);
	}

	public long getGroupId() {
		return (Long) boomPlayerStageInfo.Get("group_id");
	}

	public void setGroupId(long groupId) {
		boomPlayerStageInfo.Set("group_id", groupId);
	}

}

