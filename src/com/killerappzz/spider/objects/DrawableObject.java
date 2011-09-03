package com.killerappzz.spider.objects;

import android.graphics.Canvas;

/** 
 * Base class defining the core set of information necessary to render (and move
 * an object on the screen.  This is an abstract type and must be derived to
 * add methods to actually draw (see CanvasSprite and GLSprite).
 */
public abstract class DrawableObject {
    // Position.
    public float x;
    public float y;
    
    // Velocity. Prin velocity eu inteleg directia in care tre sa se miste
    private float velocityX;
    private float velocityY;
    
    public void setVelocity(float velocityX, float velocityY) {
    	// store the velocities in normalized format
    	if(velocityX == 0 && velocityY == 0) {
    		this.velocityX = this.velocityY =0;
    	} else {
    		float velocityNorm = (float)Math.sqrt(
    				velocityX * velocityX + velocityY * velocityY);
    		this.velocityX = velocityX / velocityNorm;
    		this.velocityY = velocityY / velocityNorm;
    	}
    		
    }
    
    public float getVelocityX() {
    	return velocityX;
    }
    
    public float getVelocityY() {
    	return velocityY;
    }
    
    // Movement speed
    public float speed;
    
    // Size.
    public float width;
    public float height;
    
    public void updatePosition(float timeDeltaSeconds) {
    	this.x = this.x + (this.velocityX * this.speed * timeDeltaSeconds);
    	this.y = this.y + (this.velocityY * this.speed * timeDeltaSeconds);
	}
    
    public abstract void draw(Canvas canvas);
    public abstract void cleanup();
	
}
