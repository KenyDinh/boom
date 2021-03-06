package dev.boom.pages;

import dev.boom.common.CommonHtmlFunc;
import dev.boom.common.enums.EventFlagEnum;
import dev.boom.common.enums.MainNavBarEnum;
import dev.boom.common.enums.UserFlagEnum;
import dev.boom.core.BoomSession;
import dev.boom.pages.manage.Index;
import dev.boom.services.UserInfo;
import dev.boom.services.UserService;
import dev.boom.socket.SocketSessionPool;
import dev.boom.socket.endpoint.FridayEndpoint;

public class BoomMainPage extends Template {

	private static final long serialVersionUID = 1L;
	protected UserInfo userInfo = null;
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		BoomSession boomSession = getBoomSession();
		if (boomSession != null) {
			userInfo = UserService.getUserById(boomSession.getId());
		}
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
		
		// re-init friday socket token
		if (userInfo != null && UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag())) {
			if (!SocketSessionPool.isExistToken(FridayEndpoint.ENDPOINT_NAME, userInfo)) {
				SocketSessionPool.generateValidToken(FridayEndpoint.ENDPOINT_NAME, userInfo);
			}
		}
	}
	
	@Override
	public void onRender() {
		super.onRender();
		initMenuBar();
	}
	
	protected UserInfo getUserInfo() {
		return userInfo;
	}

	private void initMenuBar() {
		String contextPath = getHostURL() + getContextPath();
		StringBuffer sb = new StringBuffer();
		sb.append("<nav id=\"main-nav-bar\" class=\"navbar navbar-expand-lg navbar-dark bg-primary fixed-top\" style=\"padding:0.625rem;\">");
			sb.append("<div class=\"container\" style=\"max-width:113.75rem;\" >");
			sb.append("<a class=\"navbar-brand\" href=\"#\">");
			sb.append(String.format("<img src=\"%s\" style=\"width:75%%;\" alt=\"friday\" title=\"friday\" />", contextPath + "/img/page/friday.png"));
			sb.append("</a>");
			
			sb.append("<button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarColor02\" aria-controls=\"navbarColor02\" aria-expanded=\"false\" aria-label=\"Toggle navigation\" style=\"\">");
				sb.append("<span class=\"navbar-toggler-icon\"></span>");
			sb.append("</button>");
			
			sb.append("<div class=\"collapse navbar-collapse\" id=\"navbarColor02\">");
				sb.append("<ul class=\"navbar-nav mr-auto\">");
					for (MainNavBarEnum bar : MainNavBarEnum.values()) {
						if (bar == MainNavBarEnum.NONE) {
							continue;
						}
						boolean current = (boolean) (bar.getIndex() == this.getMenuBarIndex());
						sb.append(String.format("<li class=\"nav-item %s\">", (current ? "active" : "")));
							sb.append(String.format("<a class=\"nav-link\" href=\"%s\" >", contextPath + "/" + bar.getViewPage()));
							sb.append(getMessage(bar.getLabel()));
							if (bar.getIndex() == this.getMenuBarIndex()) {
								sb.append("<span class=\"sr-only\">(current)</span>");
							}
							sb.append("</a>");
						sb.append("</li>");
					}
				sb.append("</ul>");
				if (userInfo != null) {
					sb.append("<form class=\"form-inline my-2 my-lg-0\">");
						sb.append("<div class=\"my-2 my-sm-0\">");
							sb.append(userInfo.getUsername());
						sb.append("</div>");
						sb.append("<div class=\"my-2 my-sm-0 dropdown\" style=\"margin-left:1rem;\">");
							sb.append(String.format("<img src=\"%s\" style=\"cursor:pointer;width:48px;\" data-toggle=\"dropdown\"/>", contextPath + "/img/page/common-user.png"));
							sb.append("<div class=\"dropdown-menu\" id=\"user-operate-menu\">");
								sb.append("<a class=\"dropdown-item\" href=\"#\">" + getMessage("MSG_GENERAL_MY_PROFILE") + "</a>");
								sb.append("<a class=\"dropdown-item\" href=\"#\">" + getMessage("MSG_GENERAL_MY_ACCOUNT") + "</a>");
								sb.append("<a class=\"dropdown-item\" href=\"#\" data-toggle=\"modal\" data-target=\"#change-password-modal\">" + getMessage("MSG_ACCOUNT_CHANGE_PASSWORD") + "</a>");
								if (UserFlagEnum.ADMINISTRATOR.isValid(userInfo.getFlag())) {
									sb.append("<div class=\"dropdown-divider\"></div>");
									sb.append("<a class=\"dropdown-item\" href=\"" + getHostURL() + getPagePath(Index.class) + "\">" + getMessage("MSG_GENERAL_ADMIN_PAGE") + "</a>");
								}
								sb.append("<div class=\"dropdown-divider\"></div>");
								sb.append("<a class=\"dropdown-item\" href=\"#\" id=\"logout\">" + getMessage("MSG_GENERAL_LOGOUT") + "</a>");
							sb.append("</div>");
						sb.append("</div>");
					sb.append("</form>");
					addModel("change_pwd_modal", CommonHtmlFunc.getChangePasswordFormModal(contextPath, getMessages()));
					addModel("logoutForm", getLogoutForm());
				} else {
					sb.append("<form class=\"form-inline my-2 my-lg-0\">");
						sb.append("<button style=\"width:80px;\" class=\"btn btn-info my-2 my-sm-0\" type=\"button\" data-toggle=\"modal\" data-target=\"#login-form-modal\">" + getMessage("MSG_GENERAL_LOGIN") + "</button>");
						if (worldInfo != null && worldInfo.isActiveEventFlag(EventFlagEnum.REGISTRATON)) {
							sb.append("<button style=\"width:80px;margin-left:1rem;\" class=\"btn btn-success my-2 my-sm-0\" type=\"button\" data-toggle=\"modal\" data-target=\"#regist-form-modal\">" + getMessage("MSG_GENERAL_SIGNUP") + "</button>");
							addModel("register_modal", CommonHtmlFunc.getRegisterFormModal(contextPath, getMessages()));
						}
					sb.append("</form>");
					addModel("login_modal", CommonHtmlFunc.getLoginFormModal(contextPath, getMessages()));
				}
				
			sb.append("</div>");
			sb.append("</div>");
		sb.append("</nav>");
		addModel("menu_bar", sb.toString());
	}
	
	protected int getMenuBarIndex() {
		return 0;
	}
	
	private String getLogoutForm() {
		StringBuilder sb = new StringBuilder();
		sb.append("<form id=\"logout-form\" method=\"post\" action=\"");
		sb.append(getHostURL() + getContextPath() + "/" + MainNavBarEnum.HOME.getViewPage());
		sb.append("\">");
		sb.append("<input type=\"hidden\" name=\"form_name\" value=\"logout\"/>");
		sb.append("<input type=\"hidden\" name=\"index\" value=\"").append(getMenuBarIndex()).append("\"/>");
		sb.append("</form>");
		return sb.toString();
	}
}
