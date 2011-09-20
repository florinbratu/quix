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
	 * @param edge the edge against which we bounce
	 * @return the new bounce velocity
	 */
	private Pair<Float, Float> bounceVelocity(
			Pair<Pair<Float, Float>, Pair<Float, Float>> edge) {
		// TODO Auto-generated method stub
		// trebe adunate unghiurile : unghiul de intrare al movementVector in edge
		// plus unghiul relativ al edge la sistemul de referinta al nostru(screenRect)
		// atentie la cazurile extreme!
		return null;
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
