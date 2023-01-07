package dev.boom.game.boom;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import dev.boom.services.BoomGameItem;
import dev.boom.services.BoomGameItemService;

public class BoomUtils {
	
	private static final int SUDDEN_DEATH_ITEM_COUNT = 5;
	private static final int SUDDEN_DEATH_ITEM_SPAWN_INTERVAL = 10;
	private static final Map<Integer, Integer> MAP_ITEM_SPAWN_PER_PLAYER;
	static {
		MAP_ITEM_SPAWN_PER_PLAYER = new HashMap<>();
		MAP_ITEM_SPAWN_PER_PLAYER.put(1, 3);
		MAP_ITEM_SPAWN_PER_PLAYER.put(2, 3);
		MAP_ITEM_SPAWN_PER_PLAYER.put(3, 3);
		MAP_ITEM_SPAWN_PER_PLAYER.put(4, 4);
		MAP_ITEM_SPAWN_PER_PLAYER.put(5, 4);
		MAP_ITEM_SPAWN_PER_PLAYER.put(6, 5);
		MAP_ITEM_SPAWN_PER_PLAYER.put(7, 5);
		MAP_ITEM_SPAWN_PER_PLAYER.put(8, 6);
		MAP_ITEM_SPAWN_PER_PLAYER.put(9, 6);
		MAP_ITEM_SPAWN_PER_PLAYER.put(10, 8);
	}
	public static String getGenerateGameId() {
		return RandomStringUtils.randomAlphanumeric(BoomGameManager.MAX_LENGHT_BOOM_ID);
	}
	
	public static boolean checkCollision(int x, int y, int w, int h, BoomSprite bs) {
		return checkCollision(x, y, w, h, 
								bs.getX() + bs.getAdjustX(), bs.getY() + bs.getAdjustY(), bs.getWidth() + bs.getAdjustWidth(), bs.getHeight() + bs.getAdjustHeight());
	}
	
	public static boolean checkCollision(BoomSprite bs1, BoomSprite bs2) {
		return checkCollision(bs1.getX() + bs1.getAdjustX(), bs1.getY() + bs1.getAdjustY(), bs1.getWidth() + bs1.getAdjustWidth(), bs1.getHeight() + bs1.getAdjustHeight(), 
								bs2.getX() + bs2.getAdjustX(), bs2.getY() + bs2.getAdjustY(), bs2.getWidth() + bs2.getAdjustWidth(), bs2.getHeight() + bs2.getAdjustHeight());
	}
	
	public static boolean checkCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
		
		if (x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2) {
			return true;
		}
		return false;
	}
	
	public static int getAdjustPosColision(int pos1, int size1, int pos2, int size2) {
		if (pos2 < pos1 && pos1 < pos2 + size2) {
			return (pos2 + size2 );
		}
		if (pos2 < pos1 + size1 && pos1 + size1 < pos2 + size2) {
			return (pos2 - size1);
		}
		return pos1;
	}
	
	public static int getItemCountBaseOnNumberOfPlayer(int num) {
		if (!MAP_ITEM_SPAWN_PER_PLAYER.containsKey(num)) {
			return num;
		}
		return MAP_ITEM_SPAWN_PER_PLAYER.get(num);
	}
	
	public static BoomGameItem getSuddenDeathItem() {
		return BoomGameItemService.getItemById(10);// trap
	}
	
	public static int getSuddenDeathItemCount(long startTime, long currentTime) {
		if (currentTime - startTime < BoomGameManager.TIME_OUT_SUDDEN_DEATH * BoomGameManager.NANO_SECOND) {
			return 0;
		}
		long diff = (currentTime - startTime - BoomGameManager.TIME_OUT_SUDDEN_DEATH * BoomGameManager.NANO_SECOND);
		diff = diff / (SUDDEN_DEATH_ITEM_SPAWN_INTERVAL * BoomGameManager.NANO_SECOND);
		return SUDDEN_DEATH_ITEM_COUNT * (int)diff;
	}
	
	public static boolean isInArray(int value, int[] array) {
		for (int checkVal : array) {
			if (checkVal == value) {
				return true;
			}
		}
		
		return false;
	}
	
}
