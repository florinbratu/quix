package com.killerappzz.spider.objects;

import android.graphics.RectF;

/**
 * Defines the contract for an object that
 * can "be hit"by a Collider object
 * @author fbratu
 *
 */
public interface ICollidee {
	
	/** the region where this collidee exists.
	 * If this is touched then we consider a hit */
	public RectF getBoundingBox();
	/** 
	 * test for collision with the Collider.
	 * This op is separated from the actual collision action
	 * because we want a cheap collision test
	 */
	public boolean collisionTest(ICollider collider);
	/** receive the collision from the collider */
	public void receive(ICollider collider);

}
