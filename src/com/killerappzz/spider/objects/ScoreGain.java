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
	
	public ScoreGain(int scrW, int scrH, int textSize, GameData data) {
		super(scrW, scrH);
		// use same paint as for Banner
		this.textPaint = Customization.getSurfaceTextPaint(textSize);
		this.data = data;
		this.transparency = 0;
	}
	
	public ScoreGain(ScoreGain orig) {
		super(orig);
		this.textPaint = orig.textPaint;
		this.transparency = orig.transparency;
		this.data = new GameData(orig.data);
	}
	
	@Override
	public void updatePosition(float timeDeltaSeconds) {
		super.updatePosition(timeDeltaSeconds);
		if(this.transparency > Constants.MIN_ALPHA) 
			this.transparency -= Constants.ALPHA_DECREMENT;
		else {
			this.transparency = Constants.MIN_ALPHA;
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
	public void boundsTouchBehaviour() {
	}

	@Override
	public void claimedPathTouch() {
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
		this.data.update(omologGain.data);
	}

}
