package dev.boom.game.boom;

public class BoomGameRevivalParam {
	private long revivalId;
	private int revivalGauge;
	private String revivalCheck;
	private int revivalCount;
	private long startTime;
	private BoomPlayer lastRevival;
	private boolean rankingCheckFlag;

	public BoomGameRevivalParam() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BoomGameRevivalParam(long revivalId, int revivalGauge, String revivalCheck, int revivalCount,
			long startTime) {
		super();
		this.revivalId = revivalId;
		this.revivalGauge = revivalGauge;
		this.revivalCheck = revivalCheck;
		this.revivalCount = revivalCount;
		this.startTime = startTime;
		this.rankingCheckFlag = false;
		
	}

	public void reset() {
		this.revivalId = 0;
		this.revivalGauge = 0;
		this.revivalCheck = null;
		this.revivalCount = 0;
		this.startTime = 0;
	}

	public void init(BoomPlayer player) {
		this.revivalId = player.getId();
		this.revivalGauge = 0;
		this.revivalCheck = makeCheckString(player);
		this.revivalCount = 0;
		this.startTime = System.nanoTime();
	}

	public boolean check(BoomPlayer player) {
		if (this.revivalCheck == null || !this.revivalCheck.equals(makeCheckString(player))) {
			return false;
		}
		return true;
	}

	private String makeCheckString(BoomPlayer player) {
		return String.format("r_%d_%d_%d", player.getId(), player.getX(), player.getY());
	}

	public void incRevivalGauge() {
		long currentTime = System.nanoTime();
		if (startTime == 0) {
			startTime = currentTime;
			return;
		}
		long passTime = currentTime - this.startTime;
		if (passTime <= 0) {
			return;
		}
		long checkGauge = ((1000 * passTime) / (BoomGameManager.NANO_SECOND * BoomGameManager.REVIVAL_TIME_REQUIRED));
		if (checkGauge > 1000) {
			this.revivalGauge = 1000;
		} else if (checkGauge < 0) {
			this.revivalGauge = 0;
		} else {
			this.revivalGauge = (int)checkGauge;
		}
	}
	
	public boolean isGaugeMax() {
		return (this.revivalGauge >= 1000);
	}

	public void incRevivalCount() {
		this.revivalCount += 1;
	}

	public long getRevivalId() {
		return revivalId;
	}

	public void setRevivalId(long revivalId) {
		this.revivalId = revivalId;
	}

	public int getRevivalGauge() {
		return revivalGauge;
	}

	public void setRevivalGauge(int revivalGauge) {
		this.revivalGauge = revivalGauge;
	}

	public String getRevivalCheck() {
		return revivalCheck;
	}

	public void setRevivalCheck(String revivalCheck) {
		this.revivalCheck = revivalCheck;
	}

	public int getRevivalCount() {
		return revivalCount;
	}

	public void setRevivalCount(int revivalCount) {
		this.revivalCount = revivalCount;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public BoomPlayer getLastRevival() {
		return lastRevival;
	}

	public void setLastRevival(BoomPlayer lastRevival) {
		this.lastRevival = lastRevival;
	}
	
	public boolean checkLastRevival(BoomPlayer target) {
		if (this.lastRevival == null) {
			return false;
		}
		return this.lastRevival.equals(target);
	}
	
	public void clearLastRevival() {
		this.lastRevival = null;
	}

	public boolean isRankingCheckFlag() {
		return rankingCheckFlag;
	}

	public void setRankingCheckFlag(boolean rankingCheckFlag) {
		this.rankingCheckFlag = rankingCheckFlag;
	}

}
