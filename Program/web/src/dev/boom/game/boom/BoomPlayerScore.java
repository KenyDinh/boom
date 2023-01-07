package dev.boom.game.boom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoomPlayerScore {
	
	private Map<Long, Map<String, Integer>> playerScores;// PID => {round : score}
	private int roundIndex;
	
	public BoomPlayerScore() {
		this.playerScores = new HashMap<>();
		this.roundIndex = 1;
	}
	
	public void addScore(long playerId, int score) {
		int newScore = score;
		Map<String, Integer> mp;
		if (playerScores.containsKey(playerId)) {
			mp = playerScores.get(playerId);
		} else {
			mp = new HashMap<>();
			playerScores.put(playerId, mp);
		}
		String key = getKey();
		if (mp.containsKey(key)) {
			newScore += mp.get(key);
		}
		mp.put(key, newScore);
	}
	
	public int getPlayerScore(long playerId) {
		if (playerScores.containsKey(playerId)) {
			Map<String, Integer> mp = playerScores.get(playerId);
			String key = getKey();
			if (mp.containsKey(key)) {
				return mp.get(key);
			}
		}
		return 0;
	}
	
	public List<Map<String, Map<String, Integer>>> getSendData(List<BoomPlayer> playerList) {
		List<Map<String, Map<String, Integer>>> ret = new ArrayList<>();
		for (BoomPlayer bp : playerList) {
			long pid = bp.getId();
			if (!playerScores.containsKey(pid)) {
				continue;
			}
			Map<String, Map<String, Integer>> mapNameScore = new HashMap<>();
			mapNameScore.put(bp.getFullname(), playerScores.get(pid));
			ret.add(mapNameScore);
		}
		return ret;
	}
	
	public void nextRound() {
		this.roundIndex++;
	}
	
	public String getKey() {
		return String.format("r%d", this.roundIndex);
	}
}
