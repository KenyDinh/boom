package dev.boom.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import dev.boom.entity.info.MenuInfo;
import dev.boom.entity.info.OrderInfo;

public class CommonMethod {

	public static String getEncryptMD5(String text) {
		MessageDigest md;
		StringBuffer sb = new StringBuffer();
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			byte[] data = md.digest();
			for (int i = 0; i < data.length; i++) {
				sb.append(Integer.toString((data[i] & 0xFF) + 0x100, 16).substring(1));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String getFormatDateString(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * format date with default pattern
	 * <code>CommonDefine.DATE_FORMAT_PATTERN_DB</code>
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormatDateString(Date date) {
		return getFormatDateString(date, CommonDefine.DATE_FORMAT_PATTERN_DB);
	}

	public static Date getDate(String strDate, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(strDate);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getFormatStringNow() {
		return getFormatDateString(new Date(), CommonDefine.DATE_FORMAT_PATTERN);
	}

	/**
	 * get Date with default pattern
	 * <code>CommonDefine.DATE_FORMAT_PATTERN_DB</code>
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date getDate(String strDate) {
		return getDate(strDate, CommonDefine.DATE_FORMAT_PATTERN_DB);
	}

	public static boolean isValidNumeric(String str, long min, long max) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		if (!str.matches("[-+]?[0-9]+")) {
			return false;
		}
		str = str.replace("+", "");
		str = str.replaceAll("^0+(?=[1-9])", "").replaceAll("^0+$", "0");
		str = str.replaceAll("^-0+(?=[1-9])", "-").replaceAll("^-0+$", "0");
		String strMin = Long.toString(Long.MIN_VALUE);
		String strMax = Long.toString(Long.MAX_VALUE);
		if (str.length() > strMin.length()) {
			return false;
		}
		if (str.replace("-", "").length() > strMax.length()) {
			return false;
		}
		if (str.length() == strMin.length() && str.compareTo(strMin) > 0) {
			return false;
		}
		if (str.length() == strMax.length() && str.compareTo(strMax) > 0) {
			return false;
		}
		long value = Long.parseLong(str);
		if (value < min || value > max) {
			return false;
		}
		return true;
	}

	public static String capital(String word) {
		if (word == null || word.isEmpty()) {
			return "";
		}
		return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
	}
	
	public static String getFormatPrice(long price) {
		return String.valueOf(price).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
	}
	
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
								sb.append("<input type=\"password\" class=\"form-control\" id=\"form-regist-re-password\" name=\"form-regist-re-password\" required placeholder=\"re-enter your password\"/>");
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
					sb.append("<div class=\"modal-header bg-success\">");
						sb.append("<h4 class=\"modal-title\">").append(messages.get("MSG_GENERAL_SIGNUP")).append("</h4>");
						sb.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>");
					sb.append("</div>");
					//body
					sb.append("<div class=\"modal-body\">");
						sb.append("<form id=\"change-password-form\">");
							sb.append("<div class=\"form-group\">");
								sb.append("<label for=\"form-cp-current-password\">").append(messages.get("MSG_GENERAL_CURRENT_PASSWORD") + ":").append("</label>");
								sb.append("<input type=\"text\" class=\"form-control\" id=\"form-cp-current-password\" name=\"form-cp-current-password\" required placeholder=\"enter your current password\"/>");
							sb.append("</div>");
							sb.append("<div class=\"form-group\">");
								sb.append("<label for=\"form-cp-new-password\">").append(messages.get("MSG_GENERAL_PASSWORD") +":").append("</label>");
								sb.append("<input type=\"password\" class=\"form-control\" id=\"form-cp-new-password\" name=\"form-cp-new-password\" required placeholder=\"enter your new password\"/>");
							sb.append("</div>");
							sb.append("<div class=\"form-group\">");
								sb.append("<label for=\"form-cp-re-new-password\">").append(messages.get("MSG_GENERAL_CONFIRM_PASSWORD") +":").append("</label>");
								sb.append("<input type=\"password\" class=\"form-control\" id=\"form-cp-re-new-password\" name=\"form-cp-re-new-password\" required placeholder=\"re-enter your new password\"/>");
							sb.append("</div>");
							sb.append("<div class=\"form-group\" id=\"regist-message\">");
							sb.append("</div>");
							sb.append("<button type=\"submit\" class=\"btn btn-success\" onclick=\"onclickChangePassword();this.blur();return false;\">").append(messages.get("MSG_GENERAL_SUBMIT")).append("</button>");
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
	
	public static long getFinalCost(int totalOrder, MenuInfo menuInfo, OrderInfo order) {
		int sale = 100;
		if (menuInfo.getSale() > 0 && menuInfo.getSale() < 100) {
			sale = 100 - menuInfo.getSale();
		}
		long price = (order.getDish_price() * order.getQuantity() * sale) / 100;
		long shippingFee = (long) Math.ceil(((double) menuInfo.getShipping_fee()) / totalOrder);
		long cost = price + shippingFee;
		if (cost % 1000 > 300) {
			cost = cost - (cost % 1000) + 1000;
		} else {
			cost = cost - (cost % 1000);
		}
		return cost;
	}
}
