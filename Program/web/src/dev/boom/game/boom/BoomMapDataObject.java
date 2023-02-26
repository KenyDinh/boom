package dev.boom.game.boom;

import java.util.List;

public class BoomMapDataObject {

	public List<MapData> mapData;

	public BoomMapDataObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BoomMapDataObject(List<MapData> mapData) {
		super();
		this.mapData = mapData;
	}

	public List<MapData> getMapData() {
		return mapData;
	}

	public void setMapData(List<MapData> mapData) {
		this.mapData = mapData;
	}
	
	public boolean hasMapData() {
		return (this.mapData != null && !this.mapData.isEmpty());
	}
	
	public class MapData {
		public int id;
		public int[] collision;

		public MapData() {
			super();
			// TODO Auto-generated constructor stub
		}

		public MapData(int id, int[] collision) {
			super();
			this.id = id;
			this.collision = collision;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int[] getCollision() {
			return collision;
		}

		public void setCollision(int[] collision) {
			this.collision = collision;
		}

	}

}
