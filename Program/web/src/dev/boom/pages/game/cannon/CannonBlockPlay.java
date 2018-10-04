package dev.boom.pages.game.cannon;

import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.core.GameLog;
import dev.boom.services.CannonBlockInfo;
import dev.boom.services.CannonBlockService;
import dev.boom.services.CannonPlayerInfo;

public class CannonBlockPlay extends CannonBlockMainPage{
	private static final long serialVersionUID = 1L;
	private CannonPlayerInfo cannonPlayerInfo;
	private CannonBlockInfo cannonBlockInfo;
	
	public CannonBlockPlay() {
	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new CssImport("/css/game/cannonblock/game.css"));
		headElements.add(new JsImport("/js/game/cannonblock/game_play.js"));

		return headElements;
	}
  
	@Override
	public void onInit() {
		cannonPlayerInfo = getCannonPlayer();
		if (cannonPlayerInfo == null) {
			GameLog.getInstance().error("[CannonBlockPlay] cannon player id null!");
			setRedirect(CannonBlockMenu.class);
			return;
		}
		cannonBlockInfo = CannonBlockService.getCannonBlockInfo(cannonPlayerInfo.getUserId(), CannonBlockService.GAME_IS_PLAYING);
		if (cannonBlockInfo == null || cannonBlockInfo.getStatus() != CannonBlockService.GAME_IS_PLAYING) {
			GameLog.getInstance().error("[CannonBlockPlay] game is null or not in playing state!");
			setRedirect(CannonBlockMenu.class);
			return;
		}
		addModel("userHP", cannonBlockInfo.getUserHP());
		addModel("botHP", cannonBlockInfo.getBotHP());
	}

	@Override
	public void onRender() {
		super.onRender();
		
		if (getRedirect() != null) {
			return;
		}
		addModel("gameID", cannonBlockInfo.getGameID());
		addModel("userID", cannonBlockInfo.getUserID());
	}
}
