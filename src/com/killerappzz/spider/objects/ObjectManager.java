package com.killerappzz.spider.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.killerappzz.spider.rendering.GameRenderer;
import com.killerappzz.spider.util.DeepCopy;

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
		// deep-copy the objects list
		List<DrawableObject> drawables = 
			new ArrayList<DrawableObject>(objects.size());
		for(DrawableObject obj : this.objects) {
			try {
				// banner will be added at the end
				if(!obj.equals(banner))
					drawables.add((DrawableObject)DeepCopy.copy(obj));
			} catch (Exception e) {
				Log.e("QUIX", "Deep-copy of object " + obj + " failed. Fallback - use original object instead", e);
				drawables.add(obj);
			}
		}
		// at the end only! we add the banner. It needs to show up on top of em all!!
		try {
			drawables.add((DrawableObject)DeepCopy.copy(banner));
		} catch (Exception e) {
			Log.e("QUIX", "Deep-copy of banner " + banner + " failed. Fallback - use original object instead", e);
			drawables.add(banner);
		}
		// update the draw queue. This op will hold the lock of renderer => simply update the ref
		List<DrawableObject> oldDrawables = this.renderer.updateDrawQueue(drawables);
		oldDrawables.clear();
	}

}
