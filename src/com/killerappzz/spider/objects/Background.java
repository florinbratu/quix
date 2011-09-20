package com.killerappzz.spider.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;

/**
 * Abstraction of background image.
 * essentially a sprite, but with 
 * the underlying bitmap scaled to screen size
 * 
 * @author florin
 *
 */
public class Background extends Sprite{
	
	// backup, for cleanup
	private final Bitmap originalBitmap;

	public Background(Context context, Options bitmapOptions, int resourceId,
			int width, int height) {
		super(context, bitmapOptions, resourceId, width, height);
		this.originalBitmap = mBitmap;
		this.mBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
		this.x = this.y = 0;
		setVelocity(0, 0);
		this.width = width;
		this.height = height;
	}
	
	public Background(Background orig) {
		super(orig);
		this.originalBitmap = orig.originalBitmap;
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		this.originalBitmap.recycle();
	}

	@Override
	public DrawableObject clone() {
		return new Background(this);
	}

}
