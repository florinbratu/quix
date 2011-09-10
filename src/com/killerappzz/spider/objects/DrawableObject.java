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
    
    // Movement speed
    public float speed;
    
    // Size.
    public float width;
    public float height;
    
    // screen dimensions
    protected final int screenWidth;
    protected final int screenHeight;
    
    public DrawableObject(int scrW, int scrH) {
    	screenWidth = scrW;
    	screenHeight = scrH;
	}
    
	// coordinate conversion methods
	public final float toScreenX(float worldX) {
		return worldX + width/2;
	}
	
	public final float toScreenY(float worldY) {
		return this.screenHeight - (worldY + height/2);
	}
    
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
    
    public void updatePosition(float timeDeltaSeconds) {
    	this.x = this.x + (this.velocityX * this.speed * timeDeltaSeconds);
    	this.y = this.y + (this.velocityY * this.speed * timeDeltaSeconds);
	}
    
    /**
     * Determine how the object will react when
     * encountering the screen margins
     * 
     * @param screenWidth
     * @param screenHeight
     */
    public boolean boundsCheck() {
    	if ((this.x < 0.0f && this.velocityX < 0.0f) 
                || (this.x > screenWidth- this.width 
                        && this.velocityX > 0.0f)) {
    		this.x = Math.max(0.0f, 
                    Math.min(this.x, screenWidth - this.width));
    		// border behaviour, object-specific
            boundsTouchBehaviour();
            return true;
        }
        
        if ((this.y < 0.0f && this.velocityY < 0.0f) 
                || (this.y > screenHeight - this.height 
                        && this.velocityY > 0.0f)) {
            this.y = Math.max(0.0f, 
                    Math.min(this.y, screenHeight - this.height));
            // bound behaviour
            boundsTouchBehaviour();
            return true;
        }
        
        return false;
	}
    
    public void claimedPathCheck(GeometricPath claimedPath) {
		if(claimedPath.contains( toScreenX(this.x), toScreenY(this.y) )) 
			// object-specific behaviour
			claimedPathTouch();
	}
    
    public abstract void draw(Canvas canvas);
    public abstract void cleanup();
    // specific behaviour when object touches the bounds
    public abstract void boundsTouchBehaviour();
    // specific behaviour when object touches the already-claimed path
    public abstract void claimedPathTouch();

}
