package com.killerappzz.spider.objects;

import android.content.Context;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.util.Pair;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.Customization;
import com.killerappzz.spider.engine.GameData;

/**
 * The Spider.
 * 
 * @author florin
 *
 */
public class Spider extends AnimatedSprite implements IBounceable, ICollidee{
	
    // Last Position
    private float lastX = -1;
    private float lastY = -1;
    
    // line color
    private final Paint trailingPathPaint;
    // recent path took by spider
    private final SpiderPath trailingPath;
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
    	DEATH,	// display death animation
    };
    
    private Movement movement;
    // when dead: the time to stay in death state
    private float deathTicker = 0;
    private float deathTime = 0;
    private boolean blink = false;
    // the bounding box
    private final RectF boundingBox = new RectF();
    
    public Spider(Spider orig) {
    	super(orig);
    	this.trailingPathPaint = orig.trailingPathPaint;
    	this.claimedPathPaint = orig.claimedPathPaint;
    	this.screenRect= new RectF(orig.screenRect);
    	this.data = new GameData(orig.data);
    	this.movement = orig.movement;
    	this.lastX = orig.lastX;
    	this.lastY = orig.lastY;
    	this.deathTicker = orig.deathTicker;
    	this.deathTime = orig.deathTime;
    	this.blink = orig.blink;
    	this.trailingPath = new SpiderPath(orig.trailingPath);
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
	
	@Override
	public void updatePosition(float timeDeltaSeconds) {
		updateBoundingBox();
		if(movement.equals(Movement.DEATH)) {
			this.deathTicker += timeDeltaSeconds;
			if(this.deathTicker > Constants.SPIDER_DEATH_BLINK_RATE) {
				this.deathTicker -= Constants.SPIDER_DEATH_BLINK_RATE;
				blink();
			}
			this.deathTime += timeDeltaSeconds;
			if(this.deathTime > Constants.SPIDER_DEATH_PERIOD) { 
				respawn();
			}
		}
		else
			super.updatePosition(timeDeltaSeconds);
	}
	
	private void blink() {
		this.blink = !blink;
	}

	private void updateBoundingBox() {
		this.boundingBox.left = this.getPositionX();
		this.boundingBox.right = this.getPositionX() + this.width;
		this.boundingBox.top = this.getPositionY();
		this.boundingBox.bottom = this.getPositionY() + this.height;
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
		return this.movement.equals(Movement.CLAIM) 
				|| this.movement.equals(Movement.EDGE)
				|| this.movement.equals(Movement.DEATH);
	}
	
	@Override
	public void setVelocity(float velocityX, float velocityY) {
		super.setVelocity(velocityX, velocityY);
		this.movement = Movement.CLAIM;
	}
	
	public void stopMovement() {
		super.setVelocity(0, 0);
		this.movement = Movement.NONE;
	}
	
	@Override
	public void draw(Canvas canvas) {
		// draw claimed path
		canvas.drawPath(claimedPath, claimedPathPaint);
		if(!blink) {
			// draw trailing path
			canvas.drawPath(this.trailingPath, trailingPathPaint);
			// draw trailing line
			if(lastX != -1 && lastY != -1) {
				canvas.drawLine(toScreenX(lastX), toScreenY(lastY),
						toScreenX(this.getPositionX()), toScreenY(this.getPositionY()), trailingPathPaint);
			}
			super.draw(canvas);
		}
	}
	
	public ClaimedPath getClaimedPath() {
		return this.claimedPath;
	}

	@Override
	public void boundsTouchBehaviour(BounceAxis axis) {
		contactBehaviour();
	}

	@Override
	public void claimedPathTouch(ClaimedPath path) {
		contactBehaviour();
	}
	
	/**
	 * Common behaviour for border and claimed path touch
	 */
	private void contactBehaviour() {
		stopMovement();
		// add last line to path
		this.trailingPath.lineTo(toScreenX(this.getPositionX()), toScreenY(this.getPositionY()));
		// last line reset
		this.lastX = this.lastY = -1;
		// add to trailing path segments until bounds
		GeometricPath boundedPath = 
			this.claimedPath.reduceToBounds(this.trailingPath, this.screenRect);
		// close path
		boundedPath.close();
		// display the score
		score.display(boundedPath.getCenter().x,boundedPath.getCenter().y);
		// merge into claimed path
		this.claimedPath.merge(boundedPath);
		// recalculate area!
		this.data.setClaimedArea(this.claimedPath.area());
		// reset trailing path - new adventures await us!
		this.trailingPath.rewind();
	}
	
	private void respawn() {
		this.blink = false;
		this.stopMovement();
		// set position to last position
		if(this.trailingPath.isEmpty()) 
			setPosition(this.lastX, this.lastY);
		else 
			setPosition(this.trailingPath.getStartPoint().x - this.width / 2, // hack, I think we have a bug we do too many times toScreenX TODO investigate bug
				toScreenY(this.trailingPath.getStartPoint().y));
		updateBoundingBox();
		// clear trailing path
		this.trailingPath.rewind();
		this.lastX = this.lastY = -1;
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
    	this.blink = omologSpider.blink;
    	this.deathTicker = omologSpider.deathTicker;
    	this.deathTime = omologSpider.deathTime;
	}

	@Override
	public boolean collisionTest(ICollider collider) {
		// test if it intersects with Spider
		if(RectF.intersects(this.boundingBox, collider.getBoundingBox()))
			return true;
		if(collider instanceof Bat) 
			return collisionBatTest((Bat)collider);
		return false; 
	}

	private boolean collisionBatTest(Bat collider) {
		// test if touches trailing line
		if(SpiderPath.touch(getTrailingLine(), collider.getMovementVector()))
			return true;
		if(this.trailingPath.getTouchEdge(collider.getMovementVector()) != null) 
			return true;
		return false;
	}

	private Pair<Pair<Float, Float>, Pair<Float, Float>> getTrailingLine() {
		Pair<Float, Float> v1 = new Pair<Float, Float>(lastX, lastY);
		Pair<Float, Float> v2 = new Pair<Float, Float>(this.getPositionX(), this.getPositionY());
		return new Pair<Pair<Float, Float>, Pair<Float, Float>>(v1, v2);
	}

	@Override
	public void receive(ICollider collider) {
		Log.d(Constants.LOG_TAG, "Receive collision from: " + collider);
		// if already dead => do nothing!
		// collision bat => spider
		if(collider instanceof Bat) {
			if(movement.equals(Movement.CLAIM)) {
				// let the collider do its collision thing
				collider.collide(this);
				// switch to Death
				movement = Movement.DEATH;
				this.deathTicker = 0;
				this.deathTime = 0;
				this.blink = true;
			}
		}
	}

	@Override
	public RectF getBoundingBox() {
		return this.boundingBox;
	}
	
}
