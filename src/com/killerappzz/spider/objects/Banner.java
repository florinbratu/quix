package com.killerappzz.spider.objects;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.Customization;
import com.killerappzz.spider.engine.GameData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.BitmapFactory.Options;

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
public class Banner extends DrawableObject{
	
	private final int width;
	private final int height;
	
	private Paint scorePaint;
	private Paint timeTextPaint;
	private Paint surfaceTextPaint;
	private Paint livesTextPaint;

	// banner layout parameters
	private final int spaceToBorder;
	private final int spaceBetweenText; 
	private int fontSize;
	// the image for Life
	private final Sprite lifeImg;
	
	// the game data. stats will draw its freeds from here
	private final GameData data;
	
	public Banner(Context context, String statsFontName, 
			int width, int height, int screenWidth, int screenHeight, 
			Options bitmapOptions, int resourceId, GameData data) {
		super(screenWidth, screenHeight);
		this.data = data;
		this.width = width;
		this.height = height;
		
		// the Banner font
		Customization.statsFont(Typeface.createFromAsset(
				context.getAssets(), statsFontName));
		// setup font size
		this.fontSize = this.height / 2;
		float totalTextSize;
		do {
			this.scorePaint = Customization.getScorePaint(fontSize);
			this.timeTextPaint = Customization.getTimeTextPaint(fontSize);
			this.surfaceTextPaint = Customization.getSurfaceTextPaint(fontSize);
			this.livesTextPaint = Customization.getLivesTextPaint(fontSize);
			totalTextSize = precomputeTextSize();
			if(totalTextSize > (this.width * Constants.STATISTICS_BANNER_TOLERANCE_PERCENTILE) / 100) 
				this.fontSize -= Constants.STATISTICS_BANNER_FONT_DECREMENT;
		} while( totalTextSize > (this.width * Constants.STATISTICS_BANNER_TOLERANCE_PERCENTILE) / 100 );
		float freeScreen = (float)this.width - totalTextSize;
		this.spaceToBorder = (int)(freeScreen / ( 2.0f + 2.0f * Constants.FREE_SPACE_INTER_TO_BORDER_RATIO ));
		this.spaceBetweenText = this.spaceToBorder * Constants.FREE_SPACE_INTER_TO_BORDER_RATIO;
		// the Life image
		this.lifeImg = new Sprite(context, bitmapOptions, resourceId, width, screenHeight);
	}

	public Banner(Banner orig) {
		super(orig);
		this.width = orig.width;
		this.height = orig.height;
		this.scorePaint = orig.scorePaint;
		this.timeTextPaint = orig.timeTextPaint;
		this.surfaceTextPaint = orig.surfaceTextPaint;
		this.livesTextPaint = orig.livesTextPaint;
		this.spaceToBorder = orig.spaceToBorder;
		this.spaceBetweenText = orig.spaceBetweenText;
		this.fontSize = orig.fontSize;
		this.lifeImg = orig.lifeImg;
		this.data = new GameData(orig.data);
	}

	private float precomputeTextSize() {
		return scorePaint.measureText(Constants.SCORE_TEXT + Constants.MAX_SCORE) +
			timeTextPaint.measureText(Constants.TIME_TEXT + Constants.MAX_TIME) +
			surfaceTextPaint.measureText(Constants.SURFACE_TEXT + Constants.MAX_SURFACE + Constants.SURFACE_PERCENTAGE)
			;
	}

	@Override
	public void draw(Canvas canvas) {
		float posX = spaceToBorder;
		float posY = (this.height + this.fontSize) / 2; // center the font position on the border
		canvas.drawText(Constants.SCORE_TEXT + data.getScore(), posX, posY, scorePaint);
		posX += scorePaint.measureText(Constants.SCORE_TEXT + Constants.MAX_SCORE) + spaceBetweenText;
		canvas.drawText(Constants.TIME_TEXT + this.data.getMinutes() + ":" 
				+ (this.data.getSeconds() < 10 ? "0" + this.data.getSeconds() : this.data.getSeconds()) 
				+ ":" + this.data.getDeciSeconds(), posX, posY, timeTextPaint);
		posX += timeTextPaint.measureText(Constants.TIME_TEXT + Constants.MAX_TIME) + spaceBetweenText;
		canvas.drawText(Constants.SURFACE_TEXT + (int)data.getClaimedPercentile() + Constants.SURFACE_PERCENTAGE, posX, posY, surfaceTextPaint);
		// the lives text will be placed on the bottom banner
		posX = this.width - (this.livesTextPaint.measureText(Constants.LIVES_TEXT) 
				+ Constants.MAX_LIFES * this.lifeImg.width + this.spaceToBorder); 
		posY = this.screenHeight - (this.height - this.fontSize) / 2;
		canvas.drawText(Constants.LIVES_TEXT, posX , posY, livesTextPaint);
		posX += livesTextPaint.measureText(Constants.LIVES_TEXT);
		for(int i = 0 ; i < data.getLifesCount() ; i++) {
			this.lifeImg.setPosition(posX, 0);
			this.lifeImg.draw(canvas);
			posX += this.lifeImg.width;
		}
	}

	@Override
	public void cleanup() {
		this.lifeImg.cleanup();
	}
	
	@Override
	public DrawableObject clone() {
		return new Banner(this);
	}
	
	@Override
	public void update(DrawableObject omolog) {
		super.update(omolog);
		Banner omologBanner = (Banner)omolog;
		this.data.update(omologBanner.data);
	}

	@Override
	public void reset() {
		// banner draws its state from the GameData => nothing changes
	}
}
