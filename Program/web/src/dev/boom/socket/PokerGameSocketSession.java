package dev.boom.socket;

import javax.websocket.Session;

import dev.boom.game.card.poker.PokerAction;
import dev.boom.game.card.poker.PokerGame;
import dev.boom.game.card.poker.PokerGameManager;
import dev.boom.game.card.poker.PokerGameStatus;
import net.arnx.jsonic.JSON;

public class PokerGameSocketSession extends SocketSessionBase {

	private String gameId;

	public PokerGameSocketSession(Session session, String endPointName, String token) {
		super(session, endPointName, token);
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	@Override
	public void process(String message) {
		PokerGame pokerGame = PokerGameManager.getPokerGame(gameId);
		if (pokerGame == null) {
			return;
		}
		switch (message) {
		case "start":
			if (getUserId() == pokerGame.getHostId()) {
				PokerGameManager.startPokerGame(pokerGame);
			}
			break;
		case "stop":
			if (getUserId() == pokerGame.getHostId()) {
				pokerGame.setStatus(PokerGameStatus.FINISHED);
			}
			break;
		default: {
			if (message.indexOf("ACTION") >= 0) {
				try {
					PokerAction action = JSON.decode(message.replace("ACTION", ""), PokerAction.class);
					pokerGame.processPlayerAction(getUserId(), action);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
			break;
		}
		
	}

}
