package dev.boom.core;

import java.io.FileNotFoundException;
import java.util.Properties;

public class BoomProperties {

	public static String SERVICE_HOSTNAME = "localhost";
	
	public static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/boom";
	public static String DB_CONNECTION_USER = "root";
	public static String DB_CONNECTION_PWD = "";
	
	public static void load() {
		Properties prop = new Properties();
		try {
			prop.load(BoomProperties.class.getClassLoader().getResourceAsStream("boom.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (prop.containsKey("SERVICE_HOSTNAME")) {
			SERVICE_HOSTNAME = prop.getProperty("SERVICE_HOSTNAME");
		}
		if (prop.containsKey("DB_CONNECTION_URL")) {
			DB_CONNECTION_URL = prop.getProperty("DB_CONNECTION_URL");
		}
		if (prop.containsKey("DB_CONNECTION_USER")) {
			DB_CONNECTION_USER = prop.getProperty("DB_CONNECTION_USER");
		}
		if (prop.containsKey("DB_CONNECTION_PWD")) {
			DB_CONNECTION_PWD = prop.getProperty("DB_CONNECTION_PWD");
		}
	}
}
