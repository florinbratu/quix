package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

import android.graphics.RectF;

import com.killerappzz.spider.geometry.Edge2D;
import com.killerappzz.spider.geometry.Point2D;

/**
 * The Path claimed by the spider insofar.
 * 
 * @author florin
 *
 */
public class ClaimedPath extends GeometricPath {

	// the list of polygons contained by this Path
	private final List<Polygon> polygons;
	// the currently-observed polygon
	private Polygon currentPolygon = null;
	private final RectF screenRect;
	
	public ClaimedPath(RectF sr) {
		super();
		this.polygons = new LinkedList<Polygon>();
		this.screenRect = sr;
	}
	
	public ClaimedPath(ClaimedPath orig) {
		super(orig);
		this.polygons = orig.polygons;
		this.screenRect = orig.screenRect;
	}

	@Override
	public void moveTo(float x, float y) {
		currentPolygon = new Polygon(screenRect);
		currentPolygon.moveTo(x, y);
		super.moveTo(x, y);
	}
	
	@Override
	public void lineTo(float x, float y) {
		currentPolygon.lineTo(x, y);
		super.lineTo(x, y);
	}
	
	@Override
	public void close() {
		currentPolygon.close();
		polygons.add(currentPolygon);
		currentPolygon = null;
		super.close();
	}
	
	@Override
	public void rewind() {
		for(Polygon p : polygons) {
			p.rewind();
		}
		polygons.clear();
		super.rewind();
	}
	
	/**
	 * Reduce the surface touch case 
	 *  to the bounds touch case
	 * @param path the path to be reduced to bounds. It will change
	 * @param screenRect the rectangle defining the screen area
	 */
	public GeometricPath reduceToBounds(GeometricPath path, RectF screenRect) {
		Point2D startPoint = path.getStartPoint();
		Point2D endPoint = path.getEndPoint();
		// if both points on border
		if(boundsTest(screenRect, startPoint) && boundsTest(screenRect, endPoint))
			// nothing to be done
			return path;
		Polygon endPoly = null, startPoly = null;
		Point2D endClosest = null, startClosest = null;
		for(Polygon p : polygons) {
			if(p.contains(endPoint.getX(), endPoint.getY())) {
				endPoly = p;
				endClosest = p.getClosestVertex(endPoint);
			}
			if(p.contains(startPoint.getX(), startPoint.getY())) {
				startPoly = p;
				startClosest = p.getClosestVertex(startPoint);
			}
		}
		if( endPoly == null) {
			// the endpoint is on the edge
			if(startPoly != null) {
				// the starting point is on a polygon
				// get the path from the bound point on start polygon to the start point
				GeometricPath ret = getPathToStartPoint(startPoly, startClosest, screenRect);
				// add the start point
				ret.lineTo(startPoint.getX(), startPoint.getY());
				// add the path done by the spider
				ret.addGeometricPath(path);
				return ret;
			}
			else
				// otherwise we have nothing to do!
				return path;
		} else if( startPoly == null ) {
			// the starting point is on the edge
			// move to endpoint closest on end path
			path.lineTo(endClosest.getX(), endClosest.getY());
			// it's enough to go from endpoint to edge
			// by following the polygon line containing the endpoint
			pushToBounds(path, endPoly, endClosest, startPoint);
			return path;
		} else if(startPoly.equals(endPoly)) {
			// start and end points are on the same polygon
			// just connect them via polygon lines
			// but first! get to the closest vertex
			// start closest
			GeometricPath ret = new SpiderPath(screenRect);
			ret.moveTo(startClosest.getX(), startClosest.getY());
			ret.lineTo(startPoint.getX(), startPoint.getY());
			ret.addGeometricPath(path);
			// end closest
			ret.lineTo(endClosest.getX(), endClosest.getY());
			pushEndpoint(ret, endPoly, startClosest, endClosest);
			return ret;
		} else {
			// ugliest case: start and end points are on two different polygons
			// get the path from the bound point on start polygon to the start point
			GeometricPath ret = getPathToStartPoint(startPoly, startClosest, screenRect);
			// add the start point
			ret.lineTo(startPoint.getX(), startPoint.getY());
			// add the path done by the spider
			ret.addGeometricPath(path);
			// goto closest vertex from endpoint
			ret.lineTo(endClosest.getX(), endClosest.getY());
			// add the path to the bounds on the end polygon
			pushToBounds(ret, endPoly, endClosest, ret.getStartPoint());
			return ret;
		}
	}

