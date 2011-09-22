package com.killerappzz.spider.objects;

import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory.Options;
import android.graphics.RectF;
import android.util.Log;
import android.util.Pair;

import com.killerappzz.spider.Constants;

/**
 * The Bat is the Bad Guy
 * 
 * @author florin
 *
 */
public class Bat extends AnimatedSprite implements IBounceable, ICollider{
	
	// Last Position
    private float lastX = -1;
    private float lastY = -1;
    // the Spider. don't need to shadow it, not needed for rendering
    private Spider spider;
    // the bounding box
    private final RectF boundingBox = new RectF();

	public Bat(Context context, Options bitmapOptions, int resourceId,
			int scrW, int scrH, int framesNo, int fps) {
		super(context, bitmapOptions, resourceId, scrW, scrH, framesNo, fps);
	}
	
	public Bat(Bat orig) {
		super(orig);
	}
	
	public void setSpider(Spider spider) {
		this.spider = spider;
	}
	
	@Override
	public void updatePosition(float timeDeltaSeconds) {
		// backup old pos
		this.lastX = this.getPositionX();
		this.lastY = this.getPositionY();
		updateBoundingBox();
		super.updatePosition(timeDeltaSeconds);
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
			Pair<Pair<Float,Float>, Pair<Float,Float>> movement = 
					new Pair<Pair<Float,Float>, Pair<Float,Float>>(
							new Pair<Float,Float>(toScreenX(this.lastX), toScreenY(this.lastY)),
							new Pair<Float,Float>(toScreenX(this.getPositionX()), toScreenY(this.getPositionY())));
			Pair<Pair<Float,Float>, Pair<Float,Float>> edge = 
					path.getTouchEdge(movement);
			setBounceVelocity(edge);
		} catch(IllegalArgumentException iae) {
			/* We arrive in this case if the Spider claimed 
			 * some land onto which the Bat was present!
			 * In this case, we are victorious!!!
			 * */
			// TODO Victory!!!
			Log.d(Constants.LOG_TAG, "Victory!!!");
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
	private void setBounceVelocity(
			Pair<Pair<Float, Float>, Pair<Float, Float>> edge) {
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
	private double angle(Pair<Pair<Float, Float>, Pair<Float, Float>> edge) {
		Pair<Float, Float> e1 = edge.first;
		Pair<Float, Float> e2 = edge.second;
		if(e2.first == e1.first)
			return 0.5 * Math.PI ;
		else
			return Math.atan2(e2.first - e1.first, e2.second - e1.second); 
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
	
	public Pair<Pair<Float, Float>, Pair<Float, Float>> getMovementVector() {
		Pair<Float, Float> last = new Pair<Float,Float>(this.lastX, this.lastY);
		Pair<Float, Float> current = new Pair<Float,Float>(this.getPositionX(), this.getPositionY());
		return new Pair<Pair<Float, Float>, Pair<Float, Float>>(last, current);
	}

}
