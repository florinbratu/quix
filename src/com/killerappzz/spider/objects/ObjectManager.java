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
	// the statistics banner
	private Banner banner;
	private final GameRenderer renderer;
	
	public ObjectManager(GameRenderer renderer) {
		this.objects = new LinkedList<DrawableObject>();
		this.renderer = renderer;
	}
	
	/**
	 * On the game controller side, add a new object for rendering
	 * @param obj
	 */
	public void add(DrawableObject obj) {
		this.objects.add(obj);
	}
	
	/** Add the ref to the special "banner" type */
	public void addBanner(Banner banner) {
		this.banner = banner;
	}

	public List<DrawableObject> getControllerObjects() {
		return objects;
	}
	
	public void cleanup() {
		for(DrawableObject obj: this.objects) {
			obj.cleanup();
		}
	}

	public void swap() {
		// TODO inefficient: creates list at every iteration
		// deep-copy the objects list
		List<DrawableObject> drawables = 
			new ArrayList<DrawableObject>(objects.size());
		// TODO inefficient: clones at every iteration
		for(DrawableObject obj : this.objects) {
			// banner will be added at the end
			if(!obj.equals(banner))
				drawables.add(obj.clone());
		}
		drawables.add(banner.clone());
		// update the draw queue. This op will hold the lock of renderer => simply update the ref
		List<DrawableObject> oldDrawables = this.renderer.updateDrawQueue(drawables);
		oldDrawables.clear();
	}

	public void updateScreen(int width, int height) {
		// update objects info
		for(DrawableObject obj : this.objects) {
			obj.updateScreen(width,height);
		}
	}

}
