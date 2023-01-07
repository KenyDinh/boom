package dev.boom.game.boom;

public class BoomGameThread extends Thread {

	private BoomGame boom;
	private long avgTime;
	
	public BoomGameThread(BoomGame boom) {
		this.boom = boom;
		this.avgTime = (BoomGameManager.NANO_SECOND / this.boom.getFsp());
	}
	
	@Override
	public void run() {
		long timeElapse = 0;
		long start = System.nanoTime();
		while (!this.boom.isFinished()) {
			timeElapse = System.nanoTime() - start;
			if (timeElapse >= avgTime) {
				update();
				start = System.nanoTime();
			}
		}
		update();
		BoomGameManager.stopBoomGame(this.boom);
	}
	
	private void update() {
		this.boom.update();
	}

}
