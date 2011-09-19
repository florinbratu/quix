package com.killerappzz.spider.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.BitmapFactory.Options;

import com.killerappzz.spider.Customization;
import com.killerappzz.spider.engine.GameData;

/**
 * The Spider.
 * 
 * @author florin
 *
 */
public class Spider extends AnimatedSprite {
	
    // Last Position
    private float lastX = -1;
    private float lastY = -1;
    
    // line color
    private final Paint trailingPathPaint;
    // recent path took by spider
    private final GeometricPath trailingPath;
    // path claimed so far
    private final ClaimedPath claimedPath;
    private final Paint claimedPathPaint;
    // the rectangle defining the screen area
    private final RectF screenRect;
    // the game data
    private final GameData data;
    // the score display
    private ScoreGain score;
    
    // define spider movement type
    public enum Movement {
    	NONE,  // spider does not move
    	CLAIM, // claim new land
    	EDGE,  // move on the edge of the field + claimed land
    };
    
    private Movement movement;
    
    public Spider(Spider orig) {
    	super(orig);
    	this.trailingPathPaint = orig.trailingPathPaint;
    	this.claimedPathPaint = orig.claimedPathPaint;
    	this.screenRect= new RectF(orig.screenRect);
    	this.data = new GameData(orig.data);
    	this.movement = orig.movement;
    	this.lastX = orig.lastX;
    	this.lastY = orig.lastY;
    	this.trailingPath = new GeometricPath(orig.trailingPath);
    	this.claimedPath = new ClaimedPath(orig.claimedPath);
    }

	public Spider(Context context, Options bitmapOptions, int resourceId,
			int framesNo, int fps, int scrW, int scrH, GameData data ) {
		super(context, bitmapOptions, resourceId, scrW, scrH, framesNo, fps);
		this.data = data;
		this.screenRect = new RectF(this.width / 2 , this.height / 2, 
        		this.screenWidth - this.width / 2, this.screenHeight - this.height / 2);
		this.trailingPathPaint = Customization.getTrailingPathPaint();
        this.trailingPath = new SpiderPath(this.screenRect);
        this.claimedPathPaint = Customization.getClaimedPathPaint(context,bitmapOptions);
        this.claimedPath = new ClaimedPath(this.screenRect);
        this.movement = Movement.NONE;
	}
	
	public void setScoreGain(ScoreGain score) {
		this.score = score;
	}
	
	public void setLastPosition(float lastX, float lastY) {
		if(this.lastX != -1 && this.lastY != -1) 
			this.trailingPath.lineTo(toScreenX(lastX), toScreenY(lastY));
		else
			this.trailingPath.moveTo(toScreenX(lastX), toScreenY(lastY));
		this.lastX = lastX;
		this.lastY = lastY;
	}
	
	@Override
	public boolean moves() {
		return !this.movement.equals(Movement.NONE);
	}
	
	@Override
	public void setVelocity(float velocityX, float velocityY) {
		super.setVelocity(velocityX, velocityY);
		if(velocityX == 0 && velocityY == 0) 
			this.movement = Movement.NONE;
		else 
			this.movement = Movement.CLAIM;
	}
	
	@Override
	public void draw(Canvas canvas) {
		// draw claimed path
		canvas.drawPath(claimedPath, claimedPathPaint);
		// draw trailing path
		canvas.drawPath(this.trailingPath, trailingPathPaint);
		// draw trailing line
		if(lastX != -1 && lastY != -1) {
			canvas.drawLine(toScreenX(lastX), toScreenY(lastY),
					toScreenX(x), toScreenY(y), trailingPathPaint);
		}
		super.draw(canvas);
	}
	
	public GeometricPath getClaimedPath() {
		return this.claimedPath;
	}

	@Override
	public void boundsTouchBehaviour() {
		contactBehaviour();
	}

	@Override
	public void claimedPathTouch() {
		contactBehaviour();
	}
	
	/**
	 * Common behaviour for border and claimed path touch
	 */
	private void contactBehaviour() {
		setVelocity(0,0);
		// add last line to path
		this.trailingPath.lineTo(toScreenX(x), toScreenY(y));
		// last line reset
		this.lastX = this.lastY = -1;
		// add to trailing path segments until bounds
		GeometricPath boundedPath = 
			this.claimedPath.reduceToBounds(this.trailingPath, this.screenRect);
		// close path
		boundedPath.close();
		// display the score
		score.display();
		// merge into claimed path
		this.claimedPath.merge(boundedPath);
		// recalculate area!
		this.data.setClaimedArea(this.claimedPath.area());
		// reset trailing path - new adventures await us!
		this.trailingPath.rewind();
	}
	
	@Override
	public DrawableObject clone(){
		return new Spider(this);
	}
	
	@Override
	public void update(DrawableObject omolog) {
		super.update(omolog);
		Spider omologSpider = (Spider)omolog;
    	this.screenRect.set(omologSpider.screenRect);
    	this.data.update(omologSpider.data);
    	this.movement = omologSpider.movement;
    	this.lastX = omologSpider.lastX;
    	this.lastY = omologSpider.lastY;
    	this.trailingPath.update(omologSpider.trailingPath);
    	this.claimedPath.update(omologSpider.claimedPath);
	}
	
}
