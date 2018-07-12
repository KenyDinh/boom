package dev.boom.pages.account;

import java.util.Enumeration;

import dev.boom.core.BoomSession;
import dev.boom.entity.info.UserInfo;
import dev.boom.pages.PageBase;
import dev.boom.services.UserService;

public class Register extends PageBase {

	private static final long serialVersionUID = 1L;

	private boolean error = false;
	
	public Register() {
	}
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!getContext().isAjaxRequest()) {
			return false;
		}
		if (!getContext().isPost()) {
			return false;
		}
		Enumeration<String> headerNames = getContext().getRequest().getHeaderNames();
		while (headerNames.hasMoreElements()) {
			System.out.println(headerNames.nextElement());
		}
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
	}
	
	@Override
	public void onPost() {
		super.onPost();
		BoomSession boomSession = getBoomSession();
		if (boomSession != null) {
			return;
		}
		String username = getContext().getRequestParameter("username");
		String password = getContext().getRequestParameter("password");
		String rePassword = getContext().getRequestParameter("re_password");
		if (username == null || password == null || rePassword == null) {
			error = true;
			return;
		}
		if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty() || !password.equals(rePassword)) {
			error = true;
			return;
		}
		if (!username.matches("[a-z]+\\d?\\.[a-z]+") || username.length() > 32) {
			System.out.println("username is not matches!");
			error = true;
			return;
		}
		UserInfo existUser = UserService.getUserByName(username);
		if (existUser != null) {
			error = true;
			return;
		}
		if (!UserService.createUser(username, password)) {
			error = true;
			return;
		}
	}
	
	@Override
	public void onRender() {
		if (error) {
			addModel("result", String.format("{\"error\":\"%s\"}", getMessage("MSG_LOGIN_INCORRECT")));
			return;
		}
		addModel("result", "{\"success\":1}");
	}
}
