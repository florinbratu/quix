package com.killerappzz.spider.objects;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.Customization;

import android.graphics.Canvas;

/**
 * The Statistics banner. It will be located 
 * on the top left corner
 * 
 * @author florin
 *
 */
public class Banner {
	
	public final int width;
	public final int height;
	
	public Banner(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

	public void draw(Canvas canvas) {
		canvas.drawText(Constants.SCORE_TEXT, 0, height, Customization.getScorePaint());
		canvas.drawText(Constants.TIME_TEXT + "99:99:99", width * 0.25f, height, Customization.getTimeTextPaint());
		canvas.drawText(Constants.SURFACE_TEXT + 15 + " / 75%", width * 0.5f, height, Customization.getSurfaceTextPaint());
		canvas.drawText(Constants.LIVES_TEXT + "X X X", width * 0.75f, height, Customization.getLivesTextPaint());
	}
}
