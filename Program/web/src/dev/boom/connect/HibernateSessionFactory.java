package dev.boom.connect;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {

	private static SessionFactory sessionFactory = buildSessionFactory();
	
	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
		
		Configuration config = new Configuration();
		try {
			return config.configure().buildSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void init() {
		if (sessionFactory == null) {
			sessionFactory = buildSessionFactory();
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static void shutdown() {
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
}
