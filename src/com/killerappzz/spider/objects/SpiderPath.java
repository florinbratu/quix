package com.killerappzz.spider.objects;

import com.killerappzz.spider.geometry.Point2D;

import android.graphics.RectF;


/**
 * Extra functionality for our Paths
 * @author florin
 *
 */
public class SpiderPath extends Polygon {
	
	private float firstPointX;
	private float firstPointY;
	
	private float lastPointX;
	private float lastPointY;
	
	// some coords of interest
	private final float bottomY;
	private final float topY;
	private final float leftX;
	private final float rightX;

	// handler for an ugly case
	private final TopBottomHandler tbh;
	
	public SpiderPath(RectF screenRect, TopBottomHandler t) {
		super(screenRect);
		this.bottomY = screenRect.bottom;
		this.topY = screenRect.top;
		this.leftX = screenRect.left;
		this.rightX = screenRect.right;
		this.tbh = t;
	}

	public SpiderPath(SpiderPath trailingPath) {
		super(trailingPath);
		this.bottomY = trailingPath.bottomY;
		this.topY = trailingPath.topY;
		this.leftX = trailingPath.leftX;
		this.rightX = trailingPath.rightX;
		this.tbh = trailingPath.tbh;
	}

	/** 
	 * Note the first point
	 */
	@Override
	public void moveTo(float x, float y) {
		this.firstPointX = x;
		this.firstPointY = y;
		super.moveTo(x, y);
	}
	
	/**
	 * Note the tip of the movement
	 */
	@Override
	public void lineTo(float x, float y) {
		this.lastPointX = x;
		this.lastPointY = y;
		super.lineTo(x, y);
	}

	/**
	 * Small adjustment to the path closing:
	 * if the initial point and the finish point
	 * are both on borders => select the filled in area!
	 */
	@Override
	public void close() {
		addCorners();
		super.close();
	}
	
	/**
	 * If necessary, add the corners to the Path,
	 * according to the positions of the 
	 * first and last points of the path
	 * 
	 * Worst case when points are on opposite sides.
	 * In this case we select the corners
	 * for which the smallest area is claimed.
	 * 
	 * Some simple math shows that we need to select the corners
	 * for which D > L + l, where:
	 * - D is the size of the side containig the points
	 * 		(same for opposite sides, it's a rectangle)
	 * - L, l are the two distances from the points 
	 * 		to the corners candidate for selection
	 * 
	 * */
	private void addCorners() {
		// let's label the start and end points of our path
		BorderLabel firstPointLabel = fromPos(firstPointX, firstPointY);
		BorderLabel lastPointLabel = fromPos(lastPointX, lastPointY);
		float threshold = 0;
		if(firstPointLabel != lastPointLabel &&
			!firstPointLabel.equals(BorderLabel.CENTER) &&
			!lastPointLabel.equals(BorderLabel.CENTER)) { 
			switch(lastPointLabel) {
			case LEFT:
				switch(firstPointLabel){
				case RIGHT:
					threshold = handleHorizontal();
					if(firstPointY + lastPointY < threshold) {
						// select top points
						this.lineTo(leftX, topY);
						this.lineTo(rightX, topY);
					}
					else {
						// select bottom points
						this.lineTo(leftX, bottomY);
						this.lineTo(rightX, bottomY);
					}
					break;
				case BOTTOM:
					this.lineTo(leftX, bottomY);
					break;
				case TOP:
					this.lineTo(leftX, topY);
					break;
				}
				break;
			case RIGHT:
				switch(firstPointLabel){
				case LEFT:
					threshold = handleHorizontal();
					if(firstPointY + lastPointY < threshold) {
						// select top points
						this.lineTo(rightX, topY);
						this.lineTo(leftX, topY);
					}
					else {
						// select bottom points
						this.lineTo(rightX, bottomY);
						this.lineTo(leftX, bottomY);
					}
					break;
				case BOTTOM:
					this.lineTo(rightX, bottomY);
					break;
				case TOP:
					this.lineTo(rightX, topY);
					break;
				}
				break;
			case BOTTOM:
				switch(firstPointLabel){
				case TOP:
					threshold = handleVertical();
					if(firstPointY + lastPointY < threshold) {
						// select left points
						this.lineTo(leftX, bottomY);
						this.lineTo(leftX, topY);
					} else {
						// select right points
						this.lineTo(rightX, bottomY);
						this.lineTo(rightX, topY);
					}
					break;
				case RIGHT:
					this.lineTo(rightX, bottomY);
					break;
				case LEFT:
					this.lineTo(leftX, bottomY);
					break;
				}
				break;
			case TOP:
				switch(firstPointLabel){
				case BOTTOM:
					threshold = handleVertical();
					if(firstPointY + lastPointY < threshold) {
						// select left points
						this.lineTo(leftX, topY);
						this.lineTo(leftX, bottomY);
					} else {
						// select right points
						this.lineTo(rightX, topY);
						this.lineTo(rightX, bottomY);
					}
					break;
				case LEFT:
					this.lineTo(leftX, topY);
					break;
				case RIGHT:
					this.lineTo(rightX, topY);
					break;
				}
				break;
			}
		}
	}

	private float handleHorizontal() {
		Point2D topExtremes = tbh.getTopExtremes();
		Point2D bottomExtremes = tbh.getBottomExtremes();
		double threshold = (topExtremes.getX() + topExtremes.getY() + 
				bottomExtremes.getX() + bottomExtremes.getY()) / 2;
		return (float)threshold;
	}
	
	/* handle vertical corners add op */
	private float handleVertical() {
		Point2D topExtremes = tbh.getLeftExtremes();
		Point2D bottomExtremes = tbh.getRightExtremes();
		double threshold = (topExtremes.getX() + topExtremes.getY() + 
				bottomExtremes.getX() + bottomExtremes.getY()) / 2;
		return (float)threshold;
	}

	private BorderLabel fromPos(float posX, float posY) {
		if(posX == leftX) 
			return BorderLabel.LEFT;
		if(posX == rightX)
			return BorderLabel.RIGHT;
		if(posY == topY)
			return BorderLabel.TOP;
		if(posY == bottomY)
			return BorderLabel.BOTTOM;
		return BorderLabel.CENTER; // not on border!
	}
	
	private enum BorderLabel {
		BOTTOM,
		RIGHT,
		TOP,
		LEFT,
		CENTER
	};

}
