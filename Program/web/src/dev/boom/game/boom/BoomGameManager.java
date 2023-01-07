package dev.boom.game.boom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dev.boom.core.GameLog;
import dev.boom.socket.endpoint.BoomGameLobbyEndPoint;

public class BoomGameManager {

	
	public static final long NANO_SECOND = 1000000000l;
	public static final int BOMM_MAX_GAME_CREATED = 3;
	public static final int MAX_LENGHT_BOOM_ID = 8;
	public static final int BOOM_MAX_PLAYER = 6;
	public static final int ADJUST_PROB_RATE = 10000;
	public static final int ITEM_SPAWN_RATE = 3000; // 30%
	public static final int BOOM_GAME_ITEM_SHEET_SIZE = 16;
	public static final int BOOM_GAME_PLAYER_ADJUST_SIZE = 8;
	public static final int BOOM_GAME_COLLISION_ADJUST_SIZE = 4;
	public static final int BOOM_GAME_BOOM_ADJUST_SIZE = BOOM_GAME_PLAYER_ADJUST_SIZE;
	public static final int BOOM_GAME_BOMB_EXPLOSION_ADJUST_SIZE = BOOM_GAME_PLAYER_ADJUST_SIZE;
	public static final int BOOM_GAME_ITEM_ADJUST_SIZE = BOOM_GAME_PLAYER_ADJUST_SIZE;
	
	public static final int BOOM_GAME_MAP_COLLISTION_VALUE = 1;
	
	public static final int BOOM_GAME_BOMB_THROWN_SPEED = 30;
	public static final int BOOM_GAME_BOMB_PUSHED_SPEED = 24;
	public static final double BOOM_GAME_BOMB_DELAY_EXPLODE = 3d;
	public static final double BOOM_GAME_BOMB_DELAY_EXPLODE_WITH_FAST_EXPLOSION = 1.5d;
	
	public static final int BOOM_SPRITE_FLAG_TREE = 0x01;
	public static final int BOOM_SPRITE_FLAG_TREE_ROMOVED = 0x02;
	
	public static final int BOOM_SPRITE_FLAG_BOMD_EXPLODED = 0x01;
	public static final int BOOM_SPRITE_FLAG_BOMD_THROWN = 0x02;
	public static final int BOOM_SPRITE_FLAG_BOMD_PUSHED = 0x04;
	public static final int BOOM_SPRITE_FLAG_BOMD_FAST_EXPLODE = 0x08;
	
	public static final int BOOM_SPRITE_FLAG_ABILITY_MOVING = 0x01;
	public static final int BOOM_SPRITE_FLAG_ABILITY_REMOVED = 0x02;
	public static final int BOOM_SPRITE_FLAG_ABILITY_EFFECT_PUSH = 0x04;
	
	public static final int BOOM_SPRITE_FLAG_PLAYERS_DEAD = 0x01;
	public static final int BOOM_SPRITE_FLAG_PLAYERS_RANKING = 0x02;
	
	public static final int BOOM_SPRITE_FLAG_POS_OCCUPIED = 0x01;
	
	public static final int BOOM_SPRITE_FLAG_ITEM_GONE = 0x01;
	
	public static final int BOOM_GAME_REWARD_POINT_ON_KILL = 1;
	
	public static final int BOOM_GAME_PREPARING_TIME_CD_1 = 30; //second
	public static final int BOOM_GAME_PREPARING_TIME_CD_2 = 10; //second
	public static final int BOOM_GAME_RESUMING_TIME_CD = 3;
	public static final int TIME_OUT_SUDDEN_DEATH = 3 * 60;// 3 minutes
	public static final int FIRE_WALL_SUDDEN_DEATH_INTERVAL = 20;// 20 seconds
	public static final int MAX_FIRE_WALL_SUDDEN_DEATH = 7;//
	public static final int FIRE_WALL_SUDDEN_DEATH_ADJUST = 10000;
	public static final int FIRE_WALL_SUDDEN_DEATH_DAMAGE = 25;// 0.25% per 1 fps => 5% max hp damage per second
	public static final long BOOM_GAME_PORTAL_TIME_WAIT = 3;
	
	// BOOM_MAX_PLAYER
	public static final int[] BOOM_GAME_REWARD_POINT_RANKING = new int[BOOM_MAX_PLAYER];
	
	private static final Map<String, BoomGame> CACHE_BOOM_GAME = new HashMap<>();
	private static final Map<String, BoomGameThread> CACHE_BOOM_THREAD = new HashMap<>();
	private static final Map<Integer, BoomPlayerScore> CACHE_BOOM_PLAYER_SCORE = new HashMap<>();

	static {
		for (int i = 0; i < BOOM_MAX_PLAYER; i++) {
			BOOM_GAME_REWARD_POINT_RANKING[i] = (i + 1);
		}
	}
	public static BoomGame getExistGame(long hostId) {
		if (CACHE_BOOM_GAME.isEmpty()) {
			return null;
		}
		for (String gameID : CACHE_BOOM_GAME.keySet()) {
			if (CACHE_BOOM_GAME.get(gameID).getHostId() == hostId) {
				return CACHE_BOOM_GAME.get(gameID);
			}
		}
		return null;
	}
	
