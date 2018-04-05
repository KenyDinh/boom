package dev.boom.pages;

import dev.boom.core.BoomSession;
import dev.boom.info.AccountInfo;
import dev.boom.services.AccountService;

public class HomePage extends Template {

	private static final long serialVersionUID = 1L;

	private AccountInfo info;
	public HomePage() {
	}

	@Override
	public void onInit() {
		super.onInit();
		BoomSession boomSession = getBoomSession();
		if (boomSession == null) {
			setRedirect(Index.class);
			return;
		}
		info = AccountService.getAccount(boomSession.getId());
		if (info == null) {
			setRedirect(Index.class);
			return;
		}
	}
	
	@Override
	public void onPost() {
		super.onPost();
		if (getRedirect() != null) {
			return;
		}
		if (getContext().getRequestParameter("logout") != null) {
			removeBoomSession();
			setRedirect(Index.class);
		}
	}

	@Override
	public void onRender() {
		super.onRender();
		if (getRedirect() != null) {
			return;
		}
		addModel("account", info);
	}
	
}
