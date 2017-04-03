package image.processing.vision.drawable;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;

public abstract class MultipatchDrawable extends Drawable {

	protected List<ReflectivePatch> patches = new ArrayList<>();

	public void addPatch(ReflectivePatch patch) {
		if (patches.size() == 2) {
			System.out.println();
		}
		patches.add(patch);
	}

	@Override
	protected Rectangle2D calculateBounds() {
		final Rectangle2D.Double union = patches.isEmpty() ? new Rectangle2D.Double()
				: (Double) patches.get(0).getBounds();
		for (final ReflectivePatch patch : patches) {
			System.out.println("--> patch bounds: " + patch.getBounds());
			Rectangle2D.union(union, patch.getBounds(), union);
		}
		return union;
	}

	@Override
	protected Point2D calculateCenter() {
		final Rectangle2D box = getBounds();
		final List<Point2D> allPatchPoints = getAllPatchPoints();
		final Point2D topLeft = PointsUtil.nearestPoint(box.getX(), box.getY(), allPatchPoints);
		final Point2D topRight = PointsUtil.nearestPoint(box.getX() + box.getWidth(), box.getY(), allPatchPoints);
		final Point2D bottomLeft = PointsUtil.nearestPoint(box.getX(), box.getY() + box.getHeight(), allPatchPoints);
		final Point2D bottomRight = PointsUtil.nearestPoint(box.getX() + box.getWidth(), box.getY() + box.getHeight(),
				allPatchPoints);
		Point2D intersection = PointsUtil.lineIntersect(topLeft, bottomRight, bottomLeft, topRight);
		if (intersection == null) {
			intersection = new Point2D.Double(box.getX() + (box.getWidth() / 2), box.getY() + (box.getHeight() / 2));
		}
		return intersection;
	}

	private List<Point2D> getAllPatchPoints() {
		final ArrayList<Point2D> all = new ArrayList<>();
		for (final ReflectivePatch patch : patches) {
			all.addAll(patch.getCorners());
		}
		return all;
	}
}
