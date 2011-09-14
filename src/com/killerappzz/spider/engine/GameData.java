package com.killerappzz.spider.engine;

/**
 * Game-related info
 * 
 * @author florin
 *
 */
public class GameData {

	private float claimedArea;
	private float totalClaimedArea;
	
	private long score;
	
	public GameData() {
		this.claimedArea = 0;
		this.score = 0;
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
}
