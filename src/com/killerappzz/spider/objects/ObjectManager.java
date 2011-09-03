package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;

/**
 * Handles objects which are displayed on the screen
 * throughout their lifetime
 * 
 * @author florin
 *
 */
public class ObjectManager {
	
	private final List<DrawableObject> objects; 
	
	public ObjectManager() {
		this.objects = new LinkedList<DrawableObject>();
	}
	
	public void addObject(DrawableObject object) {
    	this.objects.add(object);
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

}
