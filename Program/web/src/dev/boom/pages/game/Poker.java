package dev.boom.pages.game;

import java.util.List;

import dev.boom.common.game.GameTypeEnum;
import dev.boom.pages.Game;

public class Poker extends Game {

	private static final long serialVersionUID = 1L;
	
	public Poker() {
		initTheme(null);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(importJs("/js/game/poker/poker-images.js"));
		headElements.add(importJs("/js/game/poker/poker.js"));
		return headElements;
	}
	
	@Override
	public void onRender() {
		super.onRender();
		if (userInfo != null) {
			addModel("user", userInfo);
//			PokerGame game = PokerGameManager.createPokerGame(userInfo.getId(), userInfo.getName());
//			if (game != null && PokerGameManager.onCreatePokerGame(game)) {
//				PokerPlayer pp = new PokerPlayer(userInfo.getId(), userInfo.getName(), 1000);
//				game.addPlayer(pp);
//				String token = SocketSessionPool.generateValidToken(PokerGameEndPoint.ENDPOINT_NAME, userInfo);
//				String params = String.format("?%s=%s&%s=%s", PokerGameEndPoint.VALIDATION_KEY, token, PokerGameEndPoint.GAME_KEY, game.getGameId());
//				addModel("socket_url", getSocketUrl(PokerGameEndPoint.SOCKET_PATH, params));
//			}
		}
		
	}

	@Override
	protected int getGameIndex() {
		return GameTypeEnum.POKER.getIndex();
	}
}
