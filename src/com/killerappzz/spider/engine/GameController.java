package com.killerappzz.spider.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.MainActivity;
import com.killerappzz.spider.R;
import com.killerappzz.spider.objects.Background;
import com.killerappzz.spider.objects.Banner;
import com.killerappzz.spider.objects.Bat;
import com.killerappzz.spider.objects.CollisionSystem;
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
	// collision handler
	private final CollisionSystem collisionHandler;
	// config shit
	private BitmapFactory.Options bitmapOpts;
	// the Vibrator - for haptic feedback
	private Vibrator vibrator;
	// all possible Game events
	private GameFlowEvent[] gameEvents;
	
	public GameController(MainActivity parentActivity, ObjectManager manager) {
		this.manager = manager;
		this.bitmapOpts = new BitmapFactory.Options();
		this.data = new GameData();
		this.collisionHandler = new CollisionSystem();
		this.gameEvents = GameFlowEvent.allEvents(parentActivity);
	}
	
	/**
	 * Loads all objects from the scene. Will be called once, at game/level creation
	 */
	public void loadObjects(Context context, int screenWidth, int screenHeight) {
		// init vibrator
		vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		// Sets our preferred image format to 16-bit, 565 format.
		bitmapOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		// Make the background.
        // Note that the background image is larger than the screen, 
        // so some clipping will occur when it is drawn.
		Background background = new Background(context, bitmapOpts, 
				R.drawable.background, screenWidth, screenHeight );
		background.setZ(0);
		this.manager.add(background);
        
        // Make the spider
        this.spider = new Spider(this, data, context, bitmapOpts, 
        		R.drawable.spider, Constants.SPIDER_ANIMATION_FRAMES_COUNT, 
        		Constants.SPIDER_ANIMATION_FPS, screenWidth, screenHeight);
        // Spider location.
        int centerX = (screenWidth - (int)spider.width) / 2;
        spider.setPosition(centerX, 0);
        spider.speed = 0.5f * (screenWidth + screenHeight) / Constants.DEFAULT_SPIDER_SPEED_FACTOR;
        spider.setZ(1);
        this.manager.add(spider);
        data.setTotalArea( (screenWidth - spider.width) * (screenHeight - spider.height) );
        // register as collisions receiver
        this.collisionHandler.registerCollidee(spider);
        
        // Make the bad bat
        Bat bat = new Bat(context, bitmapOpts, 
        		R.drawable.bat, screenWidth, screenHeight, 
        		Constants.BAT_ANIMATION_FRAMES_COUNT, Constants.BAT_ANIMATION_FPS, data);
        centerX = (screenWidth - (int)bat.width) / 2;
        int centerY = (screenHeight - (int)bat.height) / 2;
        bat.setPosition(centerX, centerY);
        bat.setZ(2);
        bat.speed = 0.5f * (screenWidth + screenHeight) / Constants.DEFAULT_BAT_SPEED_FACTOR;
        bat.startMovement();
        this.manager.add(bat);
        this.spider.setBat(bat);
        // register as collisions "giver"
        this.collisionHandler.registerCollider(bat);
        
        // make the score gain floating text
        ScoreGain gain = new ScoreGain(screenWidth, screenHeight, 
        		(int)(spider.height / 4), Constants.SCORE_TEXT_FPS, data );
        gain.setPosition(centerX, centerY);
        gain.setZ(3);
        gain.speed = spider.speed;
        this.spider.setScoreGain(gain);
        this.manager.add(gain);
        
        // make the statistics banner
        Banner banner = new Banner(context, Constants.STATS_FONT_ASSET,
        		screenWidth	, (int)(spider.height / 2), screenWidth, screenHeight,
        		bitmapOpts, R.drawable.spider_life, data);
        banner.setZ(4);
        this.manager.add(banner);
	}
	
	public void cleanup() {
		// manager cleans up his mess
		manager.cleanup();
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		spider.setLastPosition(spider.getPositionX(), spider.getPositionY());
		spider.setVelocity(e2.getX() - e1.getX(), - e2.getY() + e1.getY() );
		return true;
	}
	
	// haptic feedback
	public void vibrate() {
		// if(vibrator.hasVibrator()) -> this is available only since API V11 bleah!
		// TODO test this on a device with no vibrator!
		vibrator.vibrate(Constants.VIBRATION_PERIOD);
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
	
	public void handleCollisions() {
		this.collisionHandler.handleCollisions();
	}
	
	/* handle game related events like game over, restart etc.
	 * @see MainActivity#onGameFlowEvent 
	 * */
	public void handleEvents() {
		if(data.gameOver()) {
			if(data.victorious())
				gameEvents[GameFlowEvent.EVENT_VICTORY].post();
			else
				gameEvents[GameFlowEvent.EVENT_GAME_OVER].post();
		}
	}

	public void prepareRendering() {
		this.manager.swap();
	}

	public void updateScreen(int width, int height) {
		data.setTotalArea( (width - spider.width) * (height - spider.height) );
	}
	
	public GameData getData() {
		return data;
	}

}
