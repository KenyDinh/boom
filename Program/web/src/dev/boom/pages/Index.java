package dev.boom.pages;

import dev.boom.core.BoomSession;
import dev.boom.info.AccountInfo;
import dev.boom.services.AccountService;

public class Index extends Template {

	private static final long serialVersionUID = 1L;
	
	private boolean error = false;
	private AccountInfo info;
	public Index() {
	}
	
	@Override
	public void onInit() {
		super.onInit();
		BoomSession boomSession = getBoomSession();
		if (boomSession != null) {
			setRedirect(HomePage.class);
		}
	}
	
	@Override
	public void onPost() {
		super.onPost();
		if (getRedirect() != null) {
			return;
		}
		
		String username = getContext().getRequestParameter("username");
		String password = getContext().getRequestParameter("password");
		if (username == null || password == null) {
			error = true;
			return;
		}
		if (username.isEmpty() || password.isEmpty()) {
			error = true;
			return;
		}
		info = AccountService.getAccount(username, password);
		if (info == null) {
			error = true;
			return;
		}
		storeBoomSession(info);
		setRedirect(HomePage.class);
	}

	@Override
	public void onRender() {
		if (error) {
			addModel("error", "<span style=\"color:red\">Username or Password is incorrect!</span>");
			return;
		}
		if (getRedirect() != null) {
			return;
		}
		super.onRender();
		
	}
	
}
