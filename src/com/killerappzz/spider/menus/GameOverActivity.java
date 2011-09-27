package com.killerappzz.spider.menus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.killerappzz.spider.R;
import com.killerappzz.spider.engine.GameData;

/**
 * This one is displayed when player fails to accomplish
 * the goals of the current level
 * 
 * @author fbratu
 *
 */
public class GameOverActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// retrieve game data
		final Intent callingIntent = getIntent();
		GameData data = (GameData)callingIntent.getParcelableExtra(GameData.class.getPackage().getName());
		// TODO game over should be like paused, but less options
		setContentView(R.layout.game_over);
	}
}
