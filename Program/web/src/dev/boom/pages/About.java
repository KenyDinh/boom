package dev.boom.pages;

import dev.boom.common.enums.FridayThemes;
import dev.boom.common.enums.MainNavBarEnum;

public class About extends BoomMainPage {

	private static final long serialVersionUID = 1L;

	public About() {
		initTheme(FridayThemes.CLOCK);
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
	protected int getMenuBarIndex() {
		return MainNavBarEnum.ABOUT.getIndex();
	}
	
}
