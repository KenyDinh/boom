package dev.boom.common.game;

public enum QuizProgrammingLevel {

	All((byte)0, "All", true),
	Junior((byte)0, "All", false),
	Middle((byte)0, "All", false),
	Senior((byte)0, "All", false),
	;
	
	private byte id;
	private String name;
	private boolean available;

	private QuizProgrammingLevel(byte id, String name, boolean available) {
		this.id = id;
		this.name = name;
		this.available = available;
	}

	public byte getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isAvailable() {
		return available;
	}
	
	public static QuizProgrammingLevel valueOf(byte id) {
		for (QuizProgrammingLevel PrgLevel : QuizProgrammingLevel.values()) {
			if (PrgLevel.getId() == id && PrgLevel.isAvailable()) {
				return PrgLevel;
			}
		}
		
		return QuizProgrammingLevel.All;
	}

}
