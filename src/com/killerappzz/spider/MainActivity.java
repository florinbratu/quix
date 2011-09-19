package com.killerappzz.spider;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.killerappzz.spider.engine.Game;
import com.killerappzz.spider.rendering.CanvasSurfaceView;

public class MainActivity extends Activity {
	
    private CanvasSurfaceView mCanvasSurfaceView;
    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCanvasSurfaceView = new CanvasSurfaceView(this);
        game = new Game(this);
        // load the game
        game.load(this);
        mCanvasSurfaceView.setRenderer(game.getRenderer());
        setContentView(mCanvasSurfaceView);
    }
    
    
    /** Recycles all of the bitmaps loaded in onCreate(). */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCanvasSurfaceView.clearEvent();
        mCanvasSurfaceView.stopDrawing();
        game.stop();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	return game.getTouchHandler().onTouchEvent(event);
    }
    
    @Override
    protected void onPause() {
    	game.onPause();
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	game.onResume();
    	super.onResume();
    }

}