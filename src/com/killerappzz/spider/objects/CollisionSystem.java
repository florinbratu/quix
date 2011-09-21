package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

/**
 * This handles all collisions in the Game
 * 
 * @author fbratu
 *
 */
public class CollisionSystem {
	
	public final List<ICollidee> collideeList;
	public final List<ICollider> colliderList;

	public CollisionSystem() {
		this.collideeList = new LinkedList<ICollidee>();
		this.colliderList = new LinkedList<ICollider>();
	}
	
	public void registerCollider(ICollider collider) {
		this.colliderList.add(collider);
	}
	
	public void registerCollidee(ICollidee collidee) {
		this.collideeList.add(collidee);
	}
	
	public void handleCollisions() {
		for(ICollidee collidee: collideeList) {
			for(ICollider collider : colliderList)
				if(collidee.collisionTest(collider))
					collidee.receive(collider);
		}
	}
}
