package com.killerappzz.spider.objects;

import android.graphics.Path;
import android.graphics.RectF;

import com.killerappzz.spider.geometry.Area;
import com.killerappzz.spider.geometry.Path2D;
import com.killerappzz.spider.geometry.PathIterator;
import com.killerappzz.spider.geometry.Point2D;
import com.killerappzz.spider.geometry.Shape;

/**
 * An Android Path, but augmented with geometrical information,
 * supplied via geometry package.
 *  
 * @author florin
 *
 */
public class GeometricPath extends Path {
	
	// so we can do advanced ops on it
	private final Path2D geometry;
	// store the start point and end point
	private final Point2D.Float startPoint;
	private final Point2D.Float endPoint;
	// the geometrical center of the Path. for the score ;)
	private final Point2D.Float center;
	// the path number of points
	private int vertexCount;
	
	public GeometricPath() {
		this.geometry = new Path2D.Float();
		this.startPoint = new Point2D.Float();
		this.endPoint = new Point2D.Float();
		this.center = new Point2D.Float();
		this.vertexCount = 0;
	}
	
	public GeometricPath(GeometricPath trailingPath) {
		super(trailingPath);
		// not really used...
		this.geometry = trailingPath.geometry;
		this.startPoint = trailingPath.startPoint;
		this.endPoint = trailingPath.endPoint;
		this.center = trailingPath.endPoint;
	}

	@Override
	public void moveTo(float x, float y) {
		this.geometry.moveTo(x, y);
		this.startPoint.x = x;
		this.startPoint.y = y;
		this.center.x = x;
		this.center.y = y;
		this.vertexCount++;
		super.moveTo(x, y);
	}
	
	@Override
	public void lineTo(float x, float y) {
		this.geometry.lineTo(x, y);
		this.endPoint.x = x;
		this.endPoint.y = y;
		this.center.x = (vertexCount * center.x + x) / (vertexCount + 1);
		this.center.y = (vertexCount * center.y + y) / (vertexCount + 1);
		this.vertexCount++;
		super.lineTo(x, y);
	}
	
	@Override
	public void close() {
		this.geometry.closePath();
		super.close();
	}
	
	@Override
	public void rewind() {
		this.geometry.reset();
		// reset center
		this.center.x = this.center.y = 0;
		this.vertexCount = 0;
		super.rewind();
	}
	
	/**
	 * Adauga path-ul la configuratia curenta.
	 * Fara a tine cont insa de moveTo-uri!
	 * Practic, doar adauga vertices din src 
	 * la path-ul nostru
	 * 
	 * @param src
	 */
	public void addGeometricPath(GeometricPath src) {
		float[] coords = new float[6];
		PathIterator it = src.getGeometry().getPathIterator(null);
		while(!it.isDone()){
			it.currentSegment(coords);
			lineTo(coords[0], coords[1]);
			it.next();
		}
		super.addPath(src);
	}

	/**
	 * Merge given path into our path.
	 * @param path
	 */
	public void merge(GeometricPath path) {
		Area a = new Area(this.geometry);
		Area b = new Area(path.getGeometry());
		a.add(b);
		loadFromArea(a);
		// cleanup
		a.reset();
		b.reset();
	}

	private void loadFromArea(Area area) {
		rewind();
		PathIterator it = area.getPathIterator(null);
		float[] coords = new float[6];
		/**
		 * For a polygonal shape, iteration goes as follows:
		 * - first segment is of type SEG_MOVETO with first point
		 * - then a list of SEG_LINETO segments with all points 
		 *  on the path except first one. Last one is included!
		 * - then a final SEG_CLOSE segment, with the values of last point
		 */
		while(!it.isDone()){
			switch(it.currentSegment(coords)){
			case PathIterator.SEG_MOVETO:
				moveTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_LINETO:
				lineTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_CLOSE:
				close();
			}
			it.next();
		}
	}

	/**
	 * Tests if the given point is inside out path.
	 * It relies mostly on the contains method of our Geometry.
	 * HOWEVER! we need to test ourselves for path vertices
	 * as unfortunately! our geometry reports these as NOT contained.
	 * HOWEVER! points on the border of our Geometry are correctly 
	 * reported as contained! Also see the {@link Shape} javadoc
	 */
	public boolean contains(float x, float y) {
		// test for polygon vertices.
		// this is not tested by Path2D
		PathIterator it = this.geometry.getPathIterator(null);
		float[] coords = new float[6];
		while(!it.isDone()) {
			it.currentSegment(coords);
			if(coords[0] == x && coords[1] == y)
				return true;
			it.next();
		}
		return this.geometry.contains(x, y);
	}
	
	public Path2D getGeometry() {
		return geometry;
	}
	
	public Point2D.Float getStartPoint() {
		return this.startPoint;
	}
	
	public Point2D.Float getEndPoint() {
		return this.endPoint;
	}
	
	public Point2D.Float getCenter() {
		return this.center;
	}
	
	protected boolean boundsTest(RectF bounds, float x, float y) {
		return bounds.bottom == y || bounds.top == y 
			|| bounds.left == x || bounds.right == x;
	}
	
	protected boolean boundsTest(RectF bounds, Point2D.Float point) {
		return boundsTest(bounds, point.x, point.y);
	}
	
	@Override
	public String toString() {
		PathIterator it = this.geometry.getPathIterator(null);
		float[] coords = new float[6];
		StringBuilder sb = new StringBuilder();
		while(!it.isDone()) {
			int type = it.currentSegment(coords);
			sb.append("Segment type:" + type);
			sb.append(";Coords:(" + coords[0] + "," + coords[1] + ");");
			it.next();
		}
		sb.append("Geometrical center: " + "(" + center.x + "," + center.y + ");");
		return sb.toString();
	}

	public void update(GeometricPath trailingPath) {
		set(trailingPath);
	}

}
