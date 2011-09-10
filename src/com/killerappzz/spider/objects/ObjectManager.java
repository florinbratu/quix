package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.killerappzz.spider.Constants;
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
	private final Game game;
	
	public ObjectManager(Game theGame) {
		this.objects = new LinkedList<DrawableObject>();
		this.game = theGame;
	}
	
	public void addObject(DrawableObject object) {
    	this.objects.add(object);
    }
	
	public void addSpider(Spider spider){
		this.objects.add(spider);
		this.spider = spider;
	}
	
	public void draw(Canvas canvas) {
		for(DrawableObject object : objects) {
    		object.draw(canvas);
    	}
	}
	
	public void cleanup() {
		for(DrawableObject obj:objects) {
			obj.cleanup();
		}
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		spider.setLastPosition(spider.x, spider.y);
		spider.setVelocity(e2.getX() - e1.getX(), - e2.getY() + e1.getY() );
		return true;
	}

	public void updatePositions(float timeDeltaSeconds) {
		for(DrawableObject object : objects) {
			if(object.speed!=0 && !(object.getVelocityX() == 0 && object.getVelocityY() == 0)) {
				object.updatePosition(timeDeltaSeconds);
				// test for object touching the screen bounds
				if(!object.boundsCheck())
				// test for object reaching the region claimed by the spider
				object.claimedPathCheck(spider.getClaimedPath());
			}
		}
	}

}
