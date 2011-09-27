package com.killerappzz.spider.menus;

import com.killerappzz.spider.R;
import com.killerappzz.spider.engine.GameData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
		// TODO sexy game over. rip from Replica Island
		setContentView(R.layout.victory);
	}
	
}
