package dev.boom.game.boom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import dev.boom.common.CommonMethod;

public class BoomUtils {
	
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
	
	public static boolean isInArray(int value, int[] array) {
		for (int checkVal : array) {
			if (checkVal == value) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean boolRandom() {
		return (CommonMethod.random(100) < 50);
	}
	
	public static boolean isDestroyableItem(int id) {
		return true;
	}
	
	public static List<Rect> calculateCombineCollision(int[] collision, int maxW, int maxH) {
		if (collision == null || collision.length == 0) {
			return null;
		}
		try {
			int size = (int)Math.sqrt((maxW * maxH) / collision.length);
			int width = maxW / size;
			int height = maxH / size;
			int[][] array = new int[height][width];
			for (int n = 0; n < collision.length; n++) {
				int i = n / width;
				int j = n % width;
				array[i][j] = collision[n];
			}
			int totalRectArea = 0;
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					totalRectArea += (array[i][j] > 0 ? 1 : 0);
				}
			}
			List<Rect> rectList = new ArrayList<>();
			int rectArea = 0;
			while (rectArea < totalRectArea) {
				Rect rect = findNextRect(array);
				rect.sz = size;
				rectList.add(rect);
				markRect(array, rect);
				rectArea += (rect.x2 - rect.x1 + 1) * (rect.y2 - rect.y1 + 1);
			}
			return rectList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static Rect findNextRect(int[][] array) {
		// find top left corner
		boolean foundCorner = false;
		int W = array[0].length;
		int H = array.length;
		Rect rect = new Rect(0, 0, W - 1, H - 1);
		for (int i = 0; i < W; ++i) {
			for (int j = 0; j < H; ++j) {
				if (array[j][i] == 1) {
					rect.x1 = i;
					rect.y1 = j;
					foundCorner = true;
					break;
				}
			}
			if (foundCorner)
				break;
		}
		// find bottom right corner
		for (int i = rect.x1; i <= rect.x2; ++i) {
			if (array[rect.y1][i] != 1) {
				rect.x2 = i - 1;
				return rect;
			}
			for (int j = rect.y1; j <= rect.y2; ++j) {
				if (array[j][i] != 1) {
					rect.y2 = j - 1;
					break;
				}
			}
		}
		return rect;
	}

	private static void markRect(int[][] array, Rect rect) {
		for (int i = rect.x1; i <= rect.x2; ++i) {
			for (int j = rect.y1; j <= rect.y2; ++j) {
				array[j][i] = 2;
			}
		}
	}
	
	public static class Rect {
		public int x1;
		public int y1;
		public int x2;
		public int y2;
		public int sz;

		public Rect(int x1, int y1, int x2, int y2) {
			super();
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		public int getStartX() {
			return this.x1 * this.sz;
		}
		
		public int getStartY() {
			return this.y1 * this.sz;
		}
		
		public int getWidth() {
			return Math.max(0, (this.x2 - this.x1 + 1) * this.sz);
		}
		
		public int getHeight() {
			return Math.max(0, (this.y2 - this.y1 + 1) * this.sz);
		}
		
	}
}
