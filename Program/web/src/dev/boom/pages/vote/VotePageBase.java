package dev.boom.pages.vote;

import dev.boom.pages.BoomMainPage;

public class VotePageBase extends BoomMainPage {
	
	
	private static final long serialVersionUID = 1L;
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		return true;
	}
}
