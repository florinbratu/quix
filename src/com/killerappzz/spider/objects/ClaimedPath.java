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
	public void reduceToBounds(GeometricPath path, RectF screenRect) {
		Point2D.Float startPoint = path.getStartPoint();
		Point2D.Float endPoint = path.getEndPoint();
		// if both points on border
		if(boundsTest(screenRect, startPoint) && boundsTest(screenRect, endPoint))
			// nothing to be done
			return;
		Polygon endPoly = null, startPoly = null;
		for(Polygon p : polygons) {
			if(p.contains(endPoint.x, endPoint.y)) {
				endPoly = p;
			}
			if(p.contains(startPoint.x, startPoint.y)) {
				startPoly = p;
			}
		}
		if( endPoly == null) {
			// the endpoint is on the edge
			if(startPoly != null) {
				// the starting point is on a polygon
				// otherwise we have nothing to do!
				// get the path from the bound point on start polygon to the start point
				GeometricPath startPointPath = getPathToStartPoint(startPoly, startPoint);
				// add the path done by the spider
				startPointPath.addGeometricPath(path);
				// put in path the contents of startPointPath
				path.rewind();
				path.addGeometricPath(startPointPath);
			}
		} else if( startPoly == null ) {
			// the starting point is on the edge
			// it's enough to go from endpoint to edge
			// by following the polygon line containing the endpoint
			pushToBounds(path, endPoly, endPoint);
		} else if(startPoly.equals(endPoly)) {
			// start and end points are on the same polygon
			// just connect them via polygon lines
			pushToEndpoint(path, endPoly, startPoint, endPoint);
		} else {
			// ugliest case: start and end points are on two different polygons
			// get the path from the bound point on start polygon to the start point
			GeometricPath startPointPath = getPathToStartPoint(startPoly, startPoint);
			// add the path done by the spider
			startPointPath.addGeometricPath(path);
			// put in path the contents of startPointPath
			path.rewind();
			path.addGeometricPath(startPointPath);
			// add the path to the bounds on the end polygon
			pushToBounds(path, endPoly, endPoint);
		}
	}

	private GeometricPath getPathToStartPoint(Polygon bound, Point2D.Float startPoint) {
		GeometricPath path = new GeometricPath();
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

	private void pushToEndpoint(GeometricPath path, Polygon endPoly, 
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
				if( vertex.first == bound.getBorderPoint().x
						&& vertex.second == bound.getBorderPoint().y) 
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
