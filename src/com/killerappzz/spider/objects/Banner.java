package com.killerappzz.spider.objects;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.Customization;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * The Statistics banner. Located on top of the screen.
 * 
 * The banner layout is the following:
 * 
 * s1 | Score: $score | i1 | Time: $time | i2 | Surface: $surface | i3 | Lives: * * * | s2
 * 
 * $score $time $surface and Lives are dependent on game info
 * but they are limited in size - limitation in Constants fields.
 * 
 * s1, s2 is free space margin between the banner and the borders. All equal.
 * i1, i2 and i3 is the space between the text elements. All equal.
 *
 * The free space is divided between the border and the inter-elements via a ratio defined in Constants.
 * 
 * @author florin
 *
 */
public class Banner {
	
	private final int width;
	private final int height;
	
	private final Paint scorePaint;
	private final Paint timeTextPaint;
	private final Paint surfaceTextPaint;
	private final Paint livesTextPaint;

	// banner layout parameters
	private final int spaceToBorder;
	private final int spaceBetweenText;
	private final int fontSize;
	private final int screenHeight;
	
	public Banner(int width, int height, int screenHeight) {
		super();
		this.width = width;
		this.height = height;
		this.screenHeight = screenHeight;
		this.fontSize = this.height / 2;
		this.scorePaint = Customization.getScorePaint(fontSize);
		this.timeTextPaint = Customization.getTimeTextPaint(fontSize);
		this.surfaceTextPaint = Customization.getSurfaceTextPaint(fontSize);
		this.livesTextPaint = Customization.getLivesTextPaint(fontSize);
		float totalTextSize = precomputeTextSize();
		if(totalTextSize > this.width) {
			// TODO need to decrease font size
		} 
		float freeScreen = (float)this.width - totalTextSize;
		this.spaceToBorder = (int)(freeScreen / ( 2.0f + 2.0f * Constants.FREE_SPACE_INTER_TO_BORDER_RATIO ));
		this.spaceBetweenText = this.spaceToBorder * Constants.FREE_SPACE_INTER_TO_BORDER_RATIO;
	}

	private float precomputeTextSize() {
		return scorePaint.measureText(Constants.SCORE_TEXT + Constants.MAX_SCORE) +
			timeTextPaint.measureText(Constants.TIME_TEXT + Constants.MAX_TIME) +
			surfaceTextPaint.measureText(Constants.SURFACE_TEXT + Constants.MAX_SURFACE)
			;
	}

	public void draw(Canvas canvas) {
		float posX = spaceToBorder;
		float posY = (this.height + this.fontSize) / 2; // center the font position on the border
		canvas.drawText(Constants.SCORE_TEXT + Constants.MAX_SCORE, posX, posY, scorePaint);
		posX += scorePaint.measureText(Constants.SCORE_TEXT + Constants.MAX_SCORE) + spaceBetweenText;
		canvas.drawText(Constants.TIME_TEXT + Constants.MAX_TIME, posX, posY, timeTextPaint);
		posX += timeTextPaint.measureText(Constants.TIME_TEXT + Constants.MAX_TIME) + spaceBetweenText;
		canvas.drawText(Constants.SURFACE_TEXT + Constants.MAX_SURFACE, posX, posY, surfaceTextPaint);
		// the lives text will be placed on the bottom banner
		posX = this.width - (this.livesTextPaint.measureText(Constants.LIVES_TEXT) 
				+ 3 * this.fontSize + this.spaceToBorder); // assume we have 3 lives each one is a square icon
		posY = this.screenHeight - (this.height - this.fontSize) / 2;
		canvas.drawText(Constants.LIVES_TEXT + "TODO", posX , posY, livesTextPaint);
	}
}
