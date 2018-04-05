package dev.boom.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
