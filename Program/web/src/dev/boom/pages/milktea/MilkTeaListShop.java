package dev.boom.pages.milktea;

import dev.boom.common.milktea.MilkTeaTabEnum;

public class MilkTeaListShop extends MilkTeaMainPage {

	private static final long serialVersionUID = 1L;

	public MilkTeaListShop() {
	}

	@Override
	public void onInit() {
		super.onInit();
	}

	@Override
	public void onRender() {
		super.onRender();
	}

	@Override
	protected int getMilkTeaTabIndex() {
		return MilkTeaTabEnum.LIST_SHOP.getIndex();
	}
	
}
