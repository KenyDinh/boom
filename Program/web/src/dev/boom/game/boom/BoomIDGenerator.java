package dev.boom.game.boom;

public class BoomIDGenerator {
	private long bombCount;
	private long itemCount;

	public BoomIDGenerator() {
		super();
	}

	public long getNextBombId() {
		bombCount++;
		return bombCount;
	}

	public long getNextItemId() {
		itemCount++;
		return itemCount;
	}
}
