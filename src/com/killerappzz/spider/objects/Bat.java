package com.killerappzz.spider.objects;

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
	public void boundsTouchBehaviour() {
		// TODO Auto-generated method stub
	}

	@Override
	public void claimedPathTouch() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public DrawableObject clone() {
		return new Bat(this);
	}

}
