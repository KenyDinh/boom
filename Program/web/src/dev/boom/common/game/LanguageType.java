package dev.boom.common.game;

public enum LanguageType {
	None((byte) 0, "", false),
	Javascript((byte) 1, "js", true),
	Java((byte) 2, "java", false),
	SQL((byte) 3, "sql", false),
	Python((byte) 4, "python", false),
	Linux((byte) 5, "linux", false),
	CPlus((byte) 6, "c++", false),
	;
	
	private byte lang;
	private String name;
	private boolean available;

	private LanguageType(byte lang, String name, boolean available) {
		this.lang = lang;
		this.name = name;
		this.available = available;
	}

	public byte getLang() {
		return lang;
	}
	
	public String getName() {
		return name;
	}

	public boolean isAvailable() {
		return available;
	}

	public static LanguageType valueOf(byte lang) {
		for (LanguageType language : LanguageType.values()) {
			if (language.getLang() == lang && language.isAvailable()) {
				return language;
			}
		}
		return LanguageType.None;
	}
	
	public static LanguageType getLanguageByName(String name) {
		if (name == null) {
			return LanguageType.None;
		}
		for (LanguageType language : LanguageType.values()) {
			if (language.getName().equalsIgnoreCase(name) && language.isAvailable()) {
				return language;
			}
		}
		return LanguageType.None;
	}
}
