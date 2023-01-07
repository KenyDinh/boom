package dev.boom.common;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.click.Context;

import dev.boom.core.BoomProperties;

public class CommonMethod {
	
	private static final Random random = new Random();
	
	private CommonMethod() {
	}
	
	public static String getSHA256Encrypt(String input) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(encodedhash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	private static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
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
	
	public static int random(int max) {
		return random.nextInt(max);
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

	public static String getFormatDateWithoutTimeString(Date date) {
		return getFormatDateString(date, CommonDefine.DATE_FORMAT_PATTERN_WITHOUT_TIME);
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

	public static boolean isValidDateTimeFormat(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		if (str.matches(CommonDefine.DATE_REGEX_PATTERN) || str.matches(CommonDefine.DATE_REGEX_PATTERN_2)) {
			return true;
		}
		return false;
	}
	
	public static int min(int value1, int value2) {
		return Math.min(value1, value2);
	}
	
	public static int max(int value1, int value2) {
		return Math.max(value1, value2);
	}
	
	public static int minmax(int min, int value, int max) {
		return Math.min(Math.max(min, value), max);
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
	/**
	 * 
	 * @param min
	 * @param max
	 * @return random number from <code>min</code> (inclusive) to <code>max</code> (inclusive)
	 */
	public static int randomNumber(int min, int max) {
		int range = max - min + 1;
		int randomNum = random.nextInt(range) + min;
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
	
	public static String getFormatContentHtmlForDisplaying(String content) {
		if (content == null || content.isEmpty()) {
			return "";
		}
		content = content.replace("<", "&lt;").replace(">", "&gt;");
		content = content.replaceAll("\r\n", "<br/>").replaceAll("[\r\n]", "<br/>");
		return content;
	}
	
	public static String getFormatContentHtmlForTooltip(String content) {
		if (content == null || content.isEmpty()) {
			return "";
		}
		content = content.replaceAll("\r\n", ".").replaceAll("[\r\n]", ".");
		return content;
	}
	
	public static boolean isImageFileAllowed(String fileExt) {
		if (fileExt == null || fileExt.isEmpty()) {
			return false;
		}
		return (fileExt = fileExt.toLowerCase()).matches("(jpg|jpeg|png|bmp)");
	}
	
	public static BufferedImage convertImage(InputStream stream, int toWidth, int toHeight) throws IOException {
		if (stream == null) {
			return null;
		}
		BufferedImage image = ImageIO.read(stream);
		BufferedImage out = null;
		int width = image.getWidth();
		int height = image.getHeight();
		if (width > toWidth || height > toHeight) {
			double scaleFactor = getScaleFactorToFit(new Dimension(width, height), new Dimension(toWidth, toHeight));
			int scaleWidth = (int) Math.round(width * scaleFactor);
			int scaleHeight = (int) Math.round(height * scaleFactor);
			out = scaleImage(image, scaleWidth, scaleHeight);
		} else {
			out = image;
		}
		return out;
	}
	
	private static BufferedImage scaleImage(BufferedImage img, int targetWidth, int targetHeight) {

		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = img;
		BufferedImage scratchImage = null;
		Graphics2D g2 = null;

		int w = img.getWidth();
		int h = img.getHeight();

		int prevW = w;
		int prevH = h;

		do {
			if (w > targetWidth) {
				w /= 2;
				w = (w < targetWidth) ? targetWidth : w;
			}

			if (h > targetHeight) {
				h /= 2;
				h = (h < targetHeight) ? targetHeight : h;
			}

			if (scratchImage == null) {
				scratchImage = new BufferedImage(w, h, type);
				g2 = scratchImage.createGraphics();
			}

			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

			prevW = w;
			prevH = h;
			ret = scratchImage;
		} while (w != targetWidth || h != targetHeight);

		if (g2 != null) {
			g2.dispose();
		}

		if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
			scratchImage = new BufferedImage(targetWidth, targetHeight, type);
			g2 = scratchImage.createGraphics();
			g2.drawImage(ret, 0, 0, null);
			g2.dispose();
			ret = scratchImage;
		}

		return ret;
	}
	
	private static double getScaleFactor(int iMasterSize, int iTargetSize) {
		return ((double) iTargetSize / (double) iMasterSize);
	}
	
	private static double getScaleFactorToFit(Dimension original, Dimension toFit) {
		double dScale = 1d;
		if (original != null && toFit != null) {

			double dScaleWidth = getScaleFactor(original.width, toFit.width);
			double dScaleHeight = getScaleFactor(original.height, toFit.height);

			dScale = Math.min(dScaleHeight, dScaleWidth);

		}
		return dScale;
	}
}
