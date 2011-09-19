package com.killerappzz.spider.engine;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.GestureDetector;

import com.killerappzz.spider.ProfileRecorder;
import com.killerappzz.spider.objects.ObjectManager;
import com.killerappzz.spider.rendering.GameRenderer;

/**
 * This will encapsulate the logic of the game
 * 
 * @author florin
 *
 */
public class Game {
	
	private int screenWidth;
	private int screenHeight;
	// the game logic
    private final GameController controller;
    // the rendering logic
    private final GameRenderer renderer;
    // the engine
    private final Engine engine;
    // the Object Manager
    private final ObjectManager manager;
    private GestureDetector touchHandler;
	private long mLastTime;
	
	public Game(Activity parentActivity) {
		// We need to know the width and height of the display pretty soon,
        // so grab the information now.
        DisplayMetrics dm = new DisplayMetrics();
        parentActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        
        // Clear out any old profile results.
        ProfileRecorder.sSingleton.resetAll();
        
        mLastTime = 0;
        renderer = new GameRenderer(this);
        // the object manager reference. will be shared between the game thread and the renderer thread
        manager = new ObjectManager(renderer);
        controller = new GameController(manager);
		
		engine = new Engine(this);
		touchHandler = new GestureDetector(parentActivity, controller);
	}
	
	public void load(Context context) {
        // load the scene objects
        controller.loadObjects(context, screenWidth, screenHeight);
        // start the game engine
        engine.start();
	}
	
	// game logic update
	public void update(float deltaTimeSeconds) {
		final float timeDeltaSeconds = getTimeDelta();
		// calculate new positions
		this.controller.updatePositions(timeDeltaSeconds);
	}

	private float getTimeDelta() {
		final long time = SystemClock.uptimeMillis();
        final long timeDelta = time - mLastTime;
        final float timeDeltaSeconds =
            mLastTime > 0.0f ? timeDelta / 1000.0f : 0.0f;
        mLastTime = time;
        return timeDeltaSeconds;
	}
	
	public void cleanup() {
		controller.cleanup();
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
	
	// update screen size!
	public void updateScreen(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
		this.manager.updateScreen(width, height);
		this.controller.updateScreen(width, height);
	}
	
	public GameRenderer getRenderer() {
		return renderer;
	}
	
	public GestureDetector getTouchHandler() {
		return this.touchHandler;
	}
	
	public GameController getController() {
		return this.controller;
	}
	
	public void stop() {
		this.engine.stop();
	}

	public void onPause() {
		this.engine.onPause();
	}
	
	public void onResume() {
		this.engine.onResume();
	}

}
