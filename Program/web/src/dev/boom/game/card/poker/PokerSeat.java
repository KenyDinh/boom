package dev.boom.game.card.poker;

public class PokerSeat {
	private int index;
	private PokerPlayer player;
	private boolean isRemove;
	private int roleFlag;

	public PokerSeat(int index) {
		super();
		this.index = index;
		this.roleFlag = 0;
	}

	public PokerSeat(int index, PokerPlayer player) {
		super();
		this.index = index;
		this.player = player;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public PokerPlayer getPlayer() {
		return player;
	}

	public void setPlayer(PokerPlayer player) {
		this.isRemove = false;
		this.player = player;
	}
	
	public void emptySeat() {
		player = null;
	}
	
	public boolean isEmpty() {
		return (player == null);
	}
	
	public boolean hasValidPlayer() {
		return (!isEmpty() && !isRemove());
	}

	public boolean isRemove() {
		return isRemove;
	}

	public void setRemove(boolean isRemove) {
		this.isRemove = isRemove;
	}
	
	public boolean is(PokerPlayerRole role) {
		return (roleFlag & role.getBitMask()) != 0;
	}
	
	public void addRole(PokerPlayerRole role) {
		this.roleFlag |= role.getBitMask();
	}
	
	public void resetRole() {
		this.roleFlag = 0;
	}

}
