package dev.boom.pages.manage;

import java.util.Map;

import dev.boom.common.CommonMethod;
import dev.boom.common.enums.ManageLogType;
import dev.boom.common.game.GameTypeEnum;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.pages.Home;
import dev.boom.services.ManageLogService;

public class ManageGame extends ManagePageBase{

	private static final long serialVersionUID = 1L;

	public ManageGame() {
	}

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			setRedirect(Home.class);
			return false;
		}
		if (userInfo == null || !userInfo.isAdministrator()) {
			setRedirect(Home.class);
			return false;
		}
		
		return true;
	}
	
	@Override
	public void onInit() {
		super.onInit();
	}

	@Override
	public void onPost() {
		super.onPost();
		Map<String, String[]> params = getContext().getRequest().getParameterMap();
		worldInfo.setGameFlag(0);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				if (key.equals("game-flag")) {
					String[] values = params.get(key);
					for (String strGameId : values) {
						if (CommonMethod.isValidNumeric(strGameId, 1, Integer.MAX_VALUE)) {
							int gameId = Integer.parseInt(strGameId);
							GameTypeEnum gameTypeEnum = GameTypeEnum.valueOf(gameId);
							if (gameTypeEnum == GameTypeEnum.NONE) {
								continue;
							}
							worldInfo.addGameFlag(gameTypeEnum);
						}
					}
				}
			}
		}
		if (CommonDaoFactory.Update(worldInfo.getWorldInfo()) < 0) {
			GameLog.getInstance().error("[ManageGame] update failed!");
			return;
		}
		ManageLogService.createManageLog(userInfo, ManageLogType.GAME_UPDATE);
	}
	
	@Override
	public void onRender() {
		super.onRender();
		StringBuilder sb = new StringBuilder();
		sb.append("<form id=\"form-game\" method=\"post\" action=\"" + getPagePath(this.getClass()) + "\">");
		sb.append("<div class=\"form-group\">");
		sb.append("<div class=\"row\"><div class=\"col-lg-6\">");
		sb.append("<label class=\"font-weight-bold text-info\" style=\"font-size:1.125rem;\">").append("Game List").append("</label>");
		sb.append("</div></div>");
		for (GameTypeEnum gameType : GameTypeEnum.values()) {
			if (gameType == GameTypeEnum.NONE) {
				continue;
			}
			int idx = gameType.getIndex();
			sb.append("<div class=\"row\">");
				sb.append("<div class=\"col-sm-12\">");
				sb.append("<div class=\"custom-control custom-checkbox\">");
					sb.append("<input type=\"checkbox\" class=\"custom-control-input\" id=\"game-" + idx + "\" name=\"game-flag\" value=\"" + idx + "\" " + (worldInfo.isActiveGameFlag(gameType)
							? "checked" : "") + "/>");
					sb.append("<label class=\"custom-control-label text-success\" for=\"game-" + idx + "\">").append(getMessage(gameType.getLabel())).append("</label>");
				sb.append("</div>");
				sb.append("</div>");
			sb.append("</div>");
		}
		sb.append("</div>");
		sb.append("<button type=\"submit\" class=\"btn btn-primary\">").append(getMessage("MSG_GENERAL_SUBMIT")).append("</button>");
		sb.append("</form>");
		addModel("html", sb.toString());
		addBackLink(Home.class, "MSG_MAIN_NAV_BAR_HOME");
	}

	@Override
	protected int getTabIndex() {
		return 1;
	}
	
}
