package com.killerappzz.spider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.killerappzz.spider.engine.Game;
import com.killerappzz.spider.engine.GameData;
import com.killerappzz.spider.engine.GameData.EndGameCondition;
import com.killerappzz.spider.engine.GameFlowEvent;
import com.killerappzz.spider.menus.OptionsActivity;
import com.killerappzz.spider.menus.VictoryActivity;
import com.killerappzz.spider.rendering.CanvasSurfaceView;

public class MainActivity extends Activity {
	
    private CanvasSurfaceView mCanvasSurfaceView;
    private View mPauseMenu = null;
    private View mGameOverMenu = null;
    private View mQuitGameDialog = null;
    private TextView gameOverMenuDescription;
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
        mGameOverMenu = findViewById(R.id.gameOverMenu);
        mQuitGameDialog = findViewById(R.id.quitGameDialog);
        Typeface font = Typeface.createFromAsset(
        		getAssets(), Constants.MAIN_MENU_FONT_ASSET);
        // runtime config for Pause menu
        inflatePauseMenu(font);
        // runtime config for GameOver menu
        inflateGameOverMenu(font);
        // runtime config for Quit Game menu
        inflateQuitGameMenu(font);
        game = new Game(this);
        // load the game
        game.load(this);
        mCanvasSurfaceView.setRenderer(game.getRenderer());
    }
    
    /* Load up the necessary elements for the quit game menu*/
    private void inflateQuitGameMenu(Typeface font) {
    	TextView quitGameDialogTitle = (TextView)findViewById(R.id.quitGameDialogTitle);
    	quitGameDialogTitle.setTypeface(font);
    	
        Button cancelButton = (Button)findViewById(R.id.quitGame_cancel);
        cancelButton.setTypeface(font);
        cancelButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		if(mQuitGameDialog != null) {
        			mQuitGameDialog.setVisibility(View.GONE);
        			game.onResume();
        		}
        	}
        });
        
        Button quitButton = (Button)findViewById(R.id.quitGame_OK);
        quitButton.setTypeface(font);
        quitButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		// this activity is done
        		finish();
        	}
        });		
	}

	/* Load up the necessary elements for the game over menu*/
    private void inflateGameOverMenu(Typeface font) {
    	TextView gameOverMenuTitle = (TextView)findViewById(R.id.gameOverMenuText);
    	gameOverMenuTitle.setTypeface(font);
    	
    	gameOverMenuDescription = (TextView)findViewById(R.id.gameOverMenuDescription);
    	gameOverMenuDescription.setTypeface(font);
        
        Button restartButton = (Button)findViewById(R.id.gameOver_restartButton);
        restartButton.setTypeface(font);
        restartButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		hideGameOverMenu();
        		game.restartLevel();
        	}
        });
        
        Button quitButton = (Button)findViewById(R.id.gameOver_quitButton);
        quitButton.setTypeface(font);
        quitButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		finish();
        	}
        });
	}

	/* Load up the necessary elements for the pause menu*/
    private void inflatePauseMenu(Typeface font) {
    	TextView pausedTitle = (TextView)findViewById(R.id.pausedMenuText);
    	pausedTitle.setTypeface(font);
        
        Button resumeButton = (Button)findViewById(R.id.pause_resumeButton);
        resumeButton.setTypeface(font);
        resumeButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		hidePauseMessage();
        		game.onResume();
        	}
        });
        
        Button restartButton = (Button)findViewById(R.id.pause_restartButton);
        restartButton.setTypeface(font);
        restartButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		hidePauseMessage();
        		game.restartLevel();
        	}
        });
        
        Button optionsButton = (Button)findViewById(R.id.pause_optionsButton);
        optionsButton.setTypeface(font);
        optionsButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent OptionsIntent = new Intent(MainActivity.this, OptionsActivity.class);
        		startActivity(OptionsIntent);
        	}
        });
        
        Button quitButton = (Button)findViewById(R.id.pause_quitButton);
        quitButton.setTypeface(font);
        quitButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		finish();
        	}
        });
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
    	// Now's a good time to run the GC.
        Runtime r = Runtime.getRuntime();
        r.gc();
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
    			// show quit game dialog
    			if(mQuitGameDialog != null) {
    				mQuitGameDialog.setVisibility(View.VISIBLE);
    				game.onPause();
    			}
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
            mPauseMenu.setClickable(true);
    	}
	}

	private void hidePauseMessage() {
		if (mPauseMenu != null) {
    		mPauseMenu.setVisibility(View.GONE);
    		mPauseMenu.setClickable(false);
    	}
	}
	
	private void showGameOverMenu() {
    	if (mGameOverMenu != null) {
    		gameOverMenuDescription.setText(
    				EndGameCondition.descriptionStrings[
    				    game.getController().getData().getEndGameReason().ordinal()]);
    		mGameOverMenu.setVisibility(View.VISIBLE);
    	}
	}

	private void hideGameOverMenu() {
		if (mGameOverMenu != null) {
    		mGameOverMenu.setVisibility(View.GONE);
    	}
	}

    /*
     *  When the game thread needs to stop its own execution (to go to a new level, or restart the
     *  current level), it registers a runnable on the main thread which orders the action via this
     *  function. @see replicaisland the AndouKun#onGameFlowEvent method 
     */
	public void onGameFlowEvent(int eventCode) {
		Intent i;
		switch (eventCode) {
			case GameFlowEvent.EVENT_GAME_OVER: 
				showGameOverMenu();
	        	game.onPause();
				break;
			case GameFlowEvent.EVENT_VICTORY: 
				game.stop();
				i = new Intent(this, VictoryActivity.class);
				// send the game data object to the activity
				i.putExtra(GameData.class.getPackage().getName(), game.getController().getData());
                startActivity(i);
				finish();
				// another good moment to run the GC.
	            Runtime r = Runtime.getRuntime();
	            r.gc();
				break;
			case GameFlowEvent.EVENT_RESTART_LEVEL:
				game.restartLevel();
				break;
			default:
				Log.e(Constants.LOG_TAG, "Unrecognized event code: " + eventCode);
				break;
		}
	}

}