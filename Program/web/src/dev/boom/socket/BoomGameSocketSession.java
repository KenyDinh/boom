package dev.boom.socket;

import javax.websocket.Session;

import dev.boom.game.boom.BoomDirectionEnum;
import dev.boom.game.boom.BoomGame;
import dev.boom.game.boom.BoomGameManager;

public class BoomGameSocketSession extends SocketSessionBase {

	private String gameId;

	public BoomGameSocketSession(Session session, String endPointName, String token) {
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
		BoomGame boomGame = BoomGameManager.getBoomGame(gameId);
		if (boomGame == null) {
			return;
		}
		switch (message) {
		case "start":
			if (getUserId() == boomGame.getHostId()) {
				BoomGameManager.startBoomGame(boomGame);
			}
			break;
		case "move_up":
			boomGame.playerMove(getUserId(), BoomDirectionEnum.UP);
			break;
		case "move_down":
			boomGame.playerMove(getUserId(), BoomDirectionEnum.DOWN);
			break;
		case "move_left":
			boomGame.playerMove(getUserId(), BoomDirectionEnum.LEFT);
			break;
		case "move_right":
			boomGame.playerMove(getUserId(), BoomDirectionEnum.RIGHT);
			break;
		case "move_stop":
			boomGame.playerMove(getUserId(), null);
			break;
		case "create_boom":
			if (getUserId() == boomGame.getHostId() && boomGame.isExistInspector(getUserId())) {
				if (boomGame.isInitState()) {
					BoomGameManager.startBoomGame(boomGame);
				} else if (boomGame.isPlaying()) {
					boomGame.pauseGame();
				} else if (boomGame.isPaused()) {
					boomGame.resumeGame();
				}
			} else {
				boomGame.playerActiveMainAbility(getUserId());
			}
			break;
		case "use_ability":
			//boomGame.playerUserAbility(getUserId());
			break;
		case "stop": {
			if (getUserId() == boomGame.getHostId() || boomGame.isExistInspector(getUserId())) {
				boomGame.finishGame();
			}
		}
			break;
		default:
			break;
		}
		
	}

}
