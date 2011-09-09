package com.killerappzz.spider.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.BitmapFactory.Options;

import com.killerappzz.spider.Customization;

/**
 * The Spider.
 * 
 * @author florin
 *
 */
public class Spider extends AnimatedSprite{
	
    // Last Position
    private float lastX = -1;
    private float lastY = -1;
    
    // line color
    private final Paint trailingPathPaint;
    // recent path took by spider
    private final GeometricPath trailingPath;
    // path claimed so far
    private final ClaimedPath claimedPath;
    private final Paint claimedPathPaint;
    // the rectangle defining the screen area
    private final RectF screenRect;

	public Spider(Context context, Options bitmapOptions, int resourceId,
			int framesNo, int fps, int scrW, int scrH ) {
		super(context, bitmapOptions, resourceId, scrW, scrH, framesNo, fps);
		this.screenRect = new RectF(this.width / 2 , this.height / 2, 
        		this.screenWidth - this.width / 2, this.screenHeight - this.height / 2);
		this.trailingPathPaint = Customization.getTrailingPathPaint();
        this.trailingPath = new SpiderPath(this.screenRect);
        this.claimedPathPaint = Customization.getClaimedPathPaint(context,bitmapOptions);
        this.claimedPath = new ClaimedPath(this.screenRect);
	}
	
	public void setLastPosition(float lastX, float lastY) {
		if(this.lastX != -1 && this.lastY != -1) 
			this.trailingPath.lineTo(toScreenX(lastX), toScreenY(lastY));
		else
			this.trailingPath.moveTo(toScreenX(lastX), toScreenY(lastY));
		this.lastX = lastX;
		this.lastY = lastY;
	}
	
	@Override
	public void draw(Canvas canvas) {
		// draw claimed path
		canvas.drawPath(claimedPath, claimedPathPaint);
		// draw trailing path
		canvas.drawPath(this.trailingPath, trailingPathPaint);
		// draw trailing line
		if(lastX != -1 && lastY != -1) {
			canvas.drawLine(toScreenX(lastX), toScreenY(lastY),
					toScreenX(x), toScreenY(y), trailingPathPaint);
		}
		super.draw(canvas);
	}
	
	public GeometricPath getClaimedPath() {
		return this.claimedPath;
	}

	@Override
	public void boundsTouchBehaviour() {
		setVelocity(0, 0);
		// add last line to path
		this.trailingPath.lineTo(toScreenX(x), toScreenY(y));
		// last line reset
		this.lastX = this.lastY = -1;
		// add to trailing path segments until bounds
		this.claimedPath.reduceToBounds(this.trailingPath, this.screenRect);
		// close path
		this.trailingPath.close();
		// merge into claimed path
		this.claimedPath.merge(this.trailingPath);
		// reset trailing path - new adventures await us!
		this.trailingPath.rewind();
	}

	@Override
	public void claimedPathTouch() {
		setVelocity(0,0);
		// add last line to path
		this.trailingPath.lineTo(toScreenX(x), toScreenY(y));
		// last line reset
		this.lastX = this.lastY = -1;
		// add to trailing path segments until bounds
		this.claimedPath.reduceToBounds(this.trailingPath, this.screenRect);
		// close path
		this.trailingPath.close();
		// merge into claimed path
		this.claimedPath.merge(this.trailingPath);
		// reset trailing path - new adventures await us!
		this.trailingPath.rewind();
	}
	
}
