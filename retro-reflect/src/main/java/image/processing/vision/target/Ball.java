package image.processing.vision.target;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import image.processing.vision.drawable.Drawable;

public class Ball extends Drawable {

	Point2D center;
	double radius;

	public Ball(Point2D center, double radius) {
		super();
		this.center = center;
		this.radius = radius;
	}

	@Override
	protected Rectangle2D calculateBounds() {
		final double diameter = radius * 2;
		return new Rectangle2D.Double(center.getX() - radius, center.getY() - radius, diameter, diameter);
	}

	@Override
	protected Point2D calculateCenter() {
		return center;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		final Rectangle2D box = getBounds();
		g.fillOval((int) Math.round(box.getX()), (int) Math.round(box.getY()), (int) Math.round(box.getWidth()),
				(int) Math.round(box.getHeight()));
	}

	public double getRadius() {
		return radius;
	}
}
