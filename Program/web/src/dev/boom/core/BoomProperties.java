package dev.boom.core;

import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BoomProperties {

	private static Log log = LogFactory.getLog(BoomProperties.class);
	
	public static String SERVICE_HOSTNAME = "localhost";
	
	public static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/boom";
	public static String DB_CONNECTION_USER = "root";
	public static String DB_CONNECTION_PWD = "";
	public static String WEBSOCKET_PORT_SCALE = "";
	public static String STATIC_FILE_PORT_SCALE = "";
	public static String FRIDAY_VERSION = "1";
	public static boolean BOOM_TOURNAMENT = false;
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
			log.info("SERVICE_HOSTNAME <= " + SERVICE_HOSTNAME);
		}
		if (prop.containsKey("DB_CONNECTION_URL")) {
			DB_CONNECTION_URL = prop.getProperty("DB_CONNECTION_URL");
			log.info("DB_CONNECTION_URL <= " + DB_CONNECTION_URL);
		}
		if (prop.containsKey("DB_CONNECTION_USER")) {
			DB_CONNECTION_USER = prop.getProperty("DB_CONNECTION_USER");
			log.info("DB_CONNECTION_USER <= " + DB_CONNECTION_USER);
		}
		if (prop.containsKey("DB_CONNECTION_PWD")) {
			DB_CONNECTION_PWD = prop.getProperty("DB_CONNECTION_PWD");
			log.info("DB_CONNECTION_PWD <= " + DB_CONNECTION_PWD);
		}
		if (prop.containsKey("WEBSOCKET_PORT_SCALE")) {
			WEBSOCKET_PORT_SCALE = prop.getProperty("WEBSOCKET_PORT_SCALE");
			log.info("WEBSOCKET_PORT_SCALE <= " + WEBSOCKET_PORT_SCALE);
		}
		if (prop.containsKey("STATIC_FILE_PORT_SCALE")) {
			STATIC_FILE_PORT_SCALE = prop.getProperty("STATIC_FILE_PORT_SCALE");
			log.info("STATIC_FILE_PORT_SCALE <= " + STATIC_FILE_PORT_SCALE);
		}
		if (prop.containsKey("FRIDAY_VERSION")) {
			FRIDAY_VERSION = prop.getProperty("FRIDAY_VERSION");
			log.info("FRIDAY_VERSION <= " + FRIDAY_VERSION);
		}
		if (prop.containsKey("BOOM_TOURNAMENT")) {
			BOOM_TOURNAMENT = Boolean.parseBoolean(prop.getProperty("BOOM_TOURNAMENT"));
			log.info("BOOM_TOURNAMENT <= " + BOOM_TOURNAMENT);
		}
	}
}
