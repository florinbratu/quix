package com.killerappzz.spider.engine;

import com.killerappzz.spider.Constants;

import android.util.Log;

/**
 * The engine, orchestrator of this sharade
 * 
 * @author florin
 *
 */
public class Engine{
	
	public final Game game;
	private GameThread mGameThread;
    private Thread mGame;
    private boolean mRunning;
	
	public Engine(Game theGame) {
		this.game = theGame;
		this.mGameThread = new GameThread(theGame);
	}
	
	/** Starts the game running. */
    public void start() {
        if (!mRunning) {
            assert mGame == null;
            Log.d(Constants.LOG_TAG, "Engine start!");
            mGame = new Thread(mGameThread);
            mGame.setName("Engine");
            mGame.start();
            mRunning = true;
        } else {
            mGameThread.resumeGame();
        }
    }
    
    public void stop() {
        if (mRunning) {
            Log.d(Constants.LOG_TAG, "Engine stop!");
            if (mGameThread.getPaused()) {
                mGameThread.resumeGame();
            }
            mGameThread.stopGame();
            try {
                mGame.join();
            } catch (InterruptedException e) {
                mGame.interrupt();
            }
            mGame = null;
            mRunning = false;
        }
    }
    
    public void onPause() {
    	if(mRunning)
    		mGameThread.pauseGame();
    }
    
    public void onResume() {
    	if(mRunning)
    		mGameThread.resumeGame();
    }

	public boolean isPaused() {
		return (mRunning && mGameThread != null && mGameThread.getPaused());
	}

}
