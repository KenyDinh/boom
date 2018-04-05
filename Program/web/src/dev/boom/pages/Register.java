package dev.boom.pages;

import dev.boom.core.BoomSession;
import dev.boom.services.AccountService;

public class Register extends Template {

	private static final long serialVersionUID = 1L;

	private boolean error = false;

	public Register() {
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
		if (AccountService.createAccount(username, password)) {
			setRedirect(Index.class);
			return;
		}
		error = true;
	}

	@Override
	public void onRender() {
		super.onRender();
		if (error) {
			addModel("error", "<span style=\"color:red\">Regist account failed!</span>");
			return;
		}
		if (getRedirect() != null) {
			return;
		}
	}
}
