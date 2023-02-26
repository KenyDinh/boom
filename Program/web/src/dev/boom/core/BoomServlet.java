package dev.boom.core;

import javax.servlet.ServletException;

import org.apache.click.ClickServlet;

import dev.boom.common.VoteFuncs;
import dev.boom.dao.fix.FixDataLoader;
import dev.boom.game.boom.BoomGameMapEnum;
import dev.boom.socket.endpoint.FridayEndpoint;
import dev.boom.socket.func.PatpatFunc;

public class BoomServlet extends ClickServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
//		GameLog.getInstance().setLevel(GameLog.DEBUG_LEVEL);
		BoomProperties.load();
		PatpatFunc.loadMessage();
		FixDataLoader.init(getServletContext());
		BoomGameMapEnum.init(getServletContext());
		FridayEndpoint.initFridayBotToken();
		VoteFuncs.initRewardID();
	}

	@Override
	public void destroy() {
		super.destroy();
		FixDataLoader.destroy(getServletContext());
	}

}
