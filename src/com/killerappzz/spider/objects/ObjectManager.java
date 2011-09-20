package com.killerappzz.spider.objects;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
			int index = this.drawables.indexOf(obj);
			DrawableObject omolog = this.drawables.get(index);
			omolog.update(obj);
		}
		// sort them according to Z order. this is fast for small list(like ours)
		Collections.sort(drawables, new Comparator<DrawableObject>() {
			public int compare(DrawableObject object1, DrawableObject object2) {
				return object1.z - object2.z;
			}
		});
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
