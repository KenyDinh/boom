package dev.boom.connect;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import dev.boom.core.BoomProperties;

public class HibernateSessionFactory {

	private static SessionFactory sessionFactory = buildSessionFactory();
	
	private static SessionFactory buildSessionFactory() {
		try {
			Configuration config = new Configuration();
			Properties prop = new Properties();
			prop.put("show_sql", false);
			prop.put("hibernate.bytecode.use_reflection_optimizer", false);
			prop.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
			prop.put("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
			prop.put("hibernate.connection.url", BoomProperties.DB_CONNECTION_URL);
			prop.put("hibernate.connection.password", BoomProperties.DB_CONNECTION_PWD);
			prop.put("hibernate.connection.username", BoomProperties.DB_CONNECTION_USER);
			config.addProperties(prop);
			// milktea
			config.addResource("dev/boom/tbl/mapping/TblUserInfo.hbm.xml");
			config.addResource("dev/boom/tbl/mapping/TblMilkTeaUserInfo.hbm.xml");
			config.addResource("dev/boom/tbl/mapping/TblShopInfo.hbm.xml");
			config.addResource("dev/boom/tbl/mapping/TblDishInfo.hbm.xml");
			config.addResource("dev/boom/tbl/mapping/TblMenuInfo.hbm.xml");
			config.addResource("dev/boom/tbl/mapping/TblOrderInfo.hbm.xml");
			config.addResource("dev/boom/tbl/mapping/TblWorldInfo.hbm.xml");
			//nihongo game
			config.addResource("dev/boom/tbl/mapping/TblNihongoOwningInfo.hbm.xml");
			config.addResource("dev/boom/tbl/mapping/TblNihongoPetInfo.hbm.xml");
			config.addResource("dev/boom/tbl/mapping/TblNihongoProgressInfo.hbm.xml");
			config.addResource("dev/boom/tbl/mapping/TblNihongoUserInfo.hbm.xml");
			config.addResource("dev/boom/tbl/mapping/TblNihongoWordInfo.hbm.xml");
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
