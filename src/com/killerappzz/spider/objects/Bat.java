package com.killerappzz.spider.objects;

import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory.Options;

/**
 * The Bat is the Bad Guy
 * 
 * @author florin
 *
 */
public class Bat extends AnimatedSprite implements IBounceable{

	public Bat(Context context, Options bitmapOptions, int resourceId,
			int scrW, int scrH, int framesNo, int fps) {
		super(context, bitmapOptions, resourceId, scrW, scrH, framesNo, fps);
	}
	
	public Bat(Bat orig) {
		super(orig);
	}

	@Override
	public void boundsTouchBehaviour(BounceAxis axis) {
		if(axis.equals(BounceAxis.HORIZONTAL)) 
			setVelocity(- this.getVelocityX(), this.getVelocityY());
		else
			setVelocity(this.getVelocityX(), - this.getVelocityY());
	}

	@Override
	public void claimedPathTouch() {
		// TODO Auto-generated method stub
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
