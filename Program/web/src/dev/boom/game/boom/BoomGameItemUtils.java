package dev.boom.game.boom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.boom.common.CommonMethod;
import dev.boom.core.GameLog;
import dev.boom.services.BoomGameItem;
import dev.boom.services.BoomGameItemService;

public class BoomGameItemUtils {

	private final Map<Integer, BoomGameItem> LIST_AVAILABLE_ITEMS;
	private Map<Integer, List<Integer>> itemIdMap;
	private Map<Integer, List<Integer>> itemProbMap;
	private Map<Integer, Integer> itemTotalProbMap;
	public BoomGameItemUtils() {
		super();
		LIST_AVAILABLE_ITEMS = BoomGameItemService.getItemsMap();
		itemIdMap = new HashMap<>();
		itemProbMap = new HashMap<>();
		itemTotalProbMap = new HashMap<>();
	}
	
	public void initItemIdProbsList(int[][] itemIds) {
		itemIdMap.clear();
		itemProbMap.clear();
		itemTotalProbMap.clear();
		if (itemIds == null) {
			return;
		}
		int len = itemIds.length;
		for (int i = 0; i < len; i++) {
			if (itemIds[i] == null || itemIds[i].length != 2) {
				continue;
			}
			Integer itemID = itemIds[i][0];
			Integer itemProb = itemIds[i][1];
			BoomGameItem gameItem = LIST_AVAILABLE_ITEMS.get(itemID);
			if (gameItem == null) {
				GameLog.getInstance().error("Item not found : " + itemID);
				continue;
			}
			Integer targetType = gameItem.getTargetType();
			// init
			if (!itemTotalProbMap.containsKey(targetType)) {
				itemTotalProbMap.put(targetType, 0);
				itemIdMap.put(targetType, new ArrayList<>());
				itemProbMap.put(targetType, new ArrayList<>());
			}
			itemIdMap.get(targetType).add(itemID);
			itemProbMap.get(targetType).add(itemProb);
			itemTotalProbMap.put(targetType, itemTotalProbMap.get(targetType) + itemProb);
		}
		//
//		for (Integer targetType : itemTotalProbMap.keySet()) {
//			System.out.println("TargetType : " + targetType);
//			System.out.println("Item IDs list: " + itemIdMap.get(targetType).toString());
//			System.out.println("Item Probs list: " + itemProbMap.get(targetType).toString());
//		}
	}
	
	public boolean checkIfSpawnItem() {
		return (CommonMethod.random(BoomGameManager.ADJUST_PROB_RATE) < BoomGameManager.ITEM_SPAWN_RATE);
	}
	
	public BoomGameItem getBoomGameItemById(int id) {
		return LIST_AVAILABLE_ITEMS.get(id);
	}
	
	public BoomGameItem getSpawnItem(List<BoomPlayer> players) {
		return getSpawnItem(players, false);
	}
	
	public BoomGameItem getSpawnItem(List<BoomPlayer> players, boolean forceSpawn) {
		if (!forceSpawn) {
			if (!checkIfSpawnItem()) {
				return null;
			}
		}
		if (players == null || players.isEmpty()) {
			return getBoomGameItemById(CommonMethod.random(LIST_AVAILABLE_ITEMS.size()));
		}
		int totalProb = 0;
		Map<Integer, Integer> mapTotalProbByType = new HashMap<>();
		Map<Integer, Integer> mapProbSubByType = new HashMap<>();
		for (BoomPlayer bp : players) {
			if (bp.isDead()) {
				continue;
			}
			Integer targetType = getCompatibleItemTargetType(bp);
			if (!mapTotalProbByType.containsKey(targetType)) {
				mapTotalProbByType.put(targetType, 0);
			}
			int addedProb = 100;
			if (mapProbSubByType.containsKey(targetType)) {
				int count = mapProbSubByType.get(targetType);
				addedProb = Math.max(0, addedProb - (count * 20));
				mapProbSubByType.put(targetType, count + 1);
			} else {
				mapProbSubByType.put(targetType, 1);
			}
			totalProb += addedProb;
			mapTotalProbByType.put(targetType, mapTotalProbByType.get(targetType) + addedProb);
		}
		int rand = CommonMethod.random(totalProb);
		int total = 0;
		Integer decidedTargetType = -1;
		for (Integer targetType : mapTotalProbByType.keySet()) {
			total += mapTotalProbByType.get(targetType);
			if (rand < total) {
				decidedTargetType = targetType;
				break;
			}
		}
		// NONE case handled
		Integer targetNone = BoomGameItemTargetType.NONE.getId();
		totalProb = 0;
		if (itemTotalProbMap.containsKey(decidedTargetType)) {
			totalProb += itemTotalProbMap.get(decidedTargetType);
		}
		if (itemTotalProbMap.containsKey(targetNone)) {
			totalProb += itemTotalProbMap.get(targetNone);
		}
		if (totalProb == 0) {
			return getBoomGameItemById(CommonMethod.random(LIST_AVAILABLE_ITEMS.size()));
		}
		rand = CommonMethod.random(totalProb);
		total = 0;
		Integer[] tmpArray = new Integer[] {decidedTargetType, targetNone};
		for (Integer tgtType : tmpArray) {
			if (!itemProbMap.containsKey(tgtType)) {
				continue;
			}
			List<Integer> probsList = itemProbMap.get(tgtType);
			List<Integer> itemIdsList = itemIdMap.get(tgtType);
			if (probsList.size() != itemIdsList.size()) {
				return getBoomGameItemById(CommonMethod.random(LIST_AVAILABLE_ITEMS.size()));
			}
			for (int i = 0; i < probsList.size(); i++) {
				total += probsList.get(i);
				if (rand < total) {
					return getBoomGameItemById(itemIdsList.get(i));
				}
			}
		}
		return null;
	}
	
	private int getCompatibleItemTargetType(BoomPlayer boomPlayer) {
		if (boomPlayer.isBomber()) {
			return BoomGameItemTargetType.BOMBER.getId();
		}
		return BoomGameItemTargetType.MELEE.getId();
	}
	
	
	public List<BoomGameItem> getAvailableItemsList() {
		List<BoomGameItem> ret = new ArrayList<>();
		for (Integer targetType : itemIdMap.keySet()) {
			List<Integer> itemIdsList = itemIdMap.get(targetType);
			for (int i = 0; i < itemIdsList.size(); i++) {
				ret.add(getBoomGameItemById(itemIdsList.get(i)));
			}
		}
		return ret;
	}
	
}
