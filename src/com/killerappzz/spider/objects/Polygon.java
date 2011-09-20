package com.killerappzz.spider.objects;

import java.util.List;

import android.graphics.RectF;
import android.util.Pair;

import com.killerappzz.spider.geometry.Point2D;
import com.killerappzz.spider.util.CircularLinkedList;
import com.killerappzz.spider.util.IDGenerator;

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
	private final long ID;
	private final RectF screenRect;
	
	public Polygon(RectF screenRect) {
		this.ID = IDGenerator.generate();
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
		if(o == null || !(o instanceof Polygon))
			return false;
		Polygon other = (Polygon)o;
		return other.ID == this.ID;
	}
	
	public Point2D.Float getBorderPoint() {
		return new Point2D.Float(this.borderPoint.first, this.borderPoint.second);
	}
	
	/**
	 * Get closest vertex to given point
	 */
	public Point2D.Float getClosestVertex(Point2D.Float point) {
		double dist = Double.MAX_VALUE;
		Pair<Float,Float> closestVertex = null;
		for(Pair<Float,Float> vertex: this.vertices) {
			double d = distance(vertex,point);
			if( d < dist) {
				dist = d;
				closestVertex = vertex;
			}
		}
		return new Point2D.Float(closestVertex.first, closestVertex.second);
	}
	
	/* Euclidean distance between two points */
	private double distance(Pair<Float,Float> vertex, Point2D.Float point) {
		return Math.sqrt( (vertex.first - point.x) * (vertex.first - point.x)
				+ (vertex.second - point.y) * (vertex.second - point.y));
	}
	
	/**
	 * Polygon area. We are obviously working with simple polygons
	 * Then the area is calculated with the formula from wikipedia:
	 * http://en.wikipedia.org/wiki/Polygon#Area_and_centroid
	 */
	public float area() {
		float area = 0;
		Pair<Float, Float> currentVertex;
		Pair<Float, Float> nextVertex;
		for( int i = 0 ; i < this.vertices.size() - 1; i++) {
			currentVertex = this.vertices.get(i);
			nextVertex = this.vertices.get(i+1);
			area += currentVertex.first * nextVertex.second - 
				currentVertex.second * nextVertex.first;
		}
		area = Math.abs( 0.5f * area);
		return area;
	}

	/**
	 * Detect onto which edge did we hit when we touched the polygon
	 * @param movementVector
	 * @return the edge which we touched or null if this polygon hasn't been touched
	 */
	public Pair<Pair<Float, Float>, Pair<Float, Float>> getTouchEdge(
			Pair<Pair<Float, Float>, Pair<Float, Float>> movementVector) {
		Pair<Float, Float> currentVertex;
		Pair<Float, Float> nextVertex;
		for( int i = 0 ; i < this.vertices.size() - 1; i++) {
			currentVertex = this.vertices.get(i);
			nextVertex = this.vertices.get(i+1);
			Pair<Pair<Float, Float>, Pair<Float, Float>> edge 
		 		= new Pair<Pair<Float, Float>, Pair<Float, Float>>(currentVertex, nextVertex);
			if(touch(edge, movementVector))
				return edge;
		}
		return null;
	}

	/**
	 * Test if two vectors "intersect"
	 * The test verifies if the two extreme points of the movement vector
	 * are on "different sides" of the edge.
	 * 
	 * The "different sides" test is being performed via cross product
	 * Each side is identified by the sign of the cross product over the edge
	 */
	private boolean touch(Pair<Pair<Float, Float>, Pair<Float, Float>> edge,
			Pair<Pair<Float, Float>, Pair<Float, Float>> movementVector) {
		return Math.signum(crossprod(movementVector.first, edge)) 
			!= Math.signum(crossprod(movementVector.second, edge));
	}

	/**
	 * Calculate the following cross product: 
	 * 	(e1, point) x (e1, e2)
	 * 
	 * e1 and e2 are the two extremes of the edge
	 * 
	 * @param point
	 * @param edge is (e1,e2)
	 * @return
	 */
	private double crossprod(Pair<Float, Float> point,
			Pair<Pair<Float, Float>, Pair<Float, Float>> edge) {
		Pair<Float, Float> e1 = edge.first;
		Pair<Float, Float> e2 = edge.second;
		float x1 = point.first - e1.first;
		float y1 = point.second - e1.second;
		float x2 = e2.first - e1.first;
		float y2 = e2.second - e1.second;
		return x1 * y2 - x2 * y1;
	}
	
}
