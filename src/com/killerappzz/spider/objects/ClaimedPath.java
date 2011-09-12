package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

import android.graphics.RectF;
import android.util.Pair;

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
	private int polyID;
	private final RectF screenRect;
	
	public ClaimedPath(RectF sr) {
		super();
		this.polygons = new LinkedList<Polygon>();
		this.polyID = 0;
		this.screenRect = sr;
	}
	
	@Override
	public void moveTo(float x, float y) {
		currentPolygon = new Polygon(polyID++, screenRect);
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
		Point2D.Float startPoint = path.getStartPoint();
		Point2D.Float endPoint = path.getEndPoint();
		// if both points on border
		if(boundsTest(screenRect, startPoint) && boundsTest(screenRect, endPoint))
			// nothing to be done
			return path;
		Polygon endPoly = null, startPoly = null;
		Point2D.Float endClosest = null, startClosest = null;
		for(Polygon p : polygons) {
			if(p.contains(endPoint.x, endPoint.y)) {
				endPoly = p;
				endClosest = p.getClosestVertex(endPoint);
			}
			if(p.contains(startPoint.x, startPoint.y)) {
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
				ret.lineTo(startPoint.x, startPoint.y);
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
			path.lineTo(endClosest.x, endClosest.y);
			// it's enough to go from endpoint to edge
			// by following the polygon line containing the endpoint
			pushToBounds(path, endPoly, endClosest);
			return path;
		} else if(startPoly.equals(endPoly)) {
			// start and end points are on the same polygon
			// just connect them via polygon lines
			// but first! get to the closest vertex
			// start closest
			GeometricPath ret = new SpiderPath(screenRect);
			ret.moveTo(startClosest.x, startClosest.y);
			ret.lineTo(startPoint.x, startPoint.y);
			ret.addGeometricPath(path);
			// end closest
			ret.lineTo(endClosest.x, endClosest.y);
			pushEndpoint(ret, endPoly, startClosest, endClosest);
			return ret;
		} else {
			// ugliest case: start and end points are on two different polygons
			// get the path from the bound point on start polygon to the start point
			GeometricPath ret = getPathToStartPoint(startPoly, startClosest, screenRect);
			// add the start point
			ret.lineTo(startPoint.x, startPoint.y);
			// add the path done by the spider
			ret.addGeometricPath(path);
			// goto closest vertex from endpoint
			ret.lineTo(endClosest.x, endClosest.y);
			// add the path to the bounds on the end polygon
			pushToBounds(ret, endPoly, endClosest);
			return ret;
		}
	}

	private GeometricPath getPathToStartPoint(Polygon bound, 
			Point2D.Float startPoint, RectF screenRect) {
		GeometricPath path = new SpiderPath(screenRect);
		boolean found = false;
		for(Pair<Float,Float> vertex : bound.vertices) {
			if(found) {
				path.lineTo(vertex.first, vertex.second);
				if(vertex.first == startPoint.x && 
						vertex.second == startPoint.y)
					break;
			} else {
				if(vertex.first == bound.getBorderPoint().x && 
						vertex.second == bound.getBorderPoint().y) {
					path.moveTo(bound.getBorderPoint().x, 
							bound.getBorderPoint().y);
					found = true;
				}
			}
		}
		return path;
	}

	private void pushEndpoint(GeometricPath path, Polygon endPoly, 
			Point2D.Float startPoint, Point2D.Float endPoint) {
		boolean found = false;
		for(Pair<Float,Float> vertex : endPoly.vertices) {
			if(found) {
				if(vertex.first == startPoint.x && 
						vertex.second == startPoint.y)
					break;
				path.lineTo(vertex.first, vertex.second);
			}
			else {
				if(vertex.first == endPoint.x && 
						vertex.second == endPoint.y)
					found = true;
			}
		}
	}

	private void pushToBounds(GeometricPath path, Polygon bound, 
			Point2D.Float endPoint) {
		boolean found = false;
		for(Pair<Float,Float> vertex : bound.vertices) {
			if(found) {
				path.lineTo(vertex.first, vertex.second);
				if(vertex.first == bound.getBorderPoint().x && 
						vertex.second == bound.getBorderPoint().y)
					break;
			}
			else {
				if(vertex.first == endPoint.x && 
						vertex.second == endPoint.y) 
					found = true;
			}
		}
	}
	
}
