package com.killerappzz.spider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.killerappzz.spider.engine.Game;
import com.killerappzz.spider.rendering.CanvasSurfaceView;

public class MainActivity extends Activity {
	
    private CanvasSurfaceView mCanvasSurfaceView;
    private View mPauseMenu = null;
    private Game game;
    private long mLastTouchTime = 0L;
    private long mLastRollTime = 0L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load up view contents from layout
        setContentView(R.layout.main);
        mCanvasSurfaceView = (CanvasSurfaceView) findViewById(R.id.glsurfaceview);
        mPauseMenu = findViewById(R.id.pausedMenu);
        game = new Game(this);
        // load the game
        game.load(this);
        mCanvasSurfaceView.setRenderer(game.getRenderer());
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
    	if (!game.isPaused()) {
    		game.getTouchHandler().onTouchEvent(event);
	    	
	        final long time = System.currentTimeMillis();
	        if (event.getAction() == MotionEvent.ACTION_MOVE 
	        		&& time - mLastTouchTime < Constants.TOUCH_EVENTS_FUSE) {
		        // Sleep so that the main thread doesn't get flooded with UI events.
		        try {
		            Thread.sleep(Constants.TOUCH_EVENTS_FUSE);
		        } catch (InterruptedException e) {
		            // No big deal if this sleep is interrupted.
		        }
		        game.getRenderer().waitDrawingComplete();
	        }
	        mLastTouchTime = time;
    	}
        return true;
    }
    
    @Override
    public boolean onTrackballEvent(MotionEvent event) {
    	if (!game.isPaused()) {
	        /* TODO handle trackball events
    		game.onTrackballEvent(event); */
	        final long time = System.currentTimeMillis();
	        mLastRollTime = time;
    	}
        return true;
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
    
    /**
     * Handle keyboard events. Two important events are distinguishable:
     * - press the Back button => we interpret it as Exit command
     * - press the Menu button => display Options menu
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	boolean result = true;
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
			final long time = System.currentTimeMillis();
    		if (time - mLastRollTime > Constants.ROLL_TO_FACE_BUTTON_DELAY &&
    				time - mLastTouchTime > Constants.ROLL_TO_FACE_BUTTON_DELAY) {
    			showDialog(Constants.QUIT_GAME_DIALOG_ID);
    			result = true;
    		}
    	} else if (keyCode == KeyEvent.KEYCODE_MENU) {
    		result = true;
    		if (game.isPaused()) {
    			hidePauseMessage();
    			game.onResume();
    		} else {
    			final long time = System.currentTimeMillis();
    	        if (time - mLastRollTime > Constants.ROLL_TO_FACE_BUTTON_DELAY &&
    	        		time - mLastTouchTime > Constants.ROLL_TO_FACE_BUTTON_DELAY) {
    	        	showPauseMessage();
    	        	game.onPause();
    	        }
    		}
    	} else {
    		/* TODO treat keyboard events
		    result = mGame.onKeyDownEvent(keyCode);
		    // Sleep so that the main thread doesn't get flooded with UI events.
		    try {
		        Thread.sleep(4);
		    } catch (InterruptedException e) {
		        // No big deal if this sleep is interrupted.
		    }*/
    	}
        return result;
    }
    
    private void showPauseMessage() {
    	if (mPauseMenu != null) {
    		mPauseMenu.setVisibility(View.VISIBLE);
    	}
	}

	private void hidePauseMessage() {
		if (mPauseMenu != null) {
    		mPauseMenu.setVisibility(View.GONE);
    	}
	}

	/*
     * Here's where we need to create za Exit menu 
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        if (id == Constants.QUIT_GAME_DIALOG_ID) {
        	
            dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.quit_game_dialog_title)
                .setPositiveButton(R.string.quit_game_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	finish();
                    }
                })
                .setNegativeButton(R.string.quit_game_dialog_cancel, null)
                .setMessage(R.string.quit_game_dialog_message)
                .create();
        }
        return dialog;
    }

}