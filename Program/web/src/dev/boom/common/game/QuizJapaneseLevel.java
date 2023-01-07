package dev.boom.common.game;

public enum QuizJapaneseLevel {
	
	All((byte) 0, "All", true),
	N5((byte) 1, "N5", true),
	N4((byte) 2, "N4", true),
	N3((byte) 3, "N3", true),
	N2((byte) 4, "N2", false),
	N1((byte) 5, "N1", false),
	;
	
	private byte level;
	private String name;
	private boolean avalable;

	private QuizJapaneseLevel(byte level, String name, boolean avalable) {
		this.level = level;
		this.name = name;
		this.avalable = avalable;
	}

	public byte getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public boolean isAvalable() {
		return avalable;
	}
	
	public static QuizJapaneseLevel valueOf(byte level) {
		for (QuizJapaneseLevel JPLevel : QuizJapaneseLevel.values()) {
			if (JPLevel.getLevel() == level && JPLevel.isAvalable()) {
				return JPLevel;
			}
		}
		
		return QuizJapaneseLevel.All;
	}
	
	public static QuizJapaneseLevel getJapaneseLevelByName(String name) {
		if (name == null) {
			return QuizJapaneseLevel.All;
		}
		for (QuizJapaneseLevel JPLevel : QuizJapaneseLevel.values()) {
			if (JPLevel.getName().equalsIgnoreCase(name)) {
				return JPLevel;
			}
		}
		
		return QuizJapaneseLevel.All;
	}
	
}
