package dev.boom.pages.game.json;

import java.util.List;

import dev.boom.common.enums.UserFlagEnum;
import dev.boom.core.BoomSession;
import dev.boom.core.GameLog;
import dev.boom.game.boom.BoomGame;
import dev.boom.game.boom.BoomGameManager;
import dev.boom.pages.PageBase;
import dev.boom.services.UserInfo;
import dev.boom.services.UserService;

public class BoomLobby extends PageBase {

	private static final long serialVersionUID = 1L;
	
	private  UserInfo userInfo = null;
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		BoomSession boomSession = getBoomSession();
		if (boomSession == null) {
			GameLog.getInstance().error("[BoomLobby] session is null!");
			return false;
		}
		userInfo = UserService.getUserById(boomSession.getId());
		if (userInfo == null) {
			GameLog.getInstance().error("[BoomLobby] user is null!");
			return false;
		}
		return true;
	}
	@Override
	public void onRender() {
		super.onRender();
		List<BoomGame> list;
		if (UserFlagEnum.BOOM_INSPECTOR.isValid(userInfo.getFlag())) {
			addModel("inspector", "1");
			list = BoomGameManager.getListNotFinishedGame(userInfo.getId());
		} else {
			list = BoomGameManager.getListAvailableGame(userInfo.getId());
		}
		if (list.isEmpty()) {
			return;
		}
		addModel("list", list);
	}
	
}
