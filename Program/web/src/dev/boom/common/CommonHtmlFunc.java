package dev.boom.common;

import java.util.Map;

public class CommonHtmlFunc {

	@SuppressWarnings("rawtypes")
	public static String getLoginFormModal(String contextPath, Map messages) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"modal fade\" id=\"login-form-modal\">");
			sb.append("<div class=\"modal-dialog modal-dialog-centered modal-md\" >");
				sb.append("<div class=\"modal-content\" >");
					//header
					sb.append("<div class=\"modal-header bg-info\">");
						sb.append("<h4 class=\"modal-title\">").append(messages.get("MSG_GENERAL_LOGIN")).append("</h4>");
						sb.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>");
					sb.append("</div>");
					//body
					sb.append("<div class=\"modal-body\">");
						sb.append("<form id=\"login-form\">");
							sb.append("<div class=\"form-group\">");
								sb.append("<label for=\"form-login-username\">").append(messages.get("MSG_GENERAL_USERNAME") + ":").append("</label>");
								sb.append("<input type=\"text\" class=\"form-control\" id=\"form-login-username\" name=\"form-login-username\" max-length=16 required placeholder=\"\"/>");
							sb.append("</div>");
							sb.append("<div class=\"form-group\">");
								sb.append("<label for=\"form-login-password\">").append(messages.get("MSG_GENERAL_PASSWORD") +":").append("</label>");
								sb.append("<input type=\"password\" class=\"form-control\" id=\"form-login-password\" name=\"form-login-password\" required placeholder=\"\"/>");
							sb.append("</div>");
							sb.append("<div class=\"form-group\" id=\"login-message\">");
							sb.append("</div>");
							sb.append("<button type=\"submit\" class=\"btn btn-info\" onclick=\"onclickLogin();this.blur();return false;\">").append(messages.get("MSG_GENERAL_SUBMIT")).append("</button>");
						sb.append("</form>");
					sb.append("</div>");
					//footer
					sb.append("<div class=\"modal-footer\">");
						sb.append("<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">").append(messages.get("MSG_GENERAL_CLOSE")).append("</button>");
					sb.append("</div>");
				sb.append("</div>");
			sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getRegisterFormModal(String contextPath, Map messages) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"modal fade\" id=\"regist-form-modal\">");
			sb.append("<div class=\"modal-dialog modal-dialog-centered modal-md\" >");
				sb.append("<div class=\"modal-content\" >");
					//header
					sb.append("<div class=\"modal-header bg-success\">");
						sb.append("<h4 class=\"modal-title\">").append(messages.get("MSG_GENERAL_SIGNUP")).append("</h4>");
						sb.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>");
					sb.append("</div>");
					//body
					sb.append("<div class=\"modal-body\">");
						sb.append("<form id=\"regist-form\">");
							sb.append("<div class=\"form-group\">");
								sb.append("<label for=\"form-regist-username\">").append(messages.get("MSG_GENERAL_USERNAME") + ":").append("</label>");
								sb.append("<input type=\"text\" class=\"form-control\" id=\"form-regist-username\" name=\"form-regist-username\" max-length=16 required placeholder=\"should be firstname.lastname\"/>");
							sb.append("</div>");
							sb.append("<div class=\"form-group\">");
								sb.append("<label for=\"form-regist-password\">").append(messages.get("MSG_GENERAL_PASSWORD") +":").append("</label>");
								sb.append("<input type=\"password\" class=\"form-control\" id=\"form-regist-password\" name=\"form-regist-password\" required placeholder=\"enter your password\"/>");
							sb.append("</div>");
							sb.append("<div class=\"form-group\">");
								sb.append("<label for=\"form-regist-password\">").append(messages.get("MSG_GENERAL_CONFIRM_PASSWORD") +":").append("</label>");
								sb.append("<input type=\"password\" class=\"form-control\" id=\"form-regist-re-password\" name=\"form-regist-re-password\" required placeholder=\"confirm your password\"/>");
							sb.append("</div>");
							sb.append("<div class=\"form-group\" id=\"regist-message\">");
							sb.append("</div>");
							sb.append("<button type=\"submit\" class=\"btn btn-success\" onclick=\"onclickRegist();this.blur();return false;\">").append(messages.get("MSG_GENERAL_SUBMIT")).append("</button>");
						sb.append("</form>");
					sb.append("</div>");
					//footer
					sb.append("<div class=\"modal-footer\">");
						sb.append("<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">").append(messages.get("MSG_GENERAL_CLOSE")).append("</button>");
					sb.append("</div>");
				sb.append("</div>");
			sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getChangePasswordFormModal(String contextPath, Map messages) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"modal fade\" id=\"change-password-modal\">");
			sb.append("<div class=\"modal-dialog modal-dialog-centered modal-md\" >");
				sb.append("<div class=\"modal-content\" >");
					//header
					sb.append("<div class=\"modal-header bg-info\">");
						sb.append("<h4 class=\"modal-title\">").append(messages.get("MSG_ACCOUNT_CHANGE_PASSWORD")).append("</h4>");
						sb.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>");
					sb.append("</div>");
					//body
					sb.append("<div class=\"modal-body\">");
						sb.append("<form id=\"change-password-form\">");
							sb.append("<div class=\"form-group\">");
								sb.append("<label for=\"form-cp-current-password\">").append(messages.get("MSG_GENERAL_CURRENT_PASSWORD") + ":").append("</label>");
								sb.append("<input type=\"password\" class=\"form-control\" id=\"form-cp-current-password\" name=\"form-cp-current-password\" required placeholder=\"enter your current password\"/>");
							sb.append("</div>");
							sb.append("<div class=\"form-group\">");
								sb.append("<label for=\"form-cp-new-password\">").append(messages.get("MSG_GENERAL_PASSWORD") +":").append("</label>");
								sb.append("<input type=\"password\" class=\"form-control\" id=\"form-cp-new-password\" name=\"form-cp-new-password\" required placeholder=\"enter your new password\"/>");
							sb.append("</div>");
							sb.append("<div class=\"form-group\">");
								sb.append("<label for=\"form-cp-re-new-password\">").append(messages.get("MSG_GENERAL_CONFIRM_PASSWORD") +":").append("</label>");
								sb.append("<input type=\"password\" class=\"form-control\" id=\"form-cp-re-new-password\" name=\"form-cp-re-new-password\" required placeholder=\"confirm your new password\"/>");
							sb.append("</div>");
							sb.append("<div class=\"form-group\" id=\"change-pwd-message\">");
							sb.append("</div>");
							sb.append("<button type=\"submit\" class=\"btn btn-info\" onclick=\"onclickChangePassword();this.blur();return false;\">").append(messages.get("MSG_GENERAL_SUBMIT")).append("</button>");
						sb.append("</form>");
					sb.append("</div>");
					//footer
					sb.append("<div class=\"modal-footer\">");
						sb.append("<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">").append(messages.get("MSG_GENERAL_CLOSE")).append("</button>");
					sb.append("</div>");
				sb.append("</div>");
			sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	public static String getModalAlertWithMessage(String key, String type, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<div class=\"modal fade\" id=\"%s\">", key));
			sb.append("<div class=\"modal-dialog modal-dialog-centered modal-md\" >");
				sb.append("<div class=\"modal-content\" >");
				sb.append(String.format("<div class=\"alert alert-%s\" style=\"margin:0;\">", type));
					sb.append(message);
					sb.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>");
				sb.append("</div>");
				sb.append("</div>");
			sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
}
