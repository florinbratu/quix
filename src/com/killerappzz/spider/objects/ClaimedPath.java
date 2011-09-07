package com.killerappzz.spider.objects;

import java.util.LinkedList;
import java.util.List;

import com.killerappzz.spider.geometry.Point2D;

import android.util.Pair;

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
	
	public ClaimedPath() {
		super();
		this.polygons = new LinkedList<Polygon>();
		this.polyID = 0;
	}
	
	@Override
	public void moveTo(float x, float y) {
		currentPolygon = new Polygon(polyID++);
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
	 * @param path
	 */
	public void reduceToBounds(GeometricPath path) {
		Point2D.Float startPoint = path.getStartPoint();
		Point2D.Float endPoint = path.getEndPoint();
		Polygon endPoly = null, startPoly = null;
		for(Polygon p : polygons) {
			if(p.contains(endPoint.x, endPoint.y)) {
				endPoly = p;
			}
			if(p.contains(startPoint.x, startPoint.y)) {
				startPoly = p;
			}
		}
		if( startPoly == null ) {
			// the starting point is on the edge
			// it's enough to go from endpoint to edge
			// by following the polygon line containing the endpoint
			pushToBounds(path, endPoly, endPoint);
		} else if(startPoly.equals(endPoly)) {
			// start and end points are on the same polygon
			// just connect them via polygon lines
			pushToEndpoint(path, endPoly, startPoint, endPoint);
		} else {
			// ugliest case: start and end points are
			// on two different polygons
			// TODO
		}
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
			}
			else {
				if(vertex.first == endPoint.x && 
						vertex.second == endPoint.y) 
					found = true;
			}
		}
	}
	
}
