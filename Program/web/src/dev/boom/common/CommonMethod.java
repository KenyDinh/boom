package dev.boom.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.click.Context;

import dev.boom.core.BoomProperties;

public class CommonMethod {
	
	private CommonMethod() {
	}

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
	
	public static String getFormatNumberThousandComma(long price) {
		return String.valueOf(price).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
	}
	
	public static String getStringTimeLeft(long timeMili) {
		timeMili = timeMili / 1000;
		int second = (int)(timeMili) % 60;
		int minute = (int)(timeMili / 60) % 60;
		int hour = (int)(timeMili - minute * 60 - second) / 3600;
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}
	
	public static int randomNumber(int min, int max) {
		Random rn = new Random();
		int range = max - min + 1;
		int randomNum = rn.nextInt(range) + min;
		return randomNum;
	}
	
	public static String getStaticFile(String fileName) {
		String context = "";
		String strProt = "";
		if (Context.hasThreadLocalContext()) {
			context = Context.getThreadLocalContext().getRequest().getContextPath();
			strProt = String.valueOf(Context.getThreadLocalContext().getRequest().getServerPort());
		}
		if (!BoomProperties.STATIC_FILE_PORT_SCALE.isEmpty()) {
			strProt = BoomProperties.STATIC_FILE_PORT_SCALE;
		}
		strProt = (strProt.equals("80") ? "" : ":" + strProt);
		return "http://" + BoomProperties.SERVICE_HOSTNAME + strProt + context + "/static/" + fileName;
	}
}
