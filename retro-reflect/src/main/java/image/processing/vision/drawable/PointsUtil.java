package image.processing.vision.drawable;

import java.awt.geom.Point2D;
import java.util.List;

public class PointsUtil {

	public static Point2D lineIntersect(double d, double e, double f, double g, double h, double i, double j,
			double k) {
		final double denom = (k - i) * (f - d) - (j - h) * (g - e);
		if (denom == 0.0) { // Lines are parallel.
			return null;
		}
		final double ua = ((j - h) * (e - i) - (k - i) * (d - h)) / denom;
		final double ub = ((f - d) * (e - i) - (g - e) * (d - h)) / denom;
		if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
			// Get the intersection point.
			return new Point2D.Double(d + ua * (f - d), e + ua * (g - e));

		}
		return null;
	}

	public static Point2D lineIntersect(Point2D topLeft, Point2D bottomRight, Point2D bottomLeft, Point2D topRight) {
		return lineIntersect(topLeft.getX(), topLeft.getY(), bottomRight.getX(), bottomRight.getY(), bottomLeft.getX(),
				bottomLeft.getY(), topRight.getX(), topRight.getY());
	}

	/**
	 * @return the candidate point nearest to the specified coordinates
	 */
	public static Point2D nearestPoint(double x, double y, List<Point2D> candidates) {
		double distance = Double.MAX_VALUE;
		Point2D nearest = null;
		for (final Point2D pt : candidates) {
			final double hypot = Math.hypot(x - pt.getX(), y - pt.getY());
			if (hypot < distance) {
				distance = hypot;
				nearest = pt;
			}
		}
		return nearest;
	}

}