	private GeometricPath getPathToStartPoint(Polygon bound, 
			Point2D startPoint, RectF screenRect) {
		GeometricPath path = new SpiderPath(screenRect);
		Point2D borderVertex = bound.getClosestBorderVertex(startPoint);
		boolean found = false;
		for(Point2D vertex : bound.vertices) {
			if(found) {
				path.lineTo(vertex.getX(), vertex.getY());
				if(vertex.getX() == startPoint.getX() && 
						vertex.getY() == startPoint.getY())
					break;
			} else {
				if(vertex.getX() == borderVertex.getX() && 
						vertex.getY() == borderVertex.getY()) {
					path.moveTo(borderVertex.getX(), borderVertex.getY());
					found = true;
				}
			}
		}
		return path;
	}

	private void pushEndpoint(GeometricPath path, Polygon endPoly, 
			Point2D startPoint, Point2D endPoint) {
		boolean found = false;
		for(Point2D vertex : endPoly.vertices) {
			if(found) {
				if(vertex.getX() == startPoint.getX() && 
						vertex.getY() == startPoint.getY())
					break;
				path.lineTo(vertex.getX(), vertex.getY());
			}
			else {
				if(vertex.getX() == endPoint.getX() && 
						vertex.getY() == endPoint.getY())
					found = true;
			}
		}
	}

	/*
	 * The omolog point is a point on border and we prefer to have the second 
	 * border point on the same border - if exists. Otherwise we have 
	 * some pretty nasty merges
	 */
	private void pushToBounds(GeometricPath path, Polygon bound, 
			Point2D endPoint, Point2D omolog) {
		boolean found = false;
		Point2D borderVertex = bound.getSameBorderVertex(omolog);
		for(Point2D vertex : bound.vertices) {
			if(found) {
				path.lineTo(vertex.getX(), vertex.getY());
				if(vertex.getX() == borderVertex.getX() && 
						vertex.getY() == borderVertex.getY())
					break;
			}
			else {
				if(vertex.getX() == endPoint.getX() && 
						vertex.getY() == endPoint.getY()) 
					found = true;
			}
		}
	}
	
	/**
	 * Calculate area. It is practically the sum of areas of the list of polygons
	 * @return
	 */
	public float area() {
		float area = 0 ;
		for(Polygon poly : this.polygons) 
			area += poly.area();
		return area;
	}
	
	/**
	 * Get the edge onto which the bat hit the path
	 * @param movementVector the pair of points (lastPos, Pos) 
	 * 	indicates the movement vector of the bat
	 * @return the edge touched by the movement vector
	 * @throws IllegalArgumentException if the movement vector does not touch the claimed path
	 */
	public Edge2D getTouchEdge(Edge2D movementVector) {
		Edge2D touchEdge = null;
		for(Polygon poly : this.polygons) {
			touchEdge = poly.getTouchEdge(movementVector);
			if(touchEdge != null)
				return touchEdge;
		}
		throw new IllegalArgumentException("The movement vector " + print(movementVector) 
				+ " does not intersect the claimed path " + this.toString());
	}

	private String print(Edge2D movementVector) {
		StringBuilder sb = new StringBuilder();
		Point2D e1 = movementVector.getStartPoint();
		Point2D e2 = movementVector.getEndPoint();
		sb.append("(" + e1.getX() + "," + e1.getY() + ") -> " );
		sb.append("(" + e2.getX() + "," + e2.getY() + ")" );
		return sb.toString();
	}
	
}
