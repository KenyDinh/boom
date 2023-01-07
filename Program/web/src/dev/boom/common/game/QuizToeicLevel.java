package dev.boom.common.game;

public enum QuizToeicLevel {
	Any((byte) 0, "Any", 0, 990, true),
	Beginner((byte) 1, "Beginner", 10, 300, false),
	HighBeginner((byte) 2, "High Beginner", 305, 400, false),
	Intermediate((byte) 3, "Intermediate", 405, 550, false),
	HighIntermediate((byte) 4, "High Intermediate", 555, 650, false),
	Advanced((byte) 5, "Advanced", 655, 800, false),
	HighAdvanced((byte) 6, "High Advanced", 805, 990, false),
	;
	
	private byte id;
	private String name;
	private int minScore;
	private int maxScore;
	private boolean available;

	private QuizToeicLevel(byte id, String name, int minScore, int maxScore, boolean available) {
		this.id = id;
		this.name = name;
		this.minScore = minScore;
		this.maxScore = maxScore;
		this.available = available;
	}

	public byte getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getMinScore() {
		return minScore;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public boolean isAvailable() {
		return available;
	}

	public static QuizToeicLevel valueOf(byte id) {
		for (QuizToeicLevel toeicLevel : QuizToeicLevel.values()) {
			if (toeicLevel.getId() == id && toeicLevel.isAvailable()) {
				return toeicLevel;
			}
		}
		
		return QuizToeicLevel.Any;
	}
	
}
