package dev.boom.pages;

import java.util.List;

import org.apache.click.Page;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.core.BoomSession;
import dev.boom.info.AccountInfo;

public class Template extends Page {

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
		headElements.add(new CssImport("/css/template.css"));
		headElements.add(new JsImport("/js/lib/jquery-3.3.1.min.js"));
		
		return headElements;
	}
	
	public void storeBoomSession(AccountInfo info) {
		BoomSession boomSession = new BoomSession(info.getId());
		getContext().getSession().setAttribute("boom_session", boomSession);
	}
	
	public BoomSession getBoomSession() {
		return (BoomSession)getContext().getSession().getAttribute("boom_session");
	}
	
	public void removeBoomSession() {
		getContext().getSession().removeAttribute("boom_session");
	}

}
