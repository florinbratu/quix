package com.killerappzz.spider.objects;

import android.content.Context;
import android.graphics.BitmapFactory.Options;

/**
 * The Image displaying the life of the spider
 * @author florin
 *
 */
public class Life extends Sprite{
	
	public Life(Context context, Options bitmapOptions, int resourceId,
			int width, int height) {
		super(context, bitmapOptions, resourceId, width, height);
		this.x = this.y = 0;
		setVelocity(0, 0);
	}
	
	@Override
	public void boundsTouchBehaviour() {
		// not interested
	}

	@Override
	public void claimedPathTouch() {
		// not interested
	}

}
