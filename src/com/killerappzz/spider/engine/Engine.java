package com.killerappzz.spider.engine;

import android.os.SystemClock;

/**
 * The engine, orchestrator of this sharade
 * 
 * @author florin
 *
 */
public class Engine implements Runnable {
	
	public final Game game;
	private long mLastTime;
	
	public Engine(Game theGame) {
		this.game = theGame;
		this.mLastTime = 0;
	}

	@Override
	public void run() {
		final float timeDeltaSeconds = getTimeDelta();
		this.game.getData().addTime(timeDeltaSeconds);
		// calculate new positions
		this.game.getObjectManager().updatePositions(timeDeltaSeconds);
	}

	private float getTimeDelta() {
		final long time = SystemClock.uptimeMillis();
        final long timeDelta = time - mLastTime;
        final float timeDeltaSeconds =
            mLastTime > 0.0f ? timeDelta / 1000.0f : 0.0f;
        mLastTime = time;
        return timeDeltaSeconds;
	}

}
