package image.processing.util;

import java.awt.Point;

/**
 * Represents a line using two points
 */
public class Line {
	public final Point leftTop;
	public final Point rightBottom;

	public Line(Point leftTop, Point rightBottom) {
		super();
		this.leftTop = leftTop;
		this.rightBottom = rightBottom;
	}
}