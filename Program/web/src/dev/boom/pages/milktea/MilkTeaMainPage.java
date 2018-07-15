package dev.boom.pages.milktea;

import dev.boom.common.enums.MainNavBarEnum;
import dev.boom.common.milktea.MilkTeaTabEnum;
import dev.boom.core.BoomProperties;
import dev.boom.pages.BoomMainPage;

public class MilkTeaMainPage extends BoomMainPage {

	private static final long serialVersionUID = 1L;
	
	
	public MilkTeaMainPage() {
	}
	
	@Override
	public void onInit() {
		super.onInit();
	}
	
	@Override
	public void onRender() {
		super.onRender();
		initMilkTeaBanner();
	}

	@Override
	protected int getMenuBarIndex() {
		return MainNavBarEnum.MILKTEA.getIndex();
	}
	
	protected int getMilkTeaTabIndex() {
		return MilkTeaTabEnum.OPENING_MENU.getIndex();
	}
	
	private void initMilkTeaBanner() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"row\" style=\"min-height:9.375rem;\" id=\"milktea-intro\" >");
			sb.append(initMilkTeaIntro());
		sb.append("</div>");
		
		sb.append("<div class=\"row\" id=\"milktea-banner\">");
		sb.append("<div class=\"col-lg-12\">");
		
		sb.append("<ul class=\"nav nav-tabs bg-light\" role=\"tablist\">");
		for (MilkTeaTabEnum tab : MilkTeaTabEnum.values()) {
			sb.append("<li class=\"nav-item\">");
				sb.append(String.format("<a class=\"nav-link %s\" href=\"%s\">", (tab.getIndex() == this.getMilkTeaTabIndex() ? "active bg-success" : ""), getHostURL() + getContextPath() + "/" + tab.getViewPage()));
				sb.append(getMessage(tab.getLabel()));
				sb.append("</a>");
			sb.append("</li>");
		}
		sb.append("</ul>");
	
		sb.append("</div>");
		sb.append("</div>");
		
		addModel("milkTeaBanner", sb.toString());
	}
	
	protected String initMilkTeaIntro() {
		return "";
	}
	
	protected String getSocketUrl(String params) {
		int port = getContext().getRequest().getServerPort();
		return "ws://" + BoomProperties.SERVICE_HOSTNAME + (port == 80 ? "" : ":" + port) + getContextPath() + "/socket/milktea" + params;
	}
	
}
