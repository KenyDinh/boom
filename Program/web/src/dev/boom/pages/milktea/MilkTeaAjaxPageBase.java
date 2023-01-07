package dev.boom.pages.milktea;

import dev.boom.pages.JsonPageBase;

public class MilkTeaAjaxPageBase extends JsonPageBase {

	private static final long serialVersionUID = 1L;
	
	public MilkTeaAjaxPageBase() {
	}

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!getContext().isAjaxRequest()) {
			return false;
		}
		
		return true;
	}
	
}
