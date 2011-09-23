package com.killerappzz.spider.geometry;


/**
 * An Edge of 2 points, straight line ;)
 * @author fbratu
 *
 */
public abstract class Edge2D {
	
	public static class Float extends Edge2D { 
	
		private final Point2D start;
		private final Point2D end;
		
		/* malloc ctor */
		public Float() {
			this.start = new Point2D.Float();
			this.end = new Point2D.Float();
		}

		public Float(float startx, float starty, float endx, float endy) {
			this.start = new Point2D.Float(startx, starty);
			this.end = new Point2D.Float(endx, endy);
		}

		public Float(Point2D start, Point2D end) {
			this((float)start.getX(),(float)start.getY(),
					(float)end.getX(),(float)end.getY());
		}

		@Override
		public Point2D getStartPoint() {
			return this.start;
		}

		@Override
		public Point2D getEndPoint() {
			return this.end;
		}

		@Override
		public void set(double startx, double starty, double endx, double endy) {
			this.start.setLocation(startx, starty);
			this.end.setLocation(endx, endy);
		}

		@Override
		public void setStartPoint(double startx, double starty) {
			this.start.setLocation(startx, starty);
		}

		@Override
		public void setEndPoint(double endx, double endy) {
			this.end.setLocation(endx, endy);
		}
		
		@Override
		public boolean equals(Object o) {
			if(o == null || !(o instanceof Edge2D.Float))
				return false;
			Edge2D.Float other = (Edge2D.Float)o;
			return this.start.equals(other.start) && this.end.equals(other.end);
		}
		
		@Override
		public int hashCode() {
			int hash = 1;
			hash = hash * 31 + this.start.hashCode();
			hash = hash * 31 + this.end.hashCode(); 
			return hash;
		}
		
	}
	
	public abstract Point2D getStartPoint();
	public abstract Point2D getEndPoint();
	public abstract void setStartPoint(double startx, double starty);
	public abstract void setEndPoint(double endx, double endy);
	public abstract void set(double startx, double starty, double endx, double endy);
	
	public void setStartPoint(Point2D startPoint) {
		setStartPoint(startPoint.getX(), startPoint.getY());
	}
	
	public void setEndPoint(Point2D endPoint) {
		setEndPoint(endPoint.getX(), endPoint.getY());
	}
	
	public void set(Point2D startPoint, Point2D endPoint) {
		set(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
	}
	
	public void set(Edge2D edge) {
		set(edge.getStartPoint(),edge.getEndPoint());
	}
	
	/**
	 * Test this edge "intersect" the given edge
	 * The test verifies if the two extreme points of the given edge
	 * are on "different sides" of this edge.
	 * 
	 * The "different sides" test is being performed via cross product
	 * Each side is identified by the sign of the cross product over the edge
	 * 
	 * @param edge the given edge
	 */
	public boolean touches(Edge2D edge) {
		return Math.signum(edge.crossprod(this.getStartPoint()))  
				!= Math.signum(edge.crossprod(this.getEndPoint()));
	}
	
	/**
	 * Calculate the following cross product: 
	 * 	(e1, point) x (e1, e2)
	 * 
	 * e1 is edge.startPoint
	 * e2 is edge.endPoint
	 * 
	 * @param point
	 * @return
	 */
	private double crossprod(Point2D point) {
		Point2D e1 = this.getStartPoint();
		Point2D e2 = this.getEndPoint();
		double x1 = point.getX() - e1.getX();
		double y1 = point.getY() - e1.getY();
		double x2 = e2.getX() - e1.getX();
		double y2 = e2.getY() - e1.getY();
		return x1 * y2 - x2 * y1;
	}
	
	public boolean equals(Point2D start, Point2D end) {
		return this.getStartPoint().equals(start) && this.getEndPoint().equals(end);
	}

}
