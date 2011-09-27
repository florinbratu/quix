package com.killerappzz.spider.engine;

import android.os.Parcel;
import android.os.Parcelable;

import com.killerappzz.spider.Constants;

/**
 * Game-related info.
 * We use the parcelable shit to pass it 
 * between activities(ex game over activity)
 * 
 * @author florin
 *
 */
public class GameData implements Parcelable{
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
	// game over indicator
	private boolean gameOver;
	// if game over - success flag
	private boolean victory;
	/* Victory conditions:
	 * - more than 75% claimed surface
	 * - spider traps bat inside its net
	 * - TODO score points acquired
	 * Lose conditions:
	 * - spider has no more life points
	 * - TODO time limit exceeded
	 *  */
	
	public GameData() {
		this.claimedArea = 0;
		this.score = 0;
		this.time = new TimeHandler();
		this.lifes = Constants.MAX_LIFES;
		this.gameOver = false;
		this.victory = false;
	}
	
	public GameData(GameData orig) {
		this.claimedArea = orig.claimedArea;
		this.totalClaimedArea = orig.totalClaimedArea;
		this.score = orig.score;
		this.time = new TimeHandler(orig.time);
		this.lifes = orig.lifes;
		this.gameOver = orig.gameOver;
		this.victory = orig.victory;
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
		updateScoreForGain();
		this.claimedArea = area;
		if(getClaimedPercentile() > Constants.MAX_SURFACE) 
			victory();
	}

	/**
	 * Update the score after we gained some field.
	 * For the moment, we simply add the gain to the
	 * total score. TODO something more sophisticated
	 * in the future - more score for bigger areas, and
	 * for more straight ones :)
	 */
	private void updateScoreForGain() {
		this.score += this.gain;
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
	
	private class TimeHandler{
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
	
	public void lostLife() {
		this.lifes--;
		if(this.lifes == 0) 
			death();
	}

	public void death() {
		this.gameOver = true;
		this.victory = false;
	}
	
	public void victory() {
		this.gameOver = true;
		this.victory = true;
	}

	// update at each frame
	public void update(GameData omolog) {
		this.claimedArea = omolog.claimedArea;
		this.totalClaimedArea = omolog.totalClaimedArea;
		this.score = omolog.score;
		this.gain = omolog.gain;
		this.time.update(omolog.time);
		this.lifes = omolog.lifes;
		this.gameOver = omolog.gameOver;
		this.victory = omolog.victory;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(claimedArea);
		dest.writeFloat(totalClaimedArea);
		dest.writeLong(score);
		dest.writeLong(gain);
		dest.writeFloat(time.totalTime);
		dest.writeInt(lifes);
		dest.writeBooleanArray(new boolean[]{gameOver,victory});
	}
	
	public static final Parcelable.Creator<GameData> CREATOR
		= new Parcelable.Creator<GameData>() {
		public GameData createFromParcel(Parcel in) {
			return new GameData(in);
		}

		public GameData[] newArray(int size) {
			return new GameData[size];
		}
	};
	
	private GameData(Parcel in) {
		this.claimedArea = in.readFloat();
		this.totalClaimedArea = in.readFloat();
		this.score = in.readLong();
		this.gain = in.readLong();
		this.time = new TimeHandler();
		this.time.totalTime = in.readFloat();
		this.lifes = in.readInt();
		boolean array[] = new boolean[2];
		in.readBooleanArray(array);
		this.gameOver = array[0];
		this.victory = array[1];
	}
	
}
