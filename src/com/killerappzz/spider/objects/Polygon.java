package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

import com.killerappzz.spider.geometry.Point2D;
import com.killerappzz.spider.util.CircularLinkedList;

import android.graphics.RectF;
import android.util.Pair;

/**
 * A Path having a single contour.
 * Contains detailed information about 
 * its vertices.
 * 
 * This Path has the property that it always starts 
 * from a point which is on the edge of the board
 * 
 * @author florin
 *
 */
public class Polygon extends GeometricPath {

	final List<Pair<Float, Float>> vertices;
	// the point which is also located on the edge
	private Pair<Float, Float> borderPoint;
	// for comparison. faster than comparing all vertices!
	private final int ID;
	private final RectF screenRect;
	
	public Polygon(int ID, RectF screenRect) {
		this.ID = ID;
		this.vertices = new CircularLinkedList<Pair<Float,Float>>();
		this.borderPoint = null;
		this.screenRect = screenRect;
	}
	
	@Override
	public void moveTo(float x, float y) {
		Pair<Float, Float> point = new Pair<Float, Float>(x, y);
		if(boundsTest(this.screenRect, x,y))
			this.borderPoint = point;
		this.vertices.add(point);
		super.moveTo(x, y);
	}
	
	@Override
	public void lineTo(float x, float y) {
		Pair<Float, Float> point = new Pair<Float, Float>(x, y);
		if(boundsTest(this.screenRect, x,y))
			this.borderPoint = point;
		this.vertices.add(point);
		super.lineTo(x, y);
	}
	
	@Override
	public void rewind() {
		this.vertices.clear();
		this.borderPoint = null;
		super.rewind();
	}
	
	@Override
	public void reset() {
		this.vertices.clear();
		this.borderPoint = null;
		super.reset();
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Polygon))
			return false;
		Polygon other = (Polygon)o;
		return other.ID == this.ID;
	}
	
	public Point2D.Float getBorderPoint() {
		return new Point2D.Float(this.borderPoint.first, this.borderPoint.second);
	}
	
}
