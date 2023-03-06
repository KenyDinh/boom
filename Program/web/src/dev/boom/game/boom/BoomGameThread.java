package dev.boom.game.boom;

public class BoomGameThread extends Thread {

	private BoomGame boom;
	private long avgTime;

	public BoomGameThread(BoomGame boom) {
		this.boom = boom;
		this.avgTime = (1000 / this.boom.getFsp());
	}

	@Override
	public void run() {
		try {
			while (!this.boom.isFinished()) {
				update();
				sleep(this.avgTime);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		update();
		BoomGameManager.stopBoomGame(this.boom);
	}

	private void update() {
		this.boom.update();
	}

}
