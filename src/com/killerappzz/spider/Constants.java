package com.killerappzz.spider;

import android.graphics.Color;

/**
 * The constants
 * @author florin
 *
 */
public interface Constants {

	public static final int TOUCH_ERROR_TOLERANCE_PERCENTILE = 10;
	
	/* vitezele le masor in felul urmator: cate secunde ii ia
	 * sa traverseze tot ecranul!
	 */
	public static final int DEFAULT_SPIDER_SPEED_FACTOR = 4;
	
	/*
	 * look and feel-ul darei lasate de spider
	 */
	public static final int TRAILING_LINE_COLOR = Color.BLUE;
	public static final int TRAILING_LINE_WIDTH = 3;
	
	/*
	 * look and feel-ul teritoriului claimed de spider
	 */
	public static final int CLAIMED_COLOR = Color.BLUE;
	public static final float CLAIMED_STROKE_WIDTH = 2;
	
	/*
	 * Animation options
	 */
	public static final int SPIDER_ANIMATION_FRAMES_COUNT = 3;
	public static final int SPIDER_ANIMATION_FPS = 30;
	/**
	 * The sprites will be available only for certain
	 * movement directions. The list of these is available below.
	 * Each sprite sheet will contain one row foreach of these directions.
	 * So, the sprite sheet format is a matrix of images, with Direction.size()
	 * rows for each direction, and with framesNo columns, where framesNo are
	 * the nb of frames for the sprite(sprite-specific; ctor arg for AnimatedSprite) 
	 */
	public enum Direction {
		NORTH,
		NORTHEAST,
		EAST,
		SOUTHEAST,
		SOUTH,
		SOUTHWEST,
		WEST,
		NORTHWEST;

		/*
		 * The second argument is the signum of the velocity on the X axis
		 * Without it we don't know for sure the quadrant
		 */
		public static Direction fromAngle(double angle, boolean xSignum) {
			double pi_8 = Math.PI / 8;
			if(xSignum) {
				if(angle > 3 * pi_8 )
					return Direction.NORTH;
				if(angle > pi_8)
					return Direction.NORTHEAST;
				if(angle > -pi_8)
					return Direction.EAST;
				if(angle > - 3 * pi_8)
					return Direction.SOUTHEAST;
			}
			else {
				if(angle > 3 * pi_8)
					return Direction.SOUTH;
				if(angle > pi_8)
					return Direction.SOUTHWEST;
				if(angle > -pi_8)
					return Direction.WEST;
				if(angle > - 3 * pi_8)
					return Direction.NORTHWEST;
			}
			return Direction.NORTH;
		}
	};
	
}
