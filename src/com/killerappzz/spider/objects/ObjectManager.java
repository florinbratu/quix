package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

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
	// the statistics banner
	private Banner banner;
	
	public ObjectManager() {
		this.objects = new LinkedList<DrawableObject>();
	}
	
	public void addObject(DrawableObject object) {
    	this.objects.add(object);
    }
	
	public void addSpider(Spider spider){
		this.objects.add(spider);
		this.spider = spider;
	}
	
	public void addBanner(Banner banner){
		this.banner = banner;
	}
	
	public void draw(Canvas canvas) {
		for(DrawableObject object : objects) {
    		object.draw(canvas);
    	}
		// draw banner on top of em all
		this.banner.draw(canvas);
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
			if( object.moves() ) {
				object.updatePosition(timeDeltaSeconds);
				// test for object touching the screen bounds
				if(!object.boundsCheck())
				// test for object reaching the region claimed by the spider
				object.claimedPathCheck(spider.getClaimedPath());
			}
		}
	}

}
