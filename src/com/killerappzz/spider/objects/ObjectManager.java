package com.killerappzz.spider.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.rendering.GameRenderer;

/**
 * Manages a double-buffered queue of renderable objects.  The game thread submits drawable objects
 * to the the active render queue while the render thread consumes drawables from the alternate
 * queue.  When both threads complete a frame the queues are swapped.  
 */
public class ObjectManager {

	/** the objects list, for the game thread */
	private final List<DrawableObject> objects;
	/** the drawables list, for the rendering thread*/
	private List<DrawableObject> drawables;
	// the statistics banner
	private Banner banner;
	private Banner shadowBanner;
	private final GameRenderer renderer;
	
	public ObjectManager(GameRenderer renderer) {
		this.objects = new LinkedList<DrawableObject>();
		this.drawables = new LinkedList<DrawableObject>();
		this.renderer = renderer;
	}
	
	/**
	 * On the game controller side, add a new object for rendering
	 * @param obj
	 */
	public void add(DrawableObject obj) {
		this.objects.add(obj);
		this.drawables.add(obj.clone());
	}
	
	/** Add the ref to the special "banner" type */
	public void addBanner(Banner banner) {
		this.banner = banner;
		this.shadowBanner = (Banner)banner.clone();
	}

	public List<DrawableObject> getControllerObjects() {
		return objects;
	}
	
	public void cleanup() {
		for(DrawableObject obj: this.objects) {
			obj.cleanup();
		}
		this.drawables.clear();
		this.objects.clear();
	}

	public void swap() {
		for(DrawableObject obj : this.objects) {
			if(!obj.equals(banner)) {
				int index = this.drawables.indexOf(obj);
				DrawableObject omolog = this.drawables.get(index);
				omolog.update(obj);
				// banner will be added at the end
			}
		}
		shadowBanner.update(banner);
		drawables.add(shadowBanner);
		// update the draw queue. This op will hold the lock of renderer => simply update the ref
		List<DrawableObject> oldDrawables = this.renderer.updateDrawQueue(this.drawables);
		// if new objects were added, add them in the shadow too!
		if(oldDrawables.size() < objects.size()) {
			for(int index = oldDrawables.size() ; index < objects.size() ; index++) {
				DrawableObject obj = this.objects.get(index);
				oldDrawables.add(obj.clone());
			}
		}
		this.drawables = oldDrawables;
	}

	public void updateScreen(int width, int height) {
		// update objects info
		for(DrawableObject obj : this.objects) {
			obj.updateScreen(width,height);
		}
	}

}
