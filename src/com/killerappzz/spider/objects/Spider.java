package com.killerappzz.spider.objects;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.engine.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.BitmapFactory.Options;

/**
 * The Spider.
 * 
 * @author florin
 *
 */
public class Spider extends Sprite{
	
	// the game reference
    private Game game;
	
    // Last Position
    private float lastX = -1;
    private float lastY = -1;
    
    // line color
    private final Paint trailingLinePaint;
    // recent path took by spider
    private final Path trailingPath;

	public Spider(Game theGame, Context context, Options bitmapOptions, int resourceId) {
		super(context, bitmapOptions, resourceId);
		this.game = theGame;
		
		// initialize the paint
		trailingLinePaint = new Paint();
        trailingLinePaint.setAntiAlias(true);
        trailingLinePaint.setDither(true);
        trailingLinePaint.setColor(Constants.TRAILING_LINE_COLOR);
        trailingLinePaint.setStyle(Paint.Style.STROKE);
        trailingLinePaint.setStrokeJoin(Paint.Join.ROUND);
        trailingLinePaint.setStrokeCap(Paint.Cap.BUTT);
        trailingLinePaint.setStrokeWidth(Constants.TRAILING_LINE_WIDTH);

        this.trailingPath = new Path();
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
		// draw trailing path
		canvas.drawPath(this.trailingPath, trailingLinePaint);
		// draw trailing line
		if(lastX != -1 && lastY != -1) {
			canvas.drawLine(toScreenX(lastX), toScreenY(lastY),
					toScreenX(x), toScreenY(y), trailingLinePaint);
		}
		super.draw(canvas);
	}
	
	// coordinate conversion methods
	public final float toScreenX(float worldX) {
		return worldX + width/2;
	}
	
	public final float toScreenY(float worldY) {
		return game.getScreenHeight() - (worldY + height/2);
	}

	@Override
	public void boundsTouchBehaviour() {
		setVelocity(0, 0);
		// add last line to path
		this.trailingPath.lineTo(toScreenX(x), toScreenY(y));
		// last line reset
		this.lastX = this.lastY = -1;
		// close path
		this.trailingPath.close();
	}

}
