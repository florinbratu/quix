package com.killerappzz.spider;

import android.graphics.Paint;

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
	
	public static final Paint getClaimedPathPaint() {
		Paint claimedPathPaint = new Paint();
		// initialize the paint
        claimedPathPaint.setAntiAlias(true);
        claimedPathPaint.setDither(true);
        claimedPathPaint.setColor(Constants.CLAIMED_COLOR);
        claimedPathPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        claimedPathPaint.setStrokeWidth(Constants.CLAIMED_STROKE_WIDTH);
        return claimedPathPaint;
	}

}