	public static BoomGame createBoomGame(long hostId, String hostName) {
		BoomGame game;
		do {
			game = new BoomGame(hostId, hostName);
		} while (CACHE_BOOM_GAME.containsKey(game.getGameId()));
		return game;
	}
	
	public static boolean onCreateBoomGame(BoomGame boomGame) {
		if (boomGame == null) {
			return false;
		}
		if (!addBoomGame(boomGame)) {
			return false;
		}
		if (CACHE_BOOM_THREAD.containsKey(boomGame.getGameId())) {
			return false;
		}
		BoomGameThread thread = new BoomGameThread(boomGame);
		CACHE_BOOM_THREAD.put(boomGame.getGameId(), thread);
		thread.start();
		BoomGameLobbyEndPoint.sendSocketLobbyUpdate();
		return true;
	}
	
	public static boolean startBoomGame(BoomGame boomGame) {
		if (boomGame == null || !boomGame.isInitState() || !boomGame.canStart()) {
			return false;
		}
		boomGame.preparingStart();
		return true;
	}
	
	public static boolean stopBoomGame(BoomGame boomGame) {
		if (boomGame == null) {
			return false;
		}
		if (!CACHE_BOOM_THREAD.containsKey(boomGame.getGameId())) {
			return false;
		}
		boomGame.finishGame();
		CACHE_BOOM_THREAD.remove(boomGame.getGameId());
		CACHE_BOOM_GAME.remove(boomGame.getGameId());
		if (boomGame.getStage() > 0) {
			storeBoomPlayerScore(boomGame.getStage(), boomGame.getPlayerScore());
		}
		BoomGameLobbyEndPoint.sendSocketLobbyUpdate();
		GameLog.getInstance().info("Stop Boom Game : " + boomGame.getGameId());
		return true;
	}

	public static BoomGame getBoomGame(String gameId) {
		if (StringUtils.isBlank(gameId)) {
			return null;
		}
		if (!CACHE_BOOM_GAME.containsKey(gameId)) {
			return null;
		}
		return CACHE_BOOM_GAME.get(gameId);
	}

	public static boolean addBoomGame(BoomGame boomGame) {
		if (CACHE_BOOM_GAME.size() >= BOMM_MAX_GAME_CREATED) {
			return false;
		}
		if (CACHE_BOOM_GAME.containsKey(boomGame.getGameId())) {
			return false;
		}
		CACHE_BOOM_GAME.put(boomGame.getGameId(), boomGame);
		return true;
	}
	
	public static List<BoomGame> getListAvailableGame(long id) {
		List<BoomGame> list = new ArrayList<>();
		for (String gameID : CACHE_BOOM_GAME.keySet()) {
			BoomGame boomGame = CACHE_BOOM_GAME.get(gameID);
			if (boomGame.isFinished()) {
				continue;
			}
			if (boomGame.isExistPlayerInGame(id)) {
				return Arrays.asList(boomGame);
			}
			if (boomGame.isFull() || !boomGame.isInitState()) {
				continue;
			}
			list.add(boomGame);
		}
		return list;
	}
	
	public static List<BoomGame> getListNotFinishedGame(long id) {
		List<BoomGame> list = new ArrayList<>();
		for (String gameID : CACHE_BOOM_GAME.keySet()) {
			BoomGame boomGame = CACHE_BOOM_GAME.get(gameID);
			if (boomGame.isFinished()) {
				continue;
			}
			if (boomGame.isExistPlayerInGame(id)) {
				return Arrays.asList(boomGame);
			}
			list.add(boomGame);
		}
		return list;
	}
	
	public static boolean isPlayerAlreadyInGame(long id) {
		for (String gameID : CACHE_BOOM_GAME.keySet()) {
			BoomGame boomGame = CACHE_BOOM_GAME.get(gameID);
			if (!boomGame.isFinished() && boomGame.isExistPlayerInGame(id)) {
				return true;
			}
		}
		return false;
	}
	
	public static void storeBoomPlayerScore(int stage, BoomPlayerScore playerScore) {
		CACHE_BOOM_PLAYER_SCORE.put(stage, playerScore);
	}
	
	public static BoomPlayerScore getCacheBoomPlayerScore(int stage) {
		if (stage <= 0) {
			return new BoomPlayerScore();
		}
		if (CACHE_BOOM_PLAYER_SCORE.isEmpty()) {
			return new BoomPlayerScore();
		}
		if (!CACHE_BOOM_PLAYER_SCORE.containsKey(stage)) {
			return new BoomPlayerScore();
		}
		BoomPlayerScore bps = CACHE_BOOM_PLAYER_SCORE.get(stage);
		bps.nextRound();
		return bps;
	}
}
