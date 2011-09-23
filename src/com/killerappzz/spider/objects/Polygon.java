package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

import android.graphics.RectF;

import com.killerappzz.spider.geometry.Edge2D;
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

	final List<Point2D> vertices;
	// the point which is also located on the edge
	private final List<Point2D> borderVertices;
	// for comparison. faster than comparing all vertices!
	private final long ID;
	private final RectF screenRect;
	// if the polygon contains any corners, they will be stored here
	final List<Point2D> corners; 
	
	public Polygon(RectF screenRect) {
		this.ID = IDGenerator.generate();
		this.vertices = new CircularLinkedList<Point2D>();
		this.borderVertices = new LinkedList<Point2D>();
		this.corners = new LinkedList<Point2D>();
		this.screenRect = screenRect;
	}
	
	public Polygon(Polygon orig) {
		super(orig);
		this.ID = orig.ID;
		this.vertices = orig.vertices;
		this.borderVertices = orig.borderVertices;
		this.corners = orig.corners;
		this.screenRect = orig.screenRect;
	}
	
	@Override
	public void moveTo(float x, float y) {
		Point2D point = new Point2D.Float(x, y);
		if(boundsTest(this.screenRect, x, y))
			this.borderVertices.add(point);
		if(cornersTest(x,y))
			this.corners.add(point);
		this.vertices.add(point);
		super.moveTo(x, y);
	}

	@Override
	public void lineTo(float x, float y) {
		Point2D point = new Point2D.Float(x, y);
		if(boundsTest(this.screenRect, x,y))
			this.borderVertices.add(point);
		if(cornersTest(x,y))
			this.corners.add(point);
		this.vertices.add(point);
		super.lineTo(x, y);
	}
	
	@Override
	public void rewind() {
		this.vertices.clear();
		this.borderVertices.clear();
		super.rewind();
	}
	
	@Override
	public void reset() {
		this.vertices.clear();
		this.borderVertices.clear();
		super.reset();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Polygon))
			return false;
		Polygon other = (Polygon)o;
		return other.ID == this.ID;
	}
	
	private boolean cornersTest(float x, float y) {
		return (this.screenRect.left == x && this.screenRect.top == y)
				|| (this.screenRect.right == x && this.screenRect.top == y)
				|| (this.screenRect.left == x && this.screenRect.bottom == y)
				|| (this.screenRect.right == x && this.screenRect.bottom == y);
		
	}
	
	public Point2D getClosestBorderVertex(Point2D point) {
		// get the border point closest to the given point
		double dist = Double.MAX_VALUE;
		Point2D closestPoint = null;
		for(Point2D vertex: this.borderVertices) {
			double d = vertex.distance(point);
			if( d < dist) {
				dist = d;
				closestPoint = vertex;
			}
		}
		return closestPoint;
	}
	
	/**
	 * Try to return a point that is on the same 
	 * border as the given omolog, if exists.
	 * Otherwise default to the closest border point
	 *  
	 * @param omolog the point which is on the border already
	 * @return
	 */
	public Point2D getSameBorderVertex(Point2D omolog) {
		for(Point2D vertex: this.borderVertices) 
			if( sameBorder(vertex, omolog)) 
				return vertex;
		return getClosestBorderVertex(omolog);
	}
	
	private boolean sameBorder(Point2D vertex, Point2D omolog) {
		return vertex.getX() == omolog.getX() || vertex.getY() == omolog.getY();
	}

	/**
	 * Get closest vertex to given point
	 */
	public Point2D getClosestVertex(Point2D point) {
		double dist = Double.MAX_VALUE;
		Point2D closestVertex = null;
		for(Point2D vertex: this.vertices) {
			double d = vertex.distance(point);
			if( d < dist) {
				dist = d;
				closestVertex = vertex;
			}
		}
		return closestVertex;
	}
	
	/**
	 * Polygon area. We are obviously working with simple polygons
	 * Then the area is calculated with the formula from wikipedia:
	 * http://en.wikipedia.org/wiki/Polygon#Area_and_centroid
	 */
	public float area() {
		float area = 0;
		Point2D currentVertex;
		Point2D nextVertex;
		for( int i = 0 ; i < this.vertices.size() - 1; i++) {
			currentVertex = this.vertices.get(i);
			nextVertex = this.vertices.get(i+1);
			area += currentVertex.getX() * nextVertex.getY() - 
				currentVertex.getY() * nextVertex.getX();
		}
		area = Math.abs( 0.5f * area);
		return area;
	}

	/**
	 * Detect onto which edge did we hit when we touched the polygon
	 * @param movementVector
	 * @return the edge which we touched or null if this polygon hasn't been touched
	 */
	public Edge2D getTouchEdge(Edge2D movementVector) {
		Point2D currentVertex;
		Point2D nextVertex;
		Edge2D currentEdge = new Edge2D.Float();
		for( int i = 0 ; i < this.vertices.size() - 1; i++) {
			currentVertex = this.vertices.get(i);
			nextVertex = this.vertices.get(i+1);
			currentEdge.set(currentVertex, nextVertex); 
			if(movementVector.touches(currentEdge))
				return currentEdge;
		}
		return null;
	}
	
}
