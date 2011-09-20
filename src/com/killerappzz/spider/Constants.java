package com.killerappzz.spider;

import android.graphics.Color;

/**
 * The constants
 * @author florin
 *
 */
public interface Constants {

	/* vitezele le masor in felul urmator: cate secunde ii ia
	 * sa traverseze tot ecranul!
	 */
	public static final int DEFAULT_SPIDER_SPEED_FACTOR = 4;
	public static final int DEFAULT_BAT_SPEED_FACTOR = 5;
	
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
	 * statistic banner look and feel
	 */
	public static final String STATS_FONT_ASSET = "fonts/Starcraft Normal.ttf";
	public static final String SCORE_TEXT = "Score: ";
	public static final String MAX_SCORE = "1000000";
	public static final int SCORE_TEXT_COLOR = Color.YELLOW;
	public static final String TIME_TEXT = "Time: ";
	public static final String MAX_TIME = "99:99:99";
	public static final int TIME_TEXT_COLOR = Color.GREEN;
	public static final String SURFACE_TEXT = "Area: ";
	public static final int MAX_SURFACE = 75;
	public static final String SURFACE_PERCENTAGE = "/ 75%";
	public static final int SURFACE_TEXT_COLOR = Color.BLUE;
	public static final String LIVES_TEXT = "Lives: ";
	public static final int LIVES_TEXT_COLOR = Color.YELLOW; // orange
	public static final int GAIN_TEXT_COLOR = Color.WHITE;
	// initial number of lifes
	public static final int MAX_LIFES = 3;
	public static final int FREE_SPACE_INTER_TO_BORDER_RATIO = 3;
	
	/*
	 * Animation options
	 */
	public static final int SPIDER_ANIMATION_FRAMES_COUNT = 3;
	public static final int SPIDER_ANIMATION_FPS = 30;
	public static final int BAT_ANIMATION_FRAMES_COUNT = 3;
	public static final int BAT_ANIMATION_FPS = 30;
	
	/* score text look and feel */
	public static final int MAX_ALPHA = 255;
	public static final int ALPHA_DECREMENT = 16;
	public static final int MIN_ALPHA = 64;
	public static final int SCORE_TEXT_FPS = 10;

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
				else return Direction.SOUTH;
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
				else return Direction.NORTH;
			}
		}
	};
	
	// programming-related constants
	public static final String LOG_TAG = "QUIX";
}
