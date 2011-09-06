package com.killerappzz.spider;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;
import android.graphics.Shader.TileMode;

/**
 * All customization prefs are gathered here
 * for convenience, so it is easy to modify
 * without grepping the source
 * 
 * @author florin
 *
 */
public final class Customization {
	
	public static final Paint getTrailingPathPaint() {
		Paint trailingLinePaint = new Paint();
        trailingLinePaint.setAntiAlias(true);
        trailingLinePaint.setDither(true);
        trailingLinePaint.setColor(Constants.TRAILING_LINE_COLOR);
        trailingLinePaint.setStyle(Paint.Style.STROKE);
        trailingLinePaint.setStrokeJoin(Paint.Join.ROUND);
        trailingLinePaint.setStrokeCap(Paint.Cap.BUTT);
        trailingLinePaint.setStrokeWidth(Constants.TRAILING_LINE_WIDTH);
        return trailingLinePaint;
	}
	
	public static final Bitmap loadFromRes(Context context, Options bitmapOptions, int resourceId) {
		Bitmap ret = null;
		if (context != null) {

			InputStream is = context.getResources().openRawResource(resourceId);
			try {
				ret = BitmapFactory.decodeStream(is, null, bitmapOptions);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					// Ignore.
				}
			}
		}
		return ret;
	}
	
	public static final Paint getClaimedPathPaint(Context context, Options bitmapOptions) {
		Paint claimedPathPaint = new Paint();
		// initialize the paint
        claimedPathPaint.setAntiAlias(true);
        claimedPathPaint.setDither(true);
        claimedPathPaint.setColor(Constants.CLAIMED_COLOR);
        claimedPathPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        claimedPathPaint.setStrokeWidth(Constants.CLAIMED_STROKE_WIDTH);
        // set the spider texture
        claimedPathPaint.setShader(
        		new BitmapShader(
        				loadFromRes(context, bitmapOptions, R.drawable.spiderweb), 
        				TileMode.REPEAT, TileMode.REPEAT));
        claimedPathPaint.setFilterBitmap(true);
        return claimedPathPaint;
	}

}
