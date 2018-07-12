package dev.boom.pages.account;

import dev.boom.common.enums.UserFlagEnum;
import dev.boom.core.BoomSession;
import dev.boom.entity.info.UserInfo;
import dev.boom.pages.PageBase;
import dev.boom.services.UserService;
import dev.boom.socket.endpoint.FridayEndpoint;

public class Login extends PageBase {

	private static final long serialVersionUID = 1L;
	
	private boolean error = false;
	public Login() {
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
		if (username == null || password == null) {
			error = true;
			return;
		}
		if (username.isEmpty() || password.isEmpty()) {
			error = true;
			return;
		}
		UserInfo info = UserService.getUser(username, password);
		if (info == null) {
			error = true;
			return;
		}
		storeBoomSession(info);
		if (UserFlagEnum.ADMINISTRATOR.isValid(info.getFlag())) {
			getContext().getSession().setMaxInactiveInterval(300);
			FridayEndpoint.registerToken(info);
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
