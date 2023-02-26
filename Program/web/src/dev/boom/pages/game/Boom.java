package dev.boom.pages.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dev.boom.common.enums.EventFlagEnum;
import dev.boom.common.game.GameTypeEnum;
import dev.boom.game.boom.BoomCharacterType;
import dev.boom.game.boom.BoomCharater;
import dev.boom.game.boom.BoomGame;
import dev.boom.game.boom.BoomGameManager;
import dev.boom.game.boom.BoomGameMapEnum;
import dev.boom.game.boom.BoomSoundTrackEnum;
import dev.boom.pages.Game;
import dev.boom.services.BoomGameItem;
import dev.boom.services.BoomGameItemService;
import dev.boom.services.BoomPlayerStageService;
import dev.boom.services.BoomSeason;
import dev.boom.services.BoomSeasonService;
import dev.boom.services.BoomStage;
import dev.boom.services.BoomStageService;
import dev.boom.socket.SocketSessionPool;
import dev.boom.socket.endpoint.BoomGameLobbyEndPoint;

public class Boom extends Game {

	private static final long serialVersionUID = 1L;
	
	public Boom() {
		initTheme(null);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(importJs("/js/game/boom-images.js"));
		headElements.add(importJs("/js/game/boom.js"));
		headElements.add(importCss("/css/game/boom.css"));
		return headElements;
	}
	
	@Override
	public void onRender() {
		super.onRender();
		if (userInfo != null) {
			int itemCount = BoomGameManager.ITEM_IMAGE_ITEM_COUNT_PER_ROW;
			addModel("itm_cnt", itemCount);
			initBoomSeasonStage();
			String token = SocketSessionPool.generateValidToken(BoomGameLobbyEndPoint.ENDPOINT_NAME, userInfo);
			String params = String.format("?%s=%s", BoomGameLobbyEndPoint.VALIDATION_KEY, token);
			addModel("token", getSocketUrl(BoomGameLobbyEndPoint.SOCKET_PATH, params));
			List<BoomGameItem> itemsList = BoomGameItemService.getItemsList();
			Collections.sort(itemsList, new Comparator<BoomGameItem>() {

				@Override
				public int compare(BoomGameItem o1, BoomGameItem o2) {
					// TODO Auto-generated method stub
					return o2.getId() - o1.getId();
				}
			});
			List<BoomGameMapEnum> mapList = new ArrayList<>();
			addModel("user", userInfo);
			addModel("characters", BoomCharater.values());
			addModel("char_types", BoomCharacterType.values());
			addModel("sound_tracks", BoomSoundTrackEnum.values());
			addModel("items", itemsList);
			StringBuilder sb = new StringBuilder();
			sb.append("const ITM_CNT = ").append(itemCount).append(";");
			sb.append("const TREE_CNT = ").append(BoomGameManager.TREE_IMAGE_TREE_COUNT_PER_ROW).append(";");
			sb.append("const MAP_ITEMS = new Map();");
			sb.append("const MAP_CHARS = new Map();");
			for (BoomGameMapEnum map : BoomGameMapEnum.values()) {
				if (map.getProb() > 0) {
					mapList.add(map);
				}
				int[][] items = map.getItems();
				sb.append("MAP_ITEMS.set(").append(map.getId()).append(",[");
				for (int[] item : items) {
					sb.append(item[0]).append(",");
				}
				sb.append("]);");
				sb.append("MAP_CHARS.set(").append(map.getId()).append(",").append(map.isCharClasses()).append(");");
			}
			addModel("maps", mapList);
			addModel("js_variable", sb.toString());
			List<BoomGame> list;
			if (userInfo.isGameAdmin()) {
				addModel("inspector", "1");
				list = BoomGameManager.getListNotFinishedGame(userInfo.getId());
			} else {
				list = BoomGameManager.getListAvailableGame(userInfo.getId());
			}
			if (list.isEmpty()) {
				return;
			} else if (!userInfo.isGameAdmin()){
				list = list.stream().filter(bg -> {
					return BoomPlayerStageService.hasAccess(userInfo.getId(), bg.getStage());
				}).collect(Collectors.toList());
			}
			addModel("list", list);
		}
		
	}
	
	private void initBoomSeasonStage() {
		boolean isTournament = getWorldInfo().isActiveEventFlag(EventFlagEnum.BOOM_TOURNAMENT);
		if (!isTournament) {
			return;
		}
		BoomSeason boomSeason = BoomSeasonService.getCurrentBoomSeason();
		if (boomSeason == null) {
			return;
		}
		List<BoomStage> stageList = BoomStageService.getBoomStageListAll(String.format("WHERE season_id = %d", boomSeason.getId()));
		if (stageList == null || stageList.isEmpty()) {
			return;
		}
		addModel("stage_list", stageList);
	}

	@Override
	protected int getGameIndex() {
		return GameTypeEnum.BOOM.getIndex();
	}
}
