package dev.boom.common.milktea;

public enum MilkTeaTabEnum {

	OPENING_MENU(1, "MSG_MILK_TEA_TAB_MENU","milktea/milk_tea_menu.htm", false), 
	LIST_SHOP(2, "MSG_MILK_TEA_TAB_SHOP","milktea/milk_tea_list_shop.htm", false), 
	RANKING(3, "MSG_MILK_TEA_TAB_RANKING","milktea/milk_tea_ranking.htm", false), 
	ORDER_HISTORY(4, "MSG_MILK_TEA_TAB_ORDER_HISTORY","milktea/milk_tea_order_history.htm", true),
	;

	public static final int MIN_INDEX = OPENING_MENU.getIndex();
	public static final int MAX_INDEX = ORDER_HISTORY.getIndex();
	
	private int index;
	private String label;
	private String viewPage;
	private boolean loginRequire;

	private MilkTeaTabEnum(int index, String label, String viewPage, boolean loginRequire) {
		this.index = index;
		this.label = label;
		this.viewPage = viewPage;
		this.loginRequire = loginRequire;
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

	public boolean isLoginRequire() {
		return loginRequire;
	}

}
