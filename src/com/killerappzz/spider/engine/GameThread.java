package com.killerappzz.spider.engine;

import com.killerappzz.spider.Constants;

import android.os.SystemClock;
import android.util.Log;

/** 
 * The GameThread contains the main loop for the game engine logic.  It invokes the game graph,
 * manages synchronization of input events, and handles the draw queue swap with the rendering
 * thread.
 */
public class GameThread implements Runnable {
    private long mLastTime;
    
    private Game game;
    private Object mPauseLock;
    private boolean mFinished;
    private boolean mPaused = false;
    private int mProfileFrames;
    private long mProfileTime;
    
    private static final float PROFILE_REPORT_DELAY = 3.0f;
    
    public GameThread(Game game) {
        mLastTime = SystemClock.uptimeMillis();
        this.game = game;
        mPauseLock = new Object();
        mFinished = false;
        mPaused = false;
    }

    public void run() {
        mLastTime = SystemClock.uptimeMillis();
        mFinished = false;
        while (!mFinished) {
                game.getRenderer().waitDrawingComplete();
                
                final long time = SystemClock.uptimeMillis();
                final long timeDelta = time - mLastTime;
                long finalDelta = timeDelta;
                if (timeDelta > 12) {
                    float secondsDelta = (time - mLastTime) * 0.001f;
                    if (secondsDelta > 0.1f) {
                        secondsDelta = 0.1f;
                    }
                    mLastTime = time;
    
                    game.update(secondsDelta);
                    game.getController().prepareRendering();
    
                    final long endTime = SystemClock.uptimeMillis();
                    finalDelta = endTime - time;
                    mProfileTime += finalDelta;
                    mProfileFrames++;
                    if (mProfileTime > PROFILE_REPORT_DELAY * 1000) {
                        final long averageFrameTime = mProfileTime / mProfileFrames;
                        Log.d(Constants.LOG_TAG, "Average: " + averageFrameTime);
                        mProfileTime = 0;
                        mProfileFrames = 0;
                    }
                }
                // If the game logic completed in less than 16ms, that means it's running
                // faster than 60fps, which is our target frame rate.  In that case we should
                // yield to the rendering thread, at least for the remaining frame.
               
                if (finalDelta < 16) {
                    try {
                        Thread.sleep(16 - finalDelta);
                    } catch (InterruptedException e) {
                        // Interruptions here are no big deal.
                    }
                }
                
                synchronized(mPauseLock) {
                    if (mPaused) {
                    	/* TODO if we will have sound, here's where you pause it!
                    	SoundSystem sound = BaseObject.sSystemRegistry.soundSystem;
                    	if (sound != null) {
                    		sound.pauseAll();
                    		BaseObject.sSystemRegistry.inputSystem.releaseAllKeys();
                    	}*/
                        while (mPaused) {
                            try {
                            	mPauseLock.wait();
                            } catch (InterruptedException e) {
                                // No big deal if this wait is interrupted.
                            }
                        }
                    }
                }
        }
        game.getRenderer().emptyQueue();
    }

    public void stopGame() {
    	synchronized (mPauseLock) {
            mPaused = false;
            mFinished = true;
            mPauseLock.notifyAll();
    	}
    }
    
    public void pauseGame() {
        synchronized (mPauseLock) {
            mPaused = true;
        }
    }

    public void resumeGame() {
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }
    
    public boolean getPaused() {
        return mPaused;
    }

}
