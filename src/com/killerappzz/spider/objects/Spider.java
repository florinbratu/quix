package com.killerappzz.spider.objects;

import com.killerappzz.spider.Constants;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;

/**
 * The Spider.
 * 
 * @author florin
 *
 */
public class Spider extends Sprite{
	
    // Last Position
    private float lastX = -1;
    private float lastY = -1;
    
    // line color
    private final Paint trailingLinePaint;

	public Spider(Context context, Options bitmapOptions, int resourceId) {
		super(context, bitmapOptions, resourceId);
		
		trailingLinePaint = new Paint();
        trailingLinePaint.setAntiAlias(true);
        trailingLinePaint.setDither(true);
        trailingLinePaint.setColor(Constants.TRAILING_LINE_COLOR);
        trailingLinePaint.setStyle(Paint.Style.STROKE);
        trailingLinePaint.setStrokeJoin(Paint.Join.ROUND);
        trailingLinePaint.setStrokeCap(Paint.Cap.BUTT);
        trailingLinePaint.setStrokeWidth(Constants.TRAILING_LINE_WIDTH);
	}
	
	public void setLastPosition(float lastX, float lastY) {
		this.lastX = lastX;
		this.lastY = lastY;
	}
	
	@Override
	public void draw(Canvas canvas) {
		// draw trailing line
		if(lastX != -1 && lastY != -1) {
			canvas.drawLine(lastX + width/2, 
					(canvas.getHeight() - lastY - height/2), 
					x + width/2, 
					( canvas.getHeight() - y - height/2 ), 
					trailingLinePaint);
		}
		super.draw(canvas);
	}

}
