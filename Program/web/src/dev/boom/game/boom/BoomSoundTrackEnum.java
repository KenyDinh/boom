package dev.boom.game.boom;

import java.util.ArrayList;
import java.util.List;

import dev.boom.common.CommonMethod;

public enum BoomSoundTrackEnum {

	TRACK_01(1, "Bots Boom Bang", "sound_track_1.mp3"),
	TRACK_02(2, "Chillin' With Bots", "sound_track_2.mp3"),
	TRACK_03(3, "Funky Bots", "sound_track_3.mp3"),
	TRACK_04(4, "", "sound_track_4.mp3"),
	TRACK_05(5, "The Vile Grove", "sound_track_5.wav", false),
	TRACK_06(6, "Ludum Dare 38 - Track 10", "sound_track_6.wav", false),
	TRACK_07(7, "Ludum Dare 38 - Track 4", "sound_track_7.wav"),
	TRACK_08(8, "Ludum Dare 32 - Track 3", "sound_track_8.wav", false),
	TRACK_09(9, "Ludum Dare 28 - Track 1", "sound_track_9.wav"),
	TRACK_10(10, "Ludum Dare 30 - Track 7", "sound_track_10.wav"),
	;
	
	private int id;
	private String name;
	private String fileName;
	private boolean random;

	private BoomSoundTrackEnum(int id, String name, String fileName) {
		this.id = id;
		this.name = name;
		this.fileName = fileName;
		this.random = true;
	}
	
	private BoomSoundTrackEnum(int id, String name, String fileName, boolean random) {
		this.id = id;
		this.name = name;
		this.fileName = fileName;
		this.random = random;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFileName() {
		return fileName;
	}
	
	public boolean isRandom() {
		return this.random;
	}
	
	private static BoomSoundTrackEnum[] getRandomList() {
		List<BoomSoundTrackEnum> randomList = new ArrayList<>();
		for (BoomSoundTrackEnum track : BoomSoundTrackEnum.values()) {
			if (track.isRandom()) {
				randomList.add(track);
			}
		}
		return randomList.toArray(new BoomSoundTrackEnum[randomList.size()]);
	}
	
	public static int getRandom(BoomSoundTrackEnum[] list) {
		if (list == null || list.length == 0) {
			list = getRandomList();
		}
		if (list.length == 0) {
			return BoomSoundTrackEnum.TRACK_01.getId();
		}
		if (list.length == 1) {
			return list[0].getId();
		}
		int rand = CommonMethod.random(list.length);
		if (rand >= 0 && rand < list.length) {
			return list[rand].getId();
		}
		return BoomSoundTrackEnum.TRACK_01.getId();
	}
	
	public static int getRandom() {
		return getRandom(null);
	}

}
