package dev.boom.pages.game.cannon;

import java.util.List;

import org.apache.click.element.JsImport;

import dev.boom.core.GameLog;
import dev.boom.pages.Game;
import dev.boom.pages.Home;
import dev.boom.services.CannonPlayerInfo;
import dev.boom.services.CannonPlayerService;

public class CannonBlockMainPage extends Game {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected CannonPlayerInfo cannonPlayerInfo = null;

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (getUserInfo() == null) {
			GameLog.getInstance().error("[CannonBlockMainPage] user is null!");
			setRedirect(Home.class);
			return false;
		}
		cannonPlayerInfo = CannonPlayerService.getCannonPlayerInfo(getUserInfo().getId());
		return true;
	}

	public CannonPlayerInfo getCannonPlayer() {
		return this.cannonPlayerInfo;
	}

	@Override
	public void onRender() {
		super.onRender();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new JsImport("/js/lib/jquery-3.3.1.min.js"));

		return headElements;
	}
	
}
