package com.killerappzz.spider.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.R;
import com.killerappzz.spider.objects.Background;
import com.killerappzz.spider.objects.Banner;
import com.killerappzz.spider.objects.DrawableObject;
import com.killerappzz.spider.objects.IBounceable;
import com.killerappzz.spider.objects.ObjectManager;
import com.killerappzz.spider.objects.ScoreGain;
import com.killerappzz.spider.objects.Spider;

/**
 * The game logic is encapsulated here
 * 
 * @author florin
 *
 */
public class GameController extends SimpleOnGestureListener{
	
    // the game data
    private final GameData data;
    // the object manager
    private final ObjectManager manager;
	// spider is a special role
	private Spider spider;
	// config shit
	private BitmapFactory.Options bitmapOpts;
	
	public GameController(ObjectManager manager) {
		this.manager = manager;
		this.bitmapOpts = new BitmapFactory.Options();
		this.data = new GameData();
	}
	
	/**
	 * Loads all objects from the scene. Will be called once, at game/level creation
	 */
	public void loadObjects(Context context, int screenWidth, int screenHeight) {
		// Sets our preferred image format to 16-bit, 565 format.
		bitmapOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		// Make the background.
        // Note that the background image is larger than the screen, 
        // so some clipping will occur when it is drawn.
		Background background = new Background(context, bitmapOpts, 
				R.drawable.background, screenWidth, screenHeight );
		background.z = 0;
		this.manager.add(background);
        
        // Make the spider
        this.spider = new Spider(context, bitmapOpts, 
        		R.drawable.spider, Constants.SPIDER_ANIMATION_FRAMES_COUNT, 
        		Constants.SPIDER_ANIMATION_FPS, screenWidth, screenHeight, data );
        // Spider location.
        int centerX = (screenWidth - (int)spider.width) / 2;
        spider.x = centerX;
        spider.y = 0;
        spider.speed = 0.5f * (screenWidth + screenHeight) / Constants.DEFAULT_SPIDER_SPEED_FACTOR;
        spider.z = 1;
        this.manager.add(spider);
        data.setTotalArea( (screenWidth - spider.width) * (screenHeight - spider.height) );
        
        // make the score gain floating text
        ScoreGain gain = new ScoreGain(screenWidth, screenHeight, 
        		(int)(spider.height / 4), Constants.SCORE_TEXT_FPS, data );
        gain.x = centerX;
        gain.y = centerX;
        gain.z = 3;
        gain.speed = spider.speed;
        this.spider.setScoreGain(gain);
        this.manager.add(gain);
        
        // make the statistics banner
        Banner banner = new Banner(context, Constants.STATS_FONT_ASSET,
        		screenWidth	, (int)(spider.height / 2), screenWidth, screenHeight,
        		bitmapOpts, R.drawable.spider_life, data);
        banner.z = 4;
        this.manager.add(banner);
	}
	
	public void cleanup() {
		// manager cleans up his mess
		manager.cleanup();
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		spider.setLastPosition(spider.x, spider.y);
		spider.setVelocity(e2.getX() - e1.getX(), - e2.getY() + e1.getY() );
		return true;
	}

	public void updatePositions(float timeDeltaSeconds) {
		this.data.addTime(timeDeltaSeconds);
		for(DrawableObject object : manager.getControllerObjects()) {
			if( object.moves() ) {
				object.updatePosition(timeDeltaSeconds);
				if(object instanceof IBounceable) {
					// test for object touching the screen bounds
					if(!object.boundsCheck())
						// test for object reaching the region claimed by the spider
						object.claimedPathCheck(spider.getClaimedPath());
				}
			}
		}
	}

	public void prepareRendering() {
		this.manager.swap();
	}

	public void updateScreen(int width, int height) {
		data.setTotalArea( (width - spider.width) * (height - spider.height) );
	}

}
