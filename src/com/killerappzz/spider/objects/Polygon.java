package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

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
	
	public Polygon(int ID) {
		this.ID = ID;
		this.vertices = new LinkedList<Pair<Float,Float>>();
		this.borderPoint = null;
	}
	
	@Override
	public void moveTo(float x, float y) {
		if(borderPoint != null)
			throw new IllegalStateException("A polygon needs to have a single contour!");
		this.borderPoint = new Pair<Float, Float>(x, y);
		this.vertices.add(borderPoint);
		super.moveTo(x, y);
	}
	
	@Override
	public void lineTo(float x, float y) {
		this.vertices.add(new Pair<Float, Float>(x, y));
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
	
}
