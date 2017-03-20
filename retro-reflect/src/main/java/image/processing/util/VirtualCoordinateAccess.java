package image.processing.util;

import java.awt.Point;

/**
 * For a specified Transformation, map point coordinates
 */
public class VirtualCoordinateAccess {

	public enum Transformation {
		NORMAL, REVERSE_TOP_AND_BOTTOM, MIRROR_LEFT_AND_RIGHT, REVERSE_AND_MIRROR
	}

	final int maxX;
	final int maxY;
	private final Transformation mapping;
	final Point point = new Point();

	public VirtualCoordinateAccess(int maxX, int maxY, Transformation mapping) {
		super();
		this.maxX = maxX;
		this.maxY = maxY;
		this.mapping = mapping;
	}

	public Point get(int x, int y) {
		switch (mapping) {
		case NORMAL:
			point.x = x;
			point.y = y;
			return point;
		case REVERSE_TOP_AND_BOTTOM:
			point.x = x;
			point.y = maxY - y;
			return point;
		case MIRROR_LEFT_AND_RIGHT:
			point.x = maxX - x;
			point.y = y;
			return point;
		case REVERSE_AND_MIRROR:
			point.x = maxX - x;
			point.y = maxY - y;
			return point;
		default:
			break;
		}
		throw new IllegalAccessError();
	}
}
