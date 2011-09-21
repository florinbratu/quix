package com.killerappzz.spider.objects;

import android.graphics.RectF;

/**
 * Defines the contract for an object 
 * 	that can "hit" another object
 * @author fbratu
 *
 */
public interface ICollider {

	/** the region where this collider exists
	 * */
	public RectF getBoundingBox();
	public void collide(ICollidee collidee);
}
