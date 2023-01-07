package dev.boom.core;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

public class BoomResource{

	private BoomResource() {
	}
	
	public static CssImport initCss(String href) {
		if (href == null) {
			return new CssImport();
		}
		href += "?" + BoomProperties.FRIDAY_VERSION;
		return new CssImport(href);
	}
	
	public static JsImport initJs(String href) {
		if (href == null) {
			return new JsImport();
		}
		href += "?" + BoomProperties.FRIDAY_VERSION;
		return new JsImport(href);
	}
}
