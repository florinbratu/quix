package com.killerappzz.spider.objects;

import android.graphics.Canvas;

import com.killerappzz.spider.objects.IBounceable.BounceAxis;
import com.killerappzz.spider.util.IDGenerator;

/** 
 * Base class defining the core set of information necessary to render (and move
 * an object on the screen.  This is an abstract type and must be derived to
 * add methods to actually draw (see CanvasSprite and GLSprite).
 */
public abstract class DrawableObject implements Cloneable{
	
    // Position.
    private float x;
    private float y;
    private int z;
    
    // Velocity. Prin velocity eu inteleg directia in care tre sa se miste
    private float velocityX;
    private float velocityY;
    
    // Movement speed
    public float speed;
    
    // Size.
    public float width;
    public float height;
    
    // screen dimensions
    protected int screenWidth;
    protected int screenHeight;
    
    // object ID. for comps
    private final long ID;
    
    public DrawableObject(int scrW, int scrH) {
    	screenWidth = scrW;
    	screenHeight = scrH;
    	this.ID = IDGenerator.generate();
	}
    
    public DrawableObject(DrawableObject orig) {
    	this.ID = orig.ID;
    	this.x = orig.x;
    	this.y = orig.y;
    	this.velocityX = orig.velocityX;
    	this.velocityY = orig.velocityY;
    	this.speed = orig.speed;
    	this.width = orig.width;
    	this.height = orig.height;
    	this.screenWidth = orig.screenWidth;
    	this.screenHeight = orig.screenHeight;
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
    
    public float getPositionX() {
    	return this.x;
    }
    
    public float getPositionY() {
    	return this.y;
    }
    
    public void setPosition(float x, float y) {
    	this.x = x;
    	this.y = y;
    }
    
    public int getZ() {
    	return this.z;
    }
    
    public void setZ(int z) {
    	this.z = z;
    }
    
    public void updatePosition(float timeDeltaSeconds) {
    	this.x = this.x + (this.velocityX * this.speed * timeDeltaSeconds);
    	this.y = this.y + (this.velocityY * this.speed * timeDeltaSeconds);
	}
    
    public boolean moves() {
		return this.speed != 0 && !(this.velocityX == 0 && this.velocityY == 0);
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
    		if(this instanceof IBounceable) 
    			((IBounceable)this).boundsTouchBehaviour(BounceAxis.HORIZONTAL);
            return true;
        }
        
        if ((this.y < 0.0f && this.velocityY < 0.0f) 
                || (this.y > screenHeight - this.height 
                        && this.velocityY > 0.0f)) {
            this.y = Math.max(0.0f, 
                    Math.min(this.y, screenHeight - this.height));
            // bound behaviour
            if(this instanceof IBounceable) 
    			((IBounceable)this).boundsTouchBehaviour(BounceAxis.VERTICAL);
            return true;
        }
        
        return false;
	}
    
    public void claimedPathCheck(ClaimedPath claimedPath) {
		if(claimedPath.contains( toScreenX(this.x), toScreenY(this.y) )) 
			// object-specific behaviour
			if(this instanceof IBounceable) 
    			((IBounceable)this).claimedPathTouch(claimedPath);
	}
    
    public void updateScreen(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
    
    @Override
    public boolean equals(Object obj) {
    	if(obj == null || !(obj instanceof DrawableObject))
    		return false;
    	DrawableObject object = (DrawableObject)obj;
    	return object.ID == this.ID;
    }
    
    public abstract void draw(Canvas canvas);
    public abstract void cleanup();
    // object state reset. useful for level restart for instance
    public abstract void reset();
    // cloning
    public abstract DrawableObject clone();
    // drawing optimization. this will be called at each frame 
    // to update the draw buffers with the new content
    public void update(DrawableObject omolog) {
    	this.x = omolog.x;
    	this.y = omolog.y;
    	this.velocityX = omolog.velocityX;
    	this.velocityY = omolog.velocityY;
    	this.speed = omolog.speed;
    	this.width = omolog.width;
    	this.height = omolog.height;
    	this.screenWidth = omolog.screenWidth;
    	this.screenHeight = omolog.screenHeight;
    }

}
