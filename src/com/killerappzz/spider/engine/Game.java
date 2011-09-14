package com.killerappzz.spider.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.GestureDetector;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.ProfileRecorder;
import com.killerappzz.spider.R;
import com.killerappzz.spider.objects.Background;
import com.killerappzz.spider.objects.Banner;
import com.killerappzz.spider.objects.ObjectManager;
import com.killerappzz.spider.objects.Spider;
import com.killerappzz.spider.rendering.GameRenderer;

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
    private final GameData data;
    private static BitmapFactory.Options sBitmapOptions 
      = new BitmapFactory.Options();
    private GestureDetector touchHandler;
	
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
		data = new GameData();
		touchHandler = new GestureDetector(parentActivity, manager);
	}
	
	public void load(Context context) {
		// Sets our preferred image format to 16-bit, 565 format.
		sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		// Make the background.
        // Note that the background image is larger than the screen, 
        // so some clipping will occur when it is drawn.
		Background background = new Background(context, sBitmapOptions, 
				R.drawable.background, screenWidth, screenHeight );
        manager.addObject(background);
        
        // Make the spider
        Spider spider = new Spider(context, sBitmapOptions, 
        		R.drawable.spider, Constants.SPIDER_ANIMATION_FRAMES_COUNT, 
        		Constants.SPIDER_ANIMATION_FPS, screenWidth, screenHeight, data );
        // Spider location.
        int centerX = (this.screenWidth - (int)spider.width) / 2;
        spider.x = centerX;
        spider.y = 0;
        spider.speed = 0.5f * (this.screenWidth + this.screenHeight) / Constants.DEFAULT_SPIDER_SPEED_FACTOR;
        manager.addSpider(spider);
        data.setTotalArea( (this.screenWidth - spider.width) * (this.screenHeight - spider.height) );
        
        // make the statistics banner
        Banner banner = new Banner(context, Constants.STATS_FONT_ASSET,
        		this.screenWidth	, (int)(spider.height / 2), this.screenHeight,
        		data);
        manager.addBanner(banner);

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
	
	public GestureDetector getTouchHandler() {
		return this.touchHandler;
	}
	
	public ObjectManager getObjectManager() {
		return this.manager;
	}
	
}
