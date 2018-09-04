package dev.boom.connect;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import dev.boom.core.BoomProperties;

public class HibernateSessionFactory {
	
	private static final String[] HIBERNATE_MAPPING_RESOURCE = {
			//_/_/_/_/_/_/_/_/_/_/_/_//
			"TblMilkTeaUserInfo",
			"TblShopInfo",
			"TblDishInfo",
			"TblDishRatingInfo",
			"TblMenuInfo",
			"TblOrderInfo",
			//_/_/_/_/_/_/_/_/_/_/_/_//
			"TblWorldInfo",
			"TblUserInfo",
			//_/_/_/_/_/_/_/_/_/_/_/_//
			"TblNihongoOwningInfo",
			"TblNihongoPetInfo",
			"TblNihongoProgressInfo",
			"TblNihongoUserInfo",
			"TblNihongoWordInfo",
			//_/_/_/_/_/_/_/_/_/_/_/_//
	};

	private static SessionFactory sessionFactory = buildSessionFactory();
	private static SessionFactory buildSessionFactory() {
		try {
			Configuration config = new Configuration();
			Properties prop = new Properties();
			prop.put("show_sql", false);
			prop.put("hibernate.bytecode.use_reflection_optimizer", false);
//			prop.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
			prop.put("hibernate.dialect", "dev.boom.connect.MySQLDialect");
			prop.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
			prop.put("hibernate.connection.url", BoomProperties.DB_CONNECTION_URL);
			prop.put("hibernate.connection.password", BoomProperties.DB_CONNECTION_PWD);
			prop.put("hibernate.connection.username", BoomProperties.DB_CONNECTION_USER);
			config.addProperties(prop);
			for (String resource : HIBERNATE_MAPPING_RESOURCE) {
				config.addResource("dev/boom/tbl/mapping/" + resource + ".hbm.xml");
			}
			ServiceRegistry serviceResitry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
			return config.buildSessionFactory(serviceResitry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void init() {
		if (sessionFactory == null || sessionFactory.isClosed()) {
			sessionFactory = buildSessionFactory();
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static void shutdown() {
		if (sessionFactory != null && !sessionFactory.isClosed()) {
			sessionFactory.close();
		}
	}
}
