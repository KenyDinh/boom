package dev.boom.pages.account;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonMethod;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.AuthToken;
import dev.boom.services.AuthTokenService;

public class ChangePassword extends JsonPageBase {

	private static final long serialVersionUID = 1L;
	
	public ChangePassword() {
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
		if (!initUserInfo()) {
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
		String cur_password = getContext().getRequestParameter("cur_password");
		String new_password = getContext().getRequestParameter("new_password");
		String re_new_password = getContext().getRequestParameter("re_new_password");
		if (StringUtils.isBlank(cur_password) || StringUtils.isBlank(new_password) || StringUtils.isBlank(re_new_password)) {
			putJsonData("error", getMessage("MSG_ACCOUNT_INCORRECT_VALUE"));
			return;
		}
		if (!userInfo.getPassword().equals(CommonMethod.getEncryptMD5(cur_password))) {
			putJsonData("error", getMessage("MSG_ACCOUNT_CURRENT_PWD_INCORRECT"));
			return;
		}
		if (!new_password.equals(re_new_password)) {
			putJsonData("error", getMessage("MSG_ACCOUNT_CONFIRM_PWD_INCORRECT"));
			return;
		}
		userInfo.setPassword(CommonMethod.getEncryptMD5(new_password));
		if (CommonDaoFactory.Update(userInfo.getUserInfo()) < 0) {
			GameLog.getInstance().error("[ChangePassword] update failed!");
			putJsonData("error", getMessage("MSG_GENERAL_ERROR"));
			return;
		}
		
		boolean remember = false;
		Cookie[] cookies = getContext().getRequest().getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(AuthTokenService.USER_TOKEN_KEY)) {
					AuthToken authToken = AuthTokenService.getAuthToken(cookie.getValue());
					if (authToken != null) {
						remember = true;
					}
					break;
				}
			}
		}
		
		if (!AuthTokenService.deleteAllUserToken(userInfo)) {
			GameLog.getInstance().error("Delete all token fail!");
		}
		if (remember) {
			AuthToken authToken = AuthTokenService.generateAuthToken(userInfo);
			if (authToken != null) {
				putJsonData("remember_me", AuthTokenService.getTokenValidator(authToken));
				putJsonData("expired", AuthTokenService.TOKEN_EXPIRED_DAY);
			}
		}
		putJsonData("success", 1);
	}

}
