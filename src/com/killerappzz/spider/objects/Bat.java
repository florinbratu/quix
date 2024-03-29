package com.killerappzz.spider.objects;

import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory.Options;
import android.graphics.RectF;
import android.util.Log;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.engine.GameData;
import com.killerappzz.spider.engine.GameData.EndGameCondition;
import com.killerappzz.spider.geometry.Edge2D;
import com.killerappzz.spider.geometry.Point2D;

/**
 * The Bat is the Bad Guy
 * 
 * @author florin
 *
 */
public class Bat extends AnimatedSprite implements IBounceable, ICollider{

	// movement vector
	private final Edge2D movementVector;
    // the bounding box
    private final RectF boundingBox = new RectF();
    // game data. for victory notification! ;)
    private final GameData data;
    private final float initialSpeed;

	public Bat(Context context, Options bitmapOptions, int resourceId,
			int scrW, int scrH, int framesNo, int fps, GameData data, float initialSpeed) {
		super(context, bitmapOptions, resourceId, scrW, scrH, framesNo, fps);
		this.movementVector = new Edge2D.Float();
		this.data = data;
		this.initialSpeed = initialSpeed;
		this.speed = this.initialSpeed;
	}
	
	public Bat(Bat orig) {
		super(orig);
		this.movementVector = new Edge2D.Float();
		this.data = orig.data;
		this.initialSpeed = orig.initialSpeed;
	}
	
	@Override
	public void updatePosition(float timeDeltaSeconds) {
		// backup old pos
		this.movementVector.setStartPoint(toScreenX(this.getPositionX()), 
				toScreenY(this.getPositionY()));
		updateBoundingBox();
		super.updatePosition(timeDeltaSeconds);
		// set new pos - after update!
		this.movementVector.setEndPoint(toScreenX(this.getPositionX()), 
				toScreenY(this.getPositionY()));
	}

	private void updateBoundingBox() {
		this.boundingBox.left = this.getPositionX();
		this.boundingBox.right = this.getPositionX() + this.width;
		this.boundingBox.top = this.getPositionY();
		this.boundingBox.bottom = this.getPositionY() + this.height;
	}

	@Override
	public void boundsTouchBehaviour(BounceAxis axis) {
		if(axis.equals(BounceAxis.HORIZONTAL)) 
			setVelocity(- this.getVelocityX(), this.getVelocityY());
		else
			setVelocity(this.getVelocityX(), - this.getVelocityY());
	}

	@Override
	public void claimedPathTouch(ClaimedPath path) {
		try {
			Edge2D movement = getMovementVector();
			Edge2D edge = path.getTouchEdge(movement);
			setBounceVelocity(edge);
		} catch(IllegalArgumentException iae) {
			/* We arrive in this case if the Spider claimed 
			 * some land onto which the Bat was present!
			 * In this case, we are victorious!!!
			 * */
			data.victory(EndGameCondition.SPIDER_TRAP);
		}
	}
	
	/**
	 * Calculate the new movement direction
	 * of the spider bouncing off the edge
	 * of the Polygon wall
	 * 
	 * The formulae for the new velocity is the following:
	 * (do the math ;)
	 * Let (vx,vy) be the current velocity, and a be the 
	 * angle that the edge does with the X axis.
	 * Then, the new velocity (vx',vy') will be:
	 * vx'= - vx * cos(2*a) - vy * sin(2*a)
	 * vy'= - vx * sin(2*a) + vy * cos(2*a) 
	 * 
	 * @param edge the edge against which we bounce
	 * @return the new bounce velocity
	 */
	private void setBounceVelocity(Edge2D edge) {
		double angle = angle(edge);
		double vx = - getVelocityX() * Math.cos(2*angle) - getVelocityY() * Math.sin(2*angle);
		double vy = - getVelocityX() * Math.sin(2*angle) + getVelocityY() * Math.cos(2*angle);
		setVelocity((float)vx, (float)vy);
	}

	/**
	 * Calculate the angle of the edge against the X axis
	 * 
	 * @param edge
	 * @return
	 */
	private double angle(Edge2D edge) {
		Point2D e1 = edge.getStartPoint();
		Point2D e2 = edge.getEndPoint();
		return Math.atan2(e2.getX() - e1.getX(), e2.getY() - e1.getY()); 
	}

	@Override
	public DrawableObject clone() {
		return new Bat(this);
	}

	/**
	 * Start the bat movement, with a random velocity.
	 * However the angle should be between 30 and 60 deg
	 * so that it won't be too easy to avoid the bat...
	 * 
	 */
	public void startMovement() {
		Random rnd = new Random(System.currentTimeMillis());
		int dir = rnd.nextInt(4);
		double randomAngle = Math.PI * dir / 2 + 
			(float)randomRange(rnd, Math.PI / 6 , Math.PI / 3 );
		float vx = (float)Math.cos(randomAngle);
		float vy = (float)Math.sin(randomAngle);
		setVelocity(vx, vy);
	}

	private double randomRange(Random rnd, double min, double max) {
		double fraction = Math.abs(rnd.nextDouble()) / Double.MAX_VALUE;
		return min + fraction * (max - min);
	}

	@Override
	public void collide(ICollidee collidee) {
		// TODO Auto-generated method stub
		Log.d(Constants.LOG_TAG, "Collision into: " + collidee);
	}

	@Override
	public RectF getBoundingBox() {
		return this.boundingBox;
	}
	
	public Edge2D getMovementVector() {
		return movementVector;
	}
	
	@Override
	public void update(DrawableObject omolog) {
		super.update(omolog);
		Bat omologBat = (Bat)omolog;
    	this.data.update(omologBat.data);
	}
	
	@Override
	public void reset() {
		super.reset();
		int centerX = (screenWidth - (int)this.width) / 2;
		int centerY = (screenHeight - (int)this.height) / 2;
		setPosition(centerX, centerY);
        this.speed = this.initialSpeed;
        startMovement();
	}

	/* Increment the bat speed according to the surface gain
	 * */
	public void incrementSpeedBySurface(float claimedPercentile) {
		this.speed = (1 + claimedPercentile / 100) * this.initialSpeed;
	}

}
