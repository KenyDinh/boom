package dev.boom.pages.milktea;

import dev.boom.common.enums.UserFlagEnum;
import dev.boom.core.GameLog;
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
		if (!initUserInfo()) {
			return false;
		}
		if (!UserFlagEnum.ACTIVE.isValid(userInfo.getFlag())) {
			GameLog.getInstance().error("[MilkTeaAjaxPageBase] user is not active yet!");
			return false;
		}
		if (UserFlagEnum.MILKTEA_BANNED.isValid(userInfo.getFlag())) {
			GameLog.getInstance().error("[MilkTeaAjaxPageBase] user is banned!");
			return false;
		}
		
		return true;
	}
	
}
