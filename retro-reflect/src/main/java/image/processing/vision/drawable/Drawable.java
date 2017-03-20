package image.processing.vision.drawable;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class Drawable {
	Point2D center;
	Rectangle2D bounds;

	protected abstract Rectangle2D calculateBounds();

	protected abstract Point2D calculateCenter();

	public abstract void draw(Graphics2D g);

	public Rectangle2D getBounds() {
		if (bounds == null) {
			bounds = calculateBounds();
		}
		return bounds;
	}

	public Point2D getCenter() {
		if (center == null) {
			center = calculateCenter();
		}
		return center;
	}
}
