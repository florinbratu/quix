package com.killerappzz.spider.objects;

import android.graphics.Path;

/**
 * Extra functionality for our Paths
 * @author florin
 *
 */
public class SpiderPath extends Path {
	
	private float firstPointX;
	private float firstPointY;
	
	private float lastPointX;
	private float lastPointY;
	
	// some coords of interest
	private final float bottomY;
	private final float topY;
	private final float leftX;
	private final float rightX;
	
	public SpiderPath(Spider theSpider) {
		super();
		this.bottomY = theSpider.game.getScreenHeight() - theSpider.height / 2;
		this.topY = theSpider.height / 2;
		this.leftX = theSpider.width / 2;
		this.rightX = theSpider.game.getScreenWidth() - theSpider.width / 2;
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
		if(firstPointLabel != lastPointLabel &&
			!firstPointLabel.equals(BorderLabel.CENTER) &&
			!lastPointLabel.equals(BorderLabel.CENTER)) { 
			switch(lastPointLabel) {
				case LEFT:
					switch(firstPointLabel){
					case RIGHT:
						if(firstPointY + lastPointY < bottomY) {
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
						if(firstPointY + lastPointY < bottomY) {
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
						if(lastPointX + firstPointX < rightX) {
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
						if(lastPointX + firstPointX < rightX) {
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
