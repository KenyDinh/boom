package dev.boom.game.boom;

public enum BoomImageEnum {

	NONE(0, "", "", 0, 0, 0, 0),;

	private int id;
	private String url;
	private String state;
	private int width;
	private int height;
	private int maxFrame;
	private int maxFrameHold;

	private BoomImageEnum(int id, String url, String state, int width, int height, int maxFrame, int maxFrameHold) {
		this.id = id;
		this.url = url;
		this.state = state;
		this.width = width;
		this.height = height;
		this.maxFrame = maxFrame;
		this.maxFrameHold = maxFrameHold;
	}

	public int getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getState() {
		return state;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getMaxFrame() {
		return maxFrame;
	}

	public int getMaxFrameHold() {
		return maxFrameHold;
	}

}
