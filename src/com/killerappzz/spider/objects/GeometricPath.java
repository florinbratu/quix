package com.killerappzz.spider.objects;

import android.graphics.Path;

import com.killerappzz.spider.geometry.Area;
import com.killerappzz.spider.geometry.Path2D;
import com.killerappzz.spider.geometry.PathIterator;

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
	
	public GeometricPath() {
		this.geometry = new Path2D.Float();
	}
	
	@Override
	public void moveTo(float x, float y) {
		this.geometry.moveTo(x, y);
		super.moveTo(x, y);
	}
	
	@Override
	public void lineTo(float x, float y) {
		this.geometry.lineTo(x, y);
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
		super.rewind();
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

	public boolean contains(float x, float y) {
		return this.geometry.contains(x, y);
	}
	
	public Path2D getGeometry() {
		return geometry;
	}

}
