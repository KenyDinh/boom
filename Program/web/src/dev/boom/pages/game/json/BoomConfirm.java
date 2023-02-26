package dev.boom.pages.game.json;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonMethod;
import dev.boom.common.enums.EventFlagEnum;
import dev.boom.core.GameLog;
import dev.boom.game.boom.BoomCharater;
import dev.boom.game.boom.BoomGame;
import dev.boom.game.boom.BoomGameManager;
import dev.boom.game.boom.BoomGameMapEnum;
import dev.boom.game.boom.BoomPlayer;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.BoomGroup;
import dev.boom.services.BoomGroupService;
import dev.boom.services.BoomPlayerStage;
import dev.boom.services.BoomPlayerStageService;
import dev.boom.services.BoomSeason;
import dev.boom.services.BoomSeasonService;
import dev.boom.services.BoomStage;
import dev.boom.services.BoomStageService;
import dev.boom.socket.SocketSessionPool;
import dev.boom.socket.endpoint.BoomGameEndPoint;

public class BoomConfirm extends JsonPageBase {

	private static final long serialVersionUID = 1L;
	private static final String TYPE_CREATE = "create";
	private static final String TYPE_JOIN = "join";
	private static final String TYPE_INSPECT = "inspect";
	private static final String TYPE_MAP_CHANGE = "map";
	private static final String TYPE_AVT_CHANGE = "avatar";
	private static final String TYPE_MODE_CHANGE = "mode";

