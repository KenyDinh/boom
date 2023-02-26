package dev.boom.game.boom;

public class BoomGameBombEffect {

	public int id;
	public int tp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTp() {
		return tp;
	}

	public void setTp(int tp) {
		this.tp = tp;
	}

	public BoomGameBombEffect() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BoomGameBombEffect(int id, int tp) {
		super();
		this.id = id;
		this.tp = tp;
	}
	
	public BoomGameBombEffect(BoomGameItemEffect effect) {
		super();
		this.id = effect.getImageID();
		switch (effect) {
		case QUICK_BOMBER:
			this.tp = AnimateType.ROTATE.ordinal();
			break;
		default:
			this.tp = AnimateType.NONE.ordinal();
			break;
		}
	}
	
	public enum AnimateType {
		NONE(),
		ROTATE(),
		;
	}
	

}
