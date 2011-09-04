package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.Customization;
import com.killerappzz.spider.engine.Game;

/**
 * Handles objects which are displayed on the screen
 * throughout their lifetime
 * 
 * @author florin
 *
 */
public class ObjectManager extends SimpleOnGestureListener{
	
	private final List<DrawableObject> objects; 
	// spider is a special role
	private Spider spider;
	// the Path built by the Spider movement so far
	private final Path claimedPath;
	private final Paint claimedPathPaint;
	private final Game game;
	
	public ObjectManager(Game theGame) {
		this.objects = new LinkedList<DrawableObject>();
		this.game = theGame;
		this.claimedPath = new Path();
		this.claimedPathPaint = Customization.getClaimedPathPaint();
	}
	
	public void addObject(DrawableObject object) {
    	this.objects.add(object);
    }
	
	public void addSpider(Spider spider){
		this.objects.add(spider);
		this.spider = spider;
		this.spider.setClaimedPath(claimedPath);
	}
	
	public void draw(Canvas canvas) {
		for(DrawableObject object : objects) {
    		object.draw(canvas);
    	}
		// draw claimed path
		canvas.drawPath(claimedPath, claimedPathPaint);
	}
	
	public void cleanup() {
		for(DrawableObject obj:objects) {
			obj.cleanup();
		}
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float touchX = e1.getX();
		// need to flip Y position
		float touchY = (float)game.getScreenHeight() - e1.getY();
		if(spiderTouched(touchX, touchY)) {
			Log.d("SPIDER", "Spider touched!");
			spider.setLastPosition(spider.x, spider.y);
			spider.setVelocity(e2.getX() - e1.getX(), - e2.getY() + e1.getY() );
		}
		return true;
	}

	private boolean spiderTouched(float x, float y) {
		float range = (1.0f * (this.game.getScreenHeight() + this.game.getScreenWidth()) 
			* Constants.TOUCH_ERROR_TOLERANCE_PERCENTILE) / 200.0f;
		// calculate the distance from touch point to spider
		double distance = Math.sqrt( (x - spider.x - spider.width * 0.5f) * (x - spider.x - spider.width * 0.5f) 
				+ (y - spider.y) * (y - spider.y) );
		return (float)distance < range;
	}

	public void updatePositions(float timeDeltaSeconds) {
		for(DrawableObject object : objects) {
			if(object.speed!=0 && !(object.getVelocityX() == 0 && object.getVelocityY() == 0)) {
				object.updatePosition(timeDeltaSeconds);
				object.boundsCheck(game.getScreenWidth(), game.getScreenHeight());
			}
		}
	}

}
