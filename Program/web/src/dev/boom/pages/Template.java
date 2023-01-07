package dev.boom.pages;

import java.util.List;

import dev.boom.common.enums.FridayThemes;

public class Template extends PageBase {

	private static final long serialVersionUID = 1L;

	protected FridayThemes theme = null;
	@Override
	public String getTemplate() {
		return "/template.htm";
	}

	@Override
	public void onInit() {
		super.onInit();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(importJs("/js/common.js"));
		headElements.add(importCss("/css/common.css"));
		if (theme != null) {
			for (String js : theme.getJsList()) {
				headElements.add(importJs(String.format("/themes/%s/%s", theme.getName(), js)));
			}
			for (String css : theme.getCssList()) {
				headElements.add(importCss(String.format("/themes/%s/%s", theme.getName(), css)));
			}
		}
		return headElements;
	}
	
	@Override
	public void onRender() {
		super.onRender();
		if (this.theme != null) {
			addModel("theme_render", theme.getRenderHtml());
		}
	}

	protected void initTheme(FridayThemes theme) {
		this.theme = theme;
	}
	
}
