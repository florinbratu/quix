package com.killerappzz.spider.menus;

import com.killerappzz.spider.MainActivity;
import com.killerappzz.spider.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        
        Button newGameButton = (Button)findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent newGameButton = new Intent(MainMenu.this, MainActivity.class);
        		startActivity(newGameButton);
        	}
        });
        
        Button ContinueGameButton = (Button)findViewById(R.id.continueGameButton);
        ContinueGameButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		// TODO resume from prev progress TODO bundle and shit
        	}
        });
        
        Button optionsButton = (Button)findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		Intent OptionsIntent = new Intent(MainMenu.this, OptionsActivity.class);
        		startActivity(OptionsIntent);
        	}
        });
        
        Button quitButton = (Button)findViewById(R.id.quitButton);
        quitButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		finish();
        	}
        });
    }
}