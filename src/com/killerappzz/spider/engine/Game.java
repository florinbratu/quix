package com.killerappzz.spider.engine;

import com.killerappzz.spider.ProfileRecorder;
import com.killerappzz.spider.R;
import com.killerappzz.spider.objects.ObjectManager;
import com.killerappzz.spider.objects.Sprite;
import com.killerappzz.spider.rendering.GameRenderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

/**
 * This will encapsulate the logic of the game
 * 
 * @author florin
 *
 */
public class Game {
	
	private final int screenWidth;
	private final int screenHeight;
	
    private final ObjectManager manager;
    private final GameRenderer renderer;
    private static BitmapFactory.Options sBitmapOptions 
      = new BitmapFactory.Options();
	
	public Game(Activity parentActivity) {
		// We need to know the width and height of the display pretty soon,
        // so grab the information now.
        DisplayMetrics dm = new DisplayMetrics();
        parentActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        
        // Clear out any old profile results.
        ProfileRecorder.sSingleton.resetAll();
        
        manager = new ObjectManager();
		renderer = new GameRenderer(manager);
	}
	
	public void load(Context context) {
		// Sets our preferred image format to 16-bit, 565 format.
		sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		// Make the background.
        // Note that the background image is larger than the screen, 
        // so some clipping will occur when it is drawn.
        manager.addObject(
        		new Sprite(context, sBitmapOptions, R.drawable.background));
        
        // Make the spider
        Sprite spider = new Sprite(context, sBitmapOptions, R.drawable.spider);
        // Spider location.
        int centerX = (this.screenWidth - (int)spider.width) / 2;
        spider.x = centerX;
        spider.y = 0;
        manager.addObject(spider);

        // Now's a good time to run the GC.  Since we won't do any explicit
        // allocation during the test, the GC should stay dormant and not
        // influence our results.
        Runtime r = Runtime.getRuntime();
        r.gc();
	}
	
	public void cleanup() {
		manager.cleanup();
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
	
	public GameRenderer getRenderer() {
		return renderer;
	}
	
}