	private String type;
	private boolean error = false;
	private String errorMessage;
	private BoomSeason boomSeason;
	private boolean isTournament;
	
	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!initUserInfo()) {
			return false;
		}
		return true;
	}
	
	@Override
	public void onInit() {
		super.onInit();
		type = getContext().getRequestParameter("type");
		boomSeason = BoomSeasonService.getCurrentBoomSeason();
		isTournament = (getWorldInfo().isActiveEventFlag(EventFlagEnum.BOOM_TOURNAMENT) && boomSeason != null);
	}

	@Override
	public void onPost() {
		super.onPost();
		switch (type) {
		case TYPE_CREATE:
			doCreateGame();
			break;
		case TYPE_JOIN:
			doJoinGame();
			break;
		case TYPE_INSPECT:
			doInspectGame();
			break;
		case TYPE_MAP_CHANGE:
			doMapChange();
			break;
		case TYPE_AVT_CHANGE:
			doCharacterChange();
			break;
		case TYPE_MODE_CHANGE:
			doModeChange();
			break;
		default:
			break;
		}
	}

	@Override
	public void onRender() {
		if (error) {
			putJsonData("error", errorMessage);
		}
		super.onRender();
	}
	
	private void doCreateGame() {
		if (BoomGameManager.isPlayerAlreadyInGame(userInfo.getId())) {
			GameLog.getInstance().error("Already in game!");
			error = true;
			errorMessage = "Player is already participated in other game, please join back to continue!";
			return;
		}
		BoomGame boomGame = BoomGameManager.createBoomGame(userInfo.getId(), userInfo.getName());
		if (boomGame == null) {
			GameLog.getInstance().error("Unable to create new game!");
			error = true;
			errorMessage = "Our server cannot carry any more boom game, please wait for other game to be finished!";
			return;
		}
		boomGame.initMap(BoomGameMapEnum.getRandomMap());
		String strInspector = getContext().getRequestParameter("inspector");
		boolean inspector = false;
		if (CommonMethod.isValidNumeric(strInspector, 1, 1)) {
			inspector = true;
		}
		if (!userInfo.isGameAdmin()) {
			if (inspector) {
				GameLog.getInstance().error("Unable to create game in inspector mode!");
				error = true;
				errorMessage = "No permission to create game in inspector mode!";
				return;
			}
			if (isTournament) {
				GameLog.getInstance().error("Unable to create game during the boom tournament!");
				error = true;
				errorMessage = "No permission to create game during the boom tournament!";
				return;
			}
		}
		int stage = 0;
		String strStage = getContext().getRequestParameter("stage");
		if (isTournament && CommonMethod.isValidNumeric(strStage, 1, Integer.MAX_VALUE)) {
			BoomStage boomStage = BoomStageService.getBoomStage(Integer.parseInt(strStage));
			if (boomStage != null) {
				stage = (int)boomStage.getId();
			}
		}
		BoomPlayerStage playerStage = BoomPlayerStageService.getBoomPlayerStage(userInfo.getId(), stage);
		if (!inspector && stage > 0 && playerStage == null) {
			GameLog.getInstance().error("You don't have access to this stage during the boom tournament!");
			error = true;
			errorMessage = "No permission to join the stage during the boom tournament!";
			return;
		}
		boomGame.applyStage(stage);
		int imgId = BoomCharater.CHARACTER_01.getId();
		if (!inspector) {
			BoomPlayer bp = new BoomPlayer(userInfo.getId(), userInfo.getUsername(), BoomCharater.CHARACTER_01, 0, 0, boomGame.getUnitSize(), boomGame.getUnitSize());
			boolean ret = boomGame.allocatePos(bp);
			if (!ret) {
				error = true;
				errorMessage = "No space in game for new player!";
				return;
			}
			if (playerStage != null && playerStage.getGroupId() > 0) {
				BoomGroup boomGroup = BoomGroupService.getBoomGroup(playerStage.getGroupId());
				if (boomGroup != null) {
					bp.setGroupId(boomGroup.getId());
					bp.setGroupName(StringEscapeUtils.unescapeHtml(boomGroup.getName()));
				}
			}
			ret = boomGame.addPlayer(bp);
			if (!ret) {
				error = true;
				errorMessage = "No space in game for new player!";
				return;
			}
			imgId = bp.getImageID();
		} else {
			boomGame.addInspector(userInfo.getId());
		}
		BoomGameManager.onCreateBoomGame(boomGame);
		String token = SocketSessionPool.generateValidToken(BoomGameEndPoint.ENDPOINT_NAME, userInfo);
		String params = String.format("?%s=%s&%s=%s", BoomGameEndPoint.VALIDATION_KEY, token, BoomGameEndPoint.GAME_KEY, boomGame.getGameId());
		putJsonData("host", 1);
		putJsonData("mid", boomGame.getMapId());
		putJsonData("game_id", boomGame.getGameId());
		putJsonData("mid", boomGame.getMapId());
		putJsonData("avatar", imgId);
		putJsonData("game_url", getSocketUrl(BoomGameEndPoint.SOCKET_PATH, params));
		putJsonData("width", boomGame.getWidth());
		putJsonData("height", boomGame.getHeight());
	}
	
	private void doJoinGame() {
		String strGameId = getContext().getRequestParameter("game_id");
		if (StringUtils.isBlank(strGameId)) {
			error = true;
			errorMessage = "No game found!";
			return;
		}
		BoomGame boomGame = BoomGameManager.getBoomGame(strGameId);
		if (boomGame == null) {
			error = true;
			errorMessage = "No game found!";
			return;
		}
		if (boomGame.isExistInspector(userInfo.getId())) {
			error = true;
			errorMessage = "You already joined this game as inspector! Choose inspector mode!";
			return;
		}
		boolean exist = boomGame.isExistPlayerInGame(userInfo.getId());
		if (!exist && !boomGame.isInitState()) {
			error = true;
			errorMessage = "This game has started, cannot join!";
			return;
		}
		if (BoomGameManager.isPlayerAlreadyInGame(userInfo.getId()) && !exist) {
			GameLog.getInstance().error("Already in other game!");
			error = true;
			errorMessage = "Player is already participated in other game, please join back to continue!";
			return;
		}
		BoomPlayer bp;
		if (!exist) {
			BoomPlayerStage playerStage = BoomPlayerStageService.getBoomPlayerStage(userInfo.getId(), boomGame.getStage());
			if (boomGame.getStage() > 0 && playerStage == null) {
				error = true;
				errorMessage = "No permission to join the game!";
				return;
			}
			bp = new BoomPlayer(userInfo.getId(), userInfo.getUsername(), BoomCharater.CHARACTER_01, 0, 0, boomGame.getUnitSize(), boomGame.getUnitSize()); 
			boolean ret = boomGame.allocatePos(bp);
			if (!ret) {
				error = true;
				errorMessage = "No space in game for new player!";
				return;
			}
			if (playerStage != null && playerStage.getGroupId() > 0) {
				BoomGroup boomGroup = BoomGroupService.getBoomGroup(playerStage.getGroupId());
				if (boomGroup != null) {
					bp.setGroupId(boomGroup.getId());
					bp.setGroupName(StringEscapeUtils.unescapeHtml(boomGroup.getName()));
				}
			}
			ret = boomGame.addPlayer(bp);
			if (!ret) {
				error = true;
				errorMessage = "No space in game for new player!";
				return;
			}
		} else {
			bp = boomGame.getBoomPlayerById(userInfo.getId());
			if (bp == null) {
				error = true;
				errorMessage = "Player not found!";
				return;
			}
		}
		String token = SocketSessionPool.generateValidToken(BoomGameEndPoint.ENDPOINT_NAME, userInfo);
		String params = String.format("?%s=%s&%s=%s", BoomGameEndPoint.VALIDATION_KEY, token, BoomGameEndPoint.GAME_KEY, boomGame.getGameId());
		if (boomGame.getHostId() == userInfo.getId()) {
			putJsonData("host", 1);
			putJsonData("mid", boomGame.getMapId());
		}
		putJsonData("game_id", boomGame.getGameId());
		putJsonData("mid", boomGame.getMapId());
		putJsonData("avatar", bp.getImageID());
		putJsonData("game_url", getSocketUrl(BoomGameEndPoint.SOCKET_PATH, params));
		putJsonData("width", boomGame.getWidth());
		putJsonData("height", boomGame.getHeight());
	}
	
	private void doInspectGame() {
		String strGameId = getContext().getRequestParameter("game_id");
		if (StringUtils.isBlank(strGameId)) {
			error = true;
			errorMessage = "No game found!";
			return;
		}
		BoomGame boomGame = BoomGameManager.getBoomGame(strGameId);
		if (boomGame == null) {
			error = true;
			errorMessage = "No game found!";
			return;
		}
		if (boomGame.isExistPlayerInGame(userInfo.getId())) {
			error = true;
			errorMessage = "You can't inspect your own game!";
			return;
		}
		if (!userInfo.isGameAdmin()) {
			error = true;
			errorMessage = "You don't have permission to inspect this game!";
			return;
		}
		boomGame.addInspector(userInfo.getId());
		String token = SocketSessionPool.generateValidToken(BoomGameEndPoint.ENDPOINT_NAME, userInfo);
		String params = String.format("?%s=%s&%s=%s", BoomGameEndPoint.VALIDATION_KEY, token, BoomGameEndPoint.GAME_KEY, boomGame.getGameId());
		putJsonData("game_id", boomGame.getGameId());
		putJsonData("mid", boomGame.getMapId());
		putJsonData("avatar", BoomCharater.CHARACTER_01.getId());
		putJsonData("game_url", getSocketUrl(BoomGameEndPoint.SOCKET_PATH, params));
		putJsonData("width", boomGame.getWidth());
		putJsonData("height", boomGame.getHeight());
	}
	
	private void doMapChange() {
		String strMapId = getContext().getRequestParameter("map_id");
		if (StringUtils.isBlank(strMapId)) {
			error = true;
			errorMessage = "Invalid parameter!";
			return;
		}
		if (!CommonMethod.isValidNumeric(strMapId, 1, Integer.MAX_VALUE)) {
			error = true;
			errorMessage = "Invalid parameter!";
			return;
		}
		int mapID = Integer.parseInt(strMapId);
		BoomGameMapEnum map = null;
		for (BoomGameMapEnum bgm : BoomGameMapEnum.values()) {
			if (bgm.getProb() <= 0) {
				continue;
			}
			if (bgm.getId() == mapID) {
				map = bgm;
				break;
			}
		}
		if (map == null) {
			error = true;
			errorMessage = "Invalid parameter!";
			return;
		}
		String strGameId = getContext().getRequestParameter("game_id");
		if (StringUtils.isBlank(strGameId)) {
			error = true;
			errorMessage = "No game found!";
			return;
		}
		BoomGame boomGame = BoomGameManager.getBoomGame(strGameId);
		if (boomGame == null || boomGame.getHostId() != userInfo.getId()) {
			error = true;
			errorMessage = "No game found!";
			return;
		}
		if (!boomGame.isInitState()) {
			error = true;
			errorMessage = "Unable to change map!";
			return;
		}
		if (boomGame.getMapId() != map.getId()) {
			boomGame.initMap(map);
		}
		putJsonData("width", boomGame.getWidth());
		putJsonData("height", boomGame.getHeight());
	}
	
	private void doCharacterChange() {
		String strAvatarId = getContext().getRequestParameter("id");
		if (!CommonMethod.isValidNumeric(strAvatarId, 1, BoomCharater.values().length - 1)) {
			error = true;
			errorMessage = "Invalid parameter!";
			return;
		}
		String strGameId = getContext().getRequestParameter("game_id");
		if (StringUtils.isBlank(strGameId)) {
			error = true;
			errorMessage = "No game found!";
			return;
		}
		BoomGame boomGame = BoomGameManager.getBoomGame(strGameId);
		if (boomGame == null) {
			error = true;
			errorMessage = "No game found!";
			return;
		}
		if (!boomGame.isInitState() && !boomGame.isPreparing()) {
			error = true;
			errorMessage = "Unable to change character!";
			return;
		}
		BoomPlayer bp = boomGame.getBoomPlayerById(userInfo.getId());
		if (bp == null) {
			error = true;
			errorMessage = "Player not found!";
			return;
		}
		int charID = Integer.parseInt(strAvatarId);
		bp.init(BoomCharater.getById(charID));
		putJsonData("mid", boomGame.getMapId());
		putJsonData("avatar", bp.getImageID());
	}
	
	private void doModeChange() {
		if (isTournament) {
			error = true;
			errorMessage = "Not allow to change mode during tournament!";
			return;
		}
		String strMode = getContext().getRequestParameter("mode");
		if (!CommonMethod.isValidNumeric(strMode, 0, 1)) {
			error = true;
			errorMessage = "Invalid parameter!";
			return;
		}
		int mode = Integer.parseInt(strMode);
		String strGameId = getContext().getRequestParameter("game_id");
		if (StringUtils.isBlank(strGameId)) {
			error = true;
			errorMessage = "No game found!";
			return;
		}
		BoomGame boomGame = BoomGameManager.getBoomGame(strGameId);
		if (boomGame == null || boomGame.getHostId() != userInfo.getId()) {
			error = true;
			errorMessage = "No game found!";
			return;
		}
		if (!boomGame.isInitState()) {
			error = true;
			errorMessage = "Unable to change map!";
			return;
		}
		if (boomGame.getStage() > 0) {
			error = true;
			errorMessage = "No permission to change mode!";
			return;
		}
		boomGame.setPartyRandom(mode == 1);
		putJsonData("mode", mode);
	}
}
