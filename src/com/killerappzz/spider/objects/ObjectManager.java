package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

/**
 * Manages a double-buffered queue of renderable objects.  The game thread submits drawable objects
 * to the the active render queue while the render thread consumes drawables from the alternate
 * queue.  When both threads complete a frame the queues are swapped.  
 */
public class ObjectManager {

	/** the objects list, for the game thread */
	private final List<DrawableObject> objects;
	/** the drawing list, for the rendering thread */
	private final List<DrawableObject> drawables;
	// the statistics banner
	private Banner banner;
	
	public ObjectManager() {
		this.objects = new LinkedList<DrawableObject>();
		this.drawables = new LinkedList<DrawableObject>();
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
	
	public List<DrawableObject> getRendererObjects() {
		return drawables;
	}

	public void cleanup() {
		for(DrawableObject obj: this.objects) {
			obj.cleanup();
		}
	}

	public void swap() {
		// TODO Auto-generated method stub
	}

}
