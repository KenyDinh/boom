package dev.boom.core;

import javax.servlet.ServletException;

import org.apache.click.ClickServlet;

import dev.boom.connect.HibernateSessionFactory;
import dev.boom.dao.fix.FixDataLoader;

public class BoomServlet extends ClickServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
//		GameLog.getInstance().setLevel(GameLog.DEBUG_LEVEL);
		BoomProperties.load();
		FixDataLoader.init(getServletContext());
		HibernateSessionFactory.init();
	}

	@Override
	public void destroy() {
		super.destroy();
		FixDataLoader.destroy(getServletContext());
		HibernateSessionFactory.shutdown();
	}

}
