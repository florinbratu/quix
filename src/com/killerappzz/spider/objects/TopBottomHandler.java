package com.killerappzz.spider.objects;

import java.util.HashMap;
import java.util.Map;

import android.graphics.RectF;

import com.killerappzz.spider.geometry.Edge2D;
import com.killerappzz.spider.geometry.Point2D;

/**
 * 
 * A handler for the very tricky top-bottom merge case
 * TODO describe the problematique
 * @author fbratu
 *
 */
public class TopBottomHandler {
	
	private Map<Edge2D, Polygon> borderPolygons;
	private final Edge2D left,right,top,bottom;
	private final Point2D extreme;
	private final RectF rect;
	
	public TopBottomHandler(RectF sr) {
		this.borderPolygons = new HashMap<Edge2D, Polygon>();
		Point2D topLeft = new Point2D.Float(sr.left, sr.top);
		Point2D topRight = new Point2D.Float(sr.right, sr.top);
		Point2D bottomLeft = new Point2D.Float(sr.left, sr.bottom);
		Point2D bottomRight = new Point2D.Float(sr.right, sr.bottom);
		this.left = new Edge2D.Float(topLeft,bottomLeft);
		this.right = new Edge2D.Float(topRight,bottomRight);
		this.top = new Edge2D.Float(topLeft,topRight);
		this.bottom = new Edge2D.Float(bottomLeft,bottomRight);
		this.extreme = new Point2D.Float();
		this.rect = sr;
	}

	public void addBorderPolygon(Point2D corner1, Point2D corner2,
			Polygon borderPolygon) {
		if(left.equals(corner1,corner2))
			this.borderPolygons.put(left, borderPolygon);
		else if(right.equals(corner1,corner2))
			this.borderPolygons.put(right, borderPolygon);
		else if(top.equals(corner1,corner2))
			this.borderPolygons.put(top, borderPolygon);
		else if(bottom.equals(corner1,corner2))
			this.borderPolygons.put(bottom, borderPolygon);
	}

	public Point2D getTopExtremes() {
		float lt = borderPolygons.containsKey(left) ? 
				getTopExtreme(borderPolygons.get(left)) : rect.left;
		float rt = borderPolygons.containsKey(right) ?
				getTopExtreme(borderPolygons.get(right)) : rect.right;
		extreme.setLocation(lt, rt);
		return extreme;
	}

	private float getTopExtreme(Polygon polygon) {
		for(Point2D vertex : polygon.vertices) {
			if(vertex.getY() == rect.top)
				return (float)vertex.getX();
		}
		return 0;
	}

	public Point2D getBottomExtremes() {
		float lt = borderPolygons.containsKey(left) ? 
				getBottomExtreme(borderPolygons.get(left)) : rect.left;
		float rt = borderPolygons.containsKey(right) ?
				getBottomExtreme(borderPolygons.get(right)) : rect.right;
		extreme.setLocation(lt, rt);
		return extreme;
	}

	private float getBottomExtreme(Polygon polygon) {
		for(Point2D vertex : polygon.vertices) {
			if(vertex.getY() == rect.bottom)
				return (float)vertex.getX();
		}
		return 0;
	}

	public Point2D getLeftExtremes() {
		float lt = borderPolygons.containsKey(top) ? 
				getLeftExtreme(borderPolygons.get(top)) : rect.top;
		float rt = borderPolygons.containsKey(bottom) ?
				getLeftExtreme(borderPolygons.get(bottom)) : rect.bottom;
		extreme.setLocation(lt, rt);
		return extreme;
	}

	private float getLeftExtreme(Polygon polygon) {
		for(Point2D vertex : polygon.vertices) {
			if(vertex.getY() == rect.left)
				return (float)vertex.getY();
		}
		return 0;
	}

	public Point2D getRightExtremes() {
		float lt = borderPolygons.containsKey(top) ? 
				getRightExtreme(borderPolygons.get(top)) : rect.top;
		float rt = borderPolygons.containsKey(bottom) ?
				getRightExtreme(borderPolygons.get(bottom)) : rect.bottom;
		extreme.setLocation(lt, rt);
		return extreme;
	}

	private float getRightExtreme(Polygon polygon) {
		for(Point2D vertex : polygon.vertices) {
			if(vertex.getY() == rect.right)
				return (float)vertex.getY();
		}
		return 0;
	}

}
