package dev.boom.game.boom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import dev.boom.common.CommonMethod;
import dev.boom.game.boom.BoomUtils.Rect;
import dev.boom.services.BoomGameItem;
import dev.boom.socket.endpoint.BoomGameEndPoint;
import net.arnx.jsonic.JSON;

public class BoomGame {

	private String gameId;
	private long hostId;
	private String hostName;
	private int fsp = BoomGameManager.BOOM_MAX_GAME_FPS_FIXED;
	private int status;
	private int width;
	private int height;
	private int unitSize;
	private int mapId;
	private int foregroundId;
	private String mapImage;
	private boolean hasCharClasses;
	private long lastCheckHost;
	private long lastItemSpawn;
	private long itemSpawnInterval;
	private int treesCount;
	private long preparingTime;
	private List<BoomSprite> collisionsList;
	private List<BoomGameTeleportPortal> teleportPortalsList;
	private List<BoomItem> itemsList;
	private List<BoomPlayer> playersList;
	private List<BoomGameBomb> bombsList;
	private List<BoomSprite> bombEffects;
	private List<BoomSprite> playersPos;
	private List<BoomSprite> playerAbilityEffects;
	private List<BoomSprite> fireEffectList;
	private List<BoomItem> specialItems;
	private BoomIDGenerator idGenerator;
	private BoomGameItemUtils itemUtils;
	private List<Long> inspectorIdList;
	private BoomPlayerScore playerScore;
	private Map<Long, Integer> playerRanking;
	private int stage;
	private long pauseStartTime = 0l;
	private long pauseDurationTime = 0l;
	private List<Integer> bombTypeList;
	private int soundTrackId;
	private long gameStartTime = 0l;
	private boolean isPartyRandom;
	private List<Map<String, Integer>> specialItemData;
	private long lastSpecialItemSpawn;
	private boolean hasGroupEffect;

	public BoomGame(long hostId, String hostName) {
		super();
		this.hostId = hostId;
		this.hostName = hostName;
		this.gameId = BoomUtils.getGenerateGameId();
		playersList = new ArrayList<>();
		collisionsList = new LinkedList<>();
		teleportPortalsList = new ArrayList<>();
		itemsList = new LinkedList<>();
		bombsList = new LinkedList<>();
		bombEffects = new ArrayList<>();
		playersPos = new ArrayList<>();
		playerAbilityEffects = new ArrayList<>();
		fireEffectList = new ArrayList<>();
		specialItems = new ArrayList<>();
		idGenerator = new BoomIDGenerator();
		itemUtils = new BoomGameItemUtils();
		lastCheckHost = System.nanoTime() + 10 * BoomGameManager.NANO_SECOND;
		inspectorIdList = new ArrayList<>();
		playerScore = new BoomPlayerScore();
		playerRanking = new HashMap<>();
		bombTypeList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
		Collections.shuffle(bombTypeList);
		soundTrackId = BoomSoundTrackEnum.getRandom();
		hasGroupEffect = false;
	}
	
	public void initMap(BoomGameMapEnum map) {
		this.width = map.getWidth();
		this.height = map.getHeight();
		this.unitSize = map.getUnitSize();
		this.mapId = map.getId();
		this.foregroundId = map.getForegroundImgId();
		this.mapImage = map.getImage();
		this.hasCharClasses = map.isCharClasses();
		this.itemUtils.initItemIdProbsList(map.getItems());
		this.itemSpawnInterval = BoomGameManager.NANO_SECOND * map.getItemSpawn();
		this.soundTrackId = map.getSoundTrackId();
		this.specialItemData = map.getSpecialItemData();
		int columns = (this.width / this.unitSize);
		this.treesCount = 0;
		collisionsList.clear();
		teleportPortalsList.clear();
		List<Rect> rectList = BoomUtils.calculateCombineCollision(map.getCollisions(), this.width, this.height);
		if (rectList != null) {
			for (Rect rect : rectList) {
				collisionsList.add(new BoomSprite(0, rect.getStartX(), rect.getStartY(), rect.getWidth(), rect.getHeight()));
			}
		}
		for (int i = 0; i < map.getTeleports().length; i++) {
			int col = i % columns;
			int row = i / columns;
			if (map.getTeleports()[i] != 0) {
				BoomGameTeleportPortal teleportPortal = new BoomGameTeleportPortal(map.getTeleports()[i], 1, col * this.unitSize, row * this.unitSize, this.unitSize, this.unitSize);
				for (BoomGameTeleportPortal bgtp : teleportPortalsList) {
					if (teleportPortal.getLinkedId() == bgtp.getLinkedId()) {
						teleportPortal.setLinkedPortal(bgtp);
						bgtp.setLinkedPortal(teleportPortal);
					}
				}
				teleportPortalsList.add(teleportPortal);
			}
		}
		for (int i = 0; i < map.getTrees().length; i++) {
			if (map.getTrees()[i] != 0) {
				BoomSprite tree = new BoomSprite(map.getTrees()[i], (i % columns) * this.unitSize, ((int)(i / columns) * this.unitSize), this.unitSize, this.unitSize);
				tree.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_TREE);
				collisionsList.add(tree);
				this.treesCount++;
			}
		}
		
