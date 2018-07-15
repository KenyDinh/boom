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
		headElements.add(new CssImport("/css/lib/bootstrap-darkly.min.css"));
		headElements.add(new JsImport("/js/lib/jquery-3.3.1.min.js"));
		headElements.add(new JsImport("/js/lib/popper-1.14.0.min.js"));
		headElements.add(new JsImport("/js/lib/bootstrap.min.js"));
		headElements.add(new JsImport("/js/common.js"));
		headElements.add(new JsImport("/js/socket.js"));
		
		return headElements;
	}

}
