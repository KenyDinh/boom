package dev.boom.core;

import javax.servlet.ServletException;

import org.apache.click.ClickServlet;

import dev.boom.connect.HibernateSessionFactory;

public class BoomServlet extends ClickServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
		BoomProperties.load();
		HibernateSessionFactory.init();
	}

	@Override
	public void destroy() {
		super.destroy();
		HibernateSessionFactory.shutdown();
	}

}