		playersPos.clear();
		for (int i = 0; i < map.getPlayersPos().length; i++) {
			if (map.getPlayersPos()[i] != 0 && playersPos.size() < BoomGameManager.BOOM_MAX_PLAYER) {
				BoomSprite pos = new BoomSprite(0, (i % columns) * this.unitSize, ((int)(i / columns) * this.unitSize), this.unitSize, this.unitSize);
				playersPos.add(pos);
			}
		}
		Collections.shuffle(playersPos);
		updatePlayer();
	}
	
	private void updatePlayer() {
		for (BoomPlayer bp : this.playersList) {
			bp.removeFlag(BoomGameManager.BOOM_SPRITE_FLAG_PLAYERS_DEAD);
			if (allocatePos(bp)) {
				bp.setCharClasses(hasCharClasses);
			} else {
				bp.setFlag(BoomGameManager.BOOM_SPRITE_FLAG_PLAYERS_DEAD);
			}
		}
	}
	
	public void preparingStart() {
		this.preparingTime = System.nanoTime();
		this.setStatus(BoomGameStatusEnum.PREPARING.ordinal());
		for (BoomPlayer bp : playersList) {
			playerScore.addScore(bp.getId(), 0);
		}
		checkAndRandomlyGrouping();
		// init group effect
		Map<Long, Integer> memberCount = new HashMap<>();
		for (BoomPlayer bp : this.playersList) {
			if (bp.getGroupId() <= 0) {
				continue;
			}
			int cnt = 1;
			if (memberCount.containsKey(bp.getGroupId())) {
				cnt += memberCount.get(bp.getGroupId());
			}
			memberCount.put(bp.getGroupId(), cnt);
		}
		for (BoomPlayer bp : this.playersList) {
			if (!memberCount.containsKey(bp.getGroupId())) {
				continue;
			}
			if (memberCount.get(bp.getGroupId()) <= 1) {
				continue;
			}
			hasGroupEffect = true;
			bp.initGroupEffect();
		}
	}
	
	public boolean canStart() {
		return !this.playersList.isEmpty();
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public long getHostId() {
		return hostId;
	}

	public void setHostId(long hostId) {
		this.hostId = hostId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getFsp() {
		return fsp;
	}

	public void setFsp(int fsp) {
		this.fsp = fsp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getMapId() {
		return mapId;
	}

	public int getUnitSize() {
		return unitSize;
	}

	public void setUnitSize(int unitSize) {
		this.unitSize = unitSize;
	}

	public String getMapImage() {
		return mapImage;
	}

	public synchronized boolean allocatePos(BoomPlayer player) {
		for (BoomSprite bs : playersPos) {
			if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_POS_OCCUPIED)) {
				continue;
			}
			player.setX(bs.getX());
			player.setY(bs.getY());
			bs.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_POS_OCCUPIED);
			return true;
		}
		return false;
	}

	public List<BoomPlayer> getPlayersList() {
		return playersList;
	}

	public void setPlayersList(List<BoomPlayer> playersList) {
		this.playersList = playersList;
	}
	
	public synchronized boolean addPlayer(BoomPlayer player) {
		if (this.playersList.size() >= playersPos.size()) {
			return false;
		}
		for (BoomPlayer bp : playersList) {
			if (bp.getId() == player.getId()) {
				return true;
			}
		}
		player.setCharClasses(hasCharClasses);
		if (isFinalStage()) {
			int bombTypeIdx = this.playersList.size() % bombTypeList.size();
			player.setBombType(bombTypeList.get(bombTypeIdx));
		} else {
			player.setBombType(1);
		}
		return this.playersList.add(player);
	}
	
	public boolean removePlayer(BoomPlayer player) {
		if (this.playersList.size() == 0) {
			return false;
		}
		return this.playersList.remove(player);
	}
	
	public boolean isFull() {
		return (this.playersList.size() >= playersPos.size());
	}
	
	public boolean isExistPlayerInGame(long id) {
		BoomPlayer bp = getBoomPlayerById(id);
		if (bp == null || (bp.isDead() && bp.getGroupId() <= 0)) {
			return false;
		}
		return true;
	}
	
	public BoomPlayer getBoomPlayerById(long id) {
		for (BoomPlayer bp : this.playersList) {
			if (bp.getId() == id) {
				return bp;
			}
		}
		
		return null;
	}
	
	public BoomPlayerScore getPlayerScore() {
		return playerScore;
	}

	public void setPlayerScore(BoomPlayerScore playerScore) {
		this.playerScore = playerScore;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}
	
	public void applyStage(int stage) {
		setStage(stage);
		setPlayerScore(BoomGameManager.getCacheBoomPlayerScore(stage));
	}
	
	public boolean isInitState() {
		return this.status == BoomGameStatusEnum.INIT.ordinal();
	}

	public boolean isPlaying() {
		return this.status == BoomGameStatusEnum.PLAYING.ordinal();
	}
	
	public boolean isPreparing() {
		return this.status == BoomGameStatusEnum.PREPARING.ordinal();
	}
	
	public boolean isPaused() {
		return this.status == BoomGameStatusEnum.PAUSED.ordinal();
	}
	
	public boolean isResuming() {
		return this.status == BoomGameStatusEnum.RESUME.ordinal();
	}
	
	public boolean isFinished() {
		return this.status == BoomGameStatusEnum.FINISHED.ordinal();
	}
	
	public void playGame() {
		this.status = BoomGameStatusEnum.PLAYING.ordinal();
	}
	
	public void finishGame() {
		this.status = BoomGameStatusEnum.FINISHED.ordinal();
	}
	
	public void pauseGame() {
		this.status = BoomGameStatusEnum.PAUSED.ordinal();
		this.pauseStartTime = System.nanoTime();
		this.pauseDurationTime = 0l;
	}
	
	public void resumeGame() {
		this.preparingTime = System.nanoTime();
		this.status = BoomGameStatusEnum.RESUME.ordinal();
	}
	
	public boolean isFinalStage() {
		return true; // for testing
		//return getStage() == BoomGameStage.FINAL.ordinal();
	}
	
	private long systemTimeCurrent;
	public void update() {
		systemTimeCurrent = System.nanoTime();
		if (isPaused() || isResuming()) {
			pushClientData();
			return;
		}
		removeUnusedObject();
		updateBombs();
		updatePlayers();
		updatePlayerSkillEffect();
		spawnItem();
		checkSpawnSpecialItem();
		checkAndCreateFireWall();
		checkGameEnd();
		checkAndAddPoint();
		pushClientData();
	}
	
	private void checkGameEnd() {
		if (isPlaying()) {
			int countAlive = 0;
			boolean onlySameGroupAlive = true;
			long lastGroupId = -1;
			for (BoomPlayer bp : playersList) {
				if (bp.isDead()) {
					continue;
				}
				if (lastGroupId == -1) {
					lastGroupId = bp.getGroupId();
				} else {
					if (lastGroupId == 0 || lastGroupId != bp.getGroupId()) {
						onlySameGroupAlive = false;
					}
				}
				countAlive++;
			}
			int num = playersList.size();
			if ( (num == 1 && countAlive <= 0) || (num > 1 && countAlive <= 1) || (num > 1 && onlySameGroupAlive) ) { // able to play with 1 user
				setStatus(BoomGameStatusEnum.FINISHED.ordinal());
				return;
			}
		} else if (isInitState()) {
			long t = this.systemTimeCurrent;
			if (!BoomGameEndPoint.checkGameSocketUser(getGameId(), getHostId())) {
				if (t > lastCheckHost + 10 * BoomGameManager.NANO_SECOND) {
					setStatus(BoomGameStatusEnum.FINISHED.ordinal());
					return;
				}
			} else {
				lastCheckHost = t;
			}
		}
	}
	
	private void checkAndAddPoint() {
		int rank = BoomGameManager.BOOM_MAX_PLAYER - playerRanking.size();
		rank = Math.max(rank, 1);
		for (BoomPlayer bp : playersList) {
			if (bp.isDead() && !bp.isRankingChecked()) {
				playerRanking.put(bp.getId(), rank);
				bp.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_PLAYERS_RANKING);
			} else if (!bp.isDead() && bp.isRankingChecked()) {
				if (bp.triggerRevivalRankingCheck()) {
					playerRanking.remove(bp.getId());
					bp.removeFlag(BoomGameManager.BOOM_SPRITE_FLAG_PLAYERS_RANKING);
				}
			}
		}
		if (isFinished()) {
			int countNotRanking = 0;
			for (BoomPlayer bp : playersList) {
				if (!bp.isDead() && !bp.isRankingChecked()) {
					countNotRanking++;
				}
			}
			rank = BoomGameManager.BOOM_MAX_PLAYER - (playerRanking.size() + countNotRanking - 1); // expect 1 player only
			rank = Math.max(rank, 1);
			for (BoomPlayer bp : playersList) {
				if (!bp.isDead() && !bp.isRankingChecked()) {
					playerRanking.put(bp.getId(), rank);
					bp.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_PLAYERS_RANKING);
				}
			}
			if (playerRanking.size() > 0) {
				for (Long pid : playerRanking.keySet()) {
					int index = BoomGameManager.BOOM_MAX_PLAYER - playerRanking.get(pid);
					int score = 0;
					if (index >= 0 && index < BoomGameManager.BOOM_GAME_REWARD_POINT_RANKING.length) {
						score = BoomGameManager.BOOM_GAME_REWARD_POINT_RANKING[index];	
					}
					playerScore.addScore(pid, score);
				}
				playerRanking.clear();
			}
		}
	}
	
	private void extendTimeAfterPausing() {
		this.pauseDurationTime = this.systemTimeCurrent - this.pauseStartTime;
		if (this.pauseDurationTime <= 0) {
			return;
		}
		for (BoomGameBomb bomb : bombsList) {
			bomb.extendDuration(this.pauseDurationTime);
		}
		for (BoomPlayer bp : playersList) {
			bp.extendEffectDuration(this.pauseDurationTime);
		}
		this.gameStartTime += this.pauseDurationTime;
	}
	
	private void pushClientData() {
		long currentTime = systemTimeCurrent;
		Map<String, Object> data = new HashMap<String, Object>();
		// main map
		Map<String, Object> map = new HashMap<>();
		map.put("width", this.width);
		map.put("height", this.height);
		map.put("unitSize", this.unitSize);
		map.put("status", this.status);
		map.put("id", this.mapId);
		map.put("fid", this.foregroundId);
		if (getStage() <= 0) {
			map.put("mode", (this.isPartyRandom ? 2 : 1));
		}
		data.put("map", map);
		// teleport portals
		List<Map<String, Object>> portals = new ArrayList<>();
		for (BoomGameTeleportPortal bgtp : teleportPortalsList) {
			Map<String, Object> portal = new HashMap<>();
			portal.put("x", bgtp.getX());
			portal.put("y", bgtp.getY());
			portal.put("img", (bgtp.isPortalOpen(currentTime) ? 1 : 2));
			portals.add(portal);
		}
		data.put("portal", portals);
		// trees
		List<Map<String, Object>> trees = new ArrayList<>();
		for (BoomSprite bs : collisionsList) {
			if (!bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE)) {
				continue;
			}
			Map<String, Object> tree = new HashMap<>();
			tree.put("x", bs.getX());
			tree.put("y", bs.getY());
			tree.put("img", bs.getImageID());
			trees.add(tree);
		}
		data.put("tree", trees);
		// bombs
		List<Map<String, Object>> bombs = new ArrayList<>();
		for (BoomGameBomb bb : bombsList) {
			if (!bb.isValid()) {
				continue;
			}
			Map<String, Object> bomb = new HashMap<>();
			bomb.put("id", bb.getId());
			bomb.put("x", bb.getX());
			bomb.put("y", bb.getY());
			bomb.put("img", bb.getImageID());
			bomb.put("effect", bb.getEffectsList());
			bombs.add(bomb);
		}
		data.put("bomb", bombs);
		// players
		List<Map<String, Object>> players = new ArrayList<>();
		for (BoomPlayer bp : playersList) {
			Map<String, Object> player = new HashMap<>();
			player.put("id", bp.getId());
			player.put("name", bp.getFullname());
			player.put("x", bp.getX());
			player.put("y", bp.getY());
			player.put("hp", bp.getSendHp());
			player.put("maxHP", bp.getMaxHp());
			player.put("img", bp.getImageID());
			player.put("state", bp.getState().name().toLowerCase());
			player.put("effect", bp.getEffectIdsList());
			player.put("hide", (bp.checkIfHasValidEffect(BoomGameItemEffect.INVISIBLE_MAN) ? 1 : 0));
			player.put("gid", bp.getGroupId());
			player.put("gname", bp.getGroupName());
			if (bp.checkIfHaveSpecialAbilityWithGauge()) {
				player.put("gauge", bp.getCurrentGauge());
			}
			if (bp.isAvailableForRevival()) {
				player.put("rva", 1);
				if (bp.isDead()) {
					player.put("revive", bp.getReviveGauge());
				}
			} else {
				player.put("rva", 0);
			}
			player.put("rv", (bp.checkIfHasValidEffect(BoomGameItemEffect.MAGICAL_REVIVAL) ? 1 : 0));
			players.add(player);
		}
		data.put("player", players);
		// items
		List<Map<String, Object>> items = new ArrayList<>();
		for (BoomItem bi : itemsList) {
			if (!bi.isValid()) {
				continue;
			}
			Map<String, Object> item = new HashMap<>();
			item.put("id", bi.getId());
			item.put("x", bi.getX());
			item.put("y", bi.getY());
			item.put("img", bi.getImageID());
			items.add(item);
		}
		data.put("item", items);
		// special items
		List<Map<String, Object>> spItems = new ArrayList<>();
		for (BoomItem bi : specialItems) {
			if (!bi.isValid()) {
				continue;
			}
			Map<String, Object> spItem = new HashMap<>();
			spItem.put("id", bi.getId());
			spItem.put("x", bi.getX());
			spItem.put("y", bi.getY());
			spItem.put("img", bi.getImageID());
			spItems.add(spItem);
		}
		data.put("spitem", spItems);
		// bomb explosions
		List<Map<String, Object>> explosions = new ArrayList<>();
		for (BoomSprite bs : bombEffects) {
			Map<String, Object> explosion = new HashMap<>();
			explosion.put("x", bs.getX() + bs.getAdjustX());
			explosion.put("y", bs.getY() + bs.getAdjustY());
			explosion.put("w", bs.getWidth() + bs.getAdjustWidth());
			explosion.put("h", bs.getHeight() + bs.getAdjustHeight());
			explosion.put("img", bs.getImageID());
			explosions.add(explosion);
		}
		data.put("explosion", explosions);
		// player's active abilities
		List<BoomSprite> newPlayerAbilityEffects = new ArrayList<>();
		List<Map<String, Object>> abilitieEffects = new ArrayList<>();
		for (BoomSprite ae : playerAbilityEffects) {
			if (ae.is(BoomGameManager.BOOM_SPRITE_FLAG_ABILITY_MOVING)) {
				if (!ae.is(BoomGameManager.BOOM_SPRITE_FLAG_ABILITY_REMOVED)) {
					newPlayerAbilityEffects.add(ae);
				}
			}
			Map<String, Object> abilitieEffect = new HashMap<>();
			abilitieEffect.put("id", ae.getId());
			abilitieEffect.put("x", ae.getX());// + ae.getAdjustX());
			abilitieEffect.put("y", ae.getY());// + ae.getAdjustY());
			abilitieEffect.put("w", ae.getWidth());// + ae.getAdjustWidth());
			abilitieEffect.put("h", ae.getHeight());// + ae.getAdjustHeight());
			abilitieEffect.put("img", ae.getImageID());
			abilitieEffect.put("stt", ae.getState().ordinal());
			abilitieEffects.add(abilitieEffect);
		}
		data.put("ability", abilitieEffects);
		//
		List<Map<String, Object>> fireEffects = new ArrayList<>();
		for (BoomSprite fe : fireEffectList) {
			Map<String, Object> fireEffect = new HashMap<>();
			fireEffect.put("x", fe.getX());
			fireEffect.put("y", fe.getY());
			fireEffect.put("img", fe.getImageID());
			fireEffects.add(fireEffect);
		}
		data.put("fire", fireEffects);
		//
		data.put("stid", this.soundTrackId);
		//
		data.put("isp", this.inspectorIdList);
		//
		Map<String, Object> playerAvas = new HashMap<>();
		for (BoomPlayer bp : playersList) {
			playerAvas.put(bp.getFullname(), bp.getImageID());
		}
		data.put("pava", playerAvas);
		data.put("pscore", this.playerScore.getSendData(playersList));
		//
		if (isPreparing() || isResuming()) {
			long current = systemTimeCurrent;
			long passTime = (current - this.preparingTime) / BoomGameManager.NANO_SECOND;
			int timeCountdown = isPreparing() ? (isExistInspector(getHostId()) ? BoomGameManager.BOOM_GAME_PREPARING_TIME_CD_1 : BoomGameManager.BOOM_GAME_PREPARING_TIME_CD_2) : BoomGameManager.BOOM_GAME_RESUMING_TIME_CD;
			int timeLeft = CommonMethod.minmax(0, timeCountdown - (int)passTime, timeCountdown);
			data.put("prp", timeLeft);
			if (timeLeft <= 0) {
				if (isResuming()) {
					extendTimeAfterPausing();
				} else {
					gameStartTime = current;
				}
				setStatus(BoomGameStatusEnum.PLAYING.ordinal());
			}
			//
		}
		// send data to client
		BoomGameEndPoint.sendSocketGameUpdate(getGameId(), JSON.encode(data));
		// reset data
		bombEffects.clear();
		playerAbilityEffects = newPlayerAbilityEffects;
	}
	
	private void removeUnusedObject() {
		ListIterator<BoomSprite> iteratorCollisions = collisionsList.listIterator();
		while (iteratorCollisions.hasNext()) {
			BoomSprite bs = iteratorCollisions.next();
			if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
				iteratorCollisions.remove();
				this.treesCount--;
			}
		}
		ListIterator<BoomGameBomb> iteratorBombs = bombsList.listIterator();
		while (iteratorBombs.hasNext()) {
			BoomGameBomb bb = iteratorBombs.next();
			if (!bb.isValid()) {
				iteratorBombs.remove();
			}
		}
		ListIterator<BoomItem> iteratorItems = itemsList.listIterator();
		while (iteratorItems.hasNext()) {
			BoomItem bi = iteratorItems.next();
			if (!bi.isValid()) {
				iteratorItems.remove();
			}
		}
		ListIterator<BoomItem> iteratorSpItems = specialItems.listIterator();
		while (iteratorSpItems.hasNext()) {
			BoomItem bi = iteratorSpItems.next();
			if (!bi.isValid()) {
				iteratorSpItems.remove();
			}
		}
	}
	
	private void updateBombs() {
		for (BoomGameBomb bomb : bombsList) {
			bomb.update(getWidth(), getHeight(), collisionsList, playersList, bombsList);
			if (bomb.checkExplode()) {
				bomb.explode(getWidth(), getHeight(), bombEffects, collisionsList, playersList, bombsList, itemsList, idGenerator, itemUtils, playerScore);
			}
		}
		for (BoomPlayer bp : playersList) {
			bp.checkAndCreateNewBomb(collisionsList, bombsList, playersList, idGenerator);
		}
	}
	
	private void updatePlayers() {
		for (BoomPlayer bp : playersList) {
			// update move
			bp.update(getWidth(), getHeight(), collisionsList, playersList, bombsList, itemsList, teleportPortalsList, itemUtils, fireEffectList, specialItems, playerScore);
			playerAbilityEffects.addAll(bp.checkActiveAbility(collisionsList, playersList, bombsList, itemsList, idGenerator, itemUtils, playerScore));
			bp.clearCacheBombExploded();
		}
	}
	
	private void updatePlayerSkillEffect() {
		for (BoomSprite ae : playerAbilityEffects) {
			if (ae.is(BoomGameManager.BOOM_SPRITE_FLAG_ABILITY_MOVING) && !ae.is(BoomGameManager.BOOM_SPRITE_FLAG_ABILITY_REMOVED))
			ae.update(getWidth(), getHeight(), collisionsList, playersList, bombsList, itemsList, idGenerator, itemUtils, playerScore);
		}
	}
	
	private void spawnItem() {
		if (!isPlaying()) {
			return;
		}
		if (this.treesCount > 0) {
			return;
		}
		if (this.itemSpawnInterval <= 0) {
			return;
		}
		if (this.lastItemSpawn == 0) {
			this.lastItemSpawn = this.systemTimeCurrent;
			return;
		}
		long current = this.systemTimeCurrent;
		if (current - this.lastItemSpawn >= this.itemSpawnInterval) {
			int max = BoomUtils.getItemCountBaseOnNumberOfPlayer(this.playersList.size());
			while (this.itemsList.size() < max) {
				BoomGameItem item = this.itemUtils.getSpawnItem(this.playersList, true);
				if (item == null) {
					continue;
				}
				if (item.isHealingItem() && !isEnableHealingItem()) {
					continue;
				}
				boolean ret;
				BoomItem bomItem;
				int attempt = 0;
				do {
					int x = CommonMethod.random(this.getWidth() - 3 * this.unitSize) + this.unitSize;
					int y = CommonMethod.random(this.getHeight() - 3 * this.unitSize) + this.unitSize;
					bomItem = new BoomItem(item.getId(), item.getImageID(), x, y, this.unitSize, this.unitSize);
					ret = addItem(bomItem);
					// deadlock escape
					attempt++;
					if (attempt >= 100) {
						return;
					}
				} while (!ret);
				bomItem.setId(idGenerator.getNextItemId());
				this.itemsList.add(bomItem);
			}
			this.lastItemSpawn = this.systemTimeCurrent;
		}
	}
	
	private boolean addItem(BoomItem boomItem) {
		for (BoomSprite bs : this.collisionsList) {
			if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
				continue;
			}
			if (BoomUtils.checkCollision(bs, boomItem)) {
				return false;
			}
			
		}
		for (BoomPlayer bp : this.playersList) {
			if (bp.isDead()) {
				continue;
			}
			if (BoomUtils.checkCollision(bp, boomItem)) {
				return false;
			}
			
		}
		for (BoomItem bi : this.itemsList) {
			if (!bi.isValid()) {
				continue;
			}
			if (BoomUtils.checkCollision(bi, boomItem)) {
				return false;
			}
			
		}
		return true;
	}
	
	private int getCountFireWall(int depth) {
		int count = 0;
		int hcount = getWidth() / getUnitSize();
		int vcount = getHeight() / getUnitSize();
		for (int i = 0; i < depth; i++) {
			count += (hcount - 2 * i) * 2;
			count += (vcount - 2 * (i + 1)) * 2;
		}
		return count;
	}
	
	private boolean isEnableHealingItem() {
		if (this.fireEffectList.isEmpty()) {
			return true;
		}
		if (this.fireEffectList.size() < getCountFireWall(3)) { // no healing item from 3rd fire wall onward
			return true;
		}
		return false;
	}

	private void checkAndCreateFireWall() {
		if (this.gameStartTime <= 0) {
			return;
		}
		long current = this.systemTimeCurrent;
		long diff = current - this.gameStartTime;
		if (diff < (BoomGameManager.NANO_SECOND * BoomGameManager.TIME_OUT_SUDDEN_DEATH)) {
			return;
		}
		diff = diff - (BoomGameManager.NANO_SECOND * BoomGameManager.TIME_OUT_SUDDEN_DEATH);
		diff = diff / (BoomGameManager.NANO_SECOND * BoomGameManager.FIRE_WALL_SUDDEN_DEATH_INTERVAL);
		if (diff <= 0) {
			return;
		}
		//change soundtrack ???
		//
		int index = CommonMethod.minmax(1, (int) diff, BoomGameManager.MAX_FIRE_WALL_SUDDEN_DEATH);
		int count = 0;
		int hcount = getWidth() / getUnitSize();
		int vcount = getHeight() / getUnitSize();
		for (int i = 0; i < index; i++) {
			if ((hcount - 2 * i) > 0) {
				count += (hcount - 2 * i) * 2;
			}
			if ((vcount - 2 * (i + 1)) > 0) {
				count += (vcount - 2 * (i + 1)) * 2;
			}
		}
		if (this.fireEffectList.size() >= count) {
			return;
		}
		int startIdx = this.fireEffectList.size();
		for (int i = 0; i < (hcount - 2 * (index - 1)); i++) {
			int pos1 = (index - 1) * getUnitSize();
			BoomSprite bs1 = new BoomSprite(1, pos1 + (i * getUnitSize()), pos1, getUnitSize(), getUnitSize());
			this.fireEffectList.add(bs1);
			int pos2 = getHeight() - (index) * getUnitSize();
			BoomSprite bs2 = new BoomSprite(1, pos1 + (i * getUnitSize()), pos2, getUnitSize(), getUnitSize());
			this.fireEffectList.add(bs2);
		}
		for (int i = 0; i < (vcount - 2 * index); i++) {
			int pos1 = (index - 1) * getUnitSize();
			BoomSprite bs1 = new BoomSprite(1, pos1, pos1 + ((i + 1) * getUnitSize()), getUnitSize(), getUnitSize());
			this.fireEffectList.add(bs1);
			int pos2 = getWidth() - (index) * getUnitSize();
			BoomSprite bs2 = new BoomSprite(1, pos2, pos1 + ((i + 1) * getUnitSize()), getUnitSize(), getUnitSize());
			this.fireEffectList.add(bs2);
		}
		if (treesCount > 0 && startIdx < this.fireEffectList.size()) {
			for (BoomSprite bs : collisionsList) {
				if (!bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE)) {
					continue;
				}
				if (bs.is(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED)) {
					continue;
				}
				for (int i = startIdx; i < this.fireEffectList.size(); i++) {
					if (BoomUtils.checkCollision(bs, this.fireEffectList.get(i))) {
						bs.addFlag(BoomGameManager.BOOM_SPRITE_FLAG_TREE_ROMOVED);
						break;
					}
				}
			}
		}
	}
	
	private void checkAndRandomlyGrouping() {
		if (!this.isPartyRandom) {
			return;
		}
		if (this.playersList.size() < 4) {
			return;
		}
		List<BoomPlayer> random = new ArrayList<>(this.playersList);
		Collections.shuffle(random);
		int idx = 0;
		for (BoomPlayer player : random) {
			long gid = (int)(idx / 2) + 1;
			player.setGroupId(gid);
			player.setGroupName(String.format("Pt_%02d", gid));
			idx++;
		}
	}
	
	private void checkSpawnSpecialItem() {
		if (!isPlaying()) {
			return;
		}
		if (!hasGroupEffect) {
			return;
		}
		if (this.specialItemData == null || this.specialItemData.isEmpty()) {
			return;
		}
		if (this.lastSpecialItemSpawn == 0) {
			this.lastSpecialItemSpawn = this.systemTimeCurrent;
			return;
		}
		if (this.systemTimeCurrent - this.lastSpecialItemSpawn >= (BoomGameManager.REVIVAL_ITEM_DURATION_TIME * BoomGameManager.NANO_SECOND)) {
			//remove special items
			this.specialItems.clear();
		}
		if (this.systemTimeCurrent - this.lastSpecialItemSpawn >= (BoomGameManager.REVIVAL_ITEM_RESPAWN_TIME * BoomGameManager.NANO_SECOND)) {
			// spawn special items
			this.specialItems.clear();
			for (Map<String, Integer> itemData : this.specialItemData) {
				this.specialItems.add(new BoomItem(idGenerator.getNextItemId(), itemData.get("id"), itemData.get("img"), itemData.get("x") * this.unitSize, itemData.get("y") * this.unitSize, this.unitSize, this.unitSize));
			}
			this.lastSpecialItemSpawn = this.systemTimeCurrent;
		}
	}
	
	public void playerMove(long playerId, BoomDirectionEnum direction) {
		if (!isPlaying()) {
			return;
		}
		BoomPlayer bPlayer = getBoomPlayerById(playerId);
		if (bPlayer == null) {
			return;
		}
		bPlayer.movePlayer(direction);
	}
	
	public void playerActiveMainAbility(long playerId) {
		if (!isPlaying()) {
			return;
		}
		BoomPlayer bPlayer = getBoomPlayerById(playerId);
		if (bPlayer == null) {
			return;
		}
		bPlayer.activateMainAbility();
	}
	
	public void playerUserAbility(long playerId) {
		if (!isPlaying()) {
			return;
		}
		BoomPlayer bPlayer = getBoomPlayerById(playerId);
		if (bPlayer == null) {
			return;
		}
		//bPlayer.activeAbility();
	}
	
	public boolean addInspector(long id) {
		if (!this.inspectorIdList.contains(id)) {
			this.inspectorIdList.add(id);
			return true;
		}
		return false;
	}
	
	public boolean isExistInspector(long id) {
		return this.inspectorIdList.contains(id);
	}

	public boolean isPartyRandom() {
		return isPartyRandom;
	}

	public void setPartyRandom(boolean isPartyRandom) {
		this.isPartyRandom = isPartyRandom;
	}
	
}
