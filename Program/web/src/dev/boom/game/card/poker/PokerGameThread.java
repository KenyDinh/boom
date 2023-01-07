package dev.boom.game.card.poker;

import dev.boom.common.CommonDefine;

public class PokerGameThread extends Thread {

	private PokerGame pokerGame;
	private long avgTime;
	
	public PokerGameThread(PokerGame pokerGame) {
		super();
		this.pokerGame = pokerGame;
		this.avgTime = (CommonDefine.MILLION_SECOND_SECOND / 8);
	}
	
	@Override
	public void run() {
		System.out.println("START!!!");
		long timeElapse = 0;
		long start = System.currentTimeMillis();
		while (!this.pokerGame.isFinished()) {
			timeElapse = System.currentTimeMillis() - start;
			if (timeElapse >= avgTime) {
				update();
				start = System.currentTimeMillis();
			}
		}
		update();
		PokerGameManager.stopPokerGame(this.pokerGame);
	}
	
	private void update() {
		this.pokerGame.update();
	}
}
