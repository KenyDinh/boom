package dev.boom.game.boom;

public enum BoomGameStage {
	NONE("Select Stage", true),
	GROUP_A("Group A", true),
	GROUP_B("Group B", true),
	GROUP_C("Group C", true),
	GROUP_D("Group D", true),
	GROUP_E("Group E", false),
	GROUP_F("Group F", false),
	GROUP_G("Group G", false),
	GROUP_H("Group H", false),
	QUARTERFINAL_1("Quarterfinal 1", false),
	QUARTERFINAL_2("Quarterfinal 2", false),
	QUARTERFINAL_3("Quarterfinal 3", false),
	QUARTERFINAL_4("Quarterfinal 4", false),
	SEMIFINAL_1("Semifinal 1", true),
	SEMIFINAL_2("Semifinal 2", true),
	FINAL("Final", true),
	;
	
	private String name;
	private boolean avaiable;
	
	private BoomGameStage(String name, boolean avaiable) {
		this.name = name;
		this.avaiable = avaiable;
	}

	public String getName() {
		return name;
	}

	public boolean isAvaiable() {
		return avaiable;
	}
	
}
