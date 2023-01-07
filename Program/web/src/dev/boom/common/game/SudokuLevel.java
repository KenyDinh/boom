package dev.boom.common.game;

public enum SudokuLevel {

	EASY((byte)1, "Easy"),
	MEDIUM((byte)2, "Medium"),
	HARD((byte)3, "Hard"),
	EVIL((byte)4, "Evil"),
	;

	private byte level;
	private String name;

	private SudokuLevel(byte level, String name) {
		this.level = level;
		this.name = name;
	}

	public byte getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public static SudokuLevel valueOf(byte level) {
		for (SudokuLevel sudokuLevel : SudokuLevel.values()) {
			if (sudokuLevel.getLevel() == level) {
				return sudokuLevel;
			}
		}
		
		return SudokuLevel.EASY;
	}
}
