package dev.boom.game.card.poker;

public class PokerAction {

	private int id;
	private String message;
	private int minValue;
	private int maxValue;
	private boolean valid;

	public PokerAction(int id) {
		super();
		this.id = id;
		this.message = "";
		this.minValue = 0;
		this.maxValue = 0;
		this.valid = false;
	}

	public PokerAction() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public int getPlayerParam() {
		return getMinValue();
	}

	public void reset() {
		this.message = "";
		this.minValue = 0;
		this.maxValue = 0;
		this.valid = false;
	}
	
}
