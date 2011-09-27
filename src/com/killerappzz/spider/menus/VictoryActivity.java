package com.killerappzz.spider.menus;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.R;
import com.killerappzz.spider.engine.GameData;
import com.killerappzz.spider.engine.GameData.EndGameCondition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Mark the succesful completion of one level
 * 
 * @author fbratu
 *
 */
public class VictoryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// retrieve game data
		final Intent callingIntent = getIntent();
		GameData data = (GameData)callingIntent.getParcelableExtra(GameData.class.getPackage().getName());
		setContentView(R.layout.victory);
		
		// load up sexy font
		Typeface font = Typeface.createFromAsset(
        		getAssets(), Constants.VICTORY_SCREEN_FONT_ASSET);
		
		// update description text
		TextView description = (TextView)findViewById(R.id.victoryScreenDescription);
		description.setText(EndGameCondition.
				descriptionStrings[data.getEndGameReason().ordinal()]);
		
		// update buttons
		Button exitButton = (Button)findViewById(R.id.victoryScreen_exit);
		exitButton.setTypeface(font);
		exitButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		finish();
        	}
        });
		
		Button restartButton = (Button)findViewById(R.id.victoryScreen_restart);
		restartButton.setTypeface(font);
		restartButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		// TODO level restart
        	}
        });
		
		Button nextLevelButton = (Button)findViewById(R.id.victoryScreen_nextLevel);
		nextLevelButton.setTypeface(font);
		nextLevelButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		// TODO goto next level
        	}
        });
		
		// the incrementing views...
		IncrementingTextView claimedSurfaceView = (IncrementingTextView)findViewById(R.id.surface_percent);
		claimedSurfaceView.setTypeface(font);
		claimedSurfaceView.setMode(IncrementingTextView.MODE_PERCENT);
		claimedSurfaceView.setTargetValue(data.getClaimedPercentile());
		
		IncrementingTextView totalScoreView = (IncrementingTextView)findViewById(R.id.total_score);
		totalScoreView.setTypeface(font);
		totalScoreView.setIncrement(Constants.VICTORY_SCREEN_SCORE_INCREMENT);
		totalScoreView.setTargetValue(data.getScore());
		
		IncrementingTextView totalPlayTimeView = (IncrementingTextView)findViewById(R.id.total_play_time);
		totalPlayTimeView.setTypeface(font);
		totalPlayTimeView.setTargetValue(data.getTotalTime());
        totalPlayTimeView.setIncrement(Constants.VICTORY_SCREEN_TIME_INCREMENT);
        totalPlayTimeView.setMode(IncrementingTextView.MODE_TIME);
		
		registerFontStaticElems(font);
	}
	
	/*
	 * This is a dumb method that simply sets the font to all
	 * elements of the victory screen. Due to android limitation
	 * we can't set the typeface from XML, we must do it from code.
	 */
	private void registerFontStaticElems(Typeface font) {
		((TextView)findViewById(R.id.victoryScreenTitle)).setTypeface(font);
		((TextView)findViewById(R.id.victoryScreenDescription)).setTypeface(font);
		((TextView)findViewById(R.id.game_results_surface_claimed)).setTypeface(font);
		((TextView)findViewById(R.id.game_results_total_play_time)).setTypeface(font);
		((TextView)findViewById(R.id.game_results_total_score)).setTypeface(font);
	}

	public static class IncrementingTextView extends TextView {
    	private static final int MODE_NONE = 0;
    	private static final int MODE_PERCENT = 1;
    	private static final int MODE_TIME = 2;
    	
    	private float mTargetValue;
    	private float mIncrement = 1.0f;
    	private float mCurrentValue = 0.0f;
    	private long mLastTime = 0;
    	private int mMode = MODE_NONE;
    	
    	public IncrementingTextView(Context context) {
            super(context);
        }
        
        public IncrementingTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        
        public IncrementingTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }
        
        public void setTargetValue(float target) {
        	mTargetValue = target;
        	postInvalidate();
        }
        
        public void setMode(int mode) {
        	mMode = mode;
        }
        
        public void setIncrement(float increment) {
        	mIncrement = increment;
        }
        
        @Override
        public void onDraw(Canvas canvas) {
            final long time = SystemClock.uptimeMillis();
            final long delta = time - mLastTime;
            if (delta > Constants.INCREMENT_DELAY_MS) {
            	if (mCurrentValue < mTargetValue) {
            		mCurrentValue += mIncrement;
            		mCurrentValue = Math.min(mCurrentValue, mTargetValue);
            		String value;
            		if (mMode == MODE_PERCENT) {
            			value = mCurrentValue + "%";
            		} else if (mMode == MODE_TIME) {
            			float seconds = mCurrentValue;
            			float minutes = seconds / 60.0f;
            			float hours = minutes / 60.0f;
            			
            			int totalHours = (int)Math.floor(hours);
            			float totalHourMinutes = totalHours * 60.0f;
            			int totalMinutes = (int)(minutes - totalHourMinutes);
            			float totalMinuteSeconds = totalMinutes * 60.0f;
            			float totalHourSeconds = totalHourMinutes * 60.0f;
            			int totalSeconds = (int)(seconds - (totalMinuteSeconds + totalHourSeconds));
            			
            			value = totalHours + ":" + totalMinutes + ":" + totalSeconds;
            		} else {
            			value = mCurrentValue + "";
            		}
            		setText(value);
                    postInvalidateDelayed(Constants.INCREMENT_DELAY_MS);
            	}
            }
            super.onDraw(canvas);
        }
    }
	
}
