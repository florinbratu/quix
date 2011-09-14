package com.killerappzz.spider.engine;

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
	// the time
	private TimeHandler time;
	
	public GameData() {
		this.claimedArea = 0;
		this.score = 0;
		this.time = new TimeHandler();
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

	public void setClaimedArea(float area) {
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
		
		public void addTime(float timeDeltaSecs) {
			this.totalTime += timeDeltaSecs;
			this.mins = (int)(this.totalTime / 60);
			this.secs = (int)this.totalTime - this.mins * 60;
			float decimal = this.totalTime - (int)this.totalTime;
			this.deciSecs = (int)(10.0f * decimal);
		}
	}
}