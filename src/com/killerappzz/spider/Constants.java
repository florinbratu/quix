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
	
}
