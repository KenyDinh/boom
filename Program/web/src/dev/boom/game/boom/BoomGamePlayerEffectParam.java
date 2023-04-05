package dev.boom.game.boom;

public class BoomGamePlayerEffectParam {
	private long playerId;
	private long groupId;

	public BoomGamePlayerEffectParam() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BoomGamePlayerEffectParam(long playerId, long groupId) {
		super();
		this.playerId = playerId;
		this.groupId = groupId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
}
