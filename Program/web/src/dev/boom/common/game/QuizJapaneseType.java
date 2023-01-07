package dev.boom.common.game;

public enum QuizJapaneseType {
	
	All((byte) 0, "語彙-文法", true),
	Vocabulary((byte) 1, "語彙", true),
	Gramma((byte) 2, "文法", false),
	;
	
	private byte id;
	private String name;
	private boolean avalable;

	private QuizJapaneseType(byte id, String name, boolean avalable) {
		this.id = id;
		this.name = name;
		this.avalable = avalable;
	}

	public byte getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isAvalable() {
		return avalable;
	}
	
	public static QuizJapaneseType valueOf(byte id) {
		for (QuizJapaneseType JPLevel : QuizJapaneseType.values()) {
			if (JPLevel.getId() == id && JPLevel.isAvalable()) {
				return JPLevel;
			}
		}
		
		return QuizJapaneseType.All;
	}
	
}
