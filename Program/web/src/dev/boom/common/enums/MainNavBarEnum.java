package dev.boom.common.enums;

public enum MainNavBarEnum {
	NONE(0,"",""),
	HOME(1, "MSG_MAIN_NAV_BAR_HOME", "home.htm"), 
	MILKTEA(2, "MSG_MAIN_NAV_BAR_MILKTEA", "milktea/milk_tea_menu.htm"), 
	GAME(3, "MSG_MAIN_NAV_BAR_GAME", "game.htm"), 
	TOOLS(4, "MSG_MAIN_NAV_BAR_TOOLS", "tools.htm"),
	GAME_DEMO(5, "MSG_MAIN_NAV_BAR_GAMEDEMO", "game_demo.htm"),
	VOTE(6, "MSG_MAIN_NAV_BAR_VOTE", "vote.htm"),
	ABOUT(7, "MSG_MAIN_NAV_BAR_ABOUT", "about.htm"),
	;

	private int index;
	private String label;
	private String viewPage;

	private MainNavBarEnum(int index, String label, String viewPage) {
		this.index = index;
		this.label = label;
		this.viewPage = viewPage;
	}

	public int getIndex() {
		return index;
	}

	public String getLabel() {
		return label;
	}

	public String getViewPage() {
		return viewPage;
	}

	public static MainNavBarEnum valueOf(int index) {
		for (MainNavBarEnum bar : MainNavBarEnum.values()) {
			if (bar.getIndex() == index) {
				return bar;
			}
		}
		return MainNavBarEnum.NONE;
	}
}
