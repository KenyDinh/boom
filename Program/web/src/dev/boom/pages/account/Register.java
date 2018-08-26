package dev.boom.pages.account;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.enums.EventFlagEnum;
import dev.boom.core.BoomSession;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.UserService;
import dev.boom.tbl.info.TblUserInfo;

public class Register extends JsonPageBase {

	private static final long serialVersionUID = 1L;

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
		if (!worldInfo.isActiveEventFlag(EventFlagEnum.REGISTRATON)) {
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
		String rePassword = getContext().getRequestParameter("re_password");
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(rePassword)) {
			putJsonData("error", getMessage("MSG_ACCOUNT_INCORRECT_VALUE"));
			return;
		}
		if (!username.matches("[a-z]+\\d?\\.[a-z]+") || username.length() > 32) {
			putJsonData("error", getMessage("MSG_ACCOUNT_USERNAME_INVALID"));
			return;
		}
		if (!password.equals(rePassword)) {
			putJsonData("error", getMessage("MSG_ACCOUNT_CONFIRM_PWD_INCORRECT"));
			return;
		}
		TblUserInfo existUser = UserService.getUserByName(username);
		if (existUser != null) {
			putJsonData("error", getMessage("MSG_ACCOUNT_USERNAME_NOT_AVAILABLE"));
			return;
		}
		if (!UserService.createUser(username, password)) {
			putJsonData("error", getMessage("MSG_GENERAL_ERROR"));
			return;
		}
		putJsonData("success", 1);
	}
	
}
