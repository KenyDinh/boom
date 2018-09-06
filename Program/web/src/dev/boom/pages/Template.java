package dev.boom.pages;

import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

public class Template extends PageBase {

	private static final long serialVersionUID = 1L;

	@Override
	public String getTemplate() {
		return "/template.htm";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new JsImport("/js/common.js"));
		headElements.add(new CssImport("/css/common.css"));
		
		return headElements;
	}

}
