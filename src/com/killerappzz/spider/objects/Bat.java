package com.killerappzz.spider.objects;

import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory.Options;
import android.util.Pair;

/**
 * The Bat is the Bad Guy
 * 
 * @author florin
 *
 */
public class Bat extends AnimatedSprite implements IBounceable{
	
	// Last Position
    private float lastX = -1;
    private float lastY = -1;

	public Bat(Context context, Options bitmapOptions, int resourceId,
			int scrW, int scrH, int framesNo, int fps) {
		super(context, bitmapOptions, resourceId, scrW, scrH, framesNo, fps);
	}
	
	public Bat(Bat orig) {
		super(orig);
	}
	
	@Override
	public void updatePosition(float timeDeltaSeconds) {
		// backup old pos
		this.lastX = this.x;
		this.lastY = this.y;
		super.updatePosition(timeDeltaSeconds);
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
		Pair<Pair<Float,Float>, Pair<Float,Float>> movement = 
			new Pair<Pair<Float,Float>, Pair<Float,Float>>(
				new Pair<Float,Float>(this.lastX, this.lastY),
				new Pair<Float,Float>(this.x, this.y));
		Pair<Pair<Float,Float>, Pair<Float,Float>> edge = 
			path.getTouchEdge(movement);
		Pair<Float,Float> newVelocity = bounceVelocity(edge);
		this.setVelocity(newVelocity.first, newVelocity.second);
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
	private Pair<Float, Float> bounceVelocity(
			Pair<Pair<Float, Float>, Pair<Float, Float>> edge) {
		double angle = angle(edge);
		double vx = - getVelocityX() * Math.cos(2*angle) - getVelocityY() * Math.sin(2*angle);
		double vy = - getVelocityX() * Math.sin(2*angle) + getVelocityY() * Math.cos(2*angle);
		setVelocity((float)vx, (float)vy);
		return null;
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

}
