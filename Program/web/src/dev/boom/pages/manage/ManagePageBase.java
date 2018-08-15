package dev.boom.pages.manage;

import dev.boom.common.enums.UserFlagEnum;
import dev.boom.core.BoomProperties;
import dev.boom.core.BoomSession;
import dev.boom.core.GameLog;
import dev.boom.pages.Home;
import dev.boom.services.UserService;
import dev.boom.socket.endpoint.ManageMilkTeaEndPoint;
import dev.boom.tbl.info.TblUserInfo;

public class ManagePageBase extends ManageTemplate {

	private static final long serialVersionUID = 1L;

	protected TblUserInfo userInfo = null;
	
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
	
	protected void addHomeBackLink() {
		StringBuilder sb = new StringBuilder();
		sb.append("<p style=\"margin-top:1rem;\">");
		sb.append("<a href=\"").append(getPagePath(Home.class)).append("\">").append(getMessage("MSG_MAIN_NAV_BAR_HOME")).append("</a>");
		sb.append("</p>");
		addModel("home", sb.toString());
	}
	
	protected String getSocketUrl(String params) {
		if (!params.startsWith("?")) {
			params = "?" + params;
		}
		int port = getContext().getRequest().getServerPort();
		return "ws://" + BoomProperties.SERVICE_HOSTNAME + (port == 80 ? "" : ":" + port) + getContextPath() + ManageMilkTeaEndPoint.SOCKET_PATH + params;
	}
	
}
