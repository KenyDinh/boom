package dev.boom.pages.account;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.enums.UserFlagEnum;
import dev.boom.core.BoomSession;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.UserInfo;
import dev.boom.services.UserService;
import dev.boom.socket.endpoint.FridayEndpoint;

public class Login extends JsonPageBase {

	private static final long serialVersionUID = 1L;
	
	private String token = null;
	
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
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			putJsonData("error", getMessage("MSG_ACCOUNT_INCORRECT_VALUE"));
			return;
		}
		UserInfo info = UserService.getUser(username, password);
		if (info == null) {
			putJsonData("error", getMessage("MSG_ACCOUNT_LOGIN_INCORRECT"));
			return;
		}
		storeBoomSession(info);
		if (UserFlagEnum.ADMINISTRATOR.isValid(info.getFlag())) {
			token = FridayEndpoint.registerToken(info);
			putJsonData("token", token);
		}
		putJsonData("success", 1);
	}

}
