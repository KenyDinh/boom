package dev.boom.pages.milktea;

import dev.boom.common.enums.UserFlagEnum;
import dev.boom.core.BoomSession;
import dev.boom.core.GameLog;
import dev.boom.entity.info.MilkTeaUserInfo;
import dev.boom.entity.info.UserInfo;
import dev.boom.pages.PageBase;
import dev.boom.services.MilkTeaUserService;
import dev.boom.services.UserService;

public class MilkTeaAjaxPageBase extends PageBase {

	private static final long serialVersionUID = 1L;
	
	protected UserInfo userInfo = null;
	protected MilkTeaUserInfo milkteaUserInfo = null;
	
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
		BoomSession boomSession = getBoomSession();
		if (boomSession == null) {
			GameLog.getInstance().error("[MilkTeaAjaxPageBase] session is null!");
			return false;
		}
		userInfo = UserService.getUserById(boomSession.getId());
		if (userInfo == null) {
			GameLog.getInstance().error("[MilkTeaAjaxPageBase] user is null!");
			return false;
		}
		if (UserFlagEnum.MILKTEA_BANNED.isValid(userInfo.getFlag())) {
			GameLog.getInstance().error("[MilkTeaAjaxPageBase] user is banned!");
			return false;
		}
		
		return true;
	}
	
	@Override
	public void onInit() {
		super.onInit();
		if (userInfo == null) {
			return;
		}
		milkteaUserInfo = MilkTeaUserService.getFridayUserInfoByUserId(userInfo.getId());
	}
	
}
