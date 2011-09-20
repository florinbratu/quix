package com.killerappzz.spider.objects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.killerappzz.spider.Constants;
import com.killerappzz.spider.Customization;
import com.killerappzz.spider.engine.GameData;

/**
 * Display the gain after spider claims new land
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
		this.x = posX;
		this.y = posY;
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
		canvas.drawText("+" + data.getGain(), this.x, 
				this.y, this.textPaint);
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
