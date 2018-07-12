package dev.boom.pages.milktea;

import dev.boom.common.milktea.MilkTeaTabEnum;

public class MilkTeaRanking extends MilkTeaMainPage {

	private static final long serialVersionUID = 1L;

	public MilkTeaRanking() {
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
		return MilkTeaTabEnum.RANKING.getIndex();
	}

}
