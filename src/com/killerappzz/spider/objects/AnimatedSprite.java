package com.killerappzz.spider.objects;

import com.killerappzz.spider.Constants.Direction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * A moving object, using sprites
 * 
 * @author florin
 *
 */
public abstract class AnimatedSprite extends Sprite {
	
	// frame counter
	private int currentFrame;
	// total # of frames
	private final int framesCount;
	// the size of the sprite sheet. sa fie acolo!
	private final float spriteSheetWidth;
	private final float spriteSheetHeight;
	// the source rectangle i.e. the selection window for the animation frame
	private final Rect sourceRect;
	// frames time control
	private final float framePeriod;
	private float frameTicker;
	
	private Direction movementDirection;

	public AnimatedSprite(Bitmap bitmap, int scrW, int scrH, 
			int framesNo, int fps) {
		super(bitmap, scrW, scrH);
		this.framesCount = framesNo;
		this.currentFrame = 0;
		this.framePeriod = 1.0f / (float)fps;
		this.frameTicker = 0;
		this.spriteSheetWidth = this.width;
		this.spriteSheetHeight = this.height;
		this.width = this.spriteSheetWidth / this.framesCount;
		this.height = this.spriteSheetHeight / Direction.values().length;
		this.sourceRect = new Rect(0, 0, (int)this.width, (int)this.height);
		// initial direction: north
		this.movementDirection = Direction.NORTH;
	}
	
    public AnimatedSprite(Context context, BitmapFactory.Options bitmapOptions, int resourceId,
    		int scrW, int scrH, int framesNo, int fps) {
    	super(context, bitmapOptions, resourceId, scrW,scrH);
    	this.framesCount = framesNo;
    	this.currentFrame = 0;
    	this.framePeriod = 1.0f / (float)fps;
    	this.frameTicker = 0;
    	this.spriteSheetWidth = this.width;
		this.spriteSheetHeight = this.height;
		this.width = this.spriteSheetWidth / this.framesCount;
		this.height = this.spriteSheetHeight / Direction.values().length;
		this.sourceRect = new Rect(0, 0, (int)this.width, (int)this.height);
		// initial direction: north
		this.movementDirection = Direction.NORTH;
    }
    
    @Override
    public void draw(Canvas canvas) {
    	RectF destRect = new RectF(this.x, canvas.getHeight() - (this.y + this.height), 
    			this.x + this.width, canvas.getHeight() - this.y);
    	// display the selected window to be displayed
    	canvas.drawBitmap(this.mBitmap, sourceRect, destRect, null);
    }
    
    @Override
    public void updatePosition(float timeDeltaSeconds) {
    	super.updatePosition(timeDeltaSeconds);
    	// jump to next frame
    	nextFrame(timeDeltaSeconds);
    }
    
    @Override
    public void setVelocity(float velocityX, float velocityY) {
    	super.setVelocity(velocityX, velocityY);
    	// also update the movement angle - for the animated sprite!
    	if(velocityX == 0)
    		this.movementDirection = (velocityY > 0 ) ? Direction.NORTH : Direction.SOUTH;
    	else {
    		double angle = Math.atan(velocityY/velocityX);
    		this.movementDirection = Direction.fromAngle(angle, velocityX > 0);
    	}
    	// update source rect coordinates accordingly
    	this.sourceRect.top = this.movementDirection.ordinal() * (int)this.height;
    	this.sourceRect.bottom = this.sourceRect.top + (int)this.height;
    }
    
    private void nextFrame(float timeDeltaSeconds) {
    	this.frameTicker += timeDeltaSeconds;
    	if(this.frameTicker > this.framePeriod) {
    		// time to swap frames
    		this.frameTicker -= this.framePeriod;
    		this.currentFrame++;
    		if(this.currentFrame == this.framesCount)
    			this.currentFrame = 0;
    		this.sourceRect.left = this.currentFrame * (int)this.width;
    		this.sourceRect.right = this.sourceRect.left + (int)this.width;
    	}
    }
    
}
