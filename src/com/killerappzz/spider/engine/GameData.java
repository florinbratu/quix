package com.killerappzz.spider.engine;

import com.killerappzz.spider.Constants;

/**
 * Game-related info
 * 
 * @author florin
 *
 */
public class GameData {
	// the area
	private float claimedArea;
	private float totalClaimedArea;
	// the score
	private long score;
	// the gain
	private long gain;
	// the time
	private TimeHandler time;
	// the total number of lives
	private int lifes;
	
	public GameData() {
		this.claimedArea = 0;
		this.score = 0;
		this.time = new TimeHandler();
		this.lifes = Constants.MAX_LIFES;
	}
	
	public GameData(GameData orig) {
		this.claimedArea = orig.claimedArea;
		this.totalClaimedArea = orig.totalClaimedArea;
		this.score = orig.score;
		this.time = new TimeHandler(orig.time);
		this.lifes = orig.lifes;
	}

	public void addClaimedArea(float claimed) {
		this.claimedArea += claimed;
	}
	
	public float getClaimedPercentile() {
		return this.claimedArea / this.totalClaimedArea * 100;
	}
	
	public long getScore() {
		return score;
	}
	
	public long getGain() {
		return gain;
	}
	
	public void setClaimedArea(float area) {
		this.gain = (long)(area - this.claimedArea);
		this.claimedArea = area;
	}

	public void setTotalArea(float totalArea) {
		this.totalClaimedArea = totalArea;
	}

	public void addTime(float timeDeltaSeconds) {
		time.addTime(timeDeltaSeconds);
	}
	
	public int getMinutes() {return this.time.mins;}
	public int getSeconds() {return this.time.secs;}
	public int getDeciSeconds() {return this.time.deciSecs;}
	
	private class TimeHandler {
		public int deciSecs;
		public int secs;
		public int mins;
		private float totalTime;

		public TimeHandler() {
			this.deciSecs = 0;
			this.secs = 0;
			this.mins = 0;
			this.totalTime = 0;
		}
		
		public TimeHandler(TimeHandler orig) {
			this.deciSecs = orig.deciSecs;
			this.secs = orig.secs;
			this.mins = orig.mins;
			this.totalTime = orig.totalTime;
		}

		public void addTime(float timeDeltaSecs) {
			this.totalTime += timeDeltaSecs;
			this.mins = (int)(this.totalTime / 60);
			this.secs = (int)this.totalTime - this.mins * 60;
			float decimal = this.totalTime - (int)this.totalTime;
			this.deciSecs = (int)(10.0f * decimal);
		}

		public void update(TimeHandler orig) {
			this.deciSecs = orig.deciSecs;
			this.secs = orig.secs;
			this.mins = orig.mins;
			this.totalTime = orig.totalTime;
		}
	}

	public int getLifesCount() {
		return lifes;
	}

	// update at each frame
	public void update(GameData omolog) {
		this.claimedArea = omolog.claimedArea;
		this.totalClaimedArea = omolog.totalClaimedArea;
		this.score = omolog.score;
		this.gain = omolog.gain;
		this.time.update(omolog.time);
		this.lifes = omolog.lifes;
 
	}
}
