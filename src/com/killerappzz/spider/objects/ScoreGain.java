package com.killerappzz.spider.objects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.Customization;
import com.killerappzz.spider.engine.GameData;

/**
 * Display the gain after spider claims new land
 * It works as follows: display the gain at the position
 * of the geometrical center of the gained polygon.
 * Then fade out the text using alpha blending
 * from Constants.MAX_ALPHA to Constants.MIN_ALPHA
 * using a decrement of Constants.ALPHA_DECREMENT
 * 
 * Meanwhile, animate the text by moving it upwards.
 * The animation frame rate is dictated by the fps ctor arg
 * 
 * @author florin
 *
 */
public class ScoreGain extends DrawableObject{
	
	private final Paint textPaint;
	private final GameData data;
	private int transparency;
	// handle the animation speed ie how long the text fades out
	private final float animPeriod;
	private float animTicker;
	
	public ScoreGain(int scrW, int scrH, int textSize, int fps, GameData data) {
		super(scrW, scrH);
		// use same paint as for Banner
		this.textPaint = Customization.getGainTextPaint(textSize);
		this.data = data;
		this.transparency = 0;
		this.animPeriod = 1.0f / (float)fps;
		this.animTicker = 0;
	}
	
	public ScoreGain(ScoreGain orig) {
		super(orig);
		this.textPaint = orig.textPaint;
		this.transparency = orig.transparency;
		this.animPeriod = orig.animPeriod;
		this.animTicker = orig.animTicker;
		this.data = new GameData(orig.data);
	}
	
	@Override
	public void updatePosition(float timeDeltaSeconds) {
		super.updatePosition(timeDeltaSeconds);
		if(this.transparency > Constants.MIN_ALPHA) {
			this.animTicker += timeDeltaSeconds;
			if(this.animTicker > this.animPeriod) {
				this.animTicker -= this.animPeriod;
				this.transparency -= Constants.ALPHA_DECREMENT;
			}
		}
		else {
			this.transparency = 0;
			if(moves()) 
				setVelocity(0,0);
		}
	}
	
	public void display(float posX, float posY) {
		setPosition(posX, posY);
		this.transparency = Constants.MAX_ALPHA;
		setVelocity(0, -1);
	}

	@Override
	public void cleanup() {
		// nothing to be done
	}

	@Override
	public DrawableObject clone() {
		return new ScoreGain(this);
	}

	@Override
	public void draw(Canvas canvas) {
		this.textPaint.setAlpha(transparency);
		canvas.drawText("+" + data.getGain(), this.getPositionX(), 
				this.getPositionY(), this.textPaint);
	}
	
	@Override
	public void update(DrawableObject omolog) {
		super.update(omolog);
		ScoreGain omologGain = (ScoreGain)omolog;
		this.transparency = omologGain.transparency;
		this.animTicker = omologGain.animTicker;
		this.data.update(omologGain.data);
	}

}
