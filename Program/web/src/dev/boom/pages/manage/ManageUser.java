package dev.boom.pages.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.click.element.JsImport;
import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.Department;
import dev.boom.common.enums.ManageLogType;
import dev.boom.common.enums.UserFlagEnum;
import dev.boom.common.enums.UserRole;
import dev.boom.core.GameLog;
import dev.boom.pages.Home;
import dev.boom.services.CommonDaoService;
import dev.boom.services.ManageLogService;
import dev.boom.services.UserInfo;
import dev.boom.services.UserService;

public class ManageUser extends ManagePageBase {

	private static final long serialVersionUID = 1L;

	private UserInfo selectUser = null;
	private boolean update;

	public ManageUser() {
		setDataTableFormat(true);
	}

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			setRedirect(Home.class);
			return false;
		}
		if (userInfo == null || !userInfo.isAdministrator()) {
			setRedirect(Home.class);
			return false;
		}
		return true;
	}
	
	@Override
	public void onInit() {
		super.onInit();

		String strUID = getContext().getRequestParameter("user_id");
		if (CommonMethod.isValidNumeric(strUID, 1, Long.MAX_VALUE)) {
			selectUser = UserService.getUserById(Long.parseLong(strUID));
		} else {
			String strName = getContext().getRequestParameter("username");
			if (strName != null && strName.length() <= CommonDefine.MAX_LENGTH_USERNAME && strName.matches("[a-z]+\\d?\\.[a-z]+")) {
				selectUser = UserService.getUserByName(strName);
			}
		}
		String strUpdate = getContext().getRequestParameter("update");
		if (CommonMethod.isValidNumeric(strUpdate, 1, 1)) {
			update = true;
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		if (!update) {
			return;
		}
		Map<String, String[]> params = getContext().getRequest().getParameterMap();
		if (params == null || params.isEmpty()) {
			return;
		}
		// flag
		String[] flags = params.get("user_flag");
		int flag = 0;
		if (flags != null) {
			for (String strflag : flags) {
				if (CommonMethod.isValidNumeric(strflag, 1, Integer.MAX_VALUE)) {
					UserFlagEnum userFlag = UserFlagEnum.valueOf(Integer.valueOf(strflag));
					if (userFlag == UserFlagEnum.INVALID) {
						continue;
					}
					flag = userFlag.setFlag(flag);
				}
			}
		}
		//dept
		String[] arrDept = params.get("user_dept");
		int dept = 0;
		if (arrDept != null) {
			for (String strflag : arrDept) {
				if (CommonMethod.isValidNumeric(strflag, 1, Integer.MAX_VALUE)) {
					Department department = Department.valueOf(Integer.valueOf(strflag));
					if (department == Department.NONE) {
						continue;
					}
					dept |= department.getFlag();
				}
			}
		}
		//role
		int role = 0;
		String strRole = getContext().getRequestParameter("user_role");
		if (CommonMethod.isValidNumeric(strRole, 0, Integer.MAX_VALUE)) {
			role = UserRole.valueOf(Integer.parseInt(strRole)).getRole();
		}
		// employee ID
		String strEmpID = getContext().getRequestParameter("empid");
		if (StringUtils.isBlank(strEmpID)) {
			strEmpID = "";
		} else {
			strEmpID = strEmpID.trim();
		}
		// full name
		String strFullName = getContext().getRequestParameter("fullname");
		if (StringUtils.isBlank(strFullName)) {
			strFullName = "";
		} else {
			strFullName = strFullName.trim();
		}
		//
		if (selectUser == null) {
			String strUsername = getContext().getRequestParameter("n_username");
			if (strUsername == null || strUsername.length() > CommonDefine.MAX_LENGTH_USERNAME || !strUsername.matches("[a-z]+\\d?\\.[a-z]+")) {
				GameLog.getInstance().error("[ManageAccount] username is invalid!");
				return;
			}
			String strPassword = getContext().getRequestParameter("password");
			String strConfirmPassword = getContext().getRequestParameter("confirm-password");
			if (StringUtils.isBlank(strPassword) || StringUtils.isBlank(strConfirmPassword) || strPassword.length() > CommonDefine.MAX_LENGTH_PASSWORD) {
				GameLog.getInstance().error("[ManageAccount] password is invalid!");
				return;
			}
			if (!strPassword.equals(strConfirmPassword)) {
				GameLog.getInstance().error("[ManageAccount] confirm password incorrect!");
				return;
			}
			if (UserService.getUserByName(strUsername) != null) {
				GameLog.getInstance().error("[ManageAccount] username exist!");
				return;
			}
			
			if (!UserService.createUser(strUsername, strPassword, role, dept, flag, strEmpID, strFullName)) {
				GameLog.getInstance().error("[ManageAccount] create user fail!");
				return;
			}
			ManageLogService.createManageLog(userInfo, ManageLogType.ADD_USER, strUsername);
			
		} else {
			if (UserFlagEnum.PWD_CHANGE.isValid(selectUser.getFlag())) {
				flag = UserFlagEnum.PWD_CHANGE.setFlag(flag);
			}
			boolean isUpdate = false;
			if (flag != selectUser.getFlag() || role != selectUser.getRole() || dept != selectUser.getDept() || !strEmpID.equals(selectUser.getEmpid()) ||!strFullName.equals(selectUser.getName())) {
				isUpdate = true;
			}
			String strPassword = getContext().getRequestParameter("password");
			String strConfirmPassword = getContext().getRequestParameter("confirm-password");
			if (StringUtils.isNotBlank(strPassword) && StringUtils.isNotBlank(strConfirmPassword)) {
				if (!strPassword.equals(strConfirmPassword)) {
					GameLog.getInstance().error("[ManageAccount] confirm password incorrect!");
					return;
				}
				isUpdate = true;
			}
			if (isUpdate) {
				selectUser.setFlag(flag);
				selectUser.setRole(role);
				selectUser.setDept(dept);
				selectUser.setEmpid(strEmpID);
				selectUser.setName(strFullName);
				
				if (userInfo.getId() == selectUser.getId() && !selectUser.isAdministrator()) {
					GameLog.getInstance().error("[ManageAccount] cannot remove admin permission by yourself!");
					return;
				}
				if (StringUtils.isNotBlank(strPassword) && strPassword.equals(strConfirmPassword)) {
					selectUser.setPassword(CommonMethod.getEncryptMD5(strPassword));
				}
				if (!CommonDaoService.update(selectUser.getTblUserInfo())) {
					GameLog.getInstance().error("[ManageAccount] update user fail!");
					return;
				}
				ManageLogService.createManageLog(userInfo, ManageLogType.UPDATE_USER, selectUser.getUsername());
			}
		}
		update = false;
	}

	@Override
	public void onRender() {
		super.onRender();
		addBackLink(Home.class, "MSG_MAIN_NAV_BAR_HOME");
		if (!update) {
			List<UserInfo> userList = null;
			if (selectUser != null) {
				userList = new ArrayList<>();
				userList.add(selectUser);
			} else if (getContext().getRequestParameter("search") == null){
				userList = UserService.getUserList();
			} else if (getContext().getRequestParameter("search") != null) {
				if (StringUtils.isBlank(getContext().getRequestParameter("user_id")) && StringUtils.isBlank(getContext().getRequestParameter("username"))) {
					userList = UserService.getUserList();
				}
			}
			initUserTable(userList);
			initSearchForm();
		} else {
			if (selectUser == null) {
				selectUser = new UserInfo();
			}
			initUserForm();
		}
	}

	private void initUserTable(List<UserInfo> userList) {
		StringBuilder table = new StringBuilder();
		table.append("<table class=\"table table-hover\" id=\"user-table\">");
		table.append("<thead>");
		table.append("<tr role=\"role\" class=\"text-info\">");
		table.append("<th scope=\"col\">").append("Id").append("</th>");
		table.append("<th scope=\"col\">").append("Username").append("</th>");
		table.append("<th scope=\"col\">").append("EmpID").append("</th>");
		table.append("<th scope=\"col\">").append("FullName").append("</th>");
		table.append("<th scope=\"col\">").append("Role").append("</th>");
		table.append("<th scope=\"col\">").append("Department").append("</th>");
		table.append("<th scope=\"col\">").append("Active").append("</th>");
		table.append("<th scope=\"col\">").append("Milktea banned").append("</th>");
		table.append("<th scope=\"col\">").append("Device banned").append("</th>");
		table.append("<th scope=\"col\">").append("Edit").append("</th>");
		table.append("</tr>");
		table.append("</thead>");
		table.append("<tbody>");
		if (userList == null || userList.isEmpty()) {
			table.append("<tr><td colspan=\"6\" id=\"no-data\">No user found!</td></tr>");
		} else {
			for (UserInfo user : userList) {
				table.append("<tr>");
				table.append("<td>").append(user.getId()).append("</td>");
				table.append("<td>").append(user.getUsername()).append("</td>");
				table.append("<td>").append(user.getEmpid()).append("</td>");
				table.append("<td>").append(user.getName()).append("</td>");
				table.append("<td>");
				if (user.isAdministrator()) {
					table.append("Administrator");
				} else if (user.isMilkteaAdmin()) {
					table.append("Milktea manager");
				} else if (user.isDeviceAdmin()) {
					table.append("Device manager");
				} else if (user.isVoteAdmin()) {
					table.append("Vote manager");
				} else {
					table.append("User");
				}
				table.append("</td>");
				table.append("<td>").append(user.getDepartment()).append("</td>");
				table.append("<td>").append((user.isActive() ? "Yes" : "no")).append("</td>");
				table.append("<td>").append((user.isMilkteaBanned() ? "Yes" : "no")).append("</td>");
				table.append("<td>").append((user.isDeviceBanned() ? "Yes" : "no")).append("</td>");
				table.append("<td>");
				table.append(String.format("<a href=\"%s\">Edit</a>", getPagePath(this.getClass()) + "?update=1&user_id=" + user.getId()));
				table.append("</td>");
				table.append("</tr>");
			}
		}
		table.append("</tbody>");
		table.append("</table>");
		addModel("table", table.toString());
	}

	private void initSearchForm() {
		StringBuilder sb = new StringBuilder();
		sb.append("<form id=\"form-user\" method=\"post\" action=\"" + getPagePath(this.getClass()) + "\">");
		sb.append("<input type=\"hidden\" name=\"search\" value=\"1\"/>");
		sb.append("<label class=\"font-weight-bold text-info\" style=\"font-size:1.125rem;\">").append("Search for User").append("</label>");
		sb.append("<div class=\"form-group\">");
		sb.append("<fieldset>");
		sb.append("<label class=\"control-label\" for=\"user_id\">ID:</label>");
		sb.append("<input type=\"text\" class=\"form-control\" id=\"user_id\" name=\"user_id\" value=\"" + (selectUser != null ? selectUser.getId() : "") + "\"/>");
		sb.append("</fieldset>");
		sb.append("</div>");
		sb.append("<div class=\"form-group\">");
		sb.append("<fieldset>");
		sb.append("<label class=\"control-label\" for=\"user_id\">Username:</label>");
		sb.append("<input type=\"text\" class=\"form-control\" id=\"username\" name=\"username\" value=\"" + (selectUser != null ? selectUser.getUsername() : "") + "\"/>");
		sb.append("</fieldset>");
		sb.append("</div>");
		sb.append("<button type=\"submit\" class=\"btn btn-primary\">Submit</button>");
		sb.append("</form>");
		addModel("search", sb.toString());
	}
	
	private void initUserForm() {
		StringBuilder sb = new StringBuilder();
		boolean isNew = (selectUser.getId() == 0);
		sb.append("<form id=\"form-user\" method=\"post\" action=\"" + getPagePath(this.getClass()) + "\">");
		if (!isNew) {
			sb.append("<input type=\"hidden\" name=\"user_id\" value=\"" + selectUser.getId() + "\"/>");
		}
		sb.append("<input type=\"hidden\" name=\"update\" value=\"1\"/>");
		sb.append("<label class=\"font-weight-bold text-info\" style=\"font-size:1.125rem;\">").append((isNew ? "Create New User" : " Update User")).append("</label>");
		// username
		sb.append("<div class=\"form-group\">");
		sb.append("<fieldset>");
		sb.append("<label class=\"control-label\" for=\"n_username\">Username</label>");
		if (isNew) {
			sb.append("<input type=\"text\" class=\"form-control\" id=\"n_username\" name=\"n_username\" value=\"\" pattern=\"[a-z]+[0-9]?\\.[a-z]+\" placeholder=\"lastname[number].firstname\" required/>");
		} else {
			sb.append("<input type=\"text\" class=\"form-control\" id=\"n_username\" name=\"n_username\" value=\"" + selectUser.getUsername() + "\" readonly/>");
		}
		sb.append("</fieldset>");
		sb.append("</div>");
		if (isNew) {
			// password
			sb.append("<div class=\"form-group\">");
			sb.append("<fieldset>");
			sb.append("<label class=\"control-label\" for=\"password\">Password</label>");
			sb.append("<input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" value=\"\" required/>");
			sb.append("</fieldset>");
			sb.append("</div>");
			// confirm password
			sb.append("<div class=\"form-group\">");
			sb.append("<fieldset>");
			sb.append("<label class=\"control-label\" for=\"confirm-password\">Confirm Password</label>");
			sb.append("<input type=\"password\" class=\"form-control\" id=\"confirm-password\" name=\"confirm-password\" value=\"\" required/>");
			sb.append("</fieldset>");
			sb.append("</div>");
		} else {
			// password
			sb.append("<div class=\"form-group\">");
			sb.append("<fieldset>");
			sb.append("<label class=\"control-label\" for=\"password\">Password</label>");
			sb.append("<input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" value=\"\"/>");
			sb.append("</fieldset>");
			sb.append("</div>");
			// confirm password
			sb.append("<div class=\"form-group\">");
			sb.append("<fieldset>");
			sb.append("<label class=\"control-label\" for=\"confirm-password\">Confirm Password</label>");
			sb.append("<input type=\"password\" class=\"form-control\" id=\"confirm-password\" name=\"confirm-password\" value=\"\"/>");
			sb.append("</fieldset>");
			sb.append("</div>");
		}
		
		//employee ID
		sb.append("<div class=\"form-group\">");
		sb.append("<fieldset>");
		sb.append("<label class=\"control-label\" for=\"empid\">Employee ID</label>");
		if (isNew) {
			sb.append("<input type=\"text\" class=\"form-control\" id=\"empid\" name=\"empid\" value=\"\"/>");
		} else {
			sb.append("<input type=\"text\" class=\"form-control\" id=\"empid\" name=\"empid\" value=\"" + selectUser.getEmpid() + "\"/>");
		}
		sb.append("</fieldset>");
		sb.append("</div>");
		
		//FullName
		sb.append("<div class=\"form-group\">");
		sb.append("<fieldset>");
		sb.append("<label class=\"control-label\" for=\"fullname\">FullName</label>");
		if (isNew) {
			sb.append("<input type=\"text\" class=\"form-control\" id=\"fullname\" name=\"fullname\" value=\"\" required/>");
		} else {
			sb.append("<input type=\"text\" class=\"form-control\" id=\"fullname\" name=\"fullname\" value=\"" + selectUser.getName() + "\" required/>");
		}
		sb.append("</fieldset>");
		sb.append("</div>");
		
		//role
		sb.append("<div class=\"form-group\">");
		sb.append("<fieldset>");
		sb.append("<label class=\"control-label\">Role</label>");
		for (UserRole userRole : UserRole.values()) {
			sb.append("<div class=\"custom-control custom-radio\">");
			sb.append("<input type=\"radio\" class=\"custom-control-input\" name=\"user_role\" id=\"user-role-" + userRole.ordinal() + "\" value=\"" + userRole.getRole() + "\" " + ((selectUser.getRole() == userRole.getRole()) ? "checked" : "") + "/>");
			sb.append("<label class=\"custom-control-label\" for=\"user-role-" + userRole.ordinal() + "\">").append(getMessage(userRole.getLabel())).append("</label>");
			sb.append("</div>");
		}
		sb.append("</fieldset>");
		sb.append("</div>");
		//department
		sb.append("<div class=\"form-group\">");
		sb.append("<fieldset>");
		sb.append("<label class=\"control-label\">Department</label>");
		for (Department dept : Department.values()) {
			if (dept == Department.NONE) {
				continue;
			}
			sb.append("<div class=\"custom-control custom-checkbox\">");
			sb.append("<input type=\"checkbox\" class=\"custom-control-input\" name=\"user_dept\" id=\"user-dept-" + dept.ordinal() + "\" value=\"" + dept.getFlag() + "\" " + ((dept.isValid(selectUser.getDept())) ? "checked" : "") + "/>");
			sb.append("<label class=\"custom-control-label\" for=\"user-dept-" + dept.ordinal() + "\">").append(getMessage(dept.getLabel())).append("</label>");
			sb.append("</div>");
		}
		sb.append("</fieldset>");
		sb.append("</div>");
		// flag
		sb.append("<div class=\"form-group\">");
		sb.append("<fieldset>");
		sb.append("<label class=\"control-label\">Flag</label>");
		for (UserFlagEnum userFlag : UserFlagEnum.values()) {
			if (userFlag == UserFlagEnum.INVALID || userFlag == UserFlagEnum.PWD_CHANGE) {
				continue;
			}
			sb.append("<div class=\"custom-control custom-checkbox\">");
			sb.append("<input type=\"checkbox\" class=\"custom-control-input\" name=\"user_flag\" id=\"user-flag-" + userFlag.ordinal() + "\" value=\"" + userFlag.ordinal() + "\" " + (userFlag.isValid(selectUser.getFlag()) ? "checked" : "") + "/>");
			sb.append("<label class=\"custom-control-label\" for=\"user-flag-" + userFlag.ordinal() + "\">").append(getMessage(userFlag.getLabel())).append("</label>");
			sb.append("</div>");
		}
		sb.append("</fieldset>");
		sb.append("</div>");
				
		sb.append("<button type=\"submit\" class=\"btn btn-primary\">Submit</button>");
		sb.append("<a href=\"" + getPagePath(this.getClass()) + "\" style=\"margin-left:25px;\"><button type=\"button\" class=\"btn btn-danger\">Cancel</button></a>");
		sb.append("</form>");
		addModel("form", sb.toString());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		headElements = super.getHeadElements();
		if (headElements != null) {
			headElements.add(new JsImport("/js/manage/manage_user.js"));
		}
		return headElements;
	}
	
	@Override
	protected int getTabIndex() {
		return 1;
	}

}
