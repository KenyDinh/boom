package dev.boom.pages.manage;

import org.apache.click.Page;

import dev.boom.common.enums.UserFlagEnum;
import dev.boom.core.BoomSession;
import dev.boom.core.GameLog;
import dev.boom.pages.Home;
import dev.boom.services.UserInfo;
import dev.boom.services.UserService;

public class ManagePageBase extends ManageTemplate {

	private static final long serialVersionUID = 1L;

	protected UserInfo userInfo = null;
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			setRedirect(Home.class);
			return false;
		}
		BoomSession session = getBoomSession();
		if (session == null) {
			GameLog.getInstance().error("[ManagePageBase] session is null!");
			setRedirect(Home.class);
			return false;
		}
		userInfo = UserService.getUserById(session.getId());
		if (userInfo == null) {
			GameLog.getInstance().error("[ManagePageBase] user is null!");
			setRedirect(Home.class);
			return false;
		}
		if (!UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag())) {
			GameLog.getInstance().error("[ManagePageBase] user is invalid!");
			getContext().getSession().invalidate();
			setRedirect(Home.class);
			return false;
		}
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
	}

	@Override
	public void onRender() {
		super.onRender();
	}
	
	protected void addBackLink(Class<? extends Page> clazz, String label) {
		if (label == null) {
			label = "MSG_GENERAL_BACK";
		}
		if (clazz == null) {
			clazz = Home.class;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<p style=\"margin-top:1rem;\">");
		sb.append("<a href=\"").append(getPagePath(clazz)).append("\">").append(getMessage(label)).append("</a>");
		sb.append("</p>");
		addModel("home", sb.toString());
	}
	
}
