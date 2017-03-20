package image.processing.vision.drawable;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import image.processing.finder.Figure;

public class ReflectivePatch extends Drawable {

	public static Comparator<ReflectivePatch> getVerticalComparator() {
		return new Comparator<ReflectivePatch>() {
			@Override
			public int compare(ReflectivePatch o1, ReflectivePatch o2) {
				final Integer i1 = o1.getQuadrilateral().ypoints[0];
				final Integer i2 = o2.getQuadrilateral().ypoints[0];
				return i1.compareTo(i2);
			}
		};
	}

	private final Quadrilateral quadrilateral = new Quadrilateral();

	@Override
	protected Rectangle2D calculateBounds() {
		final Rectangle2D b = quadrilateral.getBounds2D();
		// sometimes there are artifacts remaining when redrawn
		return new Rectangle2D.Double(b.getX(), b.getY(), b.getWidth(), b.getHeight());
	}

	@Override
	protected Point2D calculateCenter() {
		final int[] x = quadrilateral.xpoints;
		final int[] y = quadrilateral.ypoints;
		return PointsUtil.lineIntersect(x[0], y[0], x[1], y[1], x[2], y[2], x[3], y[3]);
	}

	@Override
	public void draw(Graphics2D g) {
		quadrilateral.draw(g);
		quadrilateral.fill(g);
	}

	public Collection<? extends Point2D> getCorners() {
		final ArrayList<Point2D> corners = new ArrayList<>();
		final int[] xpoints = quadrilateral.xpoints;
		final int[] ypoints = quadrilateral.ypoints;
		for (int i = 0; i < quadrilateral.npoints; i++) {
			corners.add(new Point2D.Double(xpoints[i], ypoints[i]));
		}
		return corners;
	}

	public Quadrilateral getQuadrilateral() {
		return quadrilateral;
	}

	public void setPoints(int tLx, int tLy, int tRx, int tRy, int bRx, int bRy, int bLx, int bLy) {
		quadrilateral.addPoint(tLx, tLy);
		quadrilateral.addPoint(tRx, tRy);
		quadrilateral.addPoint(bRx, bRy);
		quadrilateral.addPoint(bLx, bLy);
	}
	
	public void setFigure(Figure f) {
		quadrilateral.addPoint(f.leftBoundary + f.topLeftCorner.x, f.topBoundary + f.topLeftCorner.y);
		quadrilateral.addPoint(f.leftBoundary + f.topRightCorner.x, f.topBoundary + f.topRightCorner.y);
		quadrilateral.addPoint(f.leftBoundary + f.bottomRightCorner.x, f.topBoundary + f.bottomRightCorner.y);
		quadrilateral.addPoint(f.leftBoundary + f.bottomLeftCorner.x, f.topBoundary + f.bottomLeftCorner.y);
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < quadrilateral.npoints; i++) {
			b.append(quadrilateral.xpoints[i]);
			b.append('@');
			b.append(quadrilateral.ypoints[i]);
			b.append(' ');
		}
		return super.toString();
	}
}