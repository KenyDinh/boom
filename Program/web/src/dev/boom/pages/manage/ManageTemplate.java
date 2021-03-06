package dev.boom.pages.manage;

import java.util.List;

import org.apache.click.element.JsImport;

import dev.boom.pages.PageBase;

public class ManageTemplate extends PageBase {

	private static final long serialVersionUID = 1L;

	@Override
	public String getTemplate() {
		return "/manage/manage_template.htm";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new JsImport("/js/manage/common.js"));
		headElements.add(new JsImport("/js/socket.js"));
		
		return headElements;
	}
}
