package dev.boom.common.milktea;

public enum MilkTeaTabEnum {

	OPENING_MENU(1, "MSG_MILK_TEA_TAB_MENU","milktea/milk_tea_menu.htm"), 
	LIST_SHOP(2, "MSG_MILK_TEA_TAB_SHOP","milktea/milk_tea_list_shop.htm"), 
	RANKING(3, "MSG_MILK_TEA_TAB_RANKING","milktea/milk_tea_ranking.htm"), 
	OTHER(4, "MSG_MILK_TEA_TAB_OTHER","milktea/milk_tea_other.htm"),
	;

	public static final int MIN_INDEX = OPENING_MENU.getIndex();
	public static final int MAX_INDEX = OTHER.getIndex();
	
	private int index;
	private String label;
	private String viewPage;

	private MilkTeaTabEnum(int index, String label, String viewPage) {
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

}
